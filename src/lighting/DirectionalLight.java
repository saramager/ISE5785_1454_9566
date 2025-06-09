/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The DirectionalLight class represents a directional light source in a 3D
 * scene.
 */
public class DirectionalLight extends Light implements LightSource {
	/**
	 * The direction of the light source.
	 */
	private final Vector direction;

	/**
	 * Constructs a directional light with the specified intensity and direction.
	 *
	 * @param intensity the color intensity of the light
	 * @param direction the direction in which the light is emitted, normalized.
	 */
	public DirectionalLight(Color intensity, Vector direction) {
		super(intensity);
		this.direction = direction.normalize();
	}

	/**
	 * Returns the direction of the light source.
	 *
	 * @return the direction of the light source as a Vector
	 */
	@Override
	public Color getIntensity(Point point) {
		return intensity;
	}

	/**
	 * Returns the direction of the light source.
	 *
	 * @param point the point at which to get the direction
	 * @return the direction of the light source as a Vector
	 */
	@Override
	public Vector getL(Point point) {
		return direction;
	}

	@Override
	public double getDistance(Point point) {
		return Double.POSITIVE_INFINITY;
	}

}
