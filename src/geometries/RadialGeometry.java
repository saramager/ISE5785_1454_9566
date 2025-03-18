package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * Represents a radial geometric shape in 3D space, characterized by a radius.
 * This is an abstract class that serves as a base for shapes like spheres, cylinders, and tubes.
 */
public abstract class RadialGeometry extends Geometry {

    /** 
     * The radius of the geometric shape.
     */
    protected final double radius;

    /**
     * Constructs a radial geometry object with a given radius.
     *
     * @param radius the radius of the shape
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }

    /**
     * Returns the normal vector to the surface at a given point.
     * This method is not implemented in this class and should be overridden by subclasses.
     *
     * @param p the point at which to calculate the normal
     * @return the normal vector (returns {@code null} by default)
     */
    @Override
    public Vector getNormal(Point p) {
        // TODO: Implement in subclasses
        return null;
    }
}
