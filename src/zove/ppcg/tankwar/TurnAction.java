/**
 * 
 */
package zove.ppcg.tankwar;

import zove.ppcg.tankwar.object.FieldObject;
import zove.ppcg.tankwar.object.RockObject;
import zove.ppcg.tankwar.object.TankObject;

/**
 * Represents a tank's action on a turn
 * 
 * @author Zove Games
 */
public class TurnAction {
	public static enum Direction {
		NORTH, SOUTH, EAST, WEST;

		public MapPoint translate(MapPoint original, int numSteps) {
			switch (this) {
			case NORTH:
				return original.cloneAndTranslate(0, numSteps);
			case SOUTH:
				return original.cloneAndTranslate(0, -numSteps);
			case EAST:
				return original.cloneAndTranslate(numSteps, 0);
			case WEST:
				return original.cloneAndTranslate(-numSteps, 0);
			default:
				// supress java error complaining about not returning anything
				// This point will never be reached anyway
				throw new IllegalStateException();
			}
		}

		/**
		 * Gets a random direction
		 * 
		 * @return A random direction
		 */
		public static Direction getRandom() {
			double d = Math.random();
			if (d > 0.75)
				return NORTH;
			else if (d > 0.5)
				return SOUTH;
			else if (d > 0.25)
				return EAST;
			else
				return WEST;
		}
	}

	protected enum ActionType {
		NOTHING, SHOOT, MOVE;
	}

	protected ActionType type;
	protected double angle;
	protected Direction direction;
	protected int numSteps;
	protected MapPoint hit;

	/**
	 * Creates a new TurnAction
	 * 
	 * @param type
	 * @param angle
	 * @param direction
	 * @param numSteps
	 */
	private TurnAction(ActionType type, double angle, Direction direction,
			int numSteps) {
		this.type = type;
		this.angle = angle;
		this.direction = direction;
		this.numSteps = numSteps;
	}

	void takeAction(TankObject tank, Battlefield field) {
		switch (type) {
		case MOVE:
			MapPoint moved = direction.translate(tank.getPosition(), 0);
			for (int steps = 1; steps <= numSteps; steps++) {
				MapPoint newPoint = direction.translate(tank.getPosition(),
						steps);
				if (newPoint.getX() >= 0 && newPoint.getY() >= 0
						&& newPoint.getX() < Battlefield.FIELD_SIZE
						&& newPoint.getY() < Battlefield.FIELD_SIZE
						&& field.getObjectAt(newPoint) == null) {
					moved = newPoint;
				} else
					break;
			}
			field.updateObjectPosition(tank.getPosition(), moved);
			tank.getTank().turnFeedback(tank.getPosition(),
					FieldObjectType.NOTHING);
			break;
		case SHOOT:
			float dist = Float.MAX_VALUE;
			FieldObject hit = null;

			for (int x = 0; x < Battlefield.FIELD_SIZE; x++) {
				for (int y = 0; y < Battlefield.FIELD_SIZE; y++) {
					FieldObject obj = field.field[x][y];
					// ignore the shooter tank, all rocks, and all empty spaces
					if (obj == null || obj instanceof RockObject || obj == tank)
						continue;
					float tankX = tank.getPosition().getX(), tankY = tank
							.getPosition().getY();
					// transform the object into tank space
					float x2 = (float) (((x - tankX) * Math.cos(-angle)) - ((y - tankY) * Math
							.sin(-angle)));
					float y2 = (float) (((x - tankX) * Math.sin(-angle)) + ((y - tankY) * Math
							.cos(-angle)));
					if (x2 > 0 && y2 >= -0.5 && y2 <= 0.5) {
						if (x2 < dist) {
							dist = x2;
							hit = obj;
						}
					}
				}
			}
			if (hit != null) {
				this.hit = hit.getPosition();
				float deltaHealth = -30
						+ (dist * 10 / Battlefield.DIAGONAL_FIELD_SIZE);
				hit.takeDamage(deltaHealth);
				if (hit.getHealth() < 0) {
					field.removeObject(hit.getPosition());
				}
				tank.getTank().turnFeedback(tank.getPosition(), hit.getType());
			} else {
				tank.getTank().turnFeedback(tank.getPosition(),
						FieldObjectType.NOTHING);
			}
			break;
		default:
			tank.getTank().turnFeedback(tank.getPosition(),
					FieldObjectType.NOTHING);
			break;
		}
	}

	/**
	 * Creates an action to do nothing
	 * 
	 * @return The action
	 */
	public static TurnAction createNothingAction() {
		// it does....nothing!
		return new TurnAction(ActionType.NOTHING, 0, Direction.NORTH, 0);
	}

	/**
	 * Creates an action to shoot
	 * 
	 * @param angle
	 *            The angle to shoot at, in degrees
	 * @return The action
	 */
	public static TurnAction createShootAction(float angle) {
		angle += (Math.random() * 10) - 5;
		return new TurnAction(ActionType.SHOOT, Math.toRadians(angle),
				Direction.NORTH, 0);
	}

	/**
	 * Creates an action to shoot
	 * 
	 * @param radians
	 *            The angle to shoot at, in radians
	 * @return The action
	 */
	public static TurnAction createShootActionRadians(double radians) {
		radians += Math.toRadians((Math.random() * 10) - 5);
		return new TurnAction(ActionType.SHOOT, radians, Direction.NORTH, 0);
	}

	/**
	 * Creates a move action. Clips numSteps to 1-3
	 * 
	 * @param direction
	 *            The direction to go in
	 * @param numSteps
	 *            The number of spaces to move
	 * @return The action
	 */
	public static TurnAction createMoveAction(Direction direction, int numSteps) {
		if (numSteps < 1)
			numSteps = 1;
		if (numSteps > 3)
			numSteps = 3;
		return new TurnAction(ActionType.MOVE, 0, direction, numSteps);
	}

}
