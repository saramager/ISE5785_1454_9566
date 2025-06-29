package renderer;

import static java.lang.Math.*;
import static primitives.Util.*;

import java.util.*;

import geometries.Geometries;
import geometries.Intersectable;
import geometries.Intersectable.Intersection;
import primitives.Double3;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * A uniform 3D grid of voxels that accelerates ray–geometry intersection by
 * traversing only the voxels a ray passes through (3D DDA).
 */
public class Grid {
	/** the size of each voxel in the grid */
	private final Double3 voxelSize;
	/** the minimum coordinates of the grid bounding box */
	private final Double3 gridMin;
	/** the maximum coordinates of the grid bounding box */
	private final Double3 gridMax;
	/** geometries with infinite bounds are collected separately */
	private final Geometries infinityGeometries;
	/**
	 * the grid is a map from voxel indices to the geometries contained in that
	 * voxel
	 */
	private final HashMap<Double3, Geometries> grid;

	/** number of voxels along each axis */
	private final int numVoxelsX;
	/** number of voxels along each axis */
	private final int numVoxelsY;
	/** number of voxels along each axis */
	private final int numVoxelsZ;

	/**
	 * Constructs a grid by subdividing the bounding box of the given geometries
	 * into a density×density×density grid, and classifying each geometry into the
	 * voxels it overlaps. Geometries with infinite bounds are collected separately.
	 *
	 * @param geometries the scene geometries
	 */
	public Grid(Geometries geometries) {
		// initialize fields
		// calculate density
		double density = cbrt(geometries.getGeometries().size()) * 2;
		var edges = geometries.getEdges();
		this.gridMin = edges.get(0);
		this.gridMax = edges.get(1);
		double dx = alignZero((gridMax.d1() - gridMin.d1()) / density);
		double dy = alignZero((gridMax.d2() - gridMin.d2()) / density);
		double dz = alignZero((gridMax.d3() - gridMin.d3()) / density);

		this.voxelSize = new Double3(dx, dy, dz);

		this.numVoxelsX = (int) ceil((gridMax.d1() - gridMin.d1()) / dx);
		this.numVoxelsY = (int) ceil((gridMax.d2() - gridMin.d2()) / dy);
		this.numVoxelsZ = (int) ceil((gridMax.d3() - gridMin.d3()) / dz);

		this.grid = new HashMap<>();
		this.infinityGeometries = new Geometries();

		// classify each geometry
		for (Intersectable geo : geometries.getGeometries()) {
			var e = geo.getEdges();
			// infinite bounds → collect separately
			if (e.get(0).d1() == Double.POSITIVE_INFINITY || e.get(0).d2() == Double.POSITIVE_INFINITY
					|| e.get(0).d3() == Double.POSITIVE_INFINITY || e.get(1).d1() == Double.NEGATIVE_INFINITY
					|| e.get(1).d2() == Double.NEGATIVE_INFINITY || e.get(1).d3() == Double.NEGATIVE_INFINITY) {
				infinityGeometries.add(geo);
				continue;
			}
			// compute voxel index ranges
			Double3 minIndex = coordinateToIndex(e.get(0));
			Double3 maxIndex = coordinateToIndex(e.get(1));
			int x0 = (int) minIndex.d1(), x1 = (int) maxIndex.d1();
			int y0 = (int) minIndex.d2(), y1 = (int) maxIndex.d2();
			int z0 = (int) minIndex.d3(), z1 = (int) maxIndex.d3();

			// add geometry to all overlapped voxels
			for (int i = x0; i <= x1; i++) {
				for (int j = y0; j <= y1; j++) {
					for (int k = z0; k <= z1; k++) {
						Double3 idx = new Double3(i, j, k);
						grid.computeIfAbsent(idx, x -> new Geometries()).add(geo);
					}
				}
			}
		}

	}

	/**
	 * Maps a 3D point to the indices of the voxel that contains it.
	 *
	 * @param p the point
	 * @return a Double3 of integer indices (ix, iy, iz)
	 */
	private Double3 coordinateToIndex(Double3 p) {
		int ix = (int) (alignZero(p.d1() - gridMin.d1()) / voxelSize.d1());
		int iy = (int) (alignZero(p.d2() - gridMin.d2()) / voxelSize.d2());
		int iz = (int) (alignZero(p.d3() - gridMin.d3()) / voxelSize.d3());
		return new Double3(ix, iy, iz);
	}

	/**
	 * Checks if a point lies within the grid bounds (including boundary).
	 *
	 * @param p the point to check
	 * @return true if inside or on boundary
	 */
	public boolean inGrid(Point p) {
		return (p.getX() >= gridMin.d1() && p.getX() <= gridMax.d1())
				&& (p.getY() >= gridMin.d2() && p.getY() <= gridMax.d2())
				&& (p.getZ() >= gridMin.d3() && p.getZ() <= gridMax.d3());
	}

	/**
	 * Checks if a point lies exactly on one of the grid’s boundary planes.
	 *
	 * @param p the point to check
	 * @return true if on boundary
	 */
	private boolean onEdge(Point p) {
		double x = p.getX(), y = p.getY(), z = p.getZ();
		return alignZero(x - gridMin.d1()) == 0 || alignZero(x - gridMax.d1()) == 0 || alignZero(y - gridMin.d2()) == 0
				|| alignZero(y - gridMax.d2()) == 0 || alignZero(z - gridMin.d3()) == 0
				|| alignZero(z - gridMax.d3()) == 0;
	}

