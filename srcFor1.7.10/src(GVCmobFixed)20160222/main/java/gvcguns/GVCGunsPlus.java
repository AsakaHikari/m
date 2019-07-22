package gvcguns;


import gvcguns.client.ClientProxyGVC;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.EntityEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;


@Mod(
		modid	= "GVCGuns",
		name	= "GVCGuns",
		version	= "1.7.x-srg-1"
		)
public class GVCGunsPlus {

	//private static final ToolMaterial IRON = null;

	//@SidedProxy(clientSide = "mmm.FN5728Guns.ProxyClient", serverSide = "mmm.lib.ProxyCommon")
	//public static ProxyCommon proxy;
	
	//public static boolean isArmorPiercing = true; 
	//public static boolean UnlimitedInfinity = false;
	@SidedProxy(clientSide = "gvcguns.client.ClientProxyGVC", serverSide = "gvcguns.CommonSideProxyGVC")
	public static CommonSideProxyGVC proxy;
	@Mod.Instance("GVCGuns")
	public static GVCGunsPlus INSTANCE;

	//GVCGunsReversion
	//public static final KeyBinding Speedreload = new KeyBinding("Key.reload", Keyboard.KEY_R, "GVCGunsPlus");
	
	public static boolean isDebugMessage = true;
	
	public static boolean cfg_exprotion = true;
	public static boolean cfg_FriendFireLMM = true;
	
	public static boolean cfg_GunModel3D;
	public static boolean cfg_ADSKeytype;
	
	
	public static Item fn_magazine;
	public static Item fn_magazinehg;
	public static Item fn_magazinemg;
	public static Item fn_magazinerpg;
	public static Item fn_shell;
	public static Item fn_rpg;
	public static Item fn_missile;
	
	public static Item fn_reddot;
	public static Item fn_box;
	public static Item fn_pra;
	
	public static Item fn_torch;
	public static Item fn_paras;
	
	public static Item fn_cm;
	public static Item fn_health;
	
	public static ArmorMaterial praarmor;
	public static Item fn_prahelmet;
	public static Item fn_prachestp;
	public static Item fn_praleggings;
	public static Item fn_praboots;
	
	public static ArmorMaterial mugen;
	public static Item fn_mugenb;
	
	
	public static Item fn_grenade;
	public static Item fn_hundframe;
	public static Item fn_scope;
	public static Item fn_bayonet;
	public static Item fn_m320;
	public static Item fn_c4;
	public static Item fn_c4cn;
	
	
	public static Item fn_ak74;
	public static Item fn_ak74sp;
	public static Item fn_rpk74;
	public static Item fn_aks74u;
	public static Item fn_ak74gb30;
	public static Item fn_ak74b;
	
	public static Item fn_m10;
	public static Item fn_uzi;
	public static Item fn_cbj;
	
	public static Item fn_m1911;
	public static Item fn_m870;
	public static Item fn_svd;
	public static Item fn_pkm;
	public static Item fn_pkmsp;
	
	
	
	public static Item fn_m16a4;
	public static Item fn_m16a4sp;
	public static Item fn_m249;
	public static Item fn_m4a1;
	public static Item fn_m16a4gl;
	public static Item fn_mp7;
	public static Item fn_m9;
	public static Item fn_p226;
	public static Item fn_g17;
	public static Item fn_g18;
	public static Item fn_m110;
	public static Item fn_m240b;
	public static Item fn_m240bsp;
	public static Item fn_r700;
	public static Item fn_m82a3;
	
	public static Item fn_g36;
	public static Item fn_g36c;
	public static Item fn_mg36;
	public static Item fn_g36m320;
	
	
	public static Item fn_rpg7;
	public static Item fn_fim92;
	public static Item fn_mbtlaw;
	
	
	public static Item fn_m134;
	
	
	public static Block fn_camp;
	
	public static Block fn_gunbox;
	public static Block fn_ied;
	public static Block fn_cont;
	
	
	public static int bullet = 170;
	public static Item fn_bullet;
	
	public static int entitygrenade = 171;
	public static Item fn_entitygrenade;
	
	public static int bulletg = 172;
	
	public static int grenadebullet = 173;
	
	public static int rpg = 174;
	
	public static int laser = 175;
	
	public static int gustorch = 176;
	
	public static int paras = 177;

	public static final int GUI_ID = 1;
	public static final int GUI_ID2 = 2;

	public static Enchantment reddot;
	public static int en_reddotID;
	public static Enchantment lasersight;
	public static int en_laserID;
	public static Enchantment scopesight;
	public static int en_scopeID;
	
	
	protected static File configFile;
	
	
	public static final CreativeTabs tabgvc = new GVCCreativeTab("GVCTab");
	


