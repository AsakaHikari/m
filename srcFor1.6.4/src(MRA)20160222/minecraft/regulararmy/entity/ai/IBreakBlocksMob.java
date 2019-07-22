package regulararmy.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IBreakBlocksMob {

	float getblockStrength(Block block, World world, int x, int y, int z);
}
