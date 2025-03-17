package primitives;

public class Point 
{
final protected Double3 xyz ;
	 
	 public Point (double x, double y, double z){
		 xyz=new Double3(x,y,z);
	 }
	 public Point(Double3 d3) {
		 xyz=d3;
	 }
	 
	 @Override
	 public boolean equals(Object obj) {
	 if (this == obj) return true;
	 return (obj instanceof Point other) && this.xyz.equals(other.xyz);
	 }
	 @Override
	 public String toString() {
		 return "Point:"+this.xyz.toString();
	 }
	 public Point add(Vector vec) {
		return new Point( xyz.add(vec.xyz));
	 }
	 public Vector subtract(Point p) {
		return new Vector(xyz.subtract(p.xyz));
	 }
	 public double distance(Point p) {
		return Math.sqrt(this.distanceSquared(p));
	 }
	 public  double distanceSquared(Point p) {
		 return (this.xyz.d1()-p.xyz.d1())*(this.xyz.d1()-p.xyz.d1())+
				 (this.xyz.d2()-p.xyz.d2())*(this.xyz.d2()-p.xyz.d2())+
				 (this.xyz.d3()-p.xyz.d3())*(this.xyz.d3()-p.xyz.d3());
	 }
	 
	 
	 
	 
}
