
package renderer;

import java.util.List;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
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
		List<Point> intersections = this.scene.geometries.findIntersections(ray);
		if (intersections == null)
			return this.scene.background;
		Point closestP = ray.findClosestPoint(intersections);
		return calcColor(closestP);

	}

	/**
	 * Calculates the color at a given point.
	 * 
	 * @param p the point at which to calculate the color
	 * @return the color at the given point
	 */
	public Color calcColor(Point p) {
		return this.scene.ambientLight.getIntensity();
	}

}
