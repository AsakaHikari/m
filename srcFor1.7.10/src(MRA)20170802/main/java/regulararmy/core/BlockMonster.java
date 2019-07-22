package regulararmy.core;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockMonster extends Block{

	public BlockMonster() {
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setResistance(1.0f);
		//this.setTickRandomly(true);
	}
	
	@Override
	public float getBlockHardness(World w,int x,int y,int z){
		return MonsterRegularArmyCore.leadersNum==-1?0:4.0f;
	}
	
	@Override
	public void updateTick(World w, int x, int y, int z, Random rand){
		if(MonsterRegularArmyCore.leadersNum==-1){
			w.setBlock(x, y, z, Blocks.air);
		}
	}
	
	@Override
	public int quantityDropped(Random p_149745_1_){
		return 0;
	}

}
