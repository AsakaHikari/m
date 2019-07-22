/*    */ package com.maverick.minekawaii;
/*    */ 
/*    */ import cpw.mods.fml.relauncher.Side;
/*    */ import cpw.mods.fml.relauncher.SideOnly;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.item.Item;
/*    */ 
/*    */ public class CreativeTabsKawaii extends CreativeTabs
/*    */ {
/*    */   public CreativeTabsKawaii(String tabLabel)
/*    */   {
/* 13 */     super(tabLabel);
/*    */   }
/*    */ 
/*    */   @SideOnly(Side.CLIENT)
/*    */   public Item getTabIconItem()
/*    */   {
/* 20 */     return MainTest.sushi;
/*    */   }
/*    */ }

/* Location:           C:\Users\na0aya2e\minekawaii-1.7.2-1.0.deobf.jar
 * Qualified Name:     com.maverick.minekawaii.CreativeTabsKawaii
 * JD-Core Version:    0.6.2
 */