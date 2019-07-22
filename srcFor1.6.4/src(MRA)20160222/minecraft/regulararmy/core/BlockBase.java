package regulararmy.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class BlockBase extends Block{

	public BlockBase(int par1) {
		super(par1, Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	@Override
	 public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List){
		 par3List.add(new ItemStack(par1, 1, 0));
		 par3List.add(new ItemStack(par1, 1, 1));
	 }

}
