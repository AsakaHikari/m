package regulararmy.entity.ai;

import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.pathfinding.AStarPathPoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class EntityRegularAIBase extends EntityAIBase {
	public static boolean isLaggy(){
		return MonsterRegularArmyCore.laggyTimer>0;
	}
}