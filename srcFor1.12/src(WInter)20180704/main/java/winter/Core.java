package winter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.*;
@Mod(modid = "winter", name = "winter", version = "alpha")
@EventBusSubscriber
public class Core {
	public static final String modid="winter";
	@Instance(value = modid)
	public static Core instance;
	public static Item minecartItem;
	public static Item minecartChestItem;
	//@SidedProxy(clientSide="newMinecart.CoreProxyClient",serverSide="newMinecart.CoreProxy")
	//public static CoreProxy proxy;
	public static Method MregisterBlock;
	public static Class NSDWClazz;
	public static Class NSWClazz;
	public static Field Flocked;
	public static Field Fpatterns;
	public static Field Flocked2;
	public static Field FenableSnow;
	public static Field Ftemperature;
	public static Field Frainfall;
	public static Field FenableRain;

	public static int updateLCG;

	public static float x1=1f,y1=2f,z1=3f,p1=10,r1=14,b1=8.0f/3,x=2f,y=3f,z=1f,p=10,r=28,b=8.0f/3;
	public static int timer=0;
	public static int weakRainTime;
	public static boolean isWeakRaining;

	public static final DataParameter<Float> MAX_SPEED = EntityDataManager.<Float>createKey(EntityMinecart.class, DataSerializers.FLOAT);
	/*
	    @ObjectHolder(modid)
	    public static class ITEMS{

	        public static Item new_minecart;
	        public static Item new_chest_minecart;
	        public static Item new_detector_rail;
	    }

	    @ObjectHolder(modid)

	    public static class BLOCKS{

	        public static Block new_detector_rail;

	    }
	 */
	@NetworkCheckHandler
	public boolean accept(Map<String, String>modList, Side side) {
		return true;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		/*
		try {
			NSDWClazz=Class.forName("net.minecraftforge.registries.NamespacedDefaultedWrapper");
			NSWClazz=Class.forName("net.minecraftforge.registries.NamespacedWrapper");
		} catch (ClassNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		Flocked=ReflectionHelper.findField(NSDWClazz, "locked");
		Flocked.setAccessible(true);
		Flocked2=ReflectionHelper.findField(NSWClazz, "locked");
		Flocked2.setAccessible(true);
		*/
		FenableRain=ReflectionHelper.findField(Biome.class, "enableRain","field_76765_S");
		FenableSnow=ReflectionHelper.findField(Biome.class, "enableSnow","field_76766_R");
		Ftemperature=ReflectionHelper.findField(Biome.class,"temperature","field_76750_F");
		Frainfall=ReflectionHelper.findField(Biome.class,"rainfall","field_76751_G");
		/*
		MregisterBlock=ReflectionHelper.findMethod(Block.class,"registerBlock","func_176219_a",int.class,String.class,Block.class);
		MregisterBlock.setAccessible(true);
		try {
			Flocked.set(Block.REGISTRY, false);
			Flocked2.set(GameData.getWrapper(EntityEntry.class),false );
			//FenableSnow.set(Biome.getBiome(1), true);

			
	    		MregisterBlock.invoke(null, 27, "golden_rail", (new BlockRailPoweredAlter()).setHardness(0.7F).setUnlocalizedName("goldenRail"));
	    		MregisterBlock.invoke(null, 28, "detector_rail", (new BlockRailDetectorAlter()).setHardness(0.7F).setUnlocalizedName("detectorRail"));
	    		MregisterBlock.invoke(null,66, "rail", (new BlockRailAlter()).setHardness(0.7F).setUnlocalizedName("rail"));
	    		MregisterBlock.invoke(null,157, "activator_rail", (new BlockRailPoweredAlter()).setHardness(0.7F).setUnlocalizedName("activatorRail"));
			 
			//MregisterBlock.invoke(null,29, "sticky_piston", (new BlockPistonBaseAlter(true)).setUnlocalizedName("pistonStickyBase"));
			//MregisterBlock.invoke(null,33, "piston", (new BlockPistonBaseAlter(false)).setUnlocalizedName("pistonBase"));

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		*/
		/*
	    	Fpatterns=ReflectionHelper.findField(TileEntityBanner.class,"field_175118_f","patterns");
	    	Fpatterns.setAccessible(true);
		 */
		//EntityMinecart.defaultMaxSpeedAirLateral=Float.MAX_VALUE;
		//EntityMinecart.defaultDragAir=1;
		/*
	    	ITEMS.new_minecart=new ItemMinecart(0).setRegistryName("new_minecart").setUnlocalizedName("new_minecart");
	        ITEMS.new_chest_minecart=new ItemMinecart(1).setRegistryName("new_chest_minecart").setUnlocalizedName("new_chest_minecart");
	        BLOCKS.new_detector_rail=(new NewBlockRailDetector()).setRegistryName("new_detector_rail").setHardness(0.7F).setUnlocalizedName("new_detector_rail").setCreativeTab(CreativeTabs.TRANSPORTATION);
	        ITEMS.new_detector_rail=new ItemBlock(BLOCKS.new_detector_rail).setRegistryName("new_detector_rail");
		 */
		/*
	        if (event.getSide() == Side.CLIENT){
	            ModelLoader.setCustomModelResourceLocation(ITEMS.new_minecart, 0, new ModelResourceLocation(ITEMS.new_minecart.getRegistryName(), "inventory"));
	            ModelLoader.setCustomModelResourceLocation(ITEMS.new_chest_minecart, 0, new ModelResourceLocation(ITEMS.new_chest_minecart.getRegistryName(), "inventory"));
	            ModelLoader.setCustomModelResourceLocation(ITEMS.new_detector_rail, 0, new ModelResourceLocation(ITEMS.new_detector_rail.getRegistryName(), "inventory"));

	        }
		 */
		//MinecraftForge.EVENT_BUS.register(new EventReceiver());
		//GameData.registerEntity(200, new ResourceLocation("ender_crystal"), EntityEnderCrystalAlter.class, "EnderCrystal");
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}


