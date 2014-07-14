/**
 * 
 */
package zove.ppcg.tankwar;

import java.util.ArrayList;
import java.util.List;

import zove.ppcg.tankwar.object.FieldObject;
import zove.ppcg.tankwar.object.RockObject;
import zove.ppcg.tankwar.object.TankObject;
import zove.ppcg.tankwar.object.TreeObject;
import zove.ppcg.tankwar.object.WallObject;

/**
 * Represents a battlefield
 * 
 * @author Zove Games
 */
public class Battlefield {
	public static final int FIELD_SIZE = Control.FIELD_SIZE;
	public static final float DIAGONAL_FIELD_SIZE = (float) ((FIELD_SIZE - 1) * Math
			.sqrt(2));

	protected List<MapPoint> emptyPoints;

	/**
	 * Represents the field, as an array with the first index as x, and the
	 * second index as y. Tanks that modify this will be disqualified. (I'm too
	 * lazy to wrap this in methods)
	 */
	protected FieldObject[][] field;

	public Battlefield() {
		field = new FieldObject[FIELD_SIZE][FIELD_SIZE];
		// walls
		// not the best algorithm ever but it works
		for (int i = 0; i < FIELD_SIZE / 2; i++) {
			int wallLength = (int) (Math.random() * FIELD_SIZE / 2);
			int x = (int) Math.floor(Math.random() * FIELD_SIZE);
			int y = (int) Math.floor(Math.random() * FIELD_SIZE);
			boolean horizontal = Math.random() >= 0.5;
			for (int j = 0; j < wallLength; j++) {
				int x2 = horizontal ? x + j : x;
				int y2 = horizontal ? y : y + j;
				if (x2 > FIELD_SIZE - 1 || y2 > FIELD_SIZE - 1)
					break;
				field[x2][y2] = new WallObject(new MapPoint(x2, y2));
			}
		}
		// rocks and trees
		// very very very very small chance of a tank being walled in by rocks
		for (int i = 0; i < FIELD_SIZE / 2; i++) {
			boolean rock = Math.random() >= 0.5;
			int x = (int) Math.floor(Math.random() * FIELD_SIZE);
			int y = (int) Math.floor(Math.random() * FIELD_SIZE);
			if (field[x][y] == null) {
				if (rock)
					field[x][y] = new RockObject(new MapPoint(x, y));
				else
					field[x][y] = new TreeObject(new MapPoint(x, y));
			} else
				i--;
		}
		emptyPoints = new ArrayList<>();
		for (int x = 0; x < FIELD_SIZE; x++) {
			for (int y = 0; y < FIELD_SIZE; y++) {
				if (field[x][y] == null)
					emptyPoints.add(new MapPoint(x, y));
			}
		}
	}

	protected FieldObject getObjectAt(MapPoint p) {
		return field[Math.round(p.getX())][Math.round(p.getY())];
	}

	protected void updateObjectPosition(MapPoint oldPosition,
			MapPoint newPosition) {
		int oldX = Math.round(oldPosition.getX()), oldY = Math
				.round(oldPosition.getY());
		int newX = Math.round(newPosition.getX()), newY = Math
				.round(newPosition.getY());
		FieldObject obj = field[oldX][oldY];
		if (field[newX][newY] == null) {
			field[oldX][oldY] = null;
			field[newX][newY] = obj;
			obj.setPosition(newPosition);
		}
	}

	protected void removeObject(MapPoint p) {
		int x = Math.round(p.getX()), y = Math.round(p.getY());
		FieldObject o = field[x][y];
		if (o != null) {
			o.onRemoved(this);
			field[x][y] = null;
		}
	}

	/**
	 * Gets the {@link FieldObjectType} of the object at the specified
	 * coordinates on the map
	 * 
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @return The {@link FieldObjectType} of the object
	 * @throws IllegalArgumentException If coordinates are out of bounds of the map
	 * @see FieldObjectType
	 */
	public FieldObjectType getObjectTypeAt(int x, int y) {
		if (x < 0 || y < 0 || x >= FIELD_SIZE || y >= FIELD_SIZE)
			throw new IllegalArgumentException("Coordinates out of bounds!");
		FieldObject o = field[x][y];
		if (o == null)
			return FieldObjectType.NOTHING;
		return o.getType();
	}

	/**
	 * Gets the {@link FieldObjectType} of the object at the specified
	 * coordinates on the map
	 * 
	 * @param point
	 *            The coordinates of the object
	 * @return The {@link FieldObjectType} of the object
	 * @see FieldObjectType
	 */
	public FieldObjectType getObjectTypeAt(MapPoint point) {
		return getObjectTypeAt((int) point.getX(), (int) point.getY());
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		for (int x = 0; x < FIELD_SIZE; x++) {
			for (int y = 0; y < FIELD_SIZE; y++) {
				FieldObject o = field[x][y];
				if (o == null)
					b.append(' ');
				else if (o instanceof RockObject)
					b.append('R');
				else if (o instanceof TreeObject)
					b.append('T');
				else if (o instanceof TankObject)
					b.append('#');
				else
					b.append('W');
				if (o != null) {
					if (x != o.getPosition().getX()
							|| y != o.getPosition().getY()) {
						throw new IllegalStateException();
					}
				}
			}
		}
		return b.toString();
	}
}
