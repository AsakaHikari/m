package mod.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import mod.entity.*;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
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

@Mod(modid="shellMod",name="shellMod",version="Beta1_2_0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ShellCore {
	@Instance("shellMod")
	public static ShellCore instance;
	public static int shellId;
	public static Item itemShell;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		
		
		//Entity Registry from here
		 int normalShellId =EntityRegistry.findGlobalUniqueEntityId();
		 int bigShellId =EntityRegistry.findGlobalUniqueEntityId();
		 int hugeShellId =EntityRegistry.findGlobalUniqueEntityId();
		 int rubberBallId =EntityRegistry.findGlobalUniqueEntityId();
		 int normalHEId =EntityRegistry.findGlobalUniqueEntityId();
		 int bigHEId =EntityRegistry.findGlobalUniqueEntityId();
		 int hugeHEId =EntityRegistry.findGlobalUniqueEntityId();
		 int smallShellId =EntityRegistry.findGlobalUniqueEntityId();
		 
		 EntityRegistry.registerGlobalEntityID(EntityNormalShell.class, "NormalShell",normalShellId );
		 EntityRegistry.registerGlobalEntityID(EntityBigShell.class, "BigShell",bigShellId );
		 EntityRegistry.registerGlobalEntityID(EntityHugeShell.class, "HugeShell",hugeShellId );
		 EntityRegistry.registerGlobalEntityID(EntityRubberBall.class,"RubberBall",rubberBallId);
		 EntityRegistry.registerGlobalEntityID(EntityNormalHE.class,"NormalHE",normalHEId);
		 EntityRegistry.registerGlobalEntityID(EntityBigHE.class,"BigHE",bigHEId);
		 EntityRegistry.registerGlobalEntityID(EntityHugeHE.class,"HugeHE",hugeHEId);
		 EntityRegistry.registerGlobalEntityID(EntityNormalShell.class, "SmallShell",smallShellId );
		 
		 EntityRegistry.registerModEntity(EntityNormalShell.class, "NormalShell", 0, this, 80, 1, true);
		 EntityRegistry.registerModEntity(EntityBigShell.class, "BigShell", 1, this, 80, 1, true);
		 EntityRegistry.registerModEntity(EntityHugeShell.class, "HugeShell", 2, this, 80, 1, true);
		 EntityRegistry.registerModEntity(EntityRubberBall.class,"RubberBall",3,this,80,1,true);
		 EntityRegistry.registerModEntity(EntityNormalHE.class,"NormalHE",4,this,80,1,true);
		 EntityRegistry.registerModEntity(EntityBigHE.class,"BigHE",5,this,80,1,true);
		 EntityRegistry.registerModEntity(EntityHugeHE.class,"HugeHE",6,this,80,1,true);
		 

			BlockDispenser.dispenseBehaviorRegistry.putObject(itemShell, new IBehaviorDispenseItem(){

				@Override
				public ItemStack dispense(IBlockSource var1,
						ItemStack var2) {
					World world = var1.getWorld();//World
	                /*Item item = var2.getItem();*/ //Item
	                IPosition iposition = BlockDispenser.getIPositionFromBlockSource(var1);
	                EnumFacing enumfacing=BlockDispenser.getFacing(var1.getBlockMetadata());
	                switch(var2.getItemDamage()){
	                case 0:
	                	double x0 = iposition.getX()+0.3*(double)enumfacing.getFrontOffsetX();//
	                	double y0 = iposition.getY()-0.2*(double)enumfacing.getFrontOffsetY();//
	                	double z0 = iposition.getZ()+0.3*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityNormalShell(world,x0,y0,z0));//
	                	break;
	                case 1:
	                	double x1 = iposition.getX()+0.8*(double)enumfacing.getFrontOffsetX();//
	                	double y1 = iposition.getY()+0.3*(double)enumfacing.getFrontOffsetY();//
	                	double z1 = iposition.getZ()+0.8*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityBigShell(world,x1,y1,z1));//
	                	break;
	                case 2:
	                	double x2 = iposition.getX()+1.3*(double)enumfacing.getFrontOffsetX();//
	                	double y2 = iposition.getY()+0.8*(double)enumfacing.getFrontOffsetY();//
	                	double z2 = iposition.getZ()+1.3*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityHugeShell(world,x2,y2,z2));//
	                	break;
	                case 3:
	                	double x3 = iposition.getX()+0.3*(double)enumfacing.getFrontOffsetX();//
	                	double y3 = iposition.getY()-0.2*(double)enumfacing.getFrontOffsetY();//
	                	double z3 = iposition.getZ()+0.3*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityRubberBall(world,x3,y3,z3));//
	                	break;
	                case 4:
	                	double x4 = iposition.getX()+0.3*(double)enumfacing.getFrontOffsetX();//
	                	double y4 = iposition.getY()-0.2*(double)enumfacing.getFrontOffsetY();//
	                	double z4 = iposition.getZ()+0.3*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityNormalHE(world,x4,y4,z4));//
	                	break;
	                case 5:
	                	double x5 = iposition.getX()+0.8*(double)enumfacing.getFrontOffsetX();//
	                	double y5 = iposition.getY()+0.3*(double)enumfacing.getFrontOffsetY();//
	                	double z5 = iposition.getZ()+0.8*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityBigHE(world,x5,y5,z5));//
	                	break;
	                case 6:
	                	double x6 = iposition.getX()+1.3*(double)enumfacing.getFrontOffsetX();//
	                	double y6 = iposition.getY()+0.8*(double)enumfacing.getFrontOffsetY();//
	                	double z6 = iposition.getZ()+1.3*(double)enumfacing.getFrontOffsetZ();//
	                	world.spawnEntityInWorld(new EntityHugeHE(world,x6,y6,z6));//
	                	break;
	                }
	                return var2.splitStack(var2.stackSize-1);//
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
		 
		 GameRegistry.addShapelessRecipe(new ItemStack(itemShell,1,4),
				 new Object[]{
			 new ItemStack(itemShell,1,0),Block.tnt
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
		 LanguageRegistry.instance().addStringLocalization("entity.SmallShell.name", "en_US", "SmallShell");
		 
		 LanguageRegistry.addName(new ItemStack(itemShell,1,0), "Shell");
		 LanguageRegistry.addName(new ItemStack(itemShell,1,1), "BigShell");
		 LanguageRegistry.addName(new ItemStack(itemShell,1,2), "HugeShell");
		 LanguageRegistry.addName(new ItemStack(itemShell,1,3), "RubberBall");
		 LanguageRegistry.addName(new ItemStack(itemShell,1,4), "HighExplosive");
		 LanguageRegistry.addName(new ItemStack(itemShell,1,5), "BigHE");
		 LanguageRegistry.addName(new ItemStack(itemShell,1,6), "HugeHE");
		 
		
		
		 //id is an internal mob id, you can start at 0 and continue adding them up.
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalShell.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigShell.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeShell.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityRubberBall.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/rubberball.png","shellmod:textures/entity/rubberball_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalHE.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigHE.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeHE.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png"));
		 
		 
	 }
}
