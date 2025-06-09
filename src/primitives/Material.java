/**
 * 
 */
package primitives;

/**
 * Material class represents the material properties of a 3D object -PDS
 */
public class Material {
	/**
	 * The diffuse coefficient.
	 */
	public Double3 kD = Double3.ZERO;
	/**
	 * The specular coefficient.
	 */
	public Double3 kS = Double3.ZERO;
	/**
	 * The shin coefficient.
	 */
	public int nShininess = 0;
	/**
	 * The ambient coefficient.
	 */
	public Double3 kA = Double3.ONE;
	/**
	 * The transparency coefficient.
	 */
	public Double3 kT = Double3.ZERO;
	/**
	 * The reflection coefficient.
	 */
	public Double3 kR = Double3.ZERO;
	/**
	 * The angle of opening the cone of the beam of the beams of transparency
	 */
	public double tAngle = 0.0;
	/**
	 * The angle of opening the cone of the beam of the beams of transparency
	 */
	public double rAngle = 0.0;

	/**
	 * set the diffuse coefficient
	 * 
	 * @param kD= the diffuse coefficient
	 * @return the material after setting the diffuse coefficient
	 */
	public Material setKD(Double3 kD) {
		this.kD = kD;
		return this;
	}

	/**
	 * set the diffuse coefficient
	 * 
	 * @param kD= the diffuse coefficient
	 * @return the material after setting the diffuse coefficient
	 */
	public Material setKD(double kD) {
		this.kD = new Double3(kD);
		return this;
	}

	/**
	 * set the specular coefficient
	 * 
	 * @param kS= the specular coefficient
	 * @return the material after setting the specular coefficient
	 */
	public Material setKS(Double3 kS) {
		this.kS = kS;
		return this;
	}

	/**
	 * set the specular coefficient
	 * 
	 * @param kS= the specular coefficient
	 * @return the material after setting the specular coefficient
	 */
	public Material setKS(double kS) {
		this.kS = new Double3(kS);
		return this;
	}

	/**
	 * set the shininess coefficient
	 * 
	 * @param nShininess = the shininess coefficient
	 * @return the material after setting the shininess coefficient
	 */
	public Material setShininess(int nShininess) {
		this.nShininess = nShininess;
		return this;
	}

	/**
	 * set the ambient coefficient
	 * 
	 * @param kA= the ambient coefficient
	 * @return the material after setting the ambient coefficient
	 */
	public Material setKA(Double3 kA) {
		this.kA = kA;
		return this;
	}

	/**
	 * set the ambient coefficient
	 * 
	 * @param kA= the ambient coefficient
	 * @return the material after setting the ambient coefficient
	 */
	public Material setKA(double kA) {
		this.kA = new Double3(kA);
		return this;
	}

	/**
	 * set the reflection coefficient
	 * 
	 * @param kR= the reflection coefficient
	 * @return the material after setting the reflection coefficient
	 */
	public Material setKR(Double3 kR) {
		this.kR = kR;
		return this;
	}

	/**
	 * set the reflection coefficient
	 * 
	 * @param kR= the reflection coefficient
	 * @return the material after setting the reflection coefficient
	 */
	public Material setKR(double kR) {
		this.kR = new Double3(kR);
		return this;
	}

	/**
	 * set the transparency coefficient
	 * 
	 * @param kT= the transparency coefficient
	 * @return the material after setting the transparency coefficient
	 */
	public Material setKT(Double3 kT) {
		this.kT = kT;
		return this;
	}

	/**
	 * set the transparency coefficient
	 * 
	 * @param kT= the transparency coefficient
	 * @return the material after setting the transparency coefficient
	 */
	public Material setKT(double kT) {
		this.kT = new Double3(kT);
		return this;
	}

	/**
	 * set the angle of opening the cone of the beam of the beams of transparency
	 * 
	 * @param tAngle= the angle of opening the cone of the beam of the beams of
	 *                transparency
	 * @return the material after setting the angle of opening the cone of the beam
	 *         of the beams of transparency
	 */
	public Material setTAngle(double tAngle) {
		this.tAngle = tAngle;
		return this;
	}

	/**
	 * set the angle of opening the cone of the beam of the beams of reflection
	 * 
	 * @param rAngle= the angle of opening the cone of the beam of the beams of
	 *                reflection
	 * @return the material after setting the angle of opening the cone of the beam
	 *         of the beams of reflection
	 */
	public Material setRAngle(double rAngle) {
		this.rAngle = rAngle;
		return this;
	}

}
