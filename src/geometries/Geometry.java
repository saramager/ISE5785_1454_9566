package geometries;
import primitives.Point;
import primitives.Vector;

/**
 * Abstract class representing a geometric shape in 3D space.
 */
public abstract class Geometry {

    /**
     * Calculates and returns the normal vector to the geometry at a given point.
     *
     * @param p the point at which to calculate the normal
     * @return the normal vector to the geometry at the given point
     */
    public abstract Vector getNormal(Point p);
}
