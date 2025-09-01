/**
 * 
 */
package primitives;

import static primitives.Util.isZero;

/**
 * This class will serve all primitive classes based on three numbers
 * 
 * @param d1 first number
 * @param d2 first number
 * @param d3 first number
 * @author Dan Zilberstein
 */
public record Double3(double d1, double d2, double d3) {

	/** Zero triad (0,0,0) */
	public static final Double3 ZERO = new Double3(0, 0, 0);

	/** One's triad (1,1,1) */
	public static final Double3 ONE = new Double3(1, 1, 1);
	/**
	 * Positive infinity triad (Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
	 * Double.POSITIVE_INFINITY)
	 */
	public static final Double3 POSITIVE_INFINITY = new Double3(Double.POSITIVE_INFINITY);

	/**
	 * Negative infinity triad (Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
	 * Double.NEGATIVE_INFINITY)
	 */
	public static final Double3 NEGATIVE_INFINITY = new Double3(Double.NEGATIVE_INFINITY);
	/**
	 * A small value triad (0.00001, 0.00001, 0.00001) used for comparisons to avoid
	 * floating point errors
	 */
	public static final Double3 DELTA = new Double3(0.00001);

	/**
	 * Constructor to initialize Double3 based object the same number values
	 * 
	 * @param value number value for all 3 numbers
	 */
	public Double3(double value) {
		this(value, value, value);
	}

	/**
	 * Constructor to initialize Double3 based object with three number values
	 * 
	 * @param other another Double3 object to copy values from
	 *
	 * @return a new Double3 object with the same values as the other
	 */
	public Double3 min(Double3 other) {
		return new Double3(Math.min(d1, other.d1), Math.min(d2, other.d2), Math.min(d3, other.d3));
	}

	/**
	 * Finds the maximum of this Double3 and another Double3 object`
	 * 
	 * @param other another Double3 object to compare with
	 * @return a new Double3 object with the maximum of each corresponding
	 */
	public Double3 max(Double3 other) {
		return new Double3(Math.max(d1, other.d1), Math.max(d2, other.d2), Math.max(d3, other.d3));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (obj instanceof Double3 other) && isZero(d1 - other.d1) && isZero(d2 - other.d2)
				&& isZero(d3 - other.d3);
	}

	@Override
	public int hashCode() {
		return (int) Math.round(d1 + d2 + d3);
	}

	@Override
	public String toString() {
		return "(" + d1 + "," + d2 + "," + d3 + ")";
	}

	/**
	 * Sum two floating point triads into a new triad where each couple of numbers
	 * is summarized
	 * 
	 * @param rhs right hand side operand for addition
	 * @return result of add
	 */
	public Double3 add(Double3 rhs) {
		return new Double3(d1 + rhs.d1, d2 + rhs.d2, d3 + rhs.d3);
	}

	/**
	 * Subtract two floating point triads into a new triad where each couple of
	 * numbers is subtracted
	 * 
	 * @param rhs right hand side operand for addition
	 * @return result of add
	 */
	public Double3 subtract(Double3 rhs) {
		return new Double3(d1 - rhs.d1, d2 - rhs.d2, d3 - rhs.d3);
	}

	/**
	 * Scale (multiply) floating point triad by a number into a new triad where each
	 * number is multiplied by the number
	 * 
	 * @param rhs right hand side operand for scaling
	 * @return result of scale
	 */
	public Double3 scale(double rhs) {
		return new Double3(d1 * rhs, d2 * rhs, d3 * rhs);
	}

	/**
	 * Reduce (divide) floating point triad by a number into a new triad where each
	 * number is divided by the number
	 * 
	 * @param rhs right hand side operand for reducing
	 * @return result of scale
	 */
	public Double3 reduce(double rhs) {
		return new Double3(d1 / rhs, d2 / rhs, d3 / rhs);
	}

	/**
	 * Product two floating point triads into a new triad where each couple of
	 * numbers is multiplied
	 * 
	 * @param rhs right hand side operand for product
	 * @return result of product
	 */
	public Double3 product(Double3 rhs) {
		return new Double3(d1 * rhs.d1, d2 * rhs.d2, d3 * rhs.d3);
	}

	/**
	 * Checks whether all the numbers are lower than a test number
	 * 
	 * @param k the test number
	 * @return true if all the numbers are less than k, false otherwise
	 */

	public boolean lowerThan(double k) {
		return d1 < k && d2 < k && d3 < k;
	}

	/**
	 * Checks whether all the numbers are lower than three numbers in another triad
	 * 
	 * @param other other triad
	 * @return true if all the numbers are less that appropriate numbers in other
	 *         triad, false otherwise
	 */
	public boolean lowerThan(Double3 other) {
		return d1 < other.d1 && d2 < other.d2 && d3 < other.d3;
	}
}
