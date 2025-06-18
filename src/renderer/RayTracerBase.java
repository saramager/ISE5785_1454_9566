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
	 * The number of rays used for anti-aliasing in the ray tracing.
	 */
	protected int antiAlasingNumOfRays = 1;

	/**
	 * The size of the anti-aliasing rays used in the ray tracing. This value is set
	 * to 0.1 for anti-aliasing and 0 for non-anti-aliasing.
	 */
	protected double antiAlasingSize = 0;

	/**
	 * The number of rays used for glossy and diffuse reflections.
	 */
	protected int glossyAndDiffuseNumOfRay = 1;

	/**
	 * Sets the number of rays to be used for ray tracing.
	 *
	 * @param numOfRays the number of rays to be used
	 * @return the updated RayTracerBasic object
	 */
	public RayTracerBase antiAlassingSetRays(int numOfRays) {
		antiAlasingNumOfRays = numOfRays;
		return this;
	}

	/**
	 * Sets the size of the anti-aliasing rays.
	 *
	 * @param isAnitalassing true if anti-aliasing is enabled, false otherwise
	 * @return the updated RayTracerBasic object
	 */
	public RayTracerBase setSize(double size) {
		this.antiAlasingSize = size;
		return this;
	}

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

	/**
	 * Traces the given ray through the scene and returns the resulting color. This
	 * method must be implemented by concrete subclasses.
	 *
	 * @param ray the ray to trace
	 * @return the color resulting from the ray tracing
	 */
	public Color traceRay(Ray ray) {
		// If anti-aliasing is enabled, construct a grid of rays
		// otherwise, trace the ray directly
		return antiAlasingNumOfRays > 1 ? traceAntiAliasing(ray) : traceRayHelper(ray);
	}

	/**
	 * Traces the given ray through the scene and returns the resulting color. This
	 * method must be implemented by concrete subclasses.
	 *
	 * @param ray the ray to trace
	 * @return the color resulting from the ray tracing
	 */
	protected Color traceAntiAliasing(Ray ray) {
		var rays = new Blackboard(ray, antiAlasingSize, antiAlasingNumOfRays).constructRayBeamGrid();

		// If no rays were generated, return black
		Color color = Color.BLACK;
		for (Ray secondRay : rays)
			color = color.add(traceRayHelper(secondRay));
		return color.reduce(antiAlasingNumOfRays);
	}

	/**
	 * Traces the given ray through the scene and returns the resulting color. This
	 * method must be implemented by concrete subclasses.
	 *
	 * @param ray the ray to trace
	 * @return the color resulting from the ray tracing
	 */
	protected abstract Color traceRayHelper(Ray ray);

}
