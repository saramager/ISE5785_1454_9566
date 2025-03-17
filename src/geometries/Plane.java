package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane extends Geometry {

	private  final Point p ;
	private final Vector normal;
	
	public Plane (Point p1,Point p2,Point p3){
		p=p1;
		normal=null;
	}
	public Plane (Point point, Vector normal){
		this.p=point;
		this.normal= normal.normalize();
	}
	
	
	@Override
	public Vector getNormal(Point p) {
		return this.normal;
	}
	

}
