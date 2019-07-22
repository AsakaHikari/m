package ovpwpiston;

import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRailDetectorAlter extends BlockRailDetector {
	@Override
	public float getRailMaxSpeed(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos)
    {
		return cart.getEntityData().hasKey("maxSpeed")?cart.getEntityData().getFloat("maxSpeed"):Float.MAX_VALUE;
    }

}
