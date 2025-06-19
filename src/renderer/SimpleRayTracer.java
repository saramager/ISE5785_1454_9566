
package renderer;

import static primitives.Util.alignZero;

import java.util.List;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

/**
 * A simple ray tracer implementation that extends the RayTracerBase class.
 */
public class SimpleRayTracer extends RayTracerBase {

	/**
	 * The maximum number of color levels used in the calculation.
	 */
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	/**
	 * The minimum color level used in the calculation.
	 */
	private static final double MIN_CALC_COLOR_K = 0.001;
	/**
	 * The initial color level used in the calculation.
	 */
	private static final Double3 INITIAL_K = Double3.ONE;

	/**
	 * Constructor - initializes the ray tracer with a given scene.
	 * 
	 * @param scene the scene to be used for ray tracing
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);

	}

	@Override
	public Color traceRay(Ray ray) {
		Intersection intersections = findClosestIntersection(ray);
		return intersections == null ? scene.background : calcColor(intersections, ray);
	}

	/**
	 * Finds the closest intersection point between a ray and the geometries in the
	 * scene.
	 * 
	 * @param ray the ray to be traced
	 * @return the closest intersection point, or null if no intersection is found
	 */

	private Intersection findClosestIntersection(Ray ray) {
		return ray.findClosestIntersection(scene.geometries.calculateIntersections(ray));
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param ray          the ray that intersects with the geometry
	 * @return the color at the intersection point
	 */
	private Color calcColor(Intersection intersection, Ray ray) {
		return preprocessIntersection(intersection, ray.getDir()) //
				? scene.ambientLight.getIntensity().scale(intersection.material.kA)
						.add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K))
				: Color.BLACK;
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param v            the ray that intersects with the geometry direction
	 * @return the color at the intersection point
	 */
	private boolean preprocessIntersection(Intersection intersection, Vector v) {
		intersection.v = v;
		intersection.normal = intersection.geometry.getNormal(intersection.point);
		intersection.vNormal = alignZero(intersection.v.dotProduct(intersection.normal));
		return intersection.vNormal != 0;
	}

	/**
	 * Sets the light source in intersection for a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param light        the light source
	 * @return true if the light source direction dot product with normal different
	 *         from zero , false otherwise
	 */
	private boolean setLightSource(Intersection intersection, LightSource light) {

		intersection.light = light;
		intersection.l = light.getL(intersection.point);
		intersection.lNormal = alignZero(intersection.l.dotProduct(intersection.normal));
		return intersection.lNormal * intersection.vNormal > 0;

	}

	/**
	 * Calculates the local effects (diffusion and specular reflection) at a given
	 * intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param k            the color factor for the current recursion level
	 * @return the color resulting from local effects
	 */
	private Color calcColorLocalEffects(Intersection intersection, Double3 k) {
		Color color = intersection.geometry.getEmission();
		for (LightSource lightSource : scene.lights) {
			if (!setLightSource(intersection, lightSource))
				continue;
			Double3 ktr = transparency(intersection);
			if (ktr.product(k).lowerThan(MIN_CALC_COLOR_K))
				continue;

			Color iL = lightSource.getIntensity(intersection.point).scale(ktr);
			color = color.add(iL.scale(calcDiffusive(intersection)), iL.scale(calcSpecular(intersection)));
		}
		return color;
	}

	/**
	 * Calculates the diffusive color component at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @return the diffusive color component
	 */
	private Double3 calcDiffusive(Intersection intersection) {
		return intersection.material.kD.scale(Math.abs(intersection.lNormal));

	}

