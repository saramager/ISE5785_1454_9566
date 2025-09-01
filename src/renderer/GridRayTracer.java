/**
 * 
 */
package renderer;

import java.util.List;

import geometries.Intersectable.Intersection;
import primitives.Ray;
import scene.Scene;

/**
 * GridRayTracer is a ray tracer that uses a grid to accelerate the intersection
 * calculations. It extends the SimpleRayTracer class and uses a Grid object to
 * traverse the scene geometries. It calculates intersections by traversing the
 * grid and returning the intersections found within a specified distance. This
 * class is part of a rendering engine that optimizes ray tracing by using
 * spatial partitioning techniques.
 * 
 */
public class GridRayTracer extends SimpleRayTracer {
	/**
	 * The grid used for accelerating ray tracing. It is initialized with the
	 * geometries of the scene.
	 */
	private Grid grid;

	/**
	 * constructor for the ray tracer grid
	 * 
	 * @param scene the scene to be rendered
	 */

	public GridRayTracer(Scene scene) {
		super(scene);
		grid = new Grid(scene.geometries);
	}

	@Override
	protected List<Intersection> calculateIntersections(Ray ray, double distance) {
		return grid.traverse(ray, distance);
	}

}