	@SubscribeEvent
	public void load(WorldEvent.Load e){
		if(e.getWorld().isRemote)return;
		File fileIDMap=new File(( (e.getWorld().getSaveHandler())).getWorldDirectory(),"WinterMod.dat");

		if(!fileIDMap.exists()){
			try {
				fileIDMap.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}else{
			FileInputStream fileinputstream;
			NBTTagCompound nbt=null;
			try {
				fileinputstream = new FileInputStream(fileIDMap);
				nbt = CompressedStreamTools.readCompressed(fileinputstream);
				fileinputstream.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(nbt!=null){
				x=nbt.getFloat("lorenz_x");
				y=nbt.getFloat("lorenz_y");
				z=nbt.getFloat("lorenz_z");
				x1=nbt.getFloat("lorenz_x1");
				y1=nbt.getFloat("lorenz_y1");
				z1=nbt.getFloat("lorenz_z1");
				weakRainTime=nbt.getInteger("weakRainTime");
				isWeakRaining=nbt.getBoolean("isWeakRaining");
			}
		}

	}

	@SubscribeEvent
	public void onWorldSaved(WorldEvent.Save e){

		if(e.getWorld().isRemote)return;
		NBTTagCompound nbt=new NBTTagCompound();
		nbt.setFloat("lorenz_x",x);
		nbt.setFloat("lorenz_y",y);
		nbt.setFloat("lorenz_z",z);
		nbt.setFloat("lorenz_x1",x1);
		nbt.setFloat("lorenz_y1",y1);
		nbt.setFloat("lorenz_z1",z1);
		nbt.setInteger("weakRainTime", weakRainTime);
		nbt.setBoolean("isWeakRaining", isWeakRaining);

		File fileIDMap=new File(( (e.getWorld().getSaveHandler())).getWorldDirectory(),"WinterMod.dat");
		if(!fileIDMap.exists()){
			try {
				fileIDMap.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		try {
			FileOutputStream fileinputstream=new FileOutputStream(fileIDMap);
			CompressedStreamTools.writeCompressed(nbt, fileinputstream);
			fileinputstream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void itemtooltip(ItemTooltipEvent e){
		if(e.getEntityPlayer()!=null&& e.getItemStack().getItem()==Items.CLOCK){
			World w=e.getEntityPlayer().world;
			BlockPos pos=e.getEntityPlayer().getPosition();
			float temp=0;
			float humid=0;
			int p=0;
			int h=0;
			for(int x=-3;x<=3;x++){
				for(int y=-2;y<=2;y++){
					for(int z=-3;z<=3;z++){
						
						Block b=w.getBlockState(pos.add(x, y, z)).getBlock();
						if(b==Blocks.FIRE || b==Blocks.MAGMA){
							p++;
						}else if(b==Blocks.LAVA || b==Blocks.FLOWING_LAVA){
							p+=2;
						}else if(b==Blocks.WATER || b==Blocks.FLOWING_WATER || b==Blocks.PRISMARINE){
							h++;
						}
						BlockPos newpos=pos.add(x*4, y*4, z*4);
						temp+=w.getBiome(newpos).getFloatTemperature(newpos);
						humid+=w.getBiome(newpos).getRainfall();
					}
				}
			}
			//System.out.println(temp);
			temp*=30;
			temp/=(7*7*5);
			temp+=-(15-w.getLightFor(EnumSkyBlock.SKY,pos ))*(1-w.getBiome(pos).getRainfall())+0.2f*w.getLightFor(EnumSkyBlock.BLOCK,pos);
			if(w.provider.isSurfaceWorld() && pos.getY()<56){
				float strength=(64-pos.getY())/8f;
				temp=(temp+15*(strength-1))/strength;
			}
			temp+=p*0.7f;
			humid*=100;
			humid/=(7*7*5);
			humid+=2*h;
			humid=MathHelper.clamp(humid, 0, 100);
			e.getToolTip().add(((int)temp)+" C");
			e.getToolTip().add((int)(humid)+" %");
			e.getToolTip().add(w.getWorldTime()/(20*60*20*14*4)+" year "+(w.getWorldTime()%(20*60*20*14*4))/(20*60*20)+" days");
		}
	}
	@SubscribeEvent
	public void tickserver(TickEvent.ServerTickEvent e){
		if(++timer%100==0){
			float x_=x1;
			float y_=y1;
			float z_=z1;
			x1+=p1*(y_-x_)*0.001;
			y1+=((r1-z_)*x_-y_)*0.001;
			z1+=(x_*y_-b1*z_)*0.001;
			x_=x;
			y_=y;
			z_=z;
			x+=p*(y_-x_)*0.001;
			y+=((r-z_)*x_-y_)*0.001;
			z+=(x_*y_-b*z_)*0.001;
			//System.out.println(x+" "+y+" "+z+"");
			//System.out.println((x+y)+" "+(y+z)+" "+(z+x));
			//System.out.println((x1+x)+" "+(y+y1)+" "+(z+z1));
		}

	}
	
	public void setRainStrength(World w,float f){
		w.prevRainingStrength=f;
		w.rainingStrength=f;
	}

	@SubscribeEvent
	public void tick(TickEvent.WorldTickEvent e){

		WorldServer w=(WorldServer) e.world;
		//w.rainingStrength=0.4f;
		if(w.provider.isSurfaceWorld()){
			if(weakRainTime<0){
				if(isWeakRaining){
					weakRainTime=(w.rand.nextInt(40000) + 8000);
				}else{
					weakRainTime=(w.rand.nextInt(10000) + 8000);
				}
				isWeakRaining=!isWeakRaining;
			}
			if(!w.getWorldInfo().isRaining() &&w.getWorldInfo().getRainTime()>3&& isWeakRaining){
				this.setRainStrength(w,0.4f);
			}
			weakRainTime--;

			if(w.getWorldTime()%100==0){
				//System.out.println(weakRainTime);

				for( Biome biome:Biome.REGISTRY){
					try {
						float humid=0;
						switch(Biome.REGISTRY.getIDForObject(biome)){
						//dry
						case 2:
						case 17:
						case 25:
						case 37:
						case 38:
						case 39:
						case 130:
						case 165:
						case 166:
						case 167:
							humid+=(y+z1)/200;

							break;
							//savanna
						case 35:
						case 36:
						case 163:
						case 164:
							humid=0.4f;
							humid-=0.4f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							humid+=(y+z1)/150;
							break;
							//Dwa
						case 1:
						case 129:

							humid=0.2f;
							humid+=0.3f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							humid+=(z+x1)/150;
							break;
							//Cw
						case 29:
						case 157:

						case 5:
						case 19:
						case 27:
						case 28:
						case 30:
						case 31:
						case 32:
						case 33:
						case 133:
						case 158:
						case 160:
						case 161:
							humid=0.5f;
							humid+=0.2f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							humid+=(z+y1)/150;
							break;
							//Cs
						case 4:
						case 18:
						case 132:
							humid=0.5f;
							humid-=0.2f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							humid+=(z+z1)/150;
							break;
						}
						if(humid<0.15){
							FenableRain.set(biome, false);
						}else{
							FenableRain.set(biome, true);
							humid+=w.getRainStrength(1)*0.2;
							
						}
						if(humid!=0){
							Frainfall.set(biome,humid);
						}

						switch(Biome.REGISTRY.getIDForObject(biome)){
						//very high temperature
						case 2:
						case 17:
						case 35:
						case 36:
						case 37:
						case 38:
						case 39:
						case 130:
						case 163:
						case 164:
						case 165:
						case 166:
						case 167:
							Ftemperature.set(biome,1f);
							break;

							//high temperature
						case 1:
						case 4:
						case 6:
						case 7:
						case 16:
						case 18:
						case 29:
						case 129:
						case 132:
						case 134:
						case 157:
							Ftemperature.set(biome,0.5f);
							break;

							//medium temperature
						case 5:
						case 19:
						case 25:
						case 27:
						case 28:
						case 32:
						case 33:
						case 133:
						case 155:
						case 156:
						case 160:
						case 161:
							Ftemperature.set(biome,0.2f);
							break;

							//low temperature

						case 3:
						case 10:
						case 11:
						case 12:
						case 13:
						case 20:
						case 26:
						case 30:
						case 31:
						case 34:
						case 131:
						case 140:
						case 158:
						case 162:
							Ftemperature.set(biome,-0.4f);
							break;
						}
						float tempadd=0;
						switch(Biome.REGISTRY.getIDForObject(biome)){
						//very small temperature change
						case 35:
						case 36:
						case 163:
						case 164:

							//year
							//tempadd=0.1f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							//day
							tempadd=0.1f*MathHelper.sin((float)w.getWorldTime()/(20*60*20/(3.14f*2)));
							tempadd+=(x+x1)/80;
							tempadd-=w.rainingStrength*0.2f;
							Ftemperature.set(biome, biome.getTemperature()+tempadd);
							break;
							//small temperature change
						case 4:
						case 6:
						case 7:
						case 10:
						case 11:
						case 12:
						case 13:
						case 16:
						case 18:
						case 25:
						case 26:
						case 27:
						case 28:
						case 29:
						case 132:
						case 140:
						case 155:
						case 156:
						case 157:
							//year
							tempadd=0.3f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							//day
							tempadd+=0.1f*MathHelper.sin((float)w.getWorldTime()/(20*60*20/(3.14f*2)));
							tempadd+=(x+y1)/70;
							tempadd-=w.rainingStrength*0.2f;
							Ftemperature.set(biome, biome.getTemperature()+tempadd);
							break;
							//medium temperature change
						case 1:
						case 5:
						case 19:
						case 30:
						case 31:
						case 32:
						case 33:
						case 129:
						case 133:
						case 158:
						case 160:
						case 161:
							//year
							tempadd=0.5f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							//day
							tempadd+=0.15f*MathHelper.sin((float)w.getWorldTime()/(20*60*20/(3.14f*2)));
							tempadd+=(x+z1)/60;
							tempadd-=w.rainingStrength*0.2f;
							Ftemperature.set(biome, biome.getTemperature()+tempadd);
							break;
							//big temperature change
						case 3:
						case 20:
						case 34:
						case 131:
						case 162:
							//year
							tempadd=0.8f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							//day
							tempadd+=0.1f*MathHelper.sin((float)w.getWorldTime()/(20*60*20/(3.14f*2)));
							tempadd+=(y+x1)/40;
							tempadd-=w.rainingStrength*0.3f;
							Ftemperature.set(biome, biome.getTemperature()+tempadd);
							break;

							//very big temperature change
						case 2:
						case 17:
						case 37:
						case 38:
						case 39:
						case 130:
						case 165:
						case 166:
						case 167:
							//year
							tempadd=0.2f*MathHelper.sin((float)w.getWorldTime()/(20*60*20*14*4/(3.14f*2)));
							tempadd+=(y+y1)/60;
							//day
							tempadd+=0.4f*MathHelper.sin((float)w.getWorldTime()/(20*60*20/(3.14f*2)));
							Ftemperature.set(biome, biome.getTemperature()+tempadd);
							break;
						}
						if(w.isRaining() && biome.getTemperature()<0.15f && !biome.canRain()){
							FenableRain.set(biome, true);
						}
					} catch (IllegalArgumentException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
				}
				/*
	    		try {
	    			Ftemperature.set(Biome.getBiome(1), 1+MathHelper.sin((float)w.getWorldTime()/1000f));
	    		} catch (IllegalArgumentException e1) {
	    			// TODO 自動生成された catch ブロック
	    			e1.printStackTrace();
	    		} catch (IllegalAccessException e1) {
	    			// TODO 自動生成された catch ブロック
	    			e1.printStackTrace();
	    		}
				 */
				//System.out.println(w.getTotalWorldTime());
				//System.out.println(w.getWorldTime());
				//System.out.println(w.getRainStrength(1));
			}
			//System.out.println(MathHelper.sin((float)e.world.getWorldTime()/1000f));
			boolean flag = w.isRaining();
			//if(flag){
			for (Iterator<Chunk> iterator = w.getPersistentChunkIterable(w.getPlayerChunkMap().getChunkIterator()); iterator.hasNext(); w.profiler.endSection())
			{
				Chunk chunk = iterator.next();
				int j = chunk.x * 16;
				int k = chunk.z * 16;
				if (w.provider.canDoRainSnowIce(chunk) && w.rand.nextInt(16) == 0)
				{
					updateLCG = updateLCG * 3 + 1013904223;
					int j2 = updateLCG >> 2;
						BlockPos blockpos1 = w.getPrecipitationHeight(new BlockPos(j + (j2 & 15), 0, k + (j2 >> 8 & 15)));
						BlockPos blockpos2 = blockpos1.down();
						Biome biome = w.getBiome(blockpos2);
						float f = biome.getFloatTemperature(blockpos2);
						if(flag && f<0.15f && w.getBlockState(blockpos1).getBlock()==Blocks.TALLGRASS){
							if (blockpos1.getY() >= 0 && blockpos1.getY() < 256 && w.getLightFor(EnumSkyBlock.BLOCK, blockpos1) < 9){
								w.setBlockState(blockpos1, Blocks.AIR.getDefaultState());
							}
						}

						if (((f>0.15f && flag)||f>0.25f)&& w.getBlockState(blockpos2).getBlock()==Blocks.ICE)
						{
							w.setBlockState(blockpos2, Blocks.WATER.getDefaultState());
						}

						if (((f>0.15f && flag)||f>0.25f) && w.getBlockState(blockpos1).getBlock()==Blocks.SNOW_LAYER&&Blocks.SNOW_LAYER.getMetaFromState(w.getBlockState(blockpos1))==0)
						{

							if(w.getBlockState(blockpos2).getBlock()==Blocks.GRASS && updateLCG%4==0){
								Random rand=w.rand;
								if (updateLCG%21==0)
								{
									w.getBiome(blockpos1).plantFlower(w, rand, blockpos1);
								}
								else if (updateLCG%7==0)
								{
									IBlockState iblockstate1 = Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT,BlockDoublePlant.EnumPlantType.GRASS).withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER);

									if (Blocks.DOUBLE_PLANT.canPlaceBlockAt(w, blockpos1))
									{
										Blocks.DOUBLE_PLANT.placeAt(w, blockpos1, BlockDoublePlant.EnumPlantType.GRASS, 3);
										//w.setBlockState(blockpos1, iblockstate1, 3);
										//w.setBlockState(blockpos1.up(), iblockstate1.withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 3);
									}
								}else
								{
									IBlockState iblockstate1 = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);

									if (Blocks.TALLGRASS.canBlockStay(w, blockpos1, iblockstate1))
									{
										w.setBlockState(blockpos1, iblockstate1, 3);
									}
									
									
								}
							}else{
								w.setBlockState(blockpos1, Blocks.AIR.getDefaultState());
							}
						}

				}
			}
			//}
		}
	}

	/*
	    @SubscribeEvent
	    protected static void registerItems(RegistryEvent.Register<Item> event){

	        event.getRegistry().registerAll(
	        		ITEMS.new_minecart,ITEMS.new_chest_minecart,ITEMS.new_detector_rail
	        );
	    }
	    @SubscribeEvent
	    protected static void registerBlocks(RegistryEvent.Register<Block> event){
	        event.getRegistry().registerAll(
	        		BLOCKS.new_detector_rail
	        );
	    }
	 */
}
