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
 * interface help with all the intersection
 */
public abstract class Intersectable {

	/**
	 * inner class to help with the intersection
	 */
	public static class Intersection {
		/**
		 * the geometry that intersect with the ray
		 */
		public final Geometry geometry;
		/**
		 * the point that intersect with the ray
		 */
		public final Point point;
		/**
		 * the material of the geometry
		 */
		public final Material material;
		/**
		 * the normal of the geometry
		 */
		public Vector normal;
		/**
		 * the vector of the ray
		 */
		public Vector v;
		/**
		 * the dot product of the vector and the normal
		 */
		public double vNormal;
		/**
		 * the light source that intersect with the ray
		 */
		public LightSource light;
		/**
		 * the vector the light source points to.
		 */
		public Vector l;
		/**
		 * the dot product of the light source and the normal
		 */
		public double lNormal;

		/**
		 * constructor for the intersection
		 * 
		 * @param geometry - the geometry that intersect with the ray
		 * @param point    - the point that intersect with the ray
		 */
		public Intersection(Geometry geometry, Point point) {
			this.geometry = geometry;
			this.point = point;
			this.material = geometry == null ? null : geometry.getMaterial();
		}

		@Override
		public String toString() {
			return "Intersection [" + geometry + "," + point + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			return (obj instanceof Intersection other) //
					&& geometry == other.geometry && point.equals(other.point);
		}

	}

	/**
	 * find the intersections point between an gematry and ray
	 * 
	 * @param ray check intsersectuion for ray
	 * @return list of Points that intsersectuion
	 */
	public final List<Point> findIntersections(Ray ray) {
		var intersections = calculateIntersections(ray);
		return intersections == null ? null : intersections.stream().map(intersection -> intersection.point).toList();
	}

	/**
	 * help method to find the intersections point between an geometry and ray
	 * 
	 * @param ray -check intersection for ray
	 * @return list of Intersections that intersection
	 */
//	protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);

	/**
	 * find the intersections point between an geometry and ray
	 * 
	 * @param ray - check intersection for ray
	 * @return list of Intersections that intersection
	 */
	public final List<Intersection> calculateIntersections(Ray ray) {
		return calculateIntersections(ray, Double.POSITIVE_INFINITY);
		// return calculateIntersectionsHelper(ray);
	}

	/**
	 * find the intersections point between an geometry and ray
	 * 
	 * @param ray         - check intersection for ray
	 * @param maxDistance - the max distance to check for intersection
	 * @return list of Intersections that intersection
	 */
	public final List<Intersection> calculateIntersections(Ray ray, double maxDistance) {
		return calculateIntersectionsHelper(ray, maxDistance);
	}

	/**
	 * help method to find the intersections point between an geometry and ray
	 * 
	 * @param ray         check intersection for ray
	 * @param maxDistance the max distance to check for intersection
	 * @return list of Intersections that intersection
	 */
	protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance);

}
