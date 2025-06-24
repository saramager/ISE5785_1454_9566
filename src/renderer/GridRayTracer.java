/**
 * 
 */
package renderer;

import java.util.List;

import geometries.Geometries;
import geometries.Intersectable.Intersection;
import primitives.*;
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

		var intersections = grid.traverse(ray, false);
		Intersection point = ray.findClosestIntersection(intersections);
		if (point != null) {
			return point.point.distance(ray.getHead()) < distanceToOuterGeometries ? point : closestPoint;
		}
		return closestPoint;
	}

	@Override
	protected Double3 transparency(Intersection inter) {
		Vector lightDir = inter.l.scale(-1);
		Ray lightRay = new Ray(inter.point, lightDir, inter.normal);

		List<Intersection> intersections;
		double res = grid.cutsGrid(lightRay);

		if (res == Double.POSITIVE_INFINITY) {
			intersections = grid.getInfinityGeometries().calculateIntersections(lightRay);
			intersections = intersections != null
					? intersections.stream().map(gp -> new Intersection(gp.geometry, gp.point)).toList()
					: null;
		} else {
			intersections = grid.traverse(lightRay, true);
		}

		Double3 ktr = Double3.ONE;
		if (intersections == null)
			return ktr;

		for (Intersection p : intersections) {
			if (p.material == null)
				continue;
			ktr = ktr.product(p.material.kT);
			if (ktr.lowerThan(MIN_CALC_COLOR_K))
				return Double3.ZERO;
		}

		return ktr;
	}

}
