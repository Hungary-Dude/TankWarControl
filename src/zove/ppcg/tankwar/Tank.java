/**
 * 
 */
package zove.ppcg.tankwar;

/**
 * Represents a tank. All tanks must have a<br/>
 * <code>
 * static {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;Control.registerTank(MyTankClass.class)<br/>
 * }<br/>
 * </code> at the top of their class
 * 
 * @author Zove Games
 */
public interface Tank {
	/**
	 * Called when your tank spawns on the battlefield. Perform init tasks here,
	 * if necessary
	 * 
	 * @param field
	 *            The current battlefield, complete with all obstacle and tank
	 *            positions
	 * @param position
	 *            The tank's current position
	 */
	public void onSpawn(Battlefield field, MapPoint position);

	/**
	 * Called on each of this tank's turns to recieve a {@link TurnAction} for
	 * this tank Create a TurnAction with<br/>
	 * {@link TurnAction#createNothingAction()},<br/>
	 * {@link TurnAction#createShootAction(float)}, or<br/>
	 * {@link TurnAction#createMoveAction(zove.ppcg.tankwar.TurnAction.Direction, int)}
	 * 
	 * @param field
	 *            The current battlefield, complete with all obstacle and tank
	 *            positions
	 * @param position
	 *            The tank's current position
	 * @param health
	 *            The tank's current health
	 * @return The current action your tank wants to take. If null, it evaluates
	 *         to {@link TurnAction#createNothingAction()}
	 */
	public TurnAction onTurn(Battlefield field, MapPoint position, float health);

	/**
	 * After a tank returns a {@link TurnAction} on their turn, this method is
	 * called to provide feedback for that action
	 * 
	 * @param newPosition
	 *            The tank's new position (may not be changed)
	 * @param hit
	 *            What the tank hit, if it decided to shoot
	 * @see FieldObjectType
	 */
	public void turnFeedback(MapPoint newPosition, FieldObjectType hit);

	/**
	 * Called when your tank dies. Perform cleanup tasks here, if necessary.
	 * 
	 * @param field
	 *            The battlefield
	 * @param won
	 *            Did this tank win the round?
	 */
	public void onDestroyed(Battlefield field, boolean won);

	/**
	 * Returns this tank's name
	 * 
	 * @return This tank's unique name
	 */
	public String getName();
}
