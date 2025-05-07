
package renderer;

import static primitives.Util.isZero;

import geometries.Intersectable.Intersection;
import lighting.LightSource;
import primitives.Color;
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
		return scene.ambientLight.getIntensity().scale(intersection.material.kA)
				.add(calcLocalEffects(intersection, ray));
	}

	/**
	 * Calculates the color at a given intersection point.
	 * 
	 * @param intersection the intersection point
	 * @param ray          the ray that intersects with the geometry
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

	}

//
//	private Color calcLocalEffects(Intersection intersection, Ray ray) {
//		Vector n = intersection.normal;
//		Vector v = ray.getDir();
//		double nv = alignZero(n.dotProduct(v));
//		if (nv > 0)
//			n = n.scale(-1);
//		Color color = intersection.geometry.getEmission();
//		for (LightSource lightSource : scene.lights) {
//			setLightSource(intersection, lightSource);
//			double nl = alignZero(n.dotProduct(intersection.l));
//			if (nl * nv > 0) { // sign(nl) == sign(nv)
//				Color iL = lightSource.getIntensity(intersection.point);
//				color = color.add(iL.scale(calcDiffusive(intersection.material, nl))
//						.add(calcSpecular(intersection.material, n, intersection.l, nl, v)));
//			}
//		}
//		return color;
//	}

}
