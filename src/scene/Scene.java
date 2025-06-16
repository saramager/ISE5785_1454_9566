package scene;

import java.util.LinkedList;
import java.util.List;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;
import renderer.Blackboard;

/**
 * Represents a 3D scene containing background color, ambient lighting, and
 * geometrical objects. This is a plain data structure (PDS) with public fields.
 */
public class Scene {

	/**
	 * The name of the scene (cannot be changed after construction).
	 */
	public final String name;

	/**
	 * The background color of the scene. Defaults to black.
	 */
	public Color background = Color.BLACK;

	/**
	 * The ambient light of the scene. Defaults to AmbientLight.NONE.
	 */
	public AmbientLight ambientLight = AmbientLight.NONE;

	/**
	 * The geometries contained in the scene. Defaults to an empty collection.
	 */
	public Geometries geometries = new Geometries();

	/**
	 * The list of light sources in the scene. Defaults to an empty collection.
	 */
	public List<LightSource> lights = new LinkedList<>();
	/**
	 * Blackboard used for rendering improvements
	 */
	public Blackboard blackboard = new Blackboard();
	/**
	 * The anti-aliasing factor for the scene. Defaults to 0 (no anti-aliasing).
	 */
	public double antiAliasing = 0;

	/**
	 * Constructs a Scene with the given name.
	 *
	 * @param name the name of the scene
	 */
	public Scene(String name) {
		this.name = name;
	}

	/**
	 * Sets the background color of the scene.
	 *
	 * @param background the background color
	 * @return the scene object itself (for method chaining)
	 */
	public Scene setBackground(Color background) {
		this.background = background;
		return this;
	}

	/**
	 * Sets the ambient light of the scene.
	 *
	 * @param ambientLight the ambient light to use
	 * @return the scene object itself (for method chaining)
	 */
	public Scene setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
		return this;
	}

	/**
	 * Sets the geometries of the scene.
	 *
	 * @param geometries the geometric model to use
	 * @return the scene object itself (for method chaining)
	 */
	public Scene setGeometries(Geometries geometries) {
		this.geometries = geometries;
		return this;
	}

	/**
	 * Adds a light sources to the scene.
	 *
	 * @param lights the lights sources list to add
	 * @return the scene object itself (for method chaining)
	 */
	public Scene setLights(List<LightSource> lights) {
		this.lights = lights;
		return this;
	}

	/**
	 * Adds a light source to the scene.
	 *
	 * @param blackboard the blackboard to use for rendering improvements
	 * @return the scene object itself (for method chaining)
	 */
	public Scene setBlackboard(Blackboard blackboard) {
		this.blackboard = blackboard;
		return this;

	}

	/**
	 * Sets the anti-aliasing factor for the scene.
	 *
	 * @param antiAliasing the anti-aliasing factor to set
	 * @return the scene object itself (for method chaining)
	 */
	public Scene setAntiAliasing(double antiAliasing) {
		this.antiAliasing = antiAliasing;
		return this;

	}
}
