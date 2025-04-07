/**
 * 
 */
package renderer;
import static primitives.Util.*;
import java.util.MissingResourceException;


import primitives.*;


/**
 * 
 */
public class Camera implements Cloneable{
	private Point location ;
	private Vector vTo;
	private Vector vUp;
	private Vector vRight;
	private double distance =0.0;
	private double height=0.0 ;
	private double length =0.0;
	private Camera ()
	{
		
	}
	public static Builder  getBuilder ()
	{
		return new Builder();
	}
	public Ray constructRay(int nX, int nY, int j, int i)
	{
		//TODO 
		return null;
	}
	public static class Builder
	{
		private final Camera camera= new Camera();
		public Builder  setLocation(Point location )
		{
			
			camera.location=location; 
			return this;
		}
		/**
	     * Sets the camera direction using forward and up vectors.
	     * 
	     * @param to forward vector
	     * @param up up vector
	     * @return the builder instance
	     * @throws IllegalArgumentException if vectors are null, not orthogonal, or zero vectors
	     */
	    public Builder setDirection(Vector to, Vector up) {
	      
	        if (!isZero(to.dotProduct(up)))
	            throw new IllegalArgumentException("Vectors 'to' and 'up' must be orthogonal");

	        camera.vTo = to.normalize();
	        camera.vUp = up.normalize();
	        return this;
	    }
	    
	    public Builder setDirection(Point target, Vector up) {
	        if (camera.location == null)
	            throw new IllegalArgumentException("Camera location must be set before setting direction");
	        if (target.equals(camera.location))
	            throw new IllegalArgumentException("Target point cannot be the same as camera location");

	        Vector to = target.subtract(camera.location).normalize();
	        Vector right = to.crossProduct(up).normalize();
	        Vector correctedUp = right.crossProduct(to).normalize();

	        camera.vTo = to;
	        camera.vUp = correctedUp;
	        camera.vRight = right;

	        return this;
	    }

	    /**
	     * Sets the camera direction using only a target point.
	     * Assumes the approximate up vector is the Y-axis (0, 1, 0).
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
	        if (width <= 0 || height <= 0)
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
	        if (distance <= 0)
	            throw new IllegalArgumentException("View plane distance must be positive");

	        camera.distance = distance;
	        return this;
	    }
		
	    public Camera build() {
	    	final String GENERAL_MSG = "Missing rendering data";
	    	final String CLASS_NAME = "Camera";
	    	final String LOCATION_FIELD = "location";
	    	final String VTO_FIELD = "vTo";
	    	final String VUP_FIELD = "vUp";
	    	final String DISTANCE_FIELD = "distance";
	    	final String HEIGHT_FIELD = "height";
	    	final String LENGTH_FIELD = "length";

	        // Check required fields
	        if (camera.location == null)
	            throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, LOCATION_FIELD);
	        if (camera.vTo == null)
	            throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, VTO_FIELD);
	        if (camera.vUp == null)
	            throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, VUP_FIELD);
	        if (camera.distance == 0)
	            throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, DISTANCE_FIELD);
	        if (camera.height == 0)
	            throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, HEIGHT_FIELD);
	        if (camera.length == 0)
	            throw new MissingResourceException(GENERAL_MSG, CLASS_NAME, LENGTH_FIELD);

	        if (camera.vTo.length()!=1)
	        	camera.vTo = camera.vTo.normalize();
	        if (camera.vUp.length()!=1)
	        	camera.vUp = camera.vUp.normalize();

	        if (!isZero(camera.vTo.dotProduct(camera.vUp)))
	            throw new IllegalArgumentException("vTo and vUp vectors must be orthogonal");

	        // Compute vRight if not already set
	        if ( camera.vRight==null || camera.vRight.dotProduct( camera.vRight)==1)
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
