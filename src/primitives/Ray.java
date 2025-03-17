package primitives;

public class Ray {
	 final private Point head ;
	 final private Vector direction;
public Ray (Point point ,Vector vector ) {
	this.direction = vector.normalize();
	this.head= point ;
}
@Override
public boolean equals(Object obj) {
if (this == obj) return true;
return (obj instanceof Ray other) && this.head.equals(other.head)&& this.direction.equals(other.direction);
}
@Override
public String toString() {
	 return  this.head.toString()+this.direction.toString();
}
}
