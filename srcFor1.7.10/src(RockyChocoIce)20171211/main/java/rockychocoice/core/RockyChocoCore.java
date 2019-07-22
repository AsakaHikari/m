/*    */package rockychocoice.core;

/*    */
/*    */import java.util.ArrayList;
import java.util.List;

import rockychocoice.enchantment.*;
import cpw.mods.fml.common.Mod;
/*    */
import cpw.mods.fml.common.Mod.EventHandler;
/*    */
import cpw.mods.fml.common.Mod.Instance;
/*    */
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
/*    */
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
/*    */
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
/*    */
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
/*    */
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
/*    */
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
/*    */
import net.minecraft.item.ItemArmor.ArmorMaterial;
/*    */
import net.minecraft.item.ItemStack;
/*    */
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
/*    */
import net.minecraftforge.common.util.EnumHelper;

/*    */
/*    */@Mod(modid = "RockyChocoIcecandy", name = "RockyChocoIcecandy", version = "Alpha1_1")
/*    */public class RockyChocoCore{
	/*    */
	/*    */@Mod.Instance("RockyChocoIcecandy")
	/*    */public static RockyChocoCore instance;
	public static Item itemRockyChoco;
	public static Enchantment icy,hardness,sweatness;
	public static DamageSource tooHardIce;
	public RockyChocoCore()
	{
		instance=this;
	}
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		tooHardIce=new DamageSource("tooHardIce").setDamageBypassesArmor();
		itemRockyChoco=new ItemRockyChoco().setUnlocalizedName("rocky_choco_icecandy").setTextureName("RockyChocoIcecandy:rockychoco");
		GameRegistry.registerItem(itemRockyChoco, "rocky_choco_icecandy");
		icy=new EnchantmentIcy(70,10,EnumEnchantmentType.weapon);
		hardness=new EnchantmentHardness(71,10,EnumEnchantmentType.weapon);
		sweatness=new EnchantmentSweatness(73,5,EnumEnchantmentType.weapon);
		MinecraftForge.EVENT_BUS.register(new EventReceiver());
		
		
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent e)
	{
		Item itemDeadrock=tryFindingItem("TwilightForest:tile.TFDeadrock");
		Item itemCastleBrick=tryFindingItem("TwilightForest:tile.CastleBrick");
		if(itemDeadrock!=null && itemCastleBrick!=null){
			GameRegistry.addRecipe(new ItemStack(itemRockyChoco), "DOD","DOD","CSC",
					'D',new ItemStack(itemDeadrock,1,2),'C',new ItemStack(itemCastleBrick,1,0),'O',Blocks.obsidian,'S',Items.stick);
		}else{
			GameRegistry.addRecipe(new ItemStack(itemRockyChoco), "DOD","DOD","CSC",
					'D',Items.snowball,'C',Blocks.snow,'O',Blocks.obsidian,'S',Items.stick);
		}
	}
	public Item tryFindingItem(String s){
		Item item = GameData.getItemRegistry().getObject(s);
		if (item != null)
		{
			return item;
		}

		Block block = GameData.getBlockRegistry().getObject(s);
		if (block != Blocks.air)
		{
			return Item.getItemFromBlock(block);
		}
		return null;
	}

}
