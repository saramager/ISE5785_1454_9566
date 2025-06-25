/**
 * 
 */
package renderer;

import java.util.List;

import geometries.Geometries;
import geometries.Intersectable.Intersection;
import primitives.Color;
import primitives.Double3;
import primitives.Ray;
import scene.Scene;

/**
 * 
 */
public class GridRayTracer extends SimpleRayTracer {

	private Grid grid;

	/**
	 * constructor for the ray tracer grid
	 * 
	 * @param scene
	 * @param density
	 */

	public GridRayTracer(Scene scene, int density) {
		super(scene);
		grid = new Grid(scene.geometries, density);
	}

	@Override
	public Color traceRay(Ray ray) {
		return super.traceRay(ray);
	}

	@Override
	protected Intersection findClosestIntersection(Ray ray) {
		Geometries infinities = grid.getInfinityGeometries();
		Intersection closestPoint = null;
		var points = infinities.calculateIntersections(ray);
		if (infinities != null && points != null)
			closestPoint = ray.findClosestIntersection(points);
		double result = grid.cutsGrid(ray);
		if (result == Double.POSITIVE_INFINITY) {
			return null;
		}

		double distanceToOuterGeometries = closestPoint == null ? Double.POSITIVE_INFINITY
				: closestPoint.point.distance(ray.getHead());
		if (distanceToOuterGeometries < result) {
			return closestPoint;
		}

		var intersections = grid.traverse(ray);
		Intersection point = ray.findClosestIntersection(intersections);
		if (point != null) {
			return point.point.distance(ray.getHead()) < distanceToOuterGeometries ? point : closestPoint;
		}
		return closestPoint;
	}

	@Override
	protected Double3 transparency(Intersection intersection) {
		Ray shadowRay = new Ray(intersection.point, intersection.l.scale(-1), intersection.normal);

		double maxDist = intersection.light.getDistance(intersection.point);

		// Find all intersections along the transparency ray
		List<Intersection> intersections = grid.traverse(shadowRay, true, maxDist);
		System.out.println(intersections);
		if (intersections == null)
			return Double3.ONE;

		Double3 ktr = Double3.ONE; // Transparency factor
		for (Intersection inter : intersections) {
			ktr = ktr.product(inter.material.kT); // Accumulate transparency factors
			if (ktr.lowerThan(MIN_CALC_COLOR_K))
				return Double3.ZERO; // Stop if transparency is negligible
		}
		return ktr;
	}
//	@Override
//	protected Double3 transparency(Intersection intersection) {
//		Ray shadowRay = new Ray(intersection.point, intersection.l.scale(-1), intersection.normal);
//
//		List<Intersection> intersections;
//		double res = grid.cutsGrid(shadowRay);
//
//		if (res == Double.POSITIVE_INFINITY) {
//			intersections = grid.getInfinityGeometries().calculateIntersections(shadowRay,
//					intersection.light.getDistance(intersection.point));
//			intersections = intersections != null
//					? intersections.stream().map(gp -> new Intersection(gp.geometry, gp.point)).toList()
//					: null;
//		} else {
//			intersections = grid.traverse(shadowRay, true);
//		}
//
//		Double3 ktr = Double3.ONE;
//		if (intersections == null)
//			return ktr;
//
//		for (Intersection p : intersections) {
//			if (p.material == null)
//				continue;
//			ktr = ktr.product(p.material.kT);
//			if (ktr.lowerThan(MIN_CALC_COLOR_K))
//				return Double3.ZERO;
//		}
//
//		return ktr;
//	}

}
