package geometries;

import java.util.List;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;
import java.util.List;

/**
 * Represents a plane in 3D space, defined by either three points or a point and
 * a normal vector.
 */
public class Plane extends Geometry {

	/**
	 * A point on the plane.
	 */
	private final Point p;

	/**
	 * The normal vector of the plane.
	 */
	private final Vector normal;

	/**
	 * Constructs a plane using three points in 3D space. The normal vector is
	 * computed using the cross product of two edges.
	 *
	 * @param p1 the first point on the plane
	 * @param p2 the second point on the plane
	 * @param p3 the third point on the plane
	 */
	public Plane(Point p1, Point p2, Point p3) {
		p = p1;
		Vector v1 = p2.subtract(p1);
		Vector v2 = p3.subtract(p1);
		normal = v1.crossProduct(v2).normalize();
	}

	/**
	 * Constructs a plane using a point and a normal vector.
	 *
	 * @param point  a point on the plane
	 * @param normal the normal vector of the plane (normalized automatically)
	 */
	public Plane(Point point, Vector normal) {
		this.p = point;
		this.normal = normal.normalize();
	}

	@Override
	public Vector getNormal(Point p) {
		return this.normal;
	}

	/*
	 * @Override public List<Point> findIntersections(Ray ray) { Point p0 =
	 * ray.getHead(); Vector vec = ray.getDir(); if (p.equals(p0)) return null;
	 * Vector vecP0P = p.subtract(p0); double denominator = normal.dotProduct(vec);
	 * if (Util.isZero(denominator) || denominator < 0) //
	 * (Util.isZero(denominator)) return null; double t = (normal.dotProduct(vecP0P)
	 * / denominator); System.out.println(t); if (!Util.isZero(t) && t < 0) {
	 * 
	 * return List.of(p0.add(vec.scale(t)));// ray.getPoint(t1);
	 * 
	 * } return null;
	 * 
	 * }
	 */
	@Override
	public List<Point> findIntersections(Ray ray) {
		Point p0 = ray.getHead();
		Vector vec = ray.getDir();
		if (p.equals(p0))
			return null;
		Vector vecP0P = p.subtract(p0);
		double denominator = normal.dotProduct(vec);
		if (Util.isZero(denominator) || denominator < 0) // (Util.isZero(denominator))
			return null;
		double t = (normal.dotProduct(vecP0P) / denominator);
		System.out.println(t);
		if (!Util.isZero(t) && t > 0)
			return List.of(p0.add(vec.scale(t)));// ray.getPoint(t1);
		return null;
	}
}
