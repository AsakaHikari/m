package gvcmob;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;

public class WWorld implements IWorldGenerator {

	
	 	@Override 
	 	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	 	{ 
	 		WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
	 		if (worldchunkmanager != null)
	 	    {
	 		if (world.provider instanceof WorldProviderSurface) 
	 		{ 
	 			this.generateOre(world, random, chunkX << 4, chunkZ << 4); 
	 		} 
	 
	 
	 		
	 		if (world.provider instanceof WorldProviderHell) 
	 		{ 
	 			this.generateOreHell(world, random, chunkX << 4, chunkZ << 4); 
	 		} 
	 
	 		
	 		if (world.provider instanceof WorldProviderEnd) 
	 		{ 
	 			this.generateOreEnd(world, random, chunkX << 4, chunkZ << 4); 
	 		}
	 		
	 		/*switch (world.provider.dimensionId)
	 	    {
	 	    case -1: 
	 	    	generateOreHell(world, random, chunkX * 16, chunkZ * 16);
	 	    case 0: 
	 	    	generateOre(world, random, chunkX * 16, chunkZ * 16);
	 	    case 1: 
	 	    	generateOreEnd(world, random, chunkX * 16, chunkZ * 16);
	 	    }*/
	 	    }
	 
	 
			
	 	} 
	 
	 
	 	
	 	private void generateOre(World world, Random random, int x, int z) 
	 	{ 
	 		//int i = world.rand.nextInt(4);
	 		/* 
	 		 
	 		 */ 
	 		//for (int i = 0; i < 1; ++i) 
	 		//for (int e = world.rand.nextInt(5); 0 < e; e--) 
	 		{ 
	 		GVCWorld1 var1 = new GVCWorld1();
 		    var1.setScale(1,1,1);
	 			
	 			//int genX =  x + random.nextInt(GVCMobPlus.cfg_creatCamp); 
	 			//int genZ =  z + random.nextInt(GVCMobPlus.cfg_creatCamp); 
	 			int genX =  x + random.nextInt(16); 
	 			int genZ =  z + random.nextInt(16); 
	 			int genY =  world.getHeightValue(genX, genZ);
	 
	 
	 			int s = world.rand.nextInt(8)+1;
	 			//if(world.rand.nextInt(GVCMobPlus.cfg_creatCamp)==0)
	 			if(genY > 60)
		 		{
	 				//if(world.getBlockLightValue(genX, genY, genZ) > 11 && world.isAirBlock(genX, genY, genZ))
	 				if(random.nextInt(80) == 0)
	 				{
	 				var1.generate(world, random, genX, genY, genZ); 
	 				}
	 			//(new WorldGenMinable(GVCMobPlus.fn_Gcamp,0, 3, Blocks.grass)).generate(world, random, genX, genY, genZ); 
	 		    } 
	 		}
	 	} 
	 
	 
	 	private void generateOreHell(World world, Random random, int x, int z) 
	 	{ 
	 		/*for (int i = 0; i < 30; ++i) 
	 		{ 
	 			int genX =  x + random.nextInt(16); 
	 			int genY = 10 + random.nextInt(50); 
	 			int genZ =  z + random.nextInt(16); 
	 
	 
	 			/* 
	100 			
	104 			 
	 			(new WorldGenMinable(Blocks.iron_block, 0, 4, Blocks.netherrack)).generate(world, random, genX, genY, genZ); 
	 		} */
	 	} 
	 
	 
	 	private void generateOreEnd(World world, Random random, int x, int z) 
	 	{ 
			/*for (int i = 0; i < 30; ++i) 
	 		{ 
	 			int genX =  x + random.nextInt(16); 
	 			int genY = 10 + random.nextInt(50); 
	 			int genZ =  z + random.nextInt(16); 
	 
	 
	 			/* 
	118 			 
	 			(new WorldGenMinable(Blocks.iron_block, 0, 4, Blocks.end_stone)).generate(world, random, genX, genY, genZ); 
	 		} */
	 	} 
	 } 
