/**
 * 
 */
package zove.ppcg.tankwar.test;

import zove.ppcg.tankwar.Battlefield;
import zove.ppcg.tankwar.FieldObjectType;
import zove.ppcg.tankwar.MapPoint;
import zove.ppcg.tankwar.Tank;
import zove.ppcg.tankwar.TurnAction;

/**
 * Shoot enough times and you're sure to hit <i>something</i>
 * @author Zove Games
 */
public class RandomShootTank implements Tank {
	String name;
	
	public RandomShootTank(){
		name = "RandomShootTank";
	}
	
	@Override
	public void onSpawn(Battlefield field, MapPoint position) {
		// derrrp nothing
	}

	@Override
	public TurnAction onTurn(Battlefield field, MapPoint position, float health) {
		return TurnAction.createShootAction((float) (Math.random()*360));
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
