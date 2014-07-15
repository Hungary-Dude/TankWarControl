/**
 * 
 */
package zove.ppcg.tankwar;

/**
 * Represents a point in some 2D coordinate space. Made so java.awt.Point is not
 * relied on. Is immutable
 * 
 * @author Zove Games
 */
public class MapPoint {
	private int x, y;

	/**
	 * Creates a new point
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 */
	public MapPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x-coordinate of this point
	 * 
	 * @return The x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y-coordinate of this point
	 * 
	 * @return The y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Clones this point such that if <code>m=new MapPoint(x, y)</code> then
	 * <code>m.clone()==m</code> is false but <code>m.clone().equals(m)</code>
	 * is true
	 * 
	 * @return A new MapPoint
	 */
	public MapPoint clone() {
		return new MapPoint(x, y);
	}

	/**
	 * Clones and translates this point.
	 * 
	 * @see #clone()
	 * @param deltaX
	 *            The change in x
	 * @param deltaY
	 *            The change in y
	 * @return The cloned and translated point
	 */
	public MapPoint cloneAndTranslate(int deltaX, int deltaY) {
		return new MapPoint(x + deltaX, y + deltaY);
	}

	public boolean equals(Object other) {
		if (other instanceof MapPoint) {
			MapPoint point = (MapPoint) other;
			return point.x == x && point.y == y;
		} else
			return false;
	}

	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	/**
	 * Returns the distance between this point and another
	 * 
	 * @param other
	 *            The other point
	 * @return The distance
	 */
	public double distanceTo(MapPoint other) {
		int distX = x - other.x, distY = y - other.y;
		return Math.sqrt(distX * distX + distY * distY);
	}

	/**
	 * Returns the angle between this point and another point, in radians
	 * 
	 * @param other
	 *            The other point
	 * @return The angle in radians
	 */
	public double angleBetweenRadians(MapPoint other) {
		int distX = other.x - x, distY = other.y - y;
		return Math.atan2(distY, distX);
	}

	/**
	 * Returns the angle between this point and another point, in degrees
	 * 
	 * @param other
	 *            The other point
	 * @return The angle in degrees
	 */
	public double angleBetween(MapPoint other) {
		return Math.toDegrees(angleBetweenRadians(other));
	}
}
