/**
 * 
 */
package zove.ppcg.tankwar.object;

import zove.ppcg.tankwar.Battlefield;
import zove.ppcg.tankwar.FieldObjectType;
import zove.ppcg.tankwar.MapPoint;
import zove.ppcg.tankwar.Tank;

/**
 * @author Zove Games
 */
public class TankObject extends FieldObject {
	protected Tank tank;

	public TankObject(Tank tank, MapPoint point) {
		super(100, point);
		this.tank = tank;
	}
	
	public Tank getTank(){
		return tank;
	}
	
	public void onRemoved(Battlefield field){
		tank.onDestroyed(field, false);
	}
	
	public boolean equals(Object other){
		if(other instanceof TankObject){
			return ((TankObject) other).tank.getClass() == tank.getClass();
		} else return false;
	}
	
	@Override
	public FieldObjectType getType() {
		return FieldObjectType.TANK;
	}
}
