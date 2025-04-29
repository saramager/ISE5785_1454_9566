package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing engines. Holds a reference to the scene
 * and defines the contract for calculating the color resulting from tracing a
 * ray through the scene.
 */
public abstract class RayTracerBase {

	/**
	 * The scene to be rendered. Immutable and accessible to subclasses.
	 */
	protected final Scene scene;

	/**
	 * Constructs a RayTracerBase with the given scene.
	 *
	 * @param scene the scene to be used for ray tracing
	 */
	public RayTracerBase(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Traces the given ray through the scene and returns the resulting color. This
	 * method must be implemented by concrete subclasses.
	 *
	 * @param ray the ray to trace
	 * @return the color resulting from the ray tracing
	 */
	public abstract Color traceRay(Ray ray);
}
