
package renderer;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
	 * Constructor - initializes the ray tracer with a given scene.
	 * 
	 * @param scene the scene to be used for ray tracing
	 */
	public SimpleRayTracer(Scene scene) {
		super(scene);

	}

	@Override
	public Color traceRay(Ray ray) {
//		List<Point> intersections = this.scene.geometries.findIntersections(ray);
//		if (intersections == null)
//			return scene.background;
//		Point closestP = ray.findClosestPoint(intersections);
//		return calcColor(closestP);

		var intersections = scene.geometries.calculateIntersections(ray);
		return intersections == null ? scene.background : calcColor(ray.findClosestIntersection(intersections), ray);
	}

	/**
	 * Calculates the color at a given point.
	 * 
	 * @param p the point at which to calculate the color
	 * @return the color at the given point
	 */
//	public Color calcColor(Point p) {
//		return this.scene.ambientLight.getIntensity();

	// return scene.ambientLight.getIntensity().add(gp.geometry.getEmission());

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param ray          the ray that intersects with the geometry
	 * @return the color at the intersection point
	 */
	private Color calcColor(Intersection intersection, Ray ray) {
		if (preprocessIntersection(intersection, ray.getDir()))
			return Color.BLACK;
		return scene.ambientLight.getIntensity().scale(intersection.material.kA).add(calcLocalEffects(intersection));
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
		intersection.vNormal = intersection.v.dotProduct(intersection.normal);
		return !isZero(intersection.vNormal);
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param ray          the ray that intersects with the geometry
	 * @return the color at the intersection point
	 */
	private void setLightSource(Intersection intersection, LightSource light) {

		intersection.light = light;
		intersection.l = light.getL(intersection.point);
		intersection.lNormal = intersection.l.dotProduct(intersection.normal);
		// return !isZero(intersection.lNormal);

	}

	private Color calcLocalEffects(Intersection intersection) {
		if (intersection.vNormal > 0)
			intersection.normal = intersection.normal.scale(-1);
		Color color = intersection.geometry.getEmission();
		for (LightSource lightSource : scene.lights) {
			setLightSource(intersection, lightSource);
			double nl = alignZero(intersection.normal.dotProduct(intersection.l));
			if (nl * intersection.vNormal > 0) { // sign(nl) == sign(nv)
				Color iL = lightSource.getIntensity(intersection.point);
				color = color.add(iL.scale(calcDiffusive(intersection)).add(calcSpecular(intersection)));
			}
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
		return intersection.material.kD * intersection.lNormal
				/ (intersection.l.length() * intersection.normal.length());

	}

	/**
	 * Calculates the specular color component at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @return the specular color component
	 */
	private Double3 calcSpecular(Intersection intersection) {
		double nv = intersection.vNormal;
		double nl = intersection.lNormal;
		double kS = intersection.material.kS;
		double kG = intersection.material.kG;
		double shininess = intersection.material.shininess;

		if (kS == 0 || kG == 0)
			return Color.BLACK;

		Vector r = intersection.l.subtract(intersection.normal.scale(2 * nl)).normalize();
		double vr = alignZero(r.dotProduct(intersection.v));
		if (vr <= 0)
			return Color.BLACK;

		return intersection.light.getIntensity(intersection.point)
				.scale(kS * Math.pow(vr / (intersection.v.length() * intersection.normal.length()), shininess));
	}

}
