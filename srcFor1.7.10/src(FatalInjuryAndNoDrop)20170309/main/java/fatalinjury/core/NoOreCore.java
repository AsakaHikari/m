package fatalinjury.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="fatalinjuryMod",name="fatalinjuryMod",version="Alpha1_0")
public class NoOreCore {
	@Instance("fatalinjuryMod")
	public static NoOreCore instance;
	public static Potion potionfatalinjury;
	public static EventCaller eventCaller;
	public static int potionId;
	public static Item itemfirstaid;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property pIdP=cfg.get("system", "potionId", 72);

		cfg.save();
		
		itemfirstaid=new ItemFirstAid(0,0,false).setAlwaysEdible().setTextureName("fatalinjury:firstaid").setCreativeTab(CreativeTabs.tabFood).setUnlocalizedName("first_aid");
		GameRegistry.registerItem(itemfirstaid,"first_aid");
		potionId=pIdP.getInt();
		eventCaller=new EventCaller();
		 FMLCommonHandler.instance().bus().register(eventCaller);
		 MinecraftForge.EVENT_BUS.register(eventCaller);
		
		GameRegistry.addShapelessRecipe(new ItemStack(itemfirstaid),
				new ItemStack(Items.potionitem,1,8225), new ItemStack(Items.potionitem,1,8229),
				new ItemStack(Blocks.wool),new ItemStack(Blocks.wool),new ItemStack(Blocks.wool),new ItemStack(Blocks.chest));
		Potion[] potionTypes =null;

		for (Field f : Potion.class.getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

					potionTypes = (Potion[])f.get(null);
					final Potion[] newPotionTypes = new Potion[256];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
					f.set(null, newPotionTypes);
				}
			} catch (Exception ex) {
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(ex);
			}
		}
		
		potionfatalinjury=new PotionFatalInjury(potionId,true,0x800000).setPotionName("potion.fatalInjury");

	}

	@EventHandler
	public void init(FMLInitializationEvent e){

	}
}
