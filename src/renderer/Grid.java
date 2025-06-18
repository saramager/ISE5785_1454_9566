/**
 * 
 */
package renderer;

import primitives.Double3;

/**
 * 
 */
public class Grid {
	Double3 voxelSize;
	Double3 gridMin;
	Double3 gridMax;

	/**
	 * Constructs a grid with the specified voxel size, minimum, and maximum
	 * coordinates.
	 *
	 * @param voxelSize the size of each voxel in the grid
	 * @param gridMin   the minimum coordinates of the grid
	 * @param gridMax   the maximum coordinates of the grid
	 */
	public Grid(Double3 voxelSize, Double3 gridMin, Double3 gridMax) {
		this.voxelSize = voxelSize;
		this.gridMin = gridMin;
		this.gridMax = gridMax;
	}

}
