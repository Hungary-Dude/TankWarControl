/**
 * 
 */
package zove.ppcg.tankwar.object;

import zove.ppcg.tankwar.Battlefield;
import zove.ppcg.tankwar.FieldObjectType;
import zove.ppcg.tankwar.MapPoint;

/**
 * Represents an object on the field
 * 
 * @author Zove Games
 */
public abstract class FieldObject {

	protected int health;
	protected MapPoint position = new MapPoint(0, 0);

	public FieldObject(int health, MapPoint position) {
		this.health = health;
		this.position = position;
	}

	public MapPoint getPosition() {
		return position;
	}

	public void setPosition(MapPoint p) {
		position = p;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void takeDamage(float damage) {
		health += damage;
	}

	public void onRemoved(Battlefield field) {
	}
	
	public abstract FieldObjectType getType();
}
