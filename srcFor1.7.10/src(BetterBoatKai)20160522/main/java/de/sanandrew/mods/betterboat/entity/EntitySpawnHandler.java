/*    */ package de.sanandrew.mods.betterboat.entity;
/*    */ 
/*    */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityBoat;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.event.entity.EntityJoinWorldEvent;
/*    */ 
/*    */ public class EntitySpawnHandler
/*    */ {
/*    */   @SubscribeEvent
/*    */   public void onEntitySpawn(EntityJoinWorldEvent event)
/*    */   {
/* 19 */     if (event.entity.getClass().equals(EntityBoat.class)) {
/* 20 */       event.setCanceled(true);
/*    */ 
/* 22 */       if (!event.world.isRemote) {
/* 23 */         EntityBetterBoat betterBoat = new EntityBetterBoat(event.world, event.entity.prevPosX, event.entity.prevPosY, event.entity.prevPosZ);
/* 24 */         betterBoat.rotationYaw = event.entity.rotationYaw;
/*    */ 
/* 26 */         event.world.spawnEntityInWorld(betterBoat);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\na0aya2e\雑多なプログラム\jd-gui-0.3.5.windows\minecraftModsToAnalyze\BetterBoat-1.7.10-1.1.0.deobf.jar
 * Qualified Name:     de.sanandrew.mods.betterboat.entity.EntitySpawnHandler
 * JD-Core Version:    0.6.2
 */