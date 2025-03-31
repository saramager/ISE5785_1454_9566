/**
 * 
 */
package geometries;
import primitives.*;
import java.util.List;


/**
 * 
 */
public interface Intersectable {
	/**
	 * 
	 * @param ray -check intsersectuion for ray 
	 * @return list of Points that intsersectuion
	 */
	List<Point> findIntsersections(Ray ray);


}
