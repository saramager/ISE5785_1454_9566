package renderer;

import static java.lang.Math.floor;
import static primitives.Util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import geometries.Geometries;
import geometries.Intersectable.Intersection;
import primitives.*;

/**
 * A uniform 3D grid of voxels that accelerates ray–geometry intersection by
 * traversing only the voxels a ray passes through (3D DDA).
 */
public class Grid {
	private final Double3 voxelSize;
	private final Double3 gridMin;
	private final Double3 gridMax;
	private final Geometries infinityGeometries;
	private final HashMap<Double3, Geometries> grid;
	private static final double EPS = 1e-7;

	public Geometries getInfinityGeometries() {
		return infinityGeometries;
	}

	/**
	 * Constructs an empty grid with specified voxel size and bounds.
	 *
	 * @param voxelSize the size of each voxel (dx, dy, dz)
	 * @param gridMin   the minimal corner of the grid
	 * @param gridMax   the maximal corner of the grid
	 */
	public Grid(Double3 voxelSize, Double3 gridMin, Double3 gridMax) {
		this.voxelSize = voxelSize;
		this.gridMin = gridMin;
		this.gridMax = gridMax;
		this.grid = new HashMap<>();
		this.infinityGeometries = new Geometries();
	}

	/**
	 * Constructs a grid by subdividing the bounding box of the given geometries
	 * into a density×density×density grid, and classifying each geometry into the
	 * voxels it overlaps. Geometries with infinite bounds are collected separately.
	 *
	 * @param geometries the scene geometries
	 * @param density    number of voxels along each axis
	 */
	public Grid(Geometries geometries, int density) {
		// initialize fields
		List<Double3> edges = geometries.edges;
		this.gridMin = edges.get(0);
		this.gridMax = edges.get(1);
		double dx = (gridMax.d1() - gridMin.d1()) / density;
		double dy = (gridMax.d2() - gridMin.d2()) / density;
		double dz = (gridMax.d3() - gridMin.d3()) / density;
		this.voxelSize = new Double3(dx, dy, dz);
		this.grid = new HashMap<>();
		this.infinityGeometries = new Geometries();

		// classify each geometry
		for (geometries.Intersectable geo : geometries.getGeometries()) {
			List<Double3> e = geo.edges;
			// infinite bounds → collect separately
			if (e.get(0).d1() == Double.NEGATIVE_INFINITY || e.get(0).d2() == Double.NEGATIVE_INFINITY
					|| e.get(0).d3() == Double.NEGATIVE_INFINITY || e.get(1).d1() == Double.POSITIVE_INFINITY
					|| e.get(1).d2() == Double.POSITIVE_INFINITY || e.get(1).d3() == Double.POSITIVE_INFINITY) {
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
		int ix = (int) floor((p.d1() - gridMin.d1()) / voxelSize.d1());
		int iy = (int) floor((p.d2() - gridMin.d2()) / voxelSize.d2());
		int iz = (int) floor((p.d3() - gridMin.d3()) / voxelSize.d3());
		return new Double3(ix, iy, iz);
	}

	/**
	 * Checks if a point lies within the grid bounds (including boundary).
	 *
	 * @param p the point to check
	 * @return true if inside or on boundary
	 */
	public boolean inGrid(Point p) {
		return (p.lowerThan(new Point(gridMax)) || p.equals(new Point(gridMax))) && !p.lowerThan(new Point(gridMin));
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
				if (o[i] < mins[i] || o[i] > maxs[i]) {
					return null;
				}
				tMin[i] = Double.NEGATIVE_INFINITY;
				tMax[i] = Double.POSITIVE_INFINITY;
			} else {
				double t1 = (mins[i] - o[i]) / d[i];
				double t2 = (maxs[i] - o[i]) / d[i];
				tMin[i] = Math.min(t1, t2);
				tMax[i] = Math.max(t1, t2);
			}
		}

		double entryT = Math.max(tMin[0], Math.max(tMin[1], tMin[2]));
		double exitT = Math.min(tMax[0], Math.min(tMax[1], tMax[2]));
		if (entryT > exitT || exitT < 0) {
			return null; // no valid overlap
		}

		Point entry = ray.getPoint(entryT);
		return inGrid(entry) || onEdge(entry) ? entry : null;
	}

