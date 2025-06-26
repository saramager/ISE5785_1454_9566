/**
 * 
 */
package renderer;

import static java.lang.Math.*;
import static primitives.Util.*;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

import primitives.*;
import renderer.PixelManager.Pixel;
import scene.Scene;

/**
 * The Camera class represents a camera in a 3D rendering system. It defines the
 * camera's position, orientation, screen size, and distance to the view plane.
 * 
 */
public class Camera implements Cloneable {
	/** Amount of threads to use fore rendering image by the camera */
	private int threadsCount = 0;
	/**
	 * Amount of threads to spare for Java VM threads:<br>
	 * Spare threads if trying to use all the cores
	 */
	private static final int SPARE_THREADS = 2;
	/**
	 * Debug print interval in seconds (for progress percentage)<br>
	 * if it is zero - there is no progress output
	 */
	private double printInterval = 0;
	/**
	 * Pixel manager for supporting:
	 * <ul>
	 * <li>multi-threading</li>
	 * <li>debug print of progress percentage in Console window/tab</li>
	 * </ul>
	 */
	private PixelManager pixelManager;
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
	private double width = 0.0;
	/**
	 * the image writer for the camera
	 */
	private ImageWriter imageWriter;
	/**
	 * the ray tracer for camera
	 */
	private RayTracerBase rayTracer;
	/**
	 * the number of pixels in the width - resolution
	 */
	private int nX = 1;
	/**
	 * the number of pixels in the height - resolution
	 */
	private int nY = 1;
	/**
	 * The center point of the view plane, calculated based on the camera's
	 * location, direction, and distance to the view plane.
	 */
	private Point centerViewPlane;
	public double rX;
	public double rY;
	/**
	 * The number of rays used for anti-aliasing in the ray tracing.
	 */
	public int antiAlasingNumOfRays = 1;

	/**
	 * The size of the anti-aliasing rays used in the ray tracing. This value is set
	 * to 0.1 for anti-aliasing and 0 for non-anti-aliasing.
	 */
	public double antiAlasingSize = 0;

	/**
	 * Empty constructor
	 */
	private Camera() {

	}

	/**
	 * This function renders image's pixel color map from the scene included in the
	 * ray tracer object
	 * 
	 * @return the camera object itself
	 */
	public Camera renderImage() {
		pixelManager = new PixelManager(nY, nX, printInterval);
		return switch (threadsCount) {
		case 0 -> renderImageNoThreads();
		case -1 -> renderImageStream();
		default -> renderImageRawThreads();
		};
	}

	/**
	 * Prints a grid on the image with the specified interval and color.
	 * 
	 * @param interval the interval between grid lines
	 * @param color    the color of the grid lines
	 * @return the camera instance after printing the grid
	 */

	public Camera printGrid(int interval, Color color) {
		for (int i = 0; i < nX; i++)
			for (int j = 0; j < nY; j++)
				if (i % interval == 0 || j % interval == 0)
					imageWriter.writePixel(i, j, color);

		return this;
	}

	/**
	 * Render image using multi-threading by parallel streaming
	 * 
	 * @return the camera object itself
	 */
	private Camera renderImageStream() {
		IntStream.range(0, nY).parallel().forEach(i -> IntStream.range(0, nX).parallel().forEach(j -> castRay(j, i)));
		return this;
	}

	/**
	 * Render image without multi-threading
	 * 
	 * @return the camera object itself
	 */
	private Camera renderImageNoThreads() {
		for (int i = 0; i < nY; ++i)
			for (int j = 0; j < nX; ++j)
				castRay(j, i);
		return this;
	}

