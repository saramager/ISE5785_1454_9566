/**
 * 
 */
package renderer;

import static java.lang.Math.floor;

import java.util.HashMap;
import java.util.List;

import geometries.Geometries;
import geometries.Intersectable;
import primitives.Double3;
import primitives.Point;

/**
 * 
 */
public class Grid {
	Double3 voxelSize;
	Double3 gridMin;
	Double3 gridMax;
	private Geometries infinityGeometries;
	private HashMap<Double3, Geometries> grid;
	private final double EPS = 0.0000001;

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

	public Grid(Geometries geometries, int density) {

		// initializing map
		grid = new HashMap<Double3, Geometries>();
		List<Double3> edges = geometries.edges;
		gridMin = edges.get(0);
		gridMax = edges.get(1);

		double xSize = (gridMax.d1() - gridMin.d1()) / density;
		double ySize = (gridMax.d2() - gridMin.d2()) / density;
		double zSize = (gridMax.d3() - gridMin.d3()) / density;
		voxelSize = new Double3(xSize, ySize, zSize);

		infinityGeometries = new Geometries();

		for (Intersectable geometry : geometries.getGeometries()) {

			List<Double3> edge = geometry.edges;
			if (edge.get(0).d1() == Double.NEGATIVE_INFINITY || edge.get(0).d2() == Double.NEGATIVE_INFINITY
					|| edge.get(0).d3() == Double.NEGATIVE_INFINITY) {
				infinityGeometries.add(geometry);
				continue; // skip geometries with negative infinity edges

			}
			if (edge.get(1).d1() == Double.POSITIVE_INFINITY || edge.get(1).d2() == Double.POSITIVE_INFINITY
					|| edge.get(1).d3() == Double.POSITIVE_INFINITY) {
				infinityGeometries.add(geometry);
				continue; // skip geometries with positive infinity edges

			}

			Double3 target = coordinateToIndex(geometry.edges.get(1));
			int xT = (int) target.d1(), yT = (int) target.d2(), zT = (int) target.d3();
			Double3 index = coordinateToIndex(geometry.edges.get(0));
			int xV = (int) index.d1(), yV = (int) index.d2(), zV = (int) index.d3();
			for (int i = xV - 1; i <= xT; i++) {
				for (int j = yV - 1; j <= yT; j++) {
					for (int k = zV - 1; k <= zT; k++) {
						index = new Double3(i, j, k);
						if (grid.containsKey(index)) {
							grid.get(index).add(geometry);
						} else {
							grid.put(index, new Geometries(geometry));
						}
					}
				}
			}
		}
	}

	/**
	 * maps a point to the coordinates of the voxel it is in
	 * 
	 * @param d the point to be mapped
	 * @return the coordinates of the voxel
	 */
	private Double3 coordinateToIndex(Double3 d) {
		double x = d.d1(), y = d.d2(), z = d.d3();
		int xI = (int) floor((x - (x - gridMin.d1()) % voxelSize.d1()) / voxelSize.d1());
		int yI = (int) floor((y - (y - gridMin.d2()) % voxelSize.d2()) / voxelSize.d2());
		int zI = (int) floor((z - (z - gridMin.d3()) % voxelSize.d3()) / voxelSize.d3());
		return new Double3(xI, yI, zI);
	}

	/**
	 * Checks if point is in grid
	 * 
	 * @param p the point to be checked
	 * @return true if the point is in the grid, false otherwise
	 */
	public boolean inGrid(Point p) {
		return (p.lowerThan(new Point(gridMax)) || p.equals(new Point(gridMax))) && !p.lowerThan(new Point(gridMin));
	}

	/**
	 * getter for the outer geometries
	 * 
	 * @return the outer geometries
	 */
	public Geometries getInfinityGeometries() {
		return infinityGeometries;
	}

}
