/**
 * 
 */
package renderer;

import java.util.List;

import geometries.Intersectable.Intersection;
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
	protected List<Intersection> calculateIntersections(Ray ray, double distance) {
		return grid.traverse(ray, distance);
	}

}