	/**
	 * Render image using multi-threading by creating and running raw threads
	 * 
	 * @return the camera object itself
	 */
	private Camera renderImageRawThreads() {
		var threads = new LinkedList<Thread>();
		while (threadsCount-- > 0)
			threads.add(new Thread(() -> {
				Pixel pixel;
				while ((pixel = pixelManager.nextPixel()) != null)
					castRay(pixel.col(), pixel.row());
			}));
		for (var thread : threads)
			thread.start();
		try {
			for (var thread : threads)
				thread.join();
		} catch (InterruptedException ignored) {
		}
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
	 * Writes the rendered image to a file.
	 * 
	 * @param imageName the name of the output image file
	 * @return the camera instance after writing the image
	 */
	public Camera writeToImage(String imageName) {
		imageWriter.writeToImage(imageName);
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
		centerViewPlane = location.add(vTo.scale(distance));
		double rY = height / nY;
		double rX = width / nX;
		double xJ = (j - (nX - 1) / 2.0) * rX;
		double yI = -(i - (nY - 1) / 2.0) * rY;
		Point pIJ = centerViewPlane;
		if (xJ != 0)
			pIJ = pIJ.add(vRight.scale(xJ));
		if (yI != 0)
			pIJ = pIJ.add(vUp.scale(yI));
		return new Ray(location, pIJ.subtract(location).normalize());
	}

	/**
	 * Casts a ray for a specific pixel and writes the resulting color to the image.
	 * 
	 * @param i the horizontal index of the pixel
	 * @param j the vertical index of the pixel
	 */
	private void castRay(int i, int j) {
		Ray rayPixel = constructRay(nX, nY, i, j);
		Color colorPixel;
		if (antiAlasingNumOfRays > 1)
			colorPixel = CastBeamRay(rayPixel);
		else
			colorPixel = rayTracer.traceRay(rayPixel);
		imageWriter.writePixel(i, j, colorPixel);
		pixelManager.pixelDone();

	}

	private Color CastBeamRay(Ray ray) {
		List<Ray> rays = new Blackboard(ray, antiAlasingSize / 10, antiAlasingNumOfRays).constructRayBeamGrid();
		int size = rays.size();
		if (size == 1)
			return rayTracer.traceRay(ray);
		Color color = Color.BLACK;
		for (Ray r : rays)
			color = color.add(rayTracer.traceRay(r));
		return color.reduce(size);
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
		 * The rotation angle in degrees for the camera.
		 */
		private double rotationAngleDegrees = 0.0;
		/**
		 * The rotation axis for the camera.
		 */
		private Vector rotationAxis = null;
		/**
		 * The target point for the camera, used to set the direction of the camera.
		 */
		private Point target = null;
		/**
		 * The number of rays used for anti-aliasing.
		 */

		private int diffusiveNumOfRays = 1;

		/**
		 * The type of ray tracer to use for rendering.
		 */
		private RayTracerType rayTracerType;

		/**
		 * The scene associated with the camera, used for ray tracing.
		 */
		private Scene scene = null;

		/**
		 * Creates a new Builder instance for constructing a Camera.
		 */
		public Builder() {

		}

		/**
		 * Creates a new Builder instance using an existing Camera object.
		 * 
		 * @param camera the Camera object to copy properties from
		 */
		public Builder(Camera camera) {
			this.camera.location = camera.location;
			this.camera.vTo = camera.vTo;
			this.camera.vUp = camera.vUp;
			this.camera.vRight = camera.vRight;
			this.camera.distance = camera.distance;
			this.camera.height = camera.height;
			this.camera.width = camera.width;
			this.camera.imageWriter = camera.imageWriter;
			this.camera.rayTracer = camera.rayTracer;
			this.camera.nX = camera.nX;
			this.camera.nY = camera.nY;
			this.camera.threadsCount = camera.threadsCount;
			this.camera.printInterval = camera.printInterval;
			this.camera.pixelManager = camera.pixelManager;
			this.camera.centerViewPlane = camera.centerViewPlane;
			this.camera.antiAlasingSize = camera.antiAlasingSize;
			this.camera.antiAlasingNumOfRays = camera.antiAlasingNumOfRays;

		}

		/**
		 * Set multi-threading <br>
		 * Parameter value meaning:
		 * <ul>
		 * <li>-2 - number of threads is number of logical processors less 2</li>
		 * <li>-1 - stream processing parallelization (implicit multi-threading) is
		 * used</li>
		 * <li>0 - multi-threading is not activated</li>
		 * <li>1 and more - literally number of threads</li>
		 * </ul>
		 * 
		 * @param threads number of threads
		 * @return builder object itself
		 */
		public Builder setMultithreading(int threads) {
			if (threads < -3)
				throw new IllegalArgumentException("Multithreading parameter must be -2 or higher");
			if (threads == -2) {
				int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
				camera.threadsCount = cores <= 2 ? 1 : cores;
			} else
				camera.threadsCount = threads;
			return this;
		}

		/**
		 * Set debug printing interval. If it's zero - there won't be printing at all
		 * 
		 * @param interval printing interval in %
		 * @return builder object itself
		 */
		public Builder setDebugPrint(double interval) {
			if (interval < 0)
				throw new IllegalArgumentException("interval parameter must be non-negative");
			camera.printInterval = interval;
			return this;
		}

		/**
		 * Sets the translation vector for the camera.
		 * 
		 * @param translation the translation vector to apply
		 * @return the builder instance
		 */
		public Builder setTranslation(Vector translation) {
			camera.location = camera.location.add(translation);
			if (target != null)
				setDirection(target, translation);
			else if (camera.distance != 0 && camera.location != null) {
				target = camera.location.add(camera.vTo.scale(camera.distance));
				setDirection(target, camera.vUp);
			}

			return this;
		}

		/**
		 * Sets the rotation for the camera.
		 * 
		 * @param angleDegrees rotation angle in degrees
		 * @param axis         unit vector representing axis of rotation (must be
		 *                     normalized)
		 * @return the builder instance
		 */
		public Builder setRotation(double angleDegrees, Vector axis) {
			this.rotationAngleDegrees = angleDegrees;
			this.rotationAxis = axis.normalize();
			double angleRad = toRadians(rotationAngleDegrees);
			if (camera.vTo == null || camera.vUp == null || camera.vRight == null)
				throw new IllegalArgumentException("Camera direction vectors must be set before rotation");
			camera.vUp = camera.vUp.rotateVector(rotationAxis, angleRad);
			camera.vRight = camera.vRight.rotateVector(rotationAxis, angleRad);
			return this;
		}

		/**
		 * Sets the rotation for the camera.
		 * 
		 * @param angleDegreess rotation angle in degrees
		 * 
		 * @return the builder instance wtith Y axis.
		 */
		public Builder setRotation(double angleDegreess) {
			return setRotation(angleDegreess, camera.vTo);
		}

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
			this.target = target;
			Vector to = target.subtract(camera.location).normalize();
			// If up and to are almost parallel, choose a different up vector (Z axis)
			if (alignZero(to.dotProduct(up) - 1) == 0 || alignZero(to.dotProduct(up) + 1) == 0) {
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
			camera.width = width;
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

		/**
		 * set the ray tracer for camera
		 * 
		 * @param scene- the scene object for the ray tracer
		 * @param type   - type for ray tracer from ENUM RayTracerType
		 * @return the camera builder after update
		 */

		public Builder setRayTracer(Scene scene, RayTracerType type) {
			if (type == RayTracerType.SIMPLE)
				rayTracerType = RayTracerType.SIMPLE;
			if (type == RayTracerType.GRID)
				rayTracerType = RayTracerType.GRID;
			this.scene = scene;
			return this;
		}

		/**
		 * set the resolution of the view plane.
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
		 * Sets the number of rays for anti-aliasing in the camera.
		 * 
		 * @param numOfRays the number of rays to use for anti-aliasing
		 * @return the builder instance
		 */
		public Builder setAntiAliasingRays(int numOfRays) {
			camera.antiAlasingNumOfRays = numOfRays;
			return this;
		}

		/**
		 * Sets the number of rays for glossy and diffuse anti-aliasing in the camera.
		 * 
		 * @param numOfRays the number of rays to use for glossy and diffuse
		 *                  anti-aliasing
		 * @return the builder instance
		 */

		public Builder setGlossyAndDiffuseRays(int numOfRays) {
			diffusiveNumOfRays = numOfRays;
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
			if (!isZero(camera.vTo.dotProduct(camera.vUp)))
				throw new IllegalArgumentException("vTo and vUp vectors must be orthogonal");
			if (!isZero(camera.vTo.length() - 1))
				camera.vTo = camera.vTo.normalize();
			if (!isZero(camera.vUp.length() - 1))
				camera.vUp = camera.vUp.normalize();
			// Compute vRight if not already set
			if (camera.vRight == null)
				camera.vRight = camera.vTo.crossProduct(camera.vUp);
			if (!isZero(camera.vRight.length() - 1))
				camera.vRight = camera.vRight.normalize();

			if (alignZero(camera.distance) <= 0)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, DISTANCE_FIELD);
			if (alignZero(camera.height) <= 0)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, HEIGHT_FIELD);
			if (alignZero(camera.width) <= 0)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, LENGTH_FIELD);
			if (camera.nX <= 0)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, RESOLUTIONX_FIELD);
			if (camera.nY <= 0)
				throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, RESOLUTIONY_FIELD);

			camera.imageWriter = new ImageWriter(camera.nX, camera.nY);

			if (diffusiveNumOfRays > 1)
				camera.rayTracer.glossyAndDiffuseSetRays(diffusiveNumOfRays);
			camera.rX = camera.width / camera.nX;
			camera.rY = camera.height / camera.nY;

			if (camera.antiAlasingNumOfRays > 1)
				camera.antiAlasingSize = min(camera.rX, camera.rY);
			if (rayTracerType == RayTracerType.GRID)
				camera.rayTracer = new GridRayTracer(scene);

			else
				camera.rayTracer = new SimpleRayTracer(scene);

			// Return a copy of the camera object
			try {
				return (Camera) camera.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}

	}

}
