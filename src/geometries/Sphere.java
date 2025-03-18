package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a sphere in 3D space, defined by a center point and a radius.
 */
public class Sphere extends RadialGeometry {

    /** 
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a sphere with a given center point and radius.
     *
     * @param p      the center point of the sphere
     * @param radius the radius of the sphere
     */
    public Sphere(Point p, Double radius) {
        super(radius);
        this.center = p;
    }

    /**
     * Calculates and returns the normal vector to the sphere at a given point.
     * The normal is the vector from the sphere's center to the given point, normalized.
     *
     * @param p the point on the sphere's surface
     * @return the normalized normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        return p.subtract(center).normalize();
    }
}
