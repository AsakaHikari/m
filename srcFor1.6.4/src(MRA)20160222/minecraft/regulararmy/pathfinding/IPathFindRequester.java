package regulararmy.pathfinding;

import net.minecraft.entity.Entity;

public interface IPathFindRequester {

	public int getTacticsCost(Entity entity,AStarPathPoint start,AStarPathPoint current,AStarPathPoint end);
	
	public boolean isEngineer();
}
