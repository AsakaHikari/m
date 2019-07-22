package mod.core;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockRSObsidianAT extends Block
{
    /** Whether this lamp block is the powered version. */

    public BlockRSObsidianAT(int par1)
    {
        super(par1, Material.rock);

            this.setHardness(150.0f);
            this.setResistance(2000.0f);
            this.setUnlocalizedName("RedStoneObsidianActiveAnytime");
            this.setCreativeTab(CreativeTabs.tabBlock);
    }

}