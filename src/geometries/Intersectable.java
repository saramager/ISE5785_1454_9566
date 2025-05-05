/**
 * 
 */
package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;

/**
 * interface help with all the intersction
 */
public interface Intersectable {
	public static class Intersection {
		public final Geometry geometry;
		public final Point point;

		public Intersection(Geometry geometry, Point point) {
			this.geometry = geometry;
			this.point = point;
		}

		@Override
		public String toString() {
			return "Intersection [" + geometry + "," + point + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj instanceof Intersection) {
				Intersection other = (Intersection) obj;
				return this.geometry == other.geometry && this.point.equals(other.point);
			}
			return false;
		}
	}

	/**
	 * find the intersections point between an gematry and ray
	 * 
	 * @param ray -check intsersectuion for ray
	 * @return list of Points that intsersectuion
	 */
	List<Point> findIntersections(Ray ray);

	/**
	 * TODO protected find the intersections point between an gematry and ray
	 * 
	 * @param ray -check intsersectuion for ray
	 * @return list of Intersections that intsersectuion
	 */
	List<Intersection> calculateIntersectionsHelper(Ray ray);

}