	/**
	 * Traverses the grid via 3D DDA, collecting intersections with contained
	 * geometries.
	 *
	 * @param inputRay             the original ray
	 * @param multipleIntersection if false, stops at first hit; if true, finds all
	 *                             along the path
	 * @return list of GeoPoints where the ray intersects geometries
	 */
	public List<Intersection> traverse(Ray inputRay, boolean multipleIntersection) {
		Point entry = gridEntryPoint(inputRay);
		// if completely outside, only test the infinite geometries
		if (entry == null) {
			return infinityGeometries.calculateIntersections(inputRay);
		}

		Ray ray = new Ray(entry, inputRay.getDir());
		Vector dir = ray.getDir();
		double dx = dir.getX(), dy = dir.getY(), dz = dir.getZ();

		// compute starting voxel indices
		Double3 startIdx = coordinateToIndex(new Double3(entry.getX(), entry.getY(), entry.getZ()));
		int ix = (int) startIdx.d1(), iy = (int) startIdx.d2(), iz = (int) startIdx.d3();

		// steps along each axis
		int stepX = (int) Math.signum(dx);
		int stepY = (int) Math.signum(dy);
		int stepZ = (int) Math.signum(dz);

		// compute tMax and tDelta per axis
		double voxelX = gridMin.d1() + ix * voxelSize.d1();
		double voxelY = gridMin.d2() + iy * voxelSize.d2();
		double voxelZ = gridMin.d3() + iz * voxelSize.d3();

		double tMaxX = dx != 0 ? ((stepX > 0 ? voxelX + voxelSize.d1() : voxelX) - entry.getX()) / dx
				: Double.POSITIVE_INFINITY;
		double tMaxY = dy != 0 ? ((stepY > 0 ? voxelY + voxelSize.d2() : voxelY) - entry.getY()) / dy
				: Double.POSITIVE_INFINITY;
		double tMaxZ = dz != 0 ? ((stepZ > 0 ? voxelZ + voxelSize.d3() : voxelZ) - entry.getZ()) / dz
				: Double.POSITIVE_INFINITY;

		double tDeltaX = dx != 0 ? voxelSize.d1() / Math.abs(dx) : Double.POSITIVE_INFINITY;
		double tDeltaY = dy != 0 ? voxelSize.d2() / Math.abs(dy) : Double.POSITIVE_INFINITY;
		double tDeltaZ = dz != 0 ? voxelSize.d3() / Math.abs(dz) : Double.POSITIVE_INFINITY;

		List<Intersection> intersections = new ArrayList<>();

		// traverse until we exit the grid
		while (true) {
			Double3 idx = new Double3(ix, iy, iz);
			if (grid.containsKey(idx)) {
				List<Intersection> hits = grid.get(idx).calculateIntersections(inputRay);
				if (hits != null) {
					intersections.addAll(hits);
					if (!multipleIntersection) {
						break;
					}
				}
			}

			// step to the next voxel
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

		// finally, test the infinity geometries as well
		List<Intersection> outerHits = infinityGeometries.calculateIntersections(inputRay);
		if (outerHits != null) {
			intersections.addAll(outerHits);
		}

		return intersections;
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
			return index < 0 || gridMin.d1() + index * voxelSize.d1() > gridMax.d1();
		case 1:
			return index < 0 || gridMin.d2() + index * voxelSize.d2() > gridMax.d2();
		case 2:
			return index < 0 || gridMin.d3() + index * voxelSize.d3() > gridMax.d3();
		default:
			return true;
		}
	}

	/**
	 * Computes the distance from the ray origin to the point it first enters the
	 * grid.
	 *
	 * @param ray the ray
	 * @return distance to entry point, or +∞ if it misses
	 */
	public double cutsGrid(Ray ray) {
		Point entry = gridEntryPoint(ray);
		return entry == null ? Double.POSITIVE_INFINITY : ray.getHead().distance(entry);
	}
}
