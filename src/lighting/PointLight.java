package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource {

	protected final Point position;
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

}
