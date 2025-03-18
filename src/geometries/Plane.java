package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a plane in 3D space, defined by either three points or a point and a normal vector.
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
     * Constructs a plane using three points in 3D space.
     * The normal vector is computed using the cross product of two edges.
     *
     * @param p1 the first point on the plane
     * @param p2 the second point on the plane
     * @param p3 the third point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        p = p1;
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        normal = v1.crossProduct(v2).normalize(); // Compute the normal correctly
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

    /**
     * Returns the normal vector to the plane at a given point.
     * Since the normal is the same at all points on the plane, the parameter is ignored.
     *
     * @param p the point at which to get the normal (ignored)
     * @return the normal vector of the plane
     */
    @Override
    public Vector getNormal(Point p) {
        return this.normal;
    }
}
