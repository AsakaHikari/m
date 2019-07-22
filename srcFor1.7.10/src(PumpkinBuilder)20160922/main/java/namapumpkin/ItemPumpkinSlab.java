package namapumpkin;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPumpkinSlab extends ItemSlab {
	public Block halfBlock;
	public Block doubleBlock;
	public boolean isDouble;

	public ItemPumpkinSlab(Block p_i45355_1_, BlockSlab p_i45355_2_,
			BlockSlab p_i45355_3_, boolean p_i45355_4_) {
		super(p_i45355_1_, p_i45355_2_, p_i45355_3_, p_i45355_4_);
		this.halfBlock=p_i45355_2_;
		this.doubleBlock=p_i45355_3_;
		this.isDouble=p_i45355_4_;
	}
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (this.isDouble)
        {
            return super.onItemUse(stack, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
        }
        else if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, stack))
        {
            return false;
        }
        else
        {
            Block block = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
            int rawMeta = p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_);
            int dirMeta = rawMeta & 7;
            boolean isUpper = (rawMeta & 8) != 0;

            if ((p_77648_7_ == 1 && !isUpper || p_77648_7_ == 0 && isUpper) && block == this.halfBlock)
            {
                if (p_77648_3_.checkNoEntityCollision(this.doubleBlock.getCollisionBoundingBoxFromPool(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_)) && p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, this.doubleBlock, dirMeta, 3))
                {
                    p_77648_3_.playSoundEffect((double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), this.doubleBlock.stepSound.func_150496_b(), (this.doubleBlock.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleBlock.stepSound.getPitch() * 0.8F);
                    --stack.stackSize;
                }

                return true;
            }
            else
            {
                return this.func_150946_a(stack, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_) ? true : super.onItemUse(stack, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
            }
        }
    }
    private boolean func_150946_a(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side)
    {
        if (side == 0)
        {
            --y;
        }

        if (side == 1)
        {
            ++y;
        }

        if (side == 2)
        {
            --z;
        }

        if (side == 3)
        {
            ++z;
        }

        if (side == 4)
        {
            --x;
        }

        if (side == 5)
        {
            ++x;
        }

        Block block = world.getBlock(x, y, z);
        int rawMeta = world.getBlockMetadata(x, y, z);
        int dirMeta = rawMeta & 7;

        if (block == this.halfBlock)
        {
            if (world.checkNoEntityCollision(this.doubleBlock.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlock(x, y, z, this.doubleBlock, dirMeta, 3))
            {
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.doubleBlock.stepSound.func_150496_b(), (this.doubleBlock.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleBlock.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

}
