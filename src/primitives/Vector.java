package primitives;

public class Vector extends Point {
	public Vector(double x, double y, double z) {
		
		super(x, y, z);
		if(this.xyz.equals(Double3.ZERO))
			  throw new IllegalArgumentException("the vector is ZERO");	
		}

	
	public Vector(Double3 d3) {
		super(d3);
		if(this.xyz.equals(Double3.ZERO))
			  throw new IllegalArgumentException("the vector is ZERO");	
	}
	public Vector add(Vector vec) {
		return new Vector(this.xyz.add(vec.xyz));
	}
	public Vector scale(double scalar ) {
		return new Vector(this.xyz.scale(scalar));
	}
	public double dotProduct(Vector vec) {
		return this.xyz.d1()*vec.xyz.d1()+
				this.xyz.d2()*vec.xyz.d2()+
				this.xyz.d3()*vec.xyz.d3();
	}
	public Vector crossProduct(Vector vec) {
		        double u1 = this.xyz.d1();
		        double u2 = this.xyz.d2();
		        double u3 =this.xyz.d3();

		        double v1 = vec.xyz.d1();
		        double v2 = vec.xyz.d2();
		        double v3 = vec.xyz.d3();

		        return new Vector((u2*v3-v2*u3),(v1*u3-u1*v3),(u1*v2-v1*u2));
		    }
public double lengthSquared() {
	return this.dotProduct(this);
}
public double length() {
	return Math.sqrt(this.lengthSquared());
}
public Vector normalize() {
	return new Vector(this.xyz.reduce(this.length()));
}

	
}
