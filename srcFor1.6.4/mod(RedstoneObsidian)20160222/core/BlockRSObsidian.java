package mod.core;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockRSObsidian extends Block
{
    /** Whether this lamp block is the powered version. */
    private final boolean powered;

    public BlockRSObsidian(int par1, boolean par2)
    {
        super(par1, Material.rock);
        this.powered = par2;
        

        if (par2)
        {
            this.setHardness(150.0f);
            this.setResistance(2000.0f);
            this.setUnlocalizedName("RedStoneObsidianActive");
        }else{
        	this.setHardness(2.0f);
        	this.setResistance(10.0f);
        	this.setCreativeTab(CreativeTabs.tabBlock);
        	this.setUnlocalizedName("RedStoneObsidian");
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            if (this.powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
            }
            else if (!this.powered && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
                par1World.setBlock(par2, par3, par4, RedstoneObsidianCore.blockActiveRSObsidian.blockID, 0, 2);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            if (this.powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 4);
            }
            else if (!this.powered && par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
            {
                par1World.setBlock(par2, par3, par4, RedstoneObsidianCore.blockActiveRSObsidian.blockID, 0, 2);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote && this.powered && !par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
        {
            par1World.setBlock(par2, par3, par4, RedstoneObsidianCore.blockRSObsidian.blockID, 0, 2);
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return RedstoneObsidianCore.blockRSObsidian.blockID;
    }

    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return RedstoneObsidianCore.blockRSObsidian.blockID;
    }
}