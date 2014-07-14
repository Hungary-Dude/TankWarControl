/**
 * 
 */
package zove.ppcg.tankwar.object;

import zove.ppcg.tankwar.FieldObjectType;
import zove.ppcg.tankwar.MapPoint;

/**
 * A wall
 * 
 * @author Zove Games
 */
public class WallObject extends FieldObject {
	int numHits;

	public WallObject(MapPoint point) {
		super(100, point);
		numHits = (int) Math.floor(Math.random() * 4) + 1;
	}

	public void takeDamage(float p) {
		numHits--;
		if (numHits <= 0) {
			health = -1;
		}
	}
	
	@Override
	public FieldObjectType getType() {
		return FieldObjectType.WALL;
	}
}
