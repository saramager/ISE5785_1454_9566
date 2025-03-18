package primitives;

/**
 * Represents a vector in 3D space, which is a subclass of {@link Point}.
 * A vector has a direction and a magnitude (length), and it provides various vector operations.
 */
public class Vector extends Point {

    /**
     * Constructs a vector with the given x, y, and z coordinates.
     * Throws an exception if the vector is a zero vector.
     *
     * @param x the x-coordinate of the vector
     * @param y the y-coordinate of the vector
     * @param z the z-coordinate of the vector
     * @throws IllegalArgumentException if the vector is the zero vector (0, 0, 0)
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("the vector is ZERO");
    }

    /**
     * Constructs a vector from a {@link Double3} object.
     * Throws an exception if the vector is a zero vector.
     *
     * @param d3 the {@link Double3} object representing the vector coordinates
     * @throws IllegalArgumentException if the vector is the zero vector (0, 0, 0)
     */
    public Vector(Double3 d3) {
        super(d3);
        if (this.xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("the vector is ZERO");
    }

    /**
     * Checks if this vector is equal to another object.
     *
     * @param obj the object to compare
     * @return {@code true} if the object is a {@link Vector} with the same coordinates, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other) && super.equals(obj);
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return a string representing the vector, including its coordinates
     */
    @Override
    public String toString() {
        return "vector" + super.toString();
    }

    /**
     * Adds the given vector to this vector and returns the resulting vector.
     *
     * @param vec the vector to add
     * @return the resulting vector after addition
     */
    public Vector add(Vector vec) {
        return new Vector(this.xyz.add(vec.xyz));
    }

    /**
     * Scales the vector by a scalar value and returns the resulting vector.
     *
     * @param scalar the scalar value by which to scale the vector
     * @return the resulting scaled vector
     */
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * Computes the dot product of this vector with another vector.
     *
     * @param vec the other vector to compute the dot product with
     * @return the result of the dot product
     */
    public double dotProduct(Vector vec) {
        return this.xyz.d1() * vec.xyz.d1() + this.xyz.d2() * vec.xyz.d2() + this.xyz.d3() * vec.xyz.d3();
    }

    /**
     * Computes the cross product of this vector with another vector.
     *
     * @param vec the other vector to compute the cross product with
     * @return the resulting vector from the cross product
     */
    public Vector crossProduct(Vector vec) {
        double u1 = this.xyz.d1();
        double u2 = this.xyz.d2();
        double u3 = this.xyz.d3();

        double v1 = vec.xyz.d1();
        double v2 = vec.xyz.d2();
        double v3 = vec.xyz.d3();

        return new Vector((u2 * v3 - v2 * u3), (v1 * u3 - u1 * v3), (u1 * v2 - v1 * u2));
    }

    /**
     * Computes the squared length (magnitude) of the vector.
     *
     * @return the squared length of the vector
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Computes the length (magnitude) of the vector.
     *
     * @return the length of the vector
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes the vector to have a length of 1.
     *
     * @return a new vector that is the normalized version of this vector
     */
    public Vector normalize() {
        return new Vector(this.xyz.reduce(this.length()));
    }
}
