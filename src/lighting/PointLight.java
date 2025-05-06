package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

	private final Point position;
	private double kC = 1, kL = 0, kQ = 0;

	/**
	 * Constructs a point light with the specified intensity and position.
	 *
	 * @param intensity the color intensity of the point light
	 * @param position  the position of the point light
	 */
	public PointLight(Color intensity, Point position) {
		super(intensity);
		this.position = position;
	}

	@Override
	public Color getIntensity(Point p) {
		double distance = position.distance(p);
		return getIntensity().scale(1 / (kC + kL * distance + kQ * distance * distance));
	}

	@Override
	public Vector getL(Point p) {
		return (p.subtract(position)).normalize();
	}

	/**
	 * Sets the constant attenuation factor.
	 *
	 * @param kC the constant attenuation factor
	 * @return this PointLight object
	 */
	public PointLight setKc(double kC) {
		this.kC = kC;
		return this;
	}

	/**
	 * Sets the linear attenuation factor.
	 *
	 * @param kL the linear attenuation factor
	 * @return this PointLight object
	 */
	public PointLight setKl(double kL) {
		this.kL = kL;
		return this;
	}

	/**
	 * Sets the quadratic attenuation factor.
	 *
	 * @param kQ the quadratic attenuation factor
	 * @return this PointLight object
	 */
	public PointLight setKq(double kQ) {
		this.kQ = kQ;
		return this;
	}

}
