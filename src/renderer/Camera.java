/**
 * 
 */
package renderer;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.MissingResourceException;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

/**
 * The Camera class represents a camera in a 3D rendering system. It defines the
 * camera's position, orientation, screen size, and distance to the view plane.
 * 
 */
public class Camera implements Cloneable {

	/**
	 * The position of the camera in 3D space.
	 */
	private Point location;

	/**
	 * The direction vector pointing forward from the camera (toward the scene).
	 */
	private Vector vTo;

	/**
	 * The direction vector pointing upward from the camera.
	 */
	private Vector vUp;

	/**
	 * The direction vector pointing to the right of the camera.
	 */
	private Vector vRight;

	/**
	 * The distance from the camera to the view plane.
	 */
	private double distance = 0.0;

	/**
	 * The height of the view plane.
	 */
	private double height = 0.0;

	/**
	 * The width (length) of the view plane.
	 */
	private double length = 0.0;
	/**
	 * TODO:
	 */
	private ImageWriter imageWriter;
	/**
	 * TODO:
	 */
	private RayTracerBase rayTracer;
	/**
	 * TODO:
	 */
	private int nX;
	/**
	 * TODO:
	 */
	private int nY;

	/**
	 * Empty constructor
	 */
	private Camera() {

	}

	public Camera renderImage() {

		for (int i = 0; i < this.nX; i++)
			for (int j = 0; j < this.nY; j++)
				this.castRay(i, j);
		return this;
	}

	public Camera printGrid(int interval, Color color) {
		for (int i = 0; i < this.nX; i++)
			for (int j = 0; j < this.nY; j++)
				if (i % interval == 0 || j % interval == 0)
					this.imageWriter.writePixel(i, j, color);

		return this;
	}

	/**
	 * get builder camera
	 * 
	 * @return new camera builder
	 */
	public static Builder getBuilder() {
		return new Builder();
	}

	/**
	 * 
	 * @param imageName
	 * @return
	 */
	public Camera writeToImage(String imageName) {
		this.imageWriter.writeToImage(imageName);
		return this;

	}

	/**
	 * create ray from the camera to pixel
	 * 
	 * @param nX the number weigh pixel
	 * @param nY the number high pixel
	 * @param j  the location weigh in the view plan
	 * @param i  the location high in the view plan
	 * @return the ray from the camera to pixel
	 */
	public Ray constructRay(int nX, int nY, int j, int i) {
		Point pCenter = this.location.add(vTo.scale(distance));
		double rY = height / nY;
		double rX = length / nX;
		double xJ = (j - (nX - 1) / 2.0) * rX;
		double yI = -(i - (nY - 1) / 2.0) * rY;
		Point pIJ = pCenter;
		if (xJ != 0)
			pIJ = pIJ.add(vRight.scale(xJ));
		if (yI != 0)
			pIJ = pIJ.add(vUp.scale(yI));
		return new Ray(this.location, pIJ.subtract(this.location).normalize());
	}

	/**
	 * TODO
	 * 
	 * @param i
	 * @param j
	 */
	private void castRay(int i, int j) {
		Ray rayPixel = this.constructRay(nX, nY, i, j);
		Color colorPixel = this.rayTracer.traceRay(rayPixel);
		this.imageWriter.writePixel(i, j, colorPixel);

	}

	/**
	 * internal help class for building camera design patter
	 */
	public static class Builder {
		/**
		 * Camera builder
		 */
		private final Camera camera = new Camera();

		/**
		 * set Location
		 * 
		 * @param location the {@link Point} representing the camera's position
		 * @return the current {@code Builder} instance for method chaining
		 */
		public Builder setLocation(Point location) {

			camera.location = location;
			return this;
		}

		/**
		 * Set the direction of the camera
		 *
		 * @param to the direction of the camera (the vector from the camera to the
		 *           "look-at" point)
		 * @param up the up direction of the camera (the vector from the camera to the
		 *           up direction)
		 * @return the camera builder after set direction
		 */
		public Builder setDirection(Vector to, Vector up) {

			if (!isZero(to.dotProduct(up)))
				throw new IllegalArgumentException("Vectors 'to' and 'up' must be orthogonal");

			camera.vTo = to.normalize();
			camera.vUp = up.normalize();
			return this;
		}

		/**
		 * Sets the orientation of the camera using a target point and an initial up
		 * vector. The method calculates the forward (vTo), right (vRight), and
		 * corrected up (vUp) vectors based on the camera's location and the given
		 * target.
		 *
		 * @param target the point the camera is looking at (must not be equal to the
		 *               camera location)
		 * @param up     an approximate up direction used to compute the orientation
		 * @return the current {@code Builder} instance for method chaining
		 * @throws IllegalArgumentException if the camera location is not set or if the
		 *                                  target equals the camera location
		 */

