/**
 * 
 */
package renderer;

import java.util.List;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

/**
 * 
 */
public class SimpleRayTracer extends RayTracerBase {

	/**
	 * constrctor - call the father constrctor
	 * 
	 * @param scene
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

	public Color calcColor(Point p) {
		// TODO לא בטוחה שזה הצבעים הנכונים באמת - לבדוק
		return this.scene.ambientLight.getIntensity();
	}

}