	public static void Debug(String pText, Object... pData) {
		if (isDebugMessage) {
			System.out.println(String.format("GVCGuns-" + pText, pData));
		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent pEvent) {
		configFile = pEvent.getSuggestedConfigurationFile();
		Configuration lconf = new Configuration(configFile);
		lconf.load();
		isDebugMessage	= lconf.get("RefinedMilitaryShovelReplica", "isDebugMessage", true).getBoolean(true);
		cfg_exprotion	= lconf.get("RefinedMilitaryShovelReplica", "cfg_exprotion", true).getBoolean(true);
		cfg_GunModel3D	= lconf.get("RefinedMilitaryShovelReplica", "cfg_GunModel3D", true).getBoolean(true);
		cfg_ADSKeytype	= lconf.get("RefinedMilitaryShovelReplica", "cfg_ADSKeytype", false).getBoolean(false);
		cfg_FriendFireLMM	= lconf.get("RefinedMilitaryShovelReplica", "cfg_FriendFireLMM", false).getBoolean(false);
		en_reddotID	= lconf.get("RefinedMilitaryShovelReplica", "cfg_EnchantID_RedDot", 185).getInt(185);
		en_laserID	= lconf.get("RefinedMilitaryShovelReplica", "cfg_EnchantID_Laser", 186).getInt(186);
		en_scopeID	= lconf.get("RefinedMilitaryShovelReplica", "cfg_EnchantID_Scope", 187).getInt(187);
		lconf.save();
		
		
		
		
		
		
		fn_magazine	= new GVCItemmagazine().setUnlocalizedName("magazine").setTextureName("gvcguns:magazine");
	    GameRegistry.registerItem(fn_magazine, "magazine");
	    fn_magazinehg	= new GVCItemmagazine().setUnlocalizedName("magazinehg").setTextureName("gvcguns:magazinehg");
	    GameRegistry.registerItem(fn_magazinehg, "magazinehg");
	    fn_magazinemg	= new GVCItemmagazine().setUnlocalizedName("magazinemg").setTextureName("gvcguns:magazinemg");
	    GameRegistry.registerItem(fn_magazinemg, "magazinemg");
	    fn_shell	= new Item().setUnlocalizedName("shell").setTextureName("gvcguns:shell").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_shell, "shell");
	    fn_rpg	= new Item().setUnlocalizedName("rpg").setTextureName("gvcguns:rpg").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_rpg, "rpg");
	    fn_missile	= new Item().setUnlocalizedName("missile").setTextureName("gvcguns:missile").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_missile, "missile");
	    
	    fn_reddot	= new Item().setUnlocalizedName("RedDot").setTextureName("gvcguns:reddot").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_reddot, "RedDot");
	    fn_box	= new GVCItemBox().setUnlocalizedName("box").setTextureName("gvcguns:box");
	    GameRegistry.registerItem(fn_box, "box");
	    fn_pra	= new Item().setUnlocalizedName("pra").setTextureName("gvcguns:pra").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_pra, "pra");
	    
	    
	    fn_torch	= new GVCItemtorch().setUnlocalizedName("torch").setTextureName("gvcguns:gustorch");
	    GameRegistry.registerItem(fn_torch, "torch");
	    //fn_paras	= new GVCItemparas().setUnlocalizedName("paras").setTextureName("gvcguns:paras");
	    //GameRegistry.registerItem(fn_paras, "paras");
	    fn_cm	= new GVCItemCM(10, false).setUnlocalizedName("cm").setTextureName("gvcguns:CM");
	    GameRegistry.registerItem(fn_cm, "cm");
	    fn_health	= new GVCItemHealthPack().setUnlocalizedName("HealthPack").setTextureName("gvcguns:HealthPack");
	    GameRegistry.registerItem(fn_health, "HealthPack");
	    
	    praarmor = EnumHelper.addArmorMaterial("PraArmor", 430, new int[] {4, 8, 6, 2}, 10);
	    fn_prahelmet	= new GVCItemArmorPra(praarmor, 0).setUnlocalizedName("prahelmet").setTextureName("gvcguns:prahelmet").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_prahelmet, "prahelmet");
	    fn_prachestp	= new GVCItemArmorPra(praarmor, 1).setUnlocalizedName("prachestp").setTextureName("gvcguns:prachestp").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_prachestp, "prachestp");
	    fn_praleggings	= new GVCItemArmorPra(praarmor, 2).setUnlocalizedName("praleggings").setTextureName("gvcguns:praleggings").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_praleggings, "praleggings");
	    fn_praboots	= new GVCItemArmorPra(praarmor, 3).setUnlocalizedName("praboots").setTextureName("gvcguns:praboots").setCreativeTab(tabgvc);
	    GameRegistry.registerItem(fn_praboots, "praboots");
	    
	    //mugen = EnumHelper.addArmorMaterial("mugen", 400, new int[] {2, 4, 3, 1}, 10);
	    //fn_mugenb	= new GVCItemArmorMugen().setUnlocalizedName("mugenbandaba").setTextureName("gvcguns:mugenbandana").setCreativeTab(tabgvc);
	    //GameRegistry.registerItem(fn_mugenb, "mugenbandana");
	    
	    
	    
	    fn_grenade	= new GVCItemGrenade().setUnlocalizedName("grenade").setTextureName("gvcguns:grenade");
	    GameRegistry.registerItem(fn_grenade, "Grenade");
	    fn_hundframe	= new GVCItemGrenade_HundF().setUnlocalizedName("HundFrame").setTextureName("gvcguns:hundframe");
	    GameRegistry.registerItem(fn_hundframe, "HundFrame");
	    fn_entitygrenade	= new GVCItem().setUnlocalizedName("entitygrenade").setTextureName("gvcguns:entitygrenade");
	    GameRegistry.registerItem(fn_entitygrenade, "EntityGrenade");
	    fn_bullet	= new GVCItem().setUnlocalizedName("bullet").setTextureName("gvcguns:bullet");
	    GameRegistry.registerItem(fn_bullet, "bullet");
	    fn_scope	= new GVCItemmagazine().setUnlocalizedName("scope").setTextureName("gvcguns:scope");
	    GameRegistry.registerItem(fn_scope, "SCOPE");
	    fn_bayonet	= new GVCItemBayonet().setUnlocalizedName("bayonet").setTextureName("gvcguns:Bayonet");
	    GameRegistry.registerItem(fn_bayonet, "Bayonet");
	    fn_m320	= new GVCItemM320().setUnlocalizedName("M320").setTextureName("gvcguns:M320");
	    GameRegistry.registerItem(fn_m320, "M320");
	    fn_c4	= new GVCItemC4().setUnlocalizedName("C4").setTextureName("gvcguns:C4");
	    GameRegistry.registerItem(fn_c4, "C4");
	    fn_c4cn	= new GVCItemC4cn().setUnlocalizedName("C4cn").setTextureName("gvcguns:C4cn");
	    GameRegistry.registerItem(fn_c4cn, "C4cn");
	    
	    
	    /*
		 * int power
		 * float speed
		 * float bure
		 * double recoil
		 * int reload
		 * float bayonet
		 * float zoom
		 */
		fn_ak74	    = new GVCItemAK74(8,4f,10f,1.2d,50,2.0f,1.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("AK74").setTextureName("gvcguns:AK74")
				.setMaxDamage(30);
		GameRegistry.registerItem(fn_ak74, "AK74");
		fn_ak74sp	= new GVCItemAK74(8,4f,10f,1.0d,50,2.0f,4.0f,"gvcguns:textures/misc/m16.png").setUnlocalizedName("AK74sp").setTextureName("gvcguns:AK74sp")
				.setMaxDamage(30);
		GameRegistry.registerItem(fn_ak74sp, "AK74 PSO-1");
		fn_rpk74	= new GVCItemAK74(8,4f,20f,1.5d,50,2.0f,1.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("RPK74").setTextureName("gvcguns:RPK74")
				.setMaxDamage(45);
		GameRegistry.registerItem(fn_rpk74, "RPK74");
		fn_aks74u	= new GVCItemAK74(7,3.5f,15f,1.5d,40,2.0f,1.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("AKS74U").setTextureName("gvcguns:AKS74U")
				.setMaxDamage(30);
		GameRegistry.registerItem(fn_aks74u, "AKS74U");
		fn_ak74gb30	= new GVCItemAK74GB30().setUnlocalizedName("AK74GB30").setTextureName("gvcguns:AK74GB30");
		GameRegistry.registerItem(fn_ak74gb30, "AK74GB30");
		fn_ak74b	= new GVCItemAK74(8,4f,10f,1.2d,50,5.0f,1.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("AK74b").setTextureName("gvcguns:AK74b")
				.setMaxDamage(30);
		GameRegistry.registerItem(fn_ak74b, "AK74Bayonet");
		
		fn_m10	    = new GVCItemM10(6,3f,20f,1.2d,30,2.0f,1.0f).setUnlocalizedName("M10").setTextureName("gvcguns:M10")
				.setMaxDamage(32);
		GameRegistry.registerItem(fn_m10, "M10");
		fn_uzi	    = new GVCItemUZI().setUnlocalizedName("UZI").setTextureName("gvcguns:UZI")
				.setMaxDamage(32);
		GameRegistry.registerItem(fn_uzi, "UZI");
		fn_cbj	    = new GVCItemMP7().setUnlocalizedName("CBJ").setTextureName("gvcguns:CBJ")
				.setMaxDamage(100);
		GameRegistry.registerItem(fn_cbj, "CBJ");
	    
	    fn_m1911	= new GVCItemM1911().setUnlocalizedName("M1911").setTextureName("gvcguns:m1911");
	    GameRegistry.registerItem(fn_m1911, "M1911");
	    fn_m870	    = new GVCItemM870().setUnlocalizedName("M870").setTextureName("gvcguns:M870");
	    GameRegistry.registerItem(fn_m870, "M870");
	    
	    fn_svd	    = new GVCItemSVD(14,4f,20f,1.5d,50,2.0f,8.0f).setUnlocalizedName("SVD").setTextureName("gvcguns:svd")
	    		.setMaxDamage(10);;
	    GameRegistry.registerItem(fn_svd, "SVD");
	    
	    fn_pkm	    = new GVCItemPKM().setUnlocalizedName("PKM").setTextureName("gvcguns:pkm");
	    GameRegistry.registerItem(fn_pkm, "PKM");
	    fn_pkmsp	= new GVCItemPKMsp().setUnlocalizedName("PKMsp").setTextureName("gvcguns:pkmsp");
	    GameRegistry.registerItem(fn_pkmsp, "PKM PSO-1");
	    
		
	    
	    
	    
	    fn_m16a4	= new GVCItemAK74(8,4f,8f,1.0d,45,2.0f,1.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("M16A4").setTextureName("gvcguns:M16A4")
	    		.setMaxDamage(30);
		GameRegistry.registerItem(fn_m16a4, "M16A4");
		fn_m16a4sp	= new GVCItemAK74(8,4f,8f,1.0d,45,2.0f,4.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("M16A4sp").setTextureName("gvcguns:M16A4sp")
				.setMaxDamage(30);
		GameRegistry.registerItem(fn_m16a4sp, "M16A4 ACOG");
		fn_m249	    = new GVCItemM249().setUnlocalizedName("M249").setTextureName("gvcguns:M249");
		GameRegistry.registerItem(fn_m249, "M249");
		fn_m4a1	    = new GVCItemAK74(7,3.8f,12f,1.3d,40,2.0f,1.0f,"gvcguns:textures/misc/m16.png").setUnlocalizedName("M4A1").setTextureName("gvcguns:M4A1")
				.setMaxDamage(30);
		GameRegistry.registerItem(fn_m4a1, "M4A1");
		fn_m16a4gl	= new GVCItemAK74GB30().setUnlocalizedName("M16A4M320").setTextureName("gvcguns:M16A4M320");
		GameRegistry.registerItem(fn_m16a4gl, "M16A4M320");
		fn_mp7	    = new GVCItemMP7().setUnlocalizedName("MP7").setTextureName("gvcguns:MP7")
				.setMaxDamage(40);
		GameRegistry.registerItem(fn_mp7, "MP7");
		fn_m9	    = new GVCItemM9(6,3f,15f,1.5d,30,2.0f,1.0f).setUnlocalizedName("M9").setTextureName("gvcguns:M9")
				.setMaxDamage(16);
	    GameRegistry.registerItem(fn_m9, "M9");
	    fn_p226	    = new GVCItemM9(6,3.5f,10f,1.2d,30,2.0f,1.0f).setUnlocalizedName("P226").setTextureName("gvcguns:P226")
	    		.setMaxDamage(16);
	    GameRegistry.registerItem(fn_p226, "P226");
	    fn_g17	    = new GVCItemM9(6,3f,15f,1.5d,30,2.0f,1.0f).setUnlocalizedName("G17").setTextureName("gvcguns:G17")
	    		.setMaxDamage(20);
	    GameRegistry.registerItem(fn_g17, "G17");
	    fn_g18	    = new GVCItemM10(6,3f,15f,1.5d,30,2.0f,1.0f).setUnlocalizedName("G18").setTextureName("gvcguns:G18")
	    		.setMaxDamage(20);
	    GameRegistry.registerItem(fn_g18, "G18C");
	    fn_m110	    = new GVCItemSVD(14,4f,20f,1.5d,50,2.0f,8.0f).setUnlocalizedName("M110").setTextureName("gvcguns:M110")
	    		.setMaxDamage(20);;
	    GameRegistry.registerItem(fn_m110, "M110");
	    fn_m240b	= new GVCItemPKM().setUnlocalizedName("M240B").setTextureName("gvcguns:M240B");
	    GameRegistry.registerItem(fn_m240b, "M240B");
	    fn_m240bsp	= new GVCItemPKMsp().setUnlocalizedName("M240Bsp").setTextureName("gvcguns:M240Bsp");
	    GameRegistry.registerItem(fn_m240bsp, "M240B ACOG");
	    fn_r700   	= new GVCItemR700(16,4f,20f,1.5d,50,2.0f,8.0f).setUnlocalizedName("R700").setTextureName("gvcguns:R700")
	    		.setMaxDamage(10);;
	    GameRegistry.registerItem(fn_r700, "R700");
	    fn_m82a3	= new GVCItemSVD(30,4f,30f,3.0d,60,2.0f,8.0f).setUnlocalizedName("M82A3").setTextureName("gvcguns:M82A3")
	    		.setMaxDamage(10);;
	    GameRegistry.registerItem(fn_m82a3, "M82A3");
	    
	    fn_g36	    = new GVCItemAK74(8,4f,8f,1.2d,50,2.0f,4.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("G36").setTextureName("gvcguns:G36")
	    		.setMaxDamage(30);;
		GameRegistry.registerItem(fn_g36, "G36");
		fn_g36c	    = new GVCItemAK74(7,3.5f,13f,1.5d,40,2.0f,1.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("G36C").setTextureName("gvcguns:G36C")
				.setMaxDamage(30);;
		GameRegistry.registerItem(fn_g36c, "G36C");
		fn_mg36	    = new GVCItemAK74(8,4f,8f,1.2d,70,2.0f,4.0f,"gvcguns:textures/misc/null.png").setUnlocalizedName("MG36").setTextureName("gvcguns:MG36")
				.setMaxDamage(100);
		GameRegistry.registerItem(fn_mg36, "MG36");
		fn_g36m320	= new GVCItemAK74GB30().setUnlocalizedName("G36M320").setTextureName("gvcguns:G36M320");
		GameRegistry.registerItem(fn_g36m320, "G36M320");
		
		
		fn_rpg7	    = new GVCItemRPG7().setUnlocalizedName("RPG7").setTextureName("gvcguns:RPG7");
	    GameRegistry.registerItem(fn_rpg7, "RPG7");
	    fn_fim92	    = new GVCItemFIM92().setUnlocalizedName("FIM92").setTextureName("gvcguns:FIM92");
	    GameRegistry.registerItem(fn_fim92, "FIM92");
	    fn_mbtlaw	    = new GVCItemMBTLAW().setUnlocalizedName("MBTLAW").setTextureName("gvcguns:MBTLAW");
	    GameRegistry.registerItem(fn_mbtlaw, "MBTLAW");
		
		
		fn_m134	= new GVCItemM134().setUnlocalizedName("M134").setTextureName("gvcguns:M134");
		GameRegistry.registerItem(fn_m134, "M134");
		
		fn_camp	= new GVCblockCamp().setBlockName("Camp").setBlockTextureName("gvcguns:camp");
		GameRegistry.registerBlock(fn_camp, "Camp");
		
		fn_gunbox	= new GVCBlockGunBox().setBlockName("GunBox").setBlockTextureName("gvcguns:gunbox");
		GameRegistry.registerBlock(fn_gunbox, "GunBox");
		
		fn_ied	= new GVCBlockGunIED().setBlockName("IED")
				.setBlockTextureName("gvcguns:IED")
				;
		GameRegistry.registerBlock(fn_ied, "IED");
		
		fn_cont	= new GVCBlockCont().setBlockName("Contna").setBlockTextureName("gvcguns:cont");
		GameRegistry.registerBlock(fn_cont, "Contna");
		
		
		
		
		
		GVCPacketHandler.init();

		
	}

	@EventHandler
	public void init(FMLInitializationEvent pEvent) {
		int D = Short.MAX_VALUE;
		
		GameRegistry.addRecipe(new ItemStack(Items.gunpowder, 1),
				"dd",
				"dd", 
				'd', Blocks.dirt
			);
		
		
		GameRegistry.addRecipe(new ItemStack(fn_magazine, 2),
				"ii",
				"gg", 
				'i', Items.iron_ingot,
				'g', Items.gunpowder
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_magazine, 2),
				"ii",
				"gg", 
				'i', Items.emerald,
				'g', Items.gunpowder
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_magazine, 2),
				"ii",
				"ii", 
				'i', Items.emerald
				
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_magazinehg, 1),
				"c",
				"g", 
				'c', Blocks.cobblestone,
				'g', Items.gunpowder
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_magazinehg, 1),
				"aa",
				"aa", 
				
				'a', Items.arrow
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_magazinemg, 1),
				"iii",
				"ggg", 
				'i', Items.iron_ingot,
				'g', Items.gunpowder
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_rpg, 4),
				"t  ",
				" i ",
				"  g", 
				't', Blocks.tnt,
				'i', Items.iron_ingot,
				'g', Items.gunpowder
			);
		GameRegistry.addRecipe(new ItemStack(fn_missile, 1),
				"b  ",
				" r ",
				"  r", 
				'b', Items.repeater,
				'r', fn_rpg
			);
		GameRegistry.addRecipe(new ItemStack(fn_missile, 2),
				"b  ",
				" r ",
				"  r", 
				'b', Items.ender_pearl,
				'r', fn_rpg
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_shell, 16),
				"c",
				"p",
				"g",
				'c', Blocks.cobblestone,
				'p', Items.paper,
				'g', Items.gunpowder
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_torch, 1),
				"d ",
				"ii",
				"ii",
				'i', Items.iron_ingot,
				'd', Blocks.diamond_block
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_reddot, 1),
				"igr", 
				'i', Items.iron_ingot,
				'r', Items.redstone,
				'g', Blocks.glass
			);
		
		
		GameRegistry.addShapelessRecipe(new ItemStack(fn_torch, 1), new ItemStack(fn_torch, 1,D), Blocks.coal_block);
		
		GameRegistry.addShapelessRecipe(new ItemStack(fn_cm, 1), 
				Items.apple, Items.sugar, Items.wheat, this.fn_box);
		
		GameRegistry.addRecipe(new ItemStack(fn_health, 2),
				"gw",
				"wg", 
				'g', Blocks.glass,
				'w', new ItemStack(Blocks.wool, 1, D)
			);
		
		
		GameRegistry.addRecipe(new ItemStack(fn_prahelmet, 1),
				"ppp",
				"pip", 
				'p', this.fn_pra,
				'i', Items.iron_helmet
			);
		GameRegistry.addRecipe(new ItemStack(fn_prachestp, 1),
				"p p",
				"pip",
				"ppp", 
				'p', this.fn_pra,
				'i', Items.iron_chestplate
			);
		GameRegistry.addRecipe(new ItemStack(fn_praleggings, 1),
				"ppp",
				"pip",
				"p p", 
				'p', this.fn_pra,
				'i', Items.iron_leggings
			);
		GameRegistry.addRecipe(new ItemStack(fn_praboots, 1),
				"pip",
				"p p",
				'p', this.fn_pra,
				'i', Items.iron_boots
			);
		
		
		
		GameRegistry.addRecipe(new ItemStack(fn_grenade, 2),
				"gg",
				"gg", 
				'g', Items.gunpowder
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_scope, 1),
				" r ",
				"gig", 
				'i', Items.iron_ingot,
				'r', Items.redstone,
				'g', Blocks.glass
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_bayonet, 1),
				"iw", 
				'i', Items.iron_ingot,
				'w',  new ItemStack(Blocks.planks, 1, D)
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_pra, 2),
				"gig",
				
				'i', Items.iron_ingot,
				'g', Blocks.glass
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m320, 1),
				"iir", 
				"p p",
				'i', Items.iron_ingot,
				'r', Items.redstone,
				'p',  fn_pra
			);
		
		
		
		GameRegistry.addRecipe(new ItemStack(fn_ak74, 1),
				"iiw",
				" ii", 
				'i', Items.iron_ingot,
				'w',  new ItemStack(Blocks.planks, 1, D)
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_ak74sp, 1),
				"s",
				"a", 
				'a', fn_ak74,
				's', fn_scope
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_rpk74, 1),
				"a",
				"m", 
				'a', fn_ak74,
				'm', fn_magazine
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_ak74gb30, 1),
				"a",
				"m", 
				'a', fn_ak74,
				'm', fn_m320
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_ak74b, 1),
				"ba",
				 
				'a', fn_ak74,
				'b', fn_bayonet
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_aks74u, 1),
				"a",
				'a', fn_ak74
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m10, 1),
				"iis",
				" i ", 
				'i', Items.iron_ingot,
				's', Items.stick
			);
		GameRegistry.addRecipe(new ItemStack(fn_uzi, 1),
				"iii",
				" is", 
				'i', Items.iron_ingot,
				's', Items.stick
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_cbj, 1),
				"iii",
				" bb", 
				'i', Items.iron_ingot,
				'b', Blocks.iron_block
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m1911, 1),
				"iii",
				"  w", 
				'i', Items.iron_ingot,
				'w',  new ItemStack(Blocks.planks, 1, D)
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m870, 1),
				"iii",
				" wi", 
				'i', Items.iron_ingot,
				'w',  new ItemStack(Blocks.planks, 1, D)
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_svd, 1),
				" s ",
				"iiw",
				" ii", 
				's', fn_scope,
				'i', Items.iron_ingot,
				'w',  new ItemStack(Blocks.planks, 1, D)
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_pkm, 1),
				"iiw",
				" mi", 
				'i', Items.iron_ingot,
				'w',  new ItemStack(Blocks.planks, 1, D),
				'm', fn_magazinemg
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_pkmsp, 1),
				"s",
				"p", 
				's', fn_scope,
				'p', fn_pkm
			);
		
		
		
		
		
		
		
		
		
		
		GameRegistry.addRecipe(new ItemStack(fn_m16a4, 1),
				"iip",
				" ii", 
				'i', Items.iron_ingot,
				'p', fn_pra
			);
		
		
		
		GameRegistry.addRecipe(new ItemStack(fn_m16a4sp, 1),
				"s",
				"p", 
				's', fn_scope,
				'p', fn_m16a4
			);
		
		/*GameRegistry.addRecipe(new ItemStack(fn_m249, 1),
				" ii",
				"ipp",
				" mi", 
				'i', Items.iron_ingot,
				'p',  fn_pra,
				'm', fn_magazinemg
			);*/
		
		GameRegistry.addRecipe(new ItemStack(fn_m4a1, 1),
				"a",
				'a', fn_m16a4
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m16a4gl, 1),
				"s",
				"p", 
				's', fn_m16a4,
				'p', fn_m320
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_mp7, 1),
				"ppp",
				"p p", 
				'p', fn_pra
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m9, 1),
				"ii",
				" i", 
				'i', Items.iron_ingot
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_p226, 1),
				"iip",
				"  i", 
				'i', Items.iron_ingot,
				'p', fn_pra
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_g17, 1),
				"ppp",
				"  p", 
				'p', fn_pra
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_g18, 1),
				"p",
				
				'p', fn_g17
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m110, 1),
				" s ",
				"iip",
				" ii", 
				's', fn_scope,
				'i', Items.iron_ingot,
				'p', fn_pra
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m240b, 1),
				"i i",
				"ibb",
				" mi", 
				'i', Items.iron_ingot,
				'b', Blocks.iron_block,
				'm', fn_magazinemg
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m240bsp, 1),
				"s",
				"p", 
				's', fn_scope,
				'p', fn_m240b
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_r700, 1),
				" s ",
				"iiw",
				" ww", 
				's', fn_scope,
				'i', Items.iron_ingot,
				'w',  new ItemStack(Blocks.planks, 1, D)
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_m82a3, 1),
				" s ",
				"iii",
				" mi", 
				's', fn_scope,
				'i', Blocks.iron_block,
				'm', fn_magazinemg
			);
		
		
		GameRegistry.addRecipe(new ItemStack(fn_g36, 1),
				" pp",
				"ppp",
				" pp", 
				
				'p', fn_pra
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_g36c, 1),
				"g",
				
				'g', fn_g36
			);
		GameRegistry.addRecipe(new ItemStack(fn_mg36, 1),
				"g",
				"m",
				'g', fn_g36,
				'm', fn_magazinemg
			);
		
		GameRegistry.addRecipe(new ItemStack(fn_g36m320, 1),
				"g",
				"m",
				'g', fn_g36,
				'm', fn_m320
			);
		
		
		
		GameRegistry.addRecipe(new ItemStack(fn_rpg7, 1),
				"riw",
				"ii ", 
				'i', Items.iron_ingot,
				'r', fn_rpg,
				'w',  new ItemStack(Blocks.planks, 1, D)
			);
		GameRegistry.addRecipe(new ItemStack(fn_mbtlaw, 1),
				"ii ",
				"imi",
				" ii",
				'i', Items.iron_ingot,
				'm', fn_missile
			);
		
		
		
		
		
		GameRegistry.addRecipe(new ItemStack(fn_m134, 1),
				"  i",
				"nnd",
				"nnd", 
				'd', Blocks.diamond_block,
				'i', Blocks.iron_block,
				'n', Items.nether_star
			);
		
		
		reddot = new GVCEnchantRedDot(en_reddotID, 1).setName("REDDot");
		lasersight = new GVCEnchantRedDot(en_laserID, 1).setName("LaserSight");
		scopesight = new GVCEnchantRedDot(en_scopeID, 1).setName("Scope");
		
		
		//m16a4red.addEnchantment(Enchantment.flame, 1);//エンチャントを付ける場合
		ItemStack m16a4laser = new ItemStack(fn_m16a4);
		m16a4laser.addEnchantment(this.lasersight, 1);
		ItemStack m16a4splaser = new ItemStack(fn_m16a4sp);
		m16a4splaser.addEnchantment(this.lasersight, 1);
		ItemStack m4a1laser = new ItemStack(fn_m4a1);
		m4a1laser.addEnchantment(this.lasersight, 1);
		ItemStack ak74laser = new ItemStack(fn_ak74);
		ak74laser.addEnchantment(this.lasersight, 1);
		ItemStack aks74ulaser = new ItemStack(fn_aks74u);
		aks74ulaser.addEnchantment(this.lasersight, 1);
		ItemStack ak74splaser = new ItemStack(fn_ak74sp);
		ak74splaser.addEnchantment(this.lasersight, 1);
		ItemStack rpk74laser = new ItemStack(fn_rpk74);
		rpk74laser.addEnchantment(this.lasersight, 1);
		ItemStack g36laser = new ItemStack(fn_g36);
		g36laser.addEnchantment(this.lasersight, 1);
		ItemStack g36claser = new ItemStack(fn_g36c);
		g36claser.addEnchantment(this.lasersight, 1);
		ItemStack mg36laser = new ItemStack(fn_mg36);
		mg36laser.addEnchantment(this.lasersight, 1);
		ItemStack m134laser = new ItemStack(fn_m134);
		m134laser.addEnchantment(this.lasersight, 1);
        NBTTagCompound nbt = new NBTTagCompound();
        GameRegistry.addShapelessRecipe(m16a4laser, fn_m16a4, Items.redstone);//レシピ登録
        GameRegistry.addShapelessRecipe(m16a4splaser, fn_m16a4sp, Items.redstone);
        GameRegistry.addShapelessRecipe(m4a1laser, fn_m4a1, Items.redstone);
        GameRegistry.addShapelessRecipe(ak74laser, fn_ak74, Items.redstone);
        GameRegistry.addShapelessRecipe(ak74splaser, fn_ak74sp, Items.redstone);
        GameRegistry.addShapelessRecipe(aks74ulaser, fn_aks74u, Items.redstone);
        GameRegistry.addShapelessRecipe(rpk74laser, fn_rpk74, Items.redstone);
        GameRegistry.addShapelessRecipe(g36laser, fn_g36, Items.redstone);
        GameRegistry.addShapelessRecipe(g36claser, fn_g36c, Items.redstone);
        GameRegistry.addShapelessRecipe(mg36laser, fn_mg36, Items.redstone);
        GameRegistry.addShapelessRecipe(m134laser, fn_m134, Items.redstone);
        
		
        
		
		
		EntityRegistry.registerModEntity(GVCEntityBullet.class, "Bullet", bullet, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityBulletG.class, "BulletG", bulletg, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityGrenade.class, "Grenade", entitygrenade, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityGurenadeBullet.class, "GurenadeBullet", grenadebullet, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityRPG.class, "RPG", rpg, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityGrenade_HundF.class, "HundFrame", 180, this, 128, 5, true);
		
		EntityRegistry.registerModEntity(GVCEntityLaser.class, "Laser", laser, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntitygustorch.class, "gustorch", gustorch, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityBulletDart.class, "Bullet", bullet+11, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityBulletFrag.class, "Bullet", bullet+12, this, 128, 5, true);
		
		EntityRegistry.registerModEntity(GVCEntityMissile.class, "Missile", 190, this, 128, 5, true);
		EntityRegistry.registerModEntity(GVCEntityC4.class, "C4", 191, this, 128, 5, true);
		//EntityRegistry.registerModEntity(GVCEntityBullettest.class, "Bullet", bullet+10, this, 128, 5, true);
		//EntityRegistry.registerModEntity(GVCEntityparas.class, "paras", paras, this, 128, 5, true);
		
		//EntityRegistry.registerModEntity(GVCEntityBulletFrag.class, "Bullet", bullet+12, this, 128, 5, true);
		
		
		/*if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{ 
			ClientRegistry.registerKeyBinding(Speedreload);
		}*/
		
		FMLCommonHandler.instance().bus().register(this);
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{ 
		MinecraftForge.EVENT_BUS.register(new GVCZoomEvent());
		}
		//MinecraftForge.EVENT_BUS.register(new GVCZonUpdate());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GVCGuiHandler());
		proxy.reisterRenderers();
		proxy.registerTileEntity();
		proxy.InitRendering();
		proxy.getCilentMinecraft();
		
		GameRegistry.registerFuelHandler(new IFuelHandler(){
		    @Override
		    public int getBurnTime(ItemStack fuel){
		        if(fuel.getItem().equals(GVCGunsPlus.fn_box)){
		                return 3200;
		        }
		        return 0;
		    }
		});
		
		
		
	}
	
	
	public static int adstype;
	
	@SubscribeEvent
    public void KeyHandlingEvent(KeyInputEvent event) 
	{
		//if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		//{
		/*Minecraft minecraft = FMLClientHandler.instance().getClient();
		World world = minecraft.theWorld;
		
		EntityPlayer entityplayer = minecraft.thePlayer;*/
		//EntityPlayer entityplayer = ClientProxyGVC.player;
		//Entity entity = FMLServerHandler.instance().getServer().getEntityWorld().;
		Minecraft minecraft = Minecraft.getMinecraft();
		ScaledResolution var21 = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
		int x1 = var21.getScaledWidth();//画面の横幅をxに代入
		int y1 = var21.getScaledHeight();//画面の縦幅をyに代入
		Entity entity = Minecraft.getMinecraft().renderViewEntity;
		EntityPlayer entityplayer = (EntityPlayer)entity;
		ItemStack itemstack = ((EntityPlayer)(entityplayer)).getCurrentEquippedItem();
		
		
		//if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{
          if (ClientProxyGVC.Speedreload.isPressed()) {
        	if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBase){
        	//	int li = itemstack.getMaxDamage() - itemstack.getItemDamage();
		      //  itemstack.damageItem(li, entityplayer);
        		GVCPacketHandler.INSTANCE.sendToServer(new GVCMessageKeyPressed(1));//1をサーバーに送る。
		    }
        	if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseSG){
        		//int li = itemstack.getMaxDamage() - itemstack.getItemDamage();
		        //itemstack.damageItem(li, entityplayer);
		        //world.playSoundAtEntity(entityplayer, "gvcguns:gvcguns.reload", 2.0F, 1.0F);
        		GVCPacketHandler.INSTANCE.sendToServer(new GVCMessageKeyPressed(1));//1をサーバーに送る。
		    }
        	if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseGL){
        		//int li = itemstack.getMaxDamage() - itemstack.getItemDamage();
		        //itemstack.damageItem(li, entityplayer);
		        //world.playSoundAtEntity(entityplayer, "gvcguns:gvcguns.reload", 2.0F, 1.0F);
        		GVCPacketHandler.INSTANCE.sendToServer(new GVCMessageKeyPressed(1));//1をサーバーに送る。
		    }
          }
          if (ClientProxyGVC.Firetype.isPressed()) {
        	if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseSG){
        		if(GVCItemGunBaseSG.firetype != 3){
        			GVCItemGunBaseSG.firetype = GVCItemGunBaseSG.firetype +1;
        		}else{
        			GVCItemGunBaseSG.firetype = 0;
        		}
		    }
        	if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseGL){
        		if(GVCItemGunBaseGL.firetype != 3){
        			GVCItemGunBaseGL.firetype = GVCItemGunBaseGL.firetype +1;
        		}else{
        			GVCItemGunBaseGL.firetype = 0;
        		}
		    }
          }
		}
		if(this.cfg_ADSKeytype){
			if (ClientProxyGVC.ADStype.isPressed()) {
				if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBase){
					if(!(this.adstype == 1)){
						this.adstype = this.adstype +1;
					}else{
						this.adstype = 0;
					}
				}else if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseSG){
					if(!(this.adstype == 1)){
						this.adstype = this.adstype +1;
					}else{
						this.adstype = 0;
					}
				}else if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBaseGL){
						if(!(this.adstype == 1)){
							this.adstype = this.adstype +1;
						}else{
							this.adstype = 0;
						}
				}else{
					this.adstype = 0;
				}
		}
		}
        /*if (ClientProxyGVC.GrenadeKey.isPressed()) {
        	if(itemstack != null && itemstack.getItem() instanceof GVCItemGunBase){
        		
        		/*if(GVCItemGunBase.grenadekey = false){
        		GVCItemGunBase.grenadekey = true;
        		}else{
        		GVCItemGunBase.grenadekey = false;
        		}
		    }
        }*/
		//}
    }
	
}