	/**
	 * Calculates the specular color component at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @return the specular color component
	 */
	private Double3 calcSpecular(Intersection intersection) {
		Vector minusV = intersection.v.scale(-1);
		Vector r = intersection.l.subtract(intersection.normal.scale(2 * intersection.lNormal));
		double minusVR = alignZero(minusV.dotProduct(r));
		return minusVR <= 0 ? Double3.ZERO
				: intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nShininess));
	}

	/**
	 * Calculates the transparency at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @return the transparency factor at the intersection point
	 */
	private Double3 transparency(Intersection intersection) {
		Double3 ktr = Double3.ONE;
		Ray shadowRay = new Ray(intersection.point, intersection.l.scale(-1), intersection.normal);
		var intersections = scene.geometries.calculateIntersections(shadowRay,
				intersection.light.getDistance(intersection.point));
		if (intersections == null)
			return ktr;

		for (Intersection inter : intersections) {
			ktr = ktr.product(inter.material.kT);
			if (ktr.lowerThan(MIN_CALC_COLOR_K))
				return Double3.ZERO;
		}

		return ktr;
	}

	/**
	 * Calculates the global effects (reflection and transparency) at a given ray.
	 * 
	 * @param ray   the ray to be traced
	 * @param level the current recursion level
	 * @param k     the color factor for the current level
	 * @param kx    the color factor for the global effects (reflection and
	 *              transparency)
	 * @return the color resulting from global effects
	 * 
	 */
	private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
		Double3 kkx = k.product(kx);
		if (kkx.lowerThan(MIN_CALC_COLOR_K))
			return Color.BLACK;
		Intersection intersection = findClosestIntersection(ray);
		if (intersection == null)
			return Color.BLACK.scale(kx);
		return preprocessIntersection(intersection, ray.getDir()) ? calcColor(intersection, level - 1, kkx).scale(kx)
				: Color.BLACK;
	}

	/**
	 * Calculates the global effects (reflection and transparency) at a given
	 * intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param level        the current recursion level
	 * @param k            the color factor for the current level
	 * @return the color resulting from global effects
	 */
	private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
		return calcRayBeamColor(level, k, intersection.material.kT, //
				constructBeamdRays(constructTransparencydRay(intersection), intersection.material.tAngle,
						intersection.normal)) //
				.add(calcRayBeamColor(level, k, intersection.material.kR, //
						constructBeamdRays(constructReflectedRay(intersection), intersection.material.rAngle,
								intersection.normal)));
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param level        the current recursion level
	 * @param k            the color factor for the current level
	 * @return the color at the intersection point
	 */
	private Color calcColor(Intersection intersection, int level, Double3 k) {
		Color color = calcColorLocalEffects(intersection, k);
		return level == 1 ? color : color.add(calcGlobalEffects(intersection, level, k));
	}

	/**
	 * Constructs a reflected ray based on the intersection point and the normal
	 * vector.
	 * 
	 * @param intersection the intersection point
	 * @return the reflected ray, or null if the reflection is not valid
	 */
	private Ray constructReflectedRay(Intersection intersection) {
		Vector r = intersection.v.subtract(intersection.normal.scale(2 * intersection.vNormal));// n*2*vn
		return new Ray(intersection.point, r, intersection.normal); // new Ray{point,v-2*(v*n)*n}
	}

	/**
	 * Constructs a transparency ray based on the intersection point, the direction
	 * vector, and the normal vector.
	 * 
	 * @param intersection the intersection point
	 * @return the transparency ray
	 */
	private Ray constructTransparencydRay(Intersection intersection) {
		return new Ray(intersection.point, intersection.v, intersection.normal);
	}

	/**
	 * Constructs a list of reflected rays based on the given ray, reflection
	 * coefficient, and normal vector.
	 * 
	 * @param ray    the original ray
	 * @param k      the reflection coefficient
	 * @param normal the normal vector at the intersection point
	 * @return a list of reflected rays
	 */
	private List<Ray> constructBeamdRays(Ray ray, double k, Vector normal) {
		double res = ray.getDir().dotProduct(normal);
		return k == 0 ? List.of(ray)//
				: new Blackboard(ray, k, glossyAndDiffuseNumOfRay)//
						.constructRayBeamGrid()//
						.stream().filter(r -> r.getDir().dotProduct(normal) * res > 0)//
						.toList();
	}

	/**
	 * Calculates the color of a ray beam based on the reflection coefficient and
	 * the normal vector.
	 * 
	 * @param level the current recursion level
	 * @param k     the recursion coefficient
	 * @param kX    the transparency or reflection coefficient
	 * @param rays  the list of rays in the beam
	 * @return the color of the ray beam
	 */
	private Color calcRayBeamColor(int level, Double3 k, Double3 kX, List<Ray> rays) {
		int size = rays.size();
		if (size == 1)
			return calcGlobalEffect(rays.getFirst(), level, k, kX);

		Color color = Color.BLACK;
		for (Ray rT : rays)
			color = color.add(calcGlobalEffect(rT, level, k, kX));
		return color.reduce(size);
	}

}
