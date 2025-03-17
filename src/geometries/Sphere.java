package geometries;
import primitives.Point;
import primitives.Vector;

public class Sphere extends RadialGeometry {
	
	private final Point center ;
	
	public Sphere (Point p, Double radius)
	{
		super(radius);
		this.center= p;
	}
	@Override
	public Vector getNormal(Point p) {
		// TODO Auto-generated method stub
		return null;
	}


}
