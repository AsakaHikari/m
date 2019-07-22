package namapumpkin;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPumpkinSlab extends BlockSlab
{
    
    public BlockPumpkinSlab(boolean p_i45431_1_)
    {
        super(p_i45431_1_, Material.gourd);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHarvestLevel("axe", -1);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        int k = meta & 7;

        if (this.field_150004_a && (meta & 8) != 0)
        {
            side = 1;
        }

        return Blocks.pumpkin.getIcon(side, k);
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(PumpkinCore.pumpkin_slab);
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int p_149644_1_)
    {
        return new ItemStack(Item.getItemFromBlock(PumpkinCore.pumpkin_slab), 2, p_149644_1_ & 7);
    }

    public String func_150002_b(int p_150002_1_)
    {
        return super.getUnlocalizedName();
    }
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
    	if(!this.field_150004_a){
    		int l = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    		boolean isUpper=p_149689_1_.getBlockMetadata(p_149689_2_, p_149689_3_, p_149689_4_)>=8;
    		p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l+(isUpper?8:0), 2);
    	}
    }
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int p_149692_1_)
    {
        return 0;
    }
}