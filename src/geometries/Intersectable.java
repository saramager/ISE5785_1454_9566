/**
 * 
 */
package geometries;

import java.util.List;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * interface help with all the intersction
 */
public abstract class Intersectable {

	public static class Intersection {
		public final Geometry geometry;
		public final Point point;
		public final Material material;
		public Vector normal;
		public Vector v;
		public double vNormal;
		public LightSource light;
		public Vector l;
		public double lNormal;

		public Intersection(Geometry geometry, Point point, Material material) {
			this.geometry = geometry;
			this.point = point;
			this.material = material;
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
	public final List<Point> findIntersections(Ray ray) {
		var intersections = calculateIntersections(ray);
		return intersections == null ? null : intersections.stream().map(intersection -> intersection.point).toList();
	}

	/**
	 * help mothode to find the intersections point between an gematry and ray
	 * 
	 * @param ray -check intsersectuion for ray
	 * @return list of Intersections that intsersectuion
	 */
	protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

	/**
	 * find the intersections point between an gematry and ray
	 * 
	 * @param ray - check intsersectuion for ray
	 * @return list of Intersections that intsersectuion
	 */
	public final List<Intersection> calculateIntersections(Ray ray) {
		return calculateIntersectionsHelper(ray);

	}

}
