package geometries;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

public class Tube extends RadialGeometry {
	protected final Ray axis ;
	public Tube(double radius, Ray ray) {
		super(radius);
		axis= ray;
	}
	@Override
	public Vector getNormal(Point p) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
