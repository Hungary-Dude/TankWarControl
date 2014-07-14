/**
 * 
 */
package zove.ppcg.tankwar.test;

import zove.ppcg.tankwar.Battlefield;
import zove.ppcg.tankwar.FieldObjectType;
import zove.ppcg.tankwar.MapPoint;
import zove.ppcg.tankwar.Tank;
import zove.ppcg.tankwar.TurnAction;
import zove.ppcg.tankwar.TurnAction.Direction;

/**
 * Evade at all costs!
 * @author Zove Games
 */
public class RandomMoveTank implements Tank {
	String name;
	
	public RandomMoveTank(){
		name = "RandomMoveTank";
	}
	
	@Override
	public void onSpawn(Battlefield field, MapPoint position) {
		// derrrp nothing
	}

	@Override
	public TurnAction onTurn(Battlefield field, MapPoint position, float health) {
		return TurnAction.createMoveAction(Direction.getRandom(), 1);
	}

	@Override
	public void turnFeedback(MapPoint newPosition, FieldObjectType hit) {
		// derrrrp nothing
	}

	@Override
	public void onDestroyed(Battlefield field, boolean won) {
		// oh no!
	}

	@Override
	public String getName() {
		return name;
	}
}
