/**
 * 
 */
package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * 
 */
public class SpotLight extends PointLight {

	private Vector direction;

	/**
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
		double spotIntensity = Math.max(0, direction.dotProduct(getL(p)));
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

}
