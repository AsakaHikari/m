package ovpwpiston;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NewBlockRailDetector extends BlockRailDetector
{

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        if (((Boolean)blockState.getValue(POWERED)).booleanValue())
        {
            List<EntityMinecartRapid> carts = this.findMinecarts(worldIn, pos, EntityMinecartRapid.class);
            if (!carts.isEmpty() && carts.get(0).getComparatorLevel() > -1) return carts.get(0).getComparatorLevel();
            List<EntityMinecartCommandBlock> list = this.<EntityMinecartCommandBlock>findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class);

            if (!list.isEmpty())
            {
                return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlockLogic().getSuccessCount();
            }

            List<EntityMinecart> list1 = this.<EntityMinecart>findMinecarts(worldIn, pos, EntityMinecart.class, EntitySelectors.HAS_INVENTORY);

            if (!list1.isEmpty())
            {
                return Container.calcRedstoneFromInventory((IInventory)list1.get(0));
            }
        }

        return 0;
    }

}