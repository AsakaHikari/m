package gvcmob;

import gvcguns.GVCGunsPlus;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GVCWorld1 extends WorldGenerator{

	public boolean flag = false;
	
	public GVCWorld1(){
		//this.flag = flag;
	}
	
	@Override
	public boolean generate(World par1World, Random random, int par1, int par2, int par3) {
		
		//for (int i0 = 0; i0 < 32; ++i0){
    		//for (int i1 = 0; i1 < 32; ++i1){
    			//for (int i2 = 0; i2 < 32; ++i2){
    				if(!(par1World.getBlock(par1, par2, par3) == Blocks.wool)){
    				if(par1World.getBlock(par1, par2-1, par3) == Blocks.grass
    						||par1World.getBlock(par1, par2-1, par3) == Blocks.sand){
    				par1World.setBlock(par1+0, par2+0, par3+0, GVCMobPlus.fn_Gcamp2);
    				}
    				//}
    			//}
    		}
		//}
		return true;
	
	}
}
