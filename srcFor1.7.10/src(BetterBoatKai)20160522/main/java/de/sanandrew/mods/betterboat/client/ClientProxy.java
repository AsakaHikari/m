/*    */ package de.sanandrew.mods.betterboat.client;
/*    */ 
/*    */ import cpw.mods.fml.client.registry.RenderingRegistry;
/*    */ import de.sanandrew.mods.betterboat.CommonProxy;
/*    */ import de.sanandrew.mods.betterboat.entity.EntityBetterBoat;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class ClientProxy extends CommonProxy
/*    */ {
/*    */   public void setBoatPosAndRot(int entityId, double posX, double posY, double posZ, float yaw, float pitch)
/*    */   {
/* 22 */     Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityId);
/*    */ 
/* 24 */     if ((entity instanceof EntityBetterBoat))
/* 25 */       entity.setPositionAndRotation2(posX, posY, posZ, yaw, pitch, 10);
/*    */   }
/*    */ 
/*    */   public void registerRenderers()
/*    */   {
/* 31 */     RenderingRegistry.registerEntityRenderingHandler(EntityBetterBoat.class, new RenderBetterBoat());
/*    */   }
/*    */ }

/* Location:           C:\Users\na0aya2e\雑多なプログラム\jd-gui-0.3.5.windows\minecraftModsToAnalyze\BetterBoat-1.7.10-1.1.0.deobf.jar
 * Qualified Name:     de.sanandrew.mods.betterboat.client.ClientProxy
 * JD-Core Version:    0.6.2
 */