package mod.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import mod.entity.*;
import mod.renderer.RenderShell;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.EnumMobType;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="shellMod",name="shellMod",version="Beta1_5")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
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
					IPosition iposition = BlockDispenser.getIPositionFromBlockSource(var1);
					EnumFacing enumfacing=BlockDispenser.getFacing(var1.getBlockMetadata());

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
						//System.out.println(var1.getWorld().isRemote);
						//System.out.println(entity.posX+","+entity.posY+","+entity.posZ);
						
					}
					return var2.splitStack(var2.stackSize-1);
				}
				
			});
		 
		 //Config Registry from here
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			Property itemProp  = cfg.getItem("shell", 19000);
			itemProp.comment   = "";
			shellId  = itemProp.getInt();
			Property damageP=cfg.get("System", "How much the shells cause damages for the entities.default:1", 1d);
			Property destructionP=cfg.get("System", "How much the shells break Block.default:1", 1d);
			Property explosionP=cfg.get("System", "How big the HEs cause explosions.default:1", 1d);

			damageRate=damageP.getDouble(1);
			destructionRate=destructionP.getDouble(1);
			explosionRate=explosionP.getDouble(1);
		}
		catch (Exception e1)
		{
			FMLLog.log(Level.SEVERE, e1, "Error Message");
		}
		finally
		{
			cfg.save();
		}
		
		//Item Registry from here
				itemShell=new ItemShell(shellId);
				GameRegistry.registerItem(itemShell,"shell");
		
		//Recipe Registry from here
		 GameRegistry.addRecipe(new ItemStack(itemShell, 1,0), 
					new Object[]{ " I ","ISI"," I ",
					'I',Item.ingotIron ,'S',Item.slimeBall});
		 
		 GameRegistry.addRecipe(new ItemStack(itemShell, 1,1), 
					new Object[]{ " I ","ISI"," I ",
					'I',Item.ingotIron ,'S',new ItemStack(itemShell, 1,0)});
		 
		 GameRegistry.addRecipe(new ItemStack(itemShell, 1,2), 
					new Object[]{ " I ","ISI"," I ",
					'I',Item.ingotIron ,'S',new ItemStack(itemShell, 1,1)});
		 
		 GameRegistry.addRecipe(new ItemStack(itemShell, 1,3), 
					new Object[]{ " I ","ISI"," I ",
					'I',Item.leather ,'S',Item.slimeBall});
		 
		 GameRegistry.addRecipe(new ItemStack(itemShell, 1,5), 
					new Object[]{ " I ","ISI"," I ",
					'I',Block.tnt ,'S',new ItemStack(itemShell,1,1)});
		 
		 GameRegistry.addRecipe(new ItemStack(itemShell, 1,6), 
					new Object[]{ "III","ISI","III",
					'I',Block.tnt ,'S',new ItemStack(itemShell,1,2)});
		 

			GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
					new Object[]{ "SN","RR","RC",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
					new Object[]{ "SN","RR","RC",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
					new Object[]{ "SN","RR","RC",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			

			GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
					new Object[]{ "NS","RR","RC",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
					new Object[]{ "NS","RR","RC",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
					new Object[]{ "NS","RR","RC",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			

			GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
					new Object[]{ "SN","RR","CR",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
					new Object[]{ "SN","RR","CR",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
					new Object[]{ "SN","RR","CR",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			

			GameRegistry.addRecipe(new ItemStack(itemShell, 1,10), 
					new Object[]{ "NS","RR","CR",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,11), 
					new Object[]{ "NS","RR","CR",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
			GameRegistry.addRecipe(new ItemStack(itemShell, 1,12), 
					new Object[]{ "NS","RR","CR",
				'N',Block.music,'S',new ItemStack(itemShell,1,7),
				'R',Item.redstone,'C',Item.comparator});
			
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,4),
				 new Object[]{
			 new ItemStack(itemShell,1,0),Block.tnt
		 });
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,7),
					new Object[]{
				new ItemStack(itemShell,1,4),Block.torchRedstoneActive
			});

			GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,8),
					new Object[]{
				new ItemStack(itemShell,1,5),Block.torchRedstoneActive
			});

			GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,9),
					new Object[]{
				new ItemStack(itemShell,1,6),Block.torchRedstoneActive
			});
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(Item.ingotIron,4),
					new Object[]{
					new ItemStack(itemShell,1,0)});
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(Item.ingotIron,8),
					new Object[]{
					new ItemStack(itemShell,1,1)});
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(Item.ingotIron,12),
					new Object[]{
					new ItemStack(itemShell,1,2)});
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(Item.leather,4),
					new Object[]{
					new ItemStack(itemShell,1,3)});
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(Block.tnt,1),
					new Object[]{
					new ItemStack(itemShell,1,4)});
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(Block.tnt,4),
					new Object[]{
					new ItemStack(itemShell,1,5)});
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(Block.tnt,8),
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
