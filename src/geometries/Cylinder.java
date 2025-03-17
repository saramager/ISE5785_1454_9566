package geometries;

import primitives.Ray;

public class Cylinder extends Tube {
	private final double height ;
	public Cylinder(double radius, Ray ray, double height ) {
		super(radius,ray);
		this.height= height;
	}

}