		public Builder setDirection(Point target, Vector up) {
			if (camera.location == null)
				throw new IllegalArgumentException("Camera location must be set before setting direction");
			if (target.equals(camera.location))
				throw new IllegalArgumentException("Target point cannot be the same as camera location");

			Vector to = target.subtract(camera.location).normalize();
			// If up and to are almost parallel, choose a different up vector (Z axis)
			if (alignZero(to.dotProduct(up)) == 1 || alignZero(to.dotProduct(up)) == -1) {
				up = Vector.AXIS_Z;
			}

			Vector right = to.crossProduct(up).normalize();
			Vector correctedUp = right.crossProduct(to).normalize();

			camera.vTo = to;
			camera.vUp = correctedUp;
			camera.vRight = right;

			return this;
		}

		/**
		 * Sets the camera direction using only a target point. Assumes the approximate
		 * up vector is the Y-axis (0, 1, 0).
		 * 
		 * @param target the point the camera is looking at
		 * @return the builder instance
		 */
		public Builder setDirection(Point target) {
			return setDirection(target, new Vector(0, 1, 0));
		}

		/**
		 * Sets the size of the view plane.
		 * 
		 * @param width  the width of the view plane
		 * @param height the height of the view plane
		 * @return the builder instance
		 * @throws IllegalArgumentException if width or height is not positive
		 */
		public Builder setVpSize(double width, double height) {
			if (width < 0 || isZero(width) || height < 0 || isZero(height))
				throw new IllegalArgumentException("View plane size must be positive");
			camera.length = width;
			camera.height = height;
			return this;
		}

		/**
		 * Sets the distance from the camera to the view plane.
		 * 
		 * @param distance the distance between camera and view plane
		 * @return the builder instance
		 * @throws IllegalArgumentException if distance is not positive
		 */
		public Builder setVpDistance(double distance) {
			if (distance < 0 || isZero(distance))
				throw new IllegalArgumentException("View plane distance must be positive");

			camera.distance = alignZero(distance);
			return this;
		}

		public Builder setRayTracer(Scene scene, RayTracerType type) {
			if (type == RayTracerType.SIMPLE)
				camera.rayTracer = new SimpleRayTracer(scene);
			return this;
		}

		/**
		 * set Resolution
		 * 
		 * @param nX the new nX
		 * @param nY the new nY
		 * @return the new builder with updaet resolution
		 */
		public Builder setResolution(int nX, int nY) {
			if (nX <= 0)
				throw new IllegalArgumentException("View plane distance must be positive");
			camera.nX = nX;
			if (nY <= 0)
				throw new IllegalArgumentException("View plane distance must be positive");
			camera.nY = nY;
			return this;

		}

		/**
		 * Camera build
		 * 
		 * @return camera
		 */
		public Camera build() {
			final String GENERAL_MSG = "Missing rendering data";
			final String CLASS_NAME = "Camera";
			final String LOCATION_FIELD = "location";
			final String VTO_FIELD = "vTo";
			final String VUP_FIELD = "vUp";
			final String DISTANCE_FIELD = "distance";
			final String HEIGHT_FIELD = "height";
			final String LENGTH_FIELD = "length";
			final String RESOLUTIONX_FIELD = "nX";
			final String RESOLUTIONY_FIELD = "nY";

			// Check required fields
			if (camera.location == null)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, LOCATION_FIELD);
			if (camera.vTo == null)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, VTO_FIELD);
			if (camera.vUp == null)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, VUP_FIELD);
			if (isZero(camera.distance))
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, DISTANCE_FIELD);
			if (isZero(camera.height))
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, HEIGHT_FIELD);
			if (isZero(camera.length))
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, LENGTH_FIELD);
			if (camera.nX <= 0)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, RESOLUTIONX_FIELD);
			if (camera.nY <= 0)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, RESOLUTIONY_FIELD);

			camera.imageWriter = new ImageWriter(camera.nX, camera.nY);
			if (camera.rayTracer == null)
				this.setRayTracer(null, RayTracerType.SIMPLE);

			if (camera.vTo.length() != 1)
				camera.vTo = camera.vTo.normalize();
			if (camera.vUp.length() != 1)
				camera.vUp = camera.vUp.normalize();

			if (!isZero(camera.vTo.dotProduct(camera.vUp)))
				throw new IllegalArgumentException("vTo and vUp vectors must be orthogonal");

			// Compute vRight if not already set
			if (camera.vRight == null || camera.vRight.dotProduct(camera.vRight) == 1)
				camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

			// Return a copy of the camera object
			try {
				return (Camera) camera.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}

	}

}
