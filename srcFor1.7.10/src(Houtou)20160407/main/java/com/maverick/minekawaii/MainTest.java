/*     */ package com.maverick.minekawaii;
/*     */ 
/*     */ import cpw.mods.fml.common.Mod;
/*     */ import cpw.mods.fml.common.Mod.EventHandler;
/*     */ import cpw.mods.fml.common.event.FMLPreInitializationEvent;
/*     */ import cpw.mods.fml.common.registry.GameData;
/*     */ import cpw.mods.fml.common.registry.GameRegistry;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.RegistryNamespaced;
/*     */ 
/*     */ @Mod(modid="pockymod", name="Minekawaii", version="1.0")
/*     */ public class MainTest
/*     */ {
/*     */   public static final String MODID = "pockymod";
/*     */   public static final String VERSION = "1.0";
/*  30 */   public static final RegistryNamespaced itemRegistry = GameData.getItemRegistry();
/*     */ 
/*  32 */   public static ItemFood pockys = new ItemFood(4, 0.3F, false);
/*  33 */   public static ItemFood pockyc = new ItemFood(4, 0.3F, false);
/*  34 */   public static ItemFood pockyt = new ItemFood(4, 0.3F, false);
/*  35 */   public static ItemFood pockyv = new ItemFood(4, 0.3F, false);
/*  36 */   public static ItemFood pockya = new ItemFood(4, 0.3F, false);
/*     */ 
/*  38 */   public static ItemFood sushi = new ItemFood(2, 0.2F, false);
/*  39 */   public static ItemFood cookednoodles = new ItemFood(2, 0.2F, false);
/*  40 */   public static ItemFood ramune = new ItemFood(1, 0.2F, false);
/*     */ 
/*  42 */   public static ItemFood ricebowl = new ItemFood(2, 0.3F, false);
/*  43 */   public static Item drynoodles = new Item();
/*  44 */   public static Item hotwater = new Item();
/*     */   public static Block tutorialBlock;
/*     */   public static Block tutorialBlock2;
/*  49 */   public static ItemStack cocoa = new ItemStack(Items.dye, 1, 3);
/*  50 */   public static CreativeTabs tabMyMod = new CreativeTabsKawaii("Minekawaii");
/*     */ 
/*     */   @Mod.EventHandler
/*     */   public void preinit(FMLPreInitializationEvent event)
/*     */   {
/*  69 */     GameRegistry.addSmelting(Items.wheat, new ItemStack(ricebowl, 1), 0.0F);
/*  70 */     GameRegistry.addSmelting(new ItemStack(Items.potionitem, 1, 0), new ItemStack(hotwater, 1), 0.0F);
/*  71 */     GameRegistry.addShapelessRecipe(new ItemStack(cookednoodles, 1), new Object[] { new ItemStack(drynoodles, 1), new ItemStack(hotwater, 1) });
/*     */ 
/*  75 */     itemRegistry.addObject(800, "pockys", pockys.setUnlocalizedName("pockys").setCreativeTab(tabMyMod));
/*  76 */     pockys.setTextureName("pockymod:pockys");
/*     */ 
/*  78 */     GameRegistry.addRecipe(new ItemStack(pockys, 4), new Object[] { "#S#", "#S#", "#C#", 
/*  82 */       Character.valueOf('S'), 
/*  82 */       Items.sugar, Character.valueOf('C'), Items.cookie });
/*     */ 
/*  88 */     itemRegistry.addObject(801, "pockyc", pockyc.setUnlocalizedName("pockyc").setCreativeTab(tabMyMod));
/*  89 */     pockyc.setTextureName("pockymod:pockyc");
/*     */ 
/*  91 */     GameRegistry.addRecipe(new ItemStack(pockyc, 4), new Object[] { "#S#", "#S#", "#C#", 
/*  95 */       Character.valueOf('S'), 
/*  95 */       cocoa, Character.valueOf('C'), Items.cookie });
/*     */ 
/* 104 */     itemRegistry.addObject(802, "pockyv", pockyv.setUnlocalizedName("pockyv").setCreativeTab(tabMyMod));
/* 105 */     pockyv.setTextureName("pockymod:pockyv");
/* 106 */     GameRegistry.addRecipe(new ItemStack(pockyv, 4), new Object[] { "#S#", "#S#", "#C#", 
/* 110 */       Character.valueOf('S'), 
/* 110 */       Items.milk_bucket, Character.valueOf('C'), Items.cookie });
/*     */ 
/* 114 */     itemRegistry.addObject(803, "pockyt", pockyt.setUnlocalizedName("pockyt").setCreativeTab(tabMyMod));
/* 115 */     pockyt.setTextureName("pockymod:pockyt");
/* 116 */     GameRegistry.addRecipe(new ItemStack(pockyt, 4), new Object[] { "#S#", "#S#", "#C#", 
/* 120 */       Character.valueOf('S'), 
/* 120 */       Blocks.leaves, Character.valueOf('C'), Items.cookie });
/*     */ 
/* 124 */     itemRegistry.addObject(804, "pockya", pockya.setUnlocalizedName("pockya").setCreativeTab(tabMyMod));
/* 125 */     pockya.setTextureName("pockymod:pockya");
/* 126 */     GameRegistry.addRecipe(new ItemStack(pockya, 4), new Object[] { "#S#", "#S#", "#C#", 
/* 130 */       Character.valueOf('S'), 
/* 130 */       Items.pumpkin_seeds, Character.valueOf('C'), Items.cookie });
/*     */ 
/* 135 */     itemRegistry.addObject(805, "sushi", sushi.setUnlocalizedName("sushi").setCreativeTab(tabMyMod));
/* 136 */     sushi.setTextureName("pockymod:sushiroll");
/*     */ 
/* 140 */     itemRegistry.addObject(806, "ricebowl", ricebowl.setUnlocalizedName("ricebowl").setCreativeTab(tabMyMod));
/* 141 */     ricebowl.setTextureName("pockymod:ricebowl");
/*     */ 
/* 143 */     itemRegistry.addObject(807, "drynoodles", drynoodles.setUnlocalizedName("drynoodles").setCreativeTab(tabMyMod));
/* 144 */     drynoodles.setTextureName("pockymod:drynoodles");
/* 145 */     GameRegistry.addRecipe(new ItemStack(drynoodles, 1), new Object[] { "###", "NNN", "###", 
/* 149 */       Character.valueOf('N'), 
/* 149 */       Items.wheat });
/*     */ 
/* 157 */     itemRegistry.addObject(808, "hotwater", hotwater.setUnlocalizedName("hotwater").setCreativeTab(tabMyMod));
/* 158 */     hotwater.setTextureName("pockymod:water_hot");
/*     */ 
/* 160 */     itemRegistry.addObject(809, "cookednoodles", cookednoodles.setUnlocalizedName("cookednoodles").setCreativeTab(tabMyMod));
/* 161 */     cookednoodles.setTextureName("pockymod:cookednoodles");
/*     */ 
/* 163 */     itemRegistry.addObject(810, "ramune", ramune.setUnlocalizedName("ramune").setCreativeTab(tabMyMod));
/* 164 */     ramune.setTextureName("pockymod:ramune_plain");
/* 165 */     GameRegistry.addRecipe(new ItemStack(ramune, 1), new Object[] { "#S#", "#S#", "#W#", 
/* 168 */       Character.valueOf('S'), 
/* 168 */       Items.sugar, Character.valueOf('W'), new ItemStack(Items.potionitem, 1, 0) });
/*     */ 
/* 171 */     GameRegistry.addRecipe(new ItemStack(Items.apple, 4), new Object[] { "AA", "AA", 
/* 174 */       Character.valueOf('A'), 
/* 174 */       Blocks.cobblestone });
/*     */ 
/* 176 */     GameRegistry.addRecipe(new ItemStack(Items.dye, 2, 15), new Object[] { "AB ", "AAC", "A  ", 
/* 180 */       Character.valueOf('A'), 
/* 180 */       Items.cookie, Character.valueOf('B'), Blocks.dirt, Character.valueOf('C'), new ItemStack(Items.dye, 1, 1) });
/*     */ 
/* 183 */     GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 1), new Object[] { new ItemStack(Items.dye, 1, 1), Items.redstone });
/*     */ 
/* 188 */     GameRegistry.addSmelting(new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 11), 0.1F);
/*     */   }
/*     */ }

/* Location:           C:\Users\na0aya2e\minekawaii-1.7.2-1.0.deobf.jar
 * Qualified Name:     com.maverick.minekawaii.MainTest
 * JD-Core Version:    0.6.2
 */