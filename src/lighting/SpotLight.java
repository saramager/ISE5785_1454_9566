/**
 * 
 */
package lighting;

import static primitives.Util.alignZero;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * The SpotLight class represents a point light source with a specific direction
 * in a 3D scene. It extends the PointLight class and adds functionality for
 * directional lighting.
 */
public class SpotLight extends PointLight {

	/**
	 * The direction of the light source.
	 */
	private final Vector direction;
	/**
	 * The narrow beam angle of the light source.
	 */
	private double narrowBeam = 1;

	/**
	 * Constructs a SpotLight with the specified intensity, position, and direction.
	 * 
	 * @param intensity= the color intensity of the light
	 * @param position=  the position of the light
	 * @param direction= the direction of the light
	 */
	public SpotLight(Color intensity, Point position, Vector direction) {
		super(intensity, position);
		this.direction = direction.normalize();
	}

	@Override
	public Color getIntensity(Point p) {
		double spotIntensity = direction.dotProduct(getL(p));
		if (alignZero(spotIntensity) <= 0)
			return Color.BLACK;
		spotIntensity = Math.pow(spotIntensity, narrowBeam);
		return super.getIntensity(p).scale(spotIntensity);
	}

	@Override
	public SpotLight setKc(double kC) {
		super.setKc(kC);
		return this;
	}

	@Override
	public SpotLight setKl(double kL) {
		super.setKl(kL);
		return this;
	}

	@Override
	public SpotLight setKq(double kQ) {
		super.setKq(kQ);
		return this;
	}

	/**
	 * Sets the direction of the light source.
	 * 
	 * @param narrowBeam the direction of the light source
	 * @return this SpotLight object
	 */
	public SpotLight setNarrowBeam(double narrowBeam) {
		if (narrowBeam >= 1)
			this.narrowBeam = narrowBeam;
		return this;
	}

}
