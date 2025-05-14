
package renderer;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.List;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
import primitives.Double3;
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
	 * Constructor - initializes the ray tracer with a given scene.
	 * 
	 * @param scene the scene to be used for ray tracing
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);

	}

	@Override
	public Color traceRay(Ray ray) {
		var intersections = scene.geometries.calculateIntersections(ray);
		return intersections == null ? scene.background : calcColor(ray.findClosestIntersection(intersections), ray);
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
				.add(calcColorLocalEffects(intersection));
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

	private boolean unshaded(Intersection intersection) {
		Vector pointToLight = intersection.l.scale(-1);
		Vector delta = intersection.normal.scale(intersection.lNormal < 0 ? DELTA : -DELTA);
		Ray shadowRay = new Ray(intersection.point.add(delta), pointToLight);

		List<Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);
		return intersections == null;
	}

}
