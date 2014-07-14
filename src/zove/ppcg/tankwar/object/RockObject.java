/**
 * 
 */
package zove.ppcg.tankwar.object;

import zove.ppcg.tankwar.FieldObjectType;
import zove.ppcg.tankwar.MapPoint;


/**
 * @author Zove Games
 */
public class RockObject extends FieldObject {
	public RockObject(MapPoint point) {
		super(1, point);
	}
	
	public void takeDamage(float h){
	}

	@Override
	public FieldObjectType getType() {
		return FieldObjectType.ROCK;
	}
	
}