	/**
	 * Finds the entry point of a ray into the grid’s bounding box.
	 *
	 * @param ray the ray
	 * @return the entry Point, or null if the ray misses or is completely behind
	 */
	public Point gridEntryPoint(Ray ray) {
		Point p0 = ray.getHead();
		if (inGrid(p0)) {
			return p0;
		}

		double[] o = { p0.getX(), p0.getY(), p0.getZ() };
		double[] d = { ray.getDir().getX(), ray.getDir().getY(), ray.getDir().getZ() };
		double[] tMin = new double[3], tMax = new double[3];

		double[] mins = { gridMin.d1(), gridMin.d2(), gridMin.d3() };
		double[] maxs = { gridMax.d1(), gridMax.d2(), gridMax.d3() };

		// compute intersection t with each slab
		for (int i = 0; i < 3; i++) {
			if (isZero(d[i])) {
				if (o[i] < mins[i] || o[i] > maxs[i])
					return null;
				tMin[i] = Double.NEGATIVE_INFINITY;
				tMax[i] = Double.POSITIVE_INFINITY;
			} else {
				double t1 = (mins[i] - o[i]) / d[i];
				double t2 = (maxs[i] - o[i]) / d[i];
				tMin[i] = min(t1, t2);
				tMax[i] = max(t1, t2);
			}
		}

		double entryT = max(tMin[0], max(tMin[1], tMin[2]));
		double exitT = min(tMax[0], min(tMax[1], tMax[2]));
		if (entryT > exitT || exitT < 0)
			return null; // no valid overlap

		Point entry = ray.getPoint(entryT);
		return inGrid(entry) || onEdge(entry) ? entry : null;

	}

	/**
	 * Traverses the grid via 3D DDA, collecting intersections with contained
	 * geometries.
	 *
	 * @param inputRay    the original ray
	 * @param maxDistance maximum distance to search for intersections
	 * @return list of GeoPoints where the ray intersects geometries
	 */

	public List<Intersection> traverse(Ray inputRay, double maxDistance) {
		List<Intersection> allIntersections = new LinkedList<>();

		Set<Intersectable> geometriesProcessedForThisRay = new HashSet<>();

		var infinityIntersections = infinityGeometries.calculateIntersections(inputRay, maxDistance);
		if (infinityIntersections != null)
			allIntersections.addAll(infinityIntersections);

		Point entry = gridEntryPoint(inputRay);
		if (entry == null) {
			return allIntersections;
		}

		Vector dir = inputRay.getDir();
		double dx = dir.getX(), dy = dir.getY(), dz = dir.getZ();

		Double3 startIdx = coordinateToIndex(new Double3(entry.getX(), entry.getY(), entry.getZ()));
		int ix = (int) startIdx.d1(), iy = (int) startIdx.d2(), iz = (int) startIdx.d3();

		int stepX = (int) signum(dx);
		int stepY = (int) signum(dy);
		int stepZ = (int) signum(dz);

		double voxelX = gridMin.d1() + ix * voxelSize.d1();
		double voxelY = gridMin.d2() + iy * voxelSize.d2();
		double voxelZ = gridMin.d3() + iz * voxelSize.d3();

		double tMaxX = dx != 0 ? ((stepX > 0 ? voxelX + voxelSize.d1() : voxelX) - entry.getX()) / dx
				: Double.POSITIVE_INFINITY;
		double tMaxY = dy != 0 ? ((stepY > 0 ? voxelY + voxelSize.d2() : voxelY) - entry.getY()) / dy
				: Double.POSITIVE_INFINITY;
		double tMaxZ = dz != 0 ? ((stepZ > 0 ? voxelZ + voxelSize.d3() : voxelZ) - entry.getZ()) / dz
				: Double.POSITIVE_INFINITY;

		double tDeltaX = dx != 0 ? voxelSize.d1() / abs(dx) : Double.POSITIVE_INFINITY;
		double tDeltaY = dy != 0 ? voxelSize.d2() / abs(dy) : Double.POSITIVE_INFINITY;
		double tDeltaZ = dz != 0 ? voxelSize.d3() / abs(dz) : Double.POSITIVE_INFINITY;

		while (true) {

			if (min(tMaxX, min(tMaxY, tMaxZ)) > maxDistance) {
				break;
			}
			Double3 idx = new Double3(ix, iy, iz);
			if (grid.containsKey(idx)) {
				Geometries geometriesInVoxel = grid.get(idx);

				for (Intersectable geo : geometriesInVoxel.getGeometries()) {
					if (!geometriesProcessedForThisRay.contains(geo)) {

						var hits = geo.calculateIntersections(inputRay, maxDistance);

						if (hits != null) {
							allIntersections.addAll(hits);
						}

						geometriesProcessedForThisRay.add(geo);
					}
				}
			}

			if (tMaxX < tMaxY && tMaxX < tMaxZ) {
				tMaxX += tDeltaX;
				ix += stepX;
				if (voxelOutOfBounds(ix, 0))
					break;
			} else if (tMaxY < tMaxZ) {
				tMaxY += tDeltaY;
				iy += stepY;
				if (voxelOutOfBounds(iy, 1))
					break;
			} else {
				tMaxZ += tDeltaZ;
				iz += stepZ;
				if (voxelOutOfBounds(iz, 2))
					break;
			}
		}

		return allIntersections;

	}

	/**
	 * Checks if stepping past a given voxel index goes outside the grid bounds.
	 *
	 * @param index the voxel index along one axis
	 * @param axis  0=x, 1=y, 2=z
	 * @return true if out of bounds
	 */
	private boolean voxelOutOfBounds(int index, int axis) {
		switch (axis) {
		case 0:
			return index < 0 || index >= numVoxelsX;
		case 1:
			return index < 0 || index >= numVoxelsY;
		case 2:
			return index < 0 || index >= numVoxelsZ;
		default:
			return true; // invalid axis
		}
	}
}
