/**
 * 
 */
package zove.ppcg.tankwar.object;

import zove.ppcg.tankwar.FieldObjectType;
import zove.ppcg.tankwar.MapPoint;

/**
 * @author Zove Games
 */
public class TreeObject extends FieldObject {
	public TreeObject(MapPoint point) {
		super(1, point);
	}
	
	@Override
	public FieldObjectType getType() {
		return FieldObjectType.TREE;
	}
}
