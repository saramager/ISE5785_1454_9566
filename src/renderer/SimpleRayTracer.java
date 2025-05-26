
package renderer;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

/**
 * A simple ray tracer implementation that extends the RayTracerBase class.
 */
public class SimpleRayTracer extends RayTracerBase {
	/**
	 * A small constant used to avoid floating-point precision issues.
	 */
	private static final double DELTA = 0.1;
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

	private Intersection findClosestIntersection(Ray ray) {
		List<Intersection> points = scene.geometries.calculateIntersections(ray);
		return points == null ? null : ray.findClosestIntersection(points);
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param ray          the ray that intersects with the geometry
	 * @return the color at the intersection point
	 */
	private Color calcColor(Intersection intersection, Ray ray) {
		if (!preprocessIntersection(intersection, ray.getDir()))
			return Color.BLACK;
		return scene.ambientLight.getIntensity().scale(intersection.material.kA)
				.add(calcColor(intersection, MAX_CALC_COLOR_LEVEL, INITIAL_K));
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
		return !isZero(intersection.vNormal);
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
	 * @return the color resulting from local effects
	 */
	private Color calcColorLocalEffects(Intersection intersection) {
		Color color = intersection.geometry.getEmission();
		for (LightSource lightSource : scene.lights) {
			if (!setLightSource(intersection, lightSource) || !unshaded(intersection))
				continue;

			Color iL = lightSource.getIntensity(intersection.point);
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
		if (minusVR <= 0)
			return Double3.ZERO;
		return intersection.material.kS.scale(Math.pow(minusVR, intersection.material.nShininess));
	}

	/**
	 * Checks if the intersection point is unshaded by any geometry.
	 * 
	 * @param intersection the intersection point
	 * @return true if the intersection point is unshaded, false otherwise
	 */
	private boolean unshaded(Intersection intersection) {
		if (!intersection.material.kR.lowerThan(MIN_CALC_COLOR_K))
			return false;
		Vector pointToLight = intersection.l.scale(-1);
		Vector delta = intersection.normal.scale(intersection.lNormal < 0 ? DELTA : -DELTA);
		Ray shadowRay = new Ray(intersection.point.add(delta), pointToLight);

		List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay,
				intersection.light.getDistance(intersection.point));
		return intersections == null;

	}

	private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
		Double3 kkx = k.product(kx);
		if (kkx.lowerThan(MIN_CALC_COLOR_K))
			return Color.BLACK;
		Intersection intersection = findClosestIntersection(ray);
		if (intersection == null)
			return scene.background.scale(kx);
		return preprocessIntersection(intersection, ray.getDir()) ? calcColor(intersection, level - 1, kkx).scale(kx)
				: Color.BLACK;
	}

	private Color calcGlobalEffects(Intersection intersection, int level, Double3 k) {
		return calcGlobalEffect(constructTransparencydRay(intersection), level, k, intersection.material.kT)
				.add(calcGlobalEffect(constructReflectedRay(intersection), level, k, intersection.material.kR));
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param level        the current recursion level
	 * @return the color at the intersection point
	 */
	private Color calcColor(Intersection intersection, int level, Double3 k) {
		Color color = calcColorLocalEffects(intersection).add(calcGlobalEffects(intersection, level, k));
//		Color color = intersection.geometry.getEmission();
//		for (LightSource lightSource : scene.lights) {
//			if (!setLightSource(intersection, lightSource) || !unshaded(intersection))
//				continue;
//
//			Color iL = lightSource.getIntensity(intersection.point);
//			color = color.add(iL.scale(calcDiffusive(intersection)), iL.scale(calcSpecular(intersection)));
//		}
		return color;
	}

	private Ray constructReflectedRay(Intersection intersection) {
		Vector v = intersection.v;
		Vector n = intersection.normal;
		double vn = alignZero(v.dotProduct(n)); // v*n
		Vector r;
		if (isZero(vn)) {
			r = v.normalize();
		} else
			r = v.subtract(n.scale(2 * vn)).normalize();// n*2*vn
		Vector delta = intersection.normal.scale(intersection.vNormal < 0 ? DELTA : -DELTA);
		Point newOrigin = intersection.point.add(delta);
		return new Ray(newOrigin, r); // new Ray{point,v-2*(v*n)*n}
	}

	private Ray constructTransparencydRay(Intersection intersection) {
		Vector delta = intersection.normal.scale(intersection.vNormal < 0 ? DELTA : -DELTA);
		Point newOrigin = intersection.point.add(delta);
		return new Ray(newOrigin, intersection.v);

	}

}
