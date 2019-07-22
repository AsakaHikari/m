package mod.core;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;

import mod.entity.*;
import mod.renderer.RenderShell;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="shellMod",name="shellMod",version="Beta1_5_1")
public class ShellCore {
	@Instance("shellMod")
	public static ShellCore instance;
	public static int shellId;
	public static Item itemShell;
	@SidedProxy(clientSide="mod.core.ShellCoreClient",serverSide="mod.core.ShellCoreProxy")
	public static ShellCoreProxy proxy;

	public static double damageRate,destructionRate,explosionRate;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e){

		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property damageP=cfg.get("System", "How much the shells cause damages for the entities.default:1", 1d);
		Property destructionP=cfg.get("System", "How much the shells break blocks.default:1", 1d);
		Property explosionP=cfg.get("System", "How big the HEs cause explosions.default:1", 1d);

		damageRate=damageP.getDouble();
		destructionRate=destructionP.getDouble();
		explosionRate=explosionP.getDouble();
		cfg.save();

		itemShell=new ItemShell();
		GameRegistry.registerItem(itemShell,"shell");

		//set a more readable name for the entity in given language
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,0), 
				new Object[]{ " I ","ISI"," I ",
			'I',Item.itemRegistry.getObject("iron_ingot") ,'S',Item.itemRegistry.getObject("slime_ball")});

		GameRegistry.addRecipe(new ItemStack(itemShell, 1,1), 
				new Object[]{ " I ","ISI"," I ",
			'I',Item.itemRegistry.getObject("iron_ingot") ,'S',new ItemStack(itemShell, 1,0)});

		GameRegistry.addRecipe(new ItemStack(itemShell, 1,2), 
				new Object[]{ " I ","ISI"," I ",
			'I',Item.itemRegistry.getObject("iron_ingot") ,'S',new ItemStack(itemShell, 1,1)});

		GameRegistry.addRecipe(new ItemStack(itemShell, 1,3), 
				new Object[]{ " I ","ISI"," I ",
			'I',Item.itemRegistry.getObject("leather") ,'S',Item.itemRegistry.getObject("slime_ball")});

		GameRegistry.addRecipe(new ItemStack(itemShell, 1,5), 
				new Object[]{ " I ","ISI"," I ",
			'I',Block.blockRegistry.getObject("tnt") ,'S',new ItemStack(itemShell,1,1)});

		GameRegistry.addRecipe(new ItemStack(itemShell, 1,6), 
				new Object[]{ "III","ISI","III",
			'I',Block.blockRegistry.getObject("tnt"),'S',new ItemStack(itemShell,1,2)});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
				new Object[]{ "SN","RR","RC",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,7),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
				new Object[]{ "SN","RR","RC",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,8),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
				new Object[]{ "SN","RR","RC",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,9),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
				new Object[]{ "SN","RR","CR",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,7),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
				new Object[]{ "SN","RR","CR",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,8),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
				new Object[]{ "SN","RR","CR",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,9),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		

		GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
				new Object[]{ "NS","RR","RC",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,7),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
				new Object[]{ "NS","RR","RC",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,8),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
				new Object[]{ "NS","RR","RC",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,9),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		

		GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
				new Object[]{ "NS","RR","CR",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,7),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
				new Object[]{ "NS","RR","CR",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,8),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});
		
		GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
				new Object[]{ "NS","RR","CR",
			'N',Block.blockRegistry.getObject("noteblock"),'S',new ItemStack(itemShell,1,9),
			'R',Item.itemRegistry.getObject("redstone"),'C',Item.itemRegistry.getObject("comparator")});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,4),
				new Object[]{
			new ItemStack(itemShell,1,0),Block.blockRegistry.getObject("tnt")
		});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,7),
				new Object[]{
			new ItemStack(itemShell,1,4),Block.blockRegistry.getObject("redstone_torch")
		});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,8),
				new Object[]{
			new ItemStack(itemShell,1,5),Block.blockRegistry.getObject("redstone_torch")
		});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,9),
				new Object[]{
			new ItemStack(itemShell,1,6),Block.blockRegistry.getObject("redstone_torch")
		});

		GameRegistry.addShapelessRecipe(new ItemStack((Item)Item.itemRegistry.getObject("iron_ingot"),4),
				new Object[]{
			new ItemStack(itemShell,1,0)});

		GameRegistry.addShapelessRecipe(new ItemStack((Item)Item.itemRegistry.getObject("iron_ingot"),8),
				new Object[]{
			new ItemStack(itemShell,1,1)});

		GameRegistry.addShapelessRecipe(new ItemStack((Item)Item.itemRegistry.getObject("iron_ingot"),12),
				new Object[]{
			new ItemStack(itemShell,1,2)});

		GameRegistry.addShapelessRecipe(new ItemStack((Item)Item.itemRegistry.getObject("leather"),4),
				new Object[]{
			new ItemStack(itemShell,1,3)});

		GameRegistry.addShapelessRecipe(new ItemStack((Block)Block.blockRegistry.getObject("tnt"),1),
				new Object[]{
			new ItemStack(itemShell,1,4)});

		GameRegistry.addShapelessRecipe(new ItemStack((Block)Block.blockRegistry.getObject("tnt"),4),
				new Object[]{
			new ItemStack(itemShell,1,5)});

		GameRegistry.addShapelessRecipe(new ItemStack((Block)Block.blockRegistry.getObject("tnt"),8),
				new Object[]{
			new ItemStack(itemShell,1,6)});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,4),
				new Object[]{
			new ItemStack(itemShell,1,7)});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,5),
				new Object[]{
			new ItemStack(itemShell,1,8)});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,6),
				new Object[]{
			new ItemStack(itemShell,1,9)});
		
		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,7),
				new Object[]{
			new ItemStack(itemShell,1,10)});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,8),
				new Object[]{
			new ItemStack(itemShell,1,11)});

		GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,9),
				new Object[]{
			new ItemStack(itemShell,1,12)});
		
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ShellChunkManager());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	
		/*
		 EntityRegistry.registerGlobalEntityID(EntityNormalShell.class, "NormalShell",normalShellId );
		 EntityRegistry.registerGlobalEntityID(EntityBigShell.class, "BigShell",bigShellId );
		 EntityRegistry.registerGlobalEntityID(EntityHugeShell.class, "HugeShell",hugeShellId );
		 EntityRegistry.registerGlobalEntityID(EntityRubberBall.class,"RubberBall",rubberBallId);
		 EntityRegistry.registerGlobalEntityID(EntityNormalHE.class,"NormalHE",normalHEId);
		 EntityRegistry.registerGlobalEntityID(EntityBigHE.class,"BigHE",bigHEId);
		 EntityRegistry.registerGlobalEntityID(EntityHugeHE.class,"HugeHE",hugeHEId);
		 EntityRegistry.registerGlobalEntityID(EntityNormalShell.class, "SmallShell",smallShellId );
		 */
		EntityRegistry.registerModEntity(EntityNormalShell.class, "NormalShell", 0, this, 80, 4, true);
		EntityRegistry.registerModEntity(EntityBigShell.class, "BigShell", 1, this, 80, 4, true);
		EntityRegistry.registerModEntity(EntityHugeShell.class, "HugeShell", 2, this, 80, 4, true);
		EntityRegistry.registerModEntity(EntityRubberBall.class,"RubberBall",3,this,80,4,true);
		EntityRegistry.registerModEntity(EntityNormalHE.class,"NormalHE",4,this,80,4,true);
		EntityRegistry.registerModEntity(EntityBigHE.class,"BigHE",5,this,80,4,true);
		EntityRegistry.registerModEntity(EntityHugeHE.class,"HugeHE",6,this,80,4,true);
		EntityRegistry.registerModEntity(EntityNormalPointImpactHE.class,"NormalHE(PointImpact)",7,this,80,4,true);
		EntityRegistry.registerModEntity(EntityBigPointImpactHE.class,"BigHE(PointImpact)",8,this,80,4,true);
		EntityRegistry.registerModEntity(EntityHugePointImpactHE.class,"HugeHE(PointImpact)",9,this,80,4,true);
		EntityRegistry.registerModEntity(EntityNormalHEProximity.class,"NormalHE(Proximity)",10,this,80,4,true);
		EntityRegistry.registerModEntity(EntityBigHEProximity.class,"BigHE(Proximity)",11,this,80,4,true);
		EntityRegistry.registerModEntity(EntityHugeHEProximity.class,"HugeHE(Proximity)",12,this,80,4,true);
		//EntityRegistry.registerModEntity(EntityHEProximityFuse.class,"NormalHE(Proximity)",10,this,80,4,true);
		proxy.registerRenderer();
		BlockDispenser.dispenseBehaviorRegistry.putObject(itemShell, new IBehaviorDispenseItem(){
			@Override
			public ItemStack dispense(IBlockSource var1,
					ItemStack var2) {
				World world = var1.getWorld();//World
				/*Item item = var2.getItem();*/ //Item
				IPosition iposition = BlockDispenser.func_149939_a(var1);
				EnumFacing enumfacing=BlockDispenser.func_149937_b(var1.getBlockMetadata());

				if(var2.getItem() instanceof ItemShell){
					ItemShell item=(ItemShell)var2.getItem();
					EntityShell entity=item.getEntityFromDamage(var2.getItemDamage(),world);
					entity.setLocationAndAngles(var1.getX()+(double)enumfacing.getFrontOffsetX()*(entity.width/2+0.5)
							, var1.getY()+(double)enumfacing.getFrontOffsetY()*(entity.height/2+0.5)-entity.height/2
							,var1.getZ()+(double)enumfacing.getFrontOffsetZ()*(entity.width/2+0.5),0.0f,0.0f);
					if (!world.getCollidingBoundingBoxes(entity, entity.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
					{
						return var2;
					}
					world.spawnEntityInWorld(entity);
					/**1_5_1*/
					var2.splitStack(1);//
					return var2;
					//System.out.println(var1.getWorld().isRemote);
					//System.out.println(entity.posX+","+entity.posY+","+entity.posZ);
					/*
	                switch(var2.getItemDamage()){
	                case 0:
	                	double x0 = iposition.getX()+0.5*(double)enumfacing.getFrontOffsetX();//
	                	double y0 = iposition.getY()+0.5*(double)enumfacing.getFrontOffsetY();//
	                	double z0 = iposition.getZ()+0.5*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityNormalShell(world,x0,y0,z0));//
	                	break;
	                case 1:
	                	double x1 = iposition.getX()+1*(double)enumfacing.getFrontOffsetX();//
	                	double y1 = iposition.getY()+1*(double)enumfacing.getFrontOffsetY();//
	                	double z1 = iposition.getZ()+1*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityBigShell(world,x1,y1,z1));//
	                	break;
	                case 2:
	                	double x2 = iposition.getX()+1.5*(double)enumfacing.getFrontOffsetX();//
	                	double y2 = iposition.getY()+1.5*(double)enumfacing.getFrontOffsetY();//
	                	double z2 = iposition.getZ()+1.5*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityHugeShell(world,x2,y2,z2));//
	                	break;
	                case 3:
	                	double x3 = iposition.getX()+0.5*(double)enumfacing.getFrontOffsetX();//
	                	double y3 = iposition.getY()+0.5*(double)enumfacing.getFrontOffsetY();//
	                	double z3 = iposition.getZ()+0.5*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityRubberBall(world,x3,y3,z3));//
	                	break;
	                case 4:
	                	double x4 = iposition.getX()+0.5*(double)enumfacing.getFrontOffsetX();//
	                	double y4 = iposition.getY()+0.5*(double)enumfacing.getFrontOffsetY();//
	                	double z4 = iposition.getZ()+0.5*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityNormalHE(world,x4,y4,z4));//
	                	break;
	                case 5:
	                	double x5 = iposition.getX()+1*(double)enumfacing.getFrontOffsetX();//
	                	double y5 = iposition.getY()+1*(double)enumfacing.getFrontOffsetY();//
	                	double z5 = iposition.getZ()+1*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityBigHE(world,x5,y5,z5));//
	                	break;
	                case 6:
	                	double x6 = iposition.getX()+1.5*(double)enumfacing.getFrontOffsetX();//
	                	double y6 = iposition.getY()+1.5*(double)enumfacing.getFrontOffsetY();//
	                	double z6 = iposition.getZ()+1.5*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityHugeHE(world,x6,y6,z6));//
	                	break;
	                case 7:
	                	double x7 = iposition.getX()+0.5*(double)enumfacing.getFrontOffsetX();//
	                	double y7 = iposition.getY()+0.5*(double)enumfacing.getFrontOffsetY();//
	                	double z7 = iposition.getZ()+0.5*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityNormalPointImpactHE(world,x7,y7,z7));//
	                	break;
	                case 8:
	                	double x8 = iposition.getX()+1*(double)enumfacing.getFrontOffsetX();//
	                	double y8 = iposition.getY()+1*(double)enumfacing.getFrontOffsetY();//
	                	double z8 = iposition.getZ()+1*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityBigPointImpactHE(world,x8,y8,z8));//
	                	break;
	                case 9:
	                	double x9 = iposition.getX()+1.5*(double)enumfacing.getFrontOffsetX();//
	                	double y9 = iposition.getY()+1.5*(double)enumfacing.getFrontOffsetY();//
	                	double z9 = iposition.getZ()+1.5*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityHugePointImpactHE(world,x9,y9,z9));//
	                	break;
	                }
					 */
				}
				return var2;
			}

		});


		LanguageRegistry.instance().addStringLocalization("entity.NormalShell.name", "en_US", "NormalShell");
		LanguageRegistry.instance().addStringLocalization("entity.BigShell.name", "en_US", "BigShell");
		LanguageRegistry.instance().addStringLocalization("entity.HugeShell.name", "en_US", "HugeShell");
		LanguageRegistry.instance().addStringLocalization("entity.RubberBall.name","en_US","RubberBall");
		LanguageRegistry.instance().addStringLocalization("entity.NormalHE.name","en_US","NormalHE");
		LanguageRegistry.instance().addStringLocalization("entity.BigHE.name","en_US","BigHE");
		LanguageRegistry.instance().addStringLocalization("entity.HugeHE.name","en_US","HugeHE");
		LanguageRegistry.instance().addStringLocalization("entity.NormalHE(PointImpact).name","en_US","NormalHE(PointImpact)");
		LanguageRegistry.instance().addStringLocalization("entity.BigHE(PointImpact).name","en_US","BigHE(PointImpact)");
		LanguageRegistry.instance().addStringLocalization("entity.HugeHE(PointImpact).name","en_US","HugeHE(PointImpact)");
		LanguageRegistry.instance().addStringLocalization("entity.NormalHEProximity.name","en_US","NormalHE(Proximity)");
		LanguageRegistry.instance().addStringLocalization("entity.BigHEProximity.name","en_US","BigHE(Proximity)");
		LanguageRegistry.instance().addStringLocalization("entity.HugeHEProximity.name","en_US","HugeHE(Proximity)");

		LanguageRegistry.addName(new ItemStack(itemShell,1,0), "Shell");
		LanguageRegistry.addName(new ItemStack(itemShell,1,1), "BigShell");
		LanguageRegistry.addName(new ItemStack(itemShell,1,2), "HugeShell");
		LanguageRegistry.addName(new ItemStack(itemShell,1,3), "RubberBall");
		LanguageRegistry.addName(new ItemStack(itemShell,1,4), "HighExplosive");
		LanguageRegistry.addName(new ItemStack(itemShell,1,5), "BigHE");
		LanguageRegistry.addName(new ItemStack(itemShell,1,6), "HugeHE");
		LanguageRegistry.addName(new ItemStack(itemShell,1,7), "HighExplosive(PointImpact)");
		LanguageRegistry.addName(new ItemStack(itemShell,1,8), "BigHE(PointImpact)");
		LanguageRegistry.addName(new ItemStack(itemShell,1,9), "HugeHE(PointImpact)");
		LanguageRegistry.addName(new ItemStack(itemShell,1,10), "HighExplosive(Proximity)");
		LanguageRegistry.addName(new ItemStack(itemShell,1,11), "BigHE(Proximity)");
		LanguageRegistry.addName(new ItemStack(itemShell,1,12), "HugeHE(Proximity)");

	}
}
