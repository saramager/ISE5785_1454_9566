/**
 * 
 */
package primitives;

/**
 * 
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
	 * @param nShininess
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

}
