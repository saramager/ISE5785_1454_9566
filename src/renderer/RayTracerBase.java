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
	 * The number of rays used for glossy and diffuse reflections.
	 */
	protected int glossyAndDiffuseNumOfRay = 1;

	/**
	 * Traces a ray through the scene and returns the resulting color.
	 *
	 * @param ray the ray to be traced
	 * @return the color resulting from tracing the ray
	 */
	public abstract Color traceRay(Ray ray);

	/**
	 * Sets the number of rays to be used for glossy and diffuse reflections.
	 *
	 * @param numOfRays the number of rays to be used
	 * @return the updated RayTracerBasic object
	 */

	public RayTracerBase glossyAndDiffuseSetRays(int numOfRays) {
		glossyAndDiffuseNumOfRay = numOfRays;
		return this;
	}

}
