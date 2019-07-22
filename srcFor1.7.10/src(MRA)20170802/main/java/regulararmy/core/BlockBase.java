package regulararmy.core;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockBase extends Block{
	public IIcon iconActivated;

	public BlockBase() {
		super( Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setBlockUnbreakable();
		this.setResistance(6000000);
	}


	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		if(world.getBlockMetadata(x, y, z)==0){
			return 2f;
		}else{
			return -1;
		}
	}
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register)
	{
		super.registerBlockIcons(register);
		this.iconActivated = register.registerIcon(this.getTextureName() + "_active");
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int meta)
	{
		if(meta==1){
			return this.iconActivated;
		}else{
			return this.blockIcon;
		}

	}

}
