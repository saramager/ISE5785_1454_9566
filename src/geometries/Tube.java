package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

/**
 * Represents an infinite tube in 3D space, defined by a central axis (a ray) and a radius.
 */
public class Tube extends RadialGeometry {

    /** 
     * The central axis of the tube, represented as a ray.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with a given radius and axis ray.
     *
     * @param radius the radius of the tube
     * @param ray    the central axis of the tube
     */
    public Tube(double radius, Ray ray) {
        super(radius);
        axis = ray;
    }

    /**
     * Calculates and returns the normal vector to the tube at a given point on its surface.
     * 
     * @param p the point on the tube's surface
     * @return the normal vector at the given point
     */
    @Override
    public Vector getNormal(Point p) {
        // TODO: Implement the correct normal calculation
        return null;
    }
}
