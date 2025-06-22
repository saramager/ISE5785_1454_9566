/**
 * 
 */
package renderer;

import java.util.List;

import geometries.Geometries;
import geometries.Intersectable.Intersection;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

/**
 * 
 */
public class GridRayTracer extends RayTracerBase {

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
	protected Intersection findClosestIntersection(Ray ray) {
		Geometries infinities = grid.getInfinityGeometries();
		Intersection closestPoint = null;
		var points = infinities.calculateIntersections(ray);
		if (infinities != null && !points.isEmpty())
			closestPoint = ray.findClosestIntersection(points);
		double result = grid.cutsGrid(ray);
		if (result == Double.POSITIVE_INFINITY)
			return null;

		double distanceToOuterGeometries = closestPoint == null ? Double.POSITIVE_INFINITY
				: closestPoint.point.distance(ray.getPoint());
		if (distanceToOuterGeometries < result)
			return closestPoint;

		var intersections = grid.traverse(ray, false);
		Intersection point = ray.findClosestIntersection(intersections);
		if (point != null) {
			return point.point.distance(ray.getPoint(distanceToOuterGeometries)) < distanceToOuterGeometries ? point
					: closestPoint;
		}
		return closestPoint;
	}

	}

	@Override
	protected Color calcColor(Intersection intersection, Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}@Override

}
