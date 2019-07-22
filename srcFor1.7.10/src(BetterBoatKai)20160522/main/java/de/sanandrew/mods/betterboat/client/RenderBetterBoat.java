/*    */ package de.sanandrew.mods.betterboat.client;
/*    */ 
/*    */ import de.sanandrew.mods.betterboat.entity.EntityBetterBoat;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelBoat;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class RenderBetterBoat extends Render
/*    */ {
/* 23 */   private static final ResourceLocation boatTexture = new ResourceLocation("textures/entity/boat.png");
/*    */   protected ModelBase modelBoat;
/*    */ 
/*    */   public RenderBetterBoat()
/*    */   {
/* 27 */     this.shadowSize = 0.5F;
/* 28 */     this.modelBoat = new ModelBoat();
/*    */   }
/*    */ 
/*    */   public void doRender(EntityBetterBoat boat, double x, double y, double z, float yaw, float partTicks) {
/* 32 */     GL11.glPushMatrix();
/* 33 */     GL11.glTranslatef((float)x, (float)y, (float)z);
/* 34 */     GL11.glRotatef(180.0F - yaw, 0.0F, 1.0F, 0.0F);
/* 35 */     float timeSinceHit = boat.getTimeSinceHit() - partTicks;
/* 36 */     float damageTaken = boat.getDamageTaken() - partTicks;
/*    */ 
/* 38 */     if (damageTaken < 0.0F) {
/* 39 */       damageTaken = 0.0F;
/*    */     }
/*    */ 
/* 42 */     if (timeSinceHit > 0.0F) {
/* 43 */       GL11.glRotatef(MathHelper.sin(timeSinceHit) * timeSinceHit * damageTaken / 10.0F * boat.getForwardDirection(), 1.0F, 0.0F, 0.0F);
/*    */     }
/*    */ 
/* 46 */     float scale = 0.75F;
/* 47 */     GL11.glScalef(scale, scale, scale);
/* 48 */     GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
/* 49 */     bindEntityTexture(boat);
/* 50 */     GL11.glScalef(-1.0F, -1.0F, 1.0F);
/* 51 */     this.modelBoat.render(boat, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/* 52 */     GL11.glPopMatrix();
/*    */   }
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(Entity entity)
/*    */   {
/* 57 */     return boatTexture;
/*    */   }
/*    */ 
/*    */   public void doRender(Entity entity, double x, double y, double z, float yaw, float partTicks)
/*    */   {
/* 62 */     doRender((EntityBetterBoat)entity, x, y, z, yaw, partTicks);
/*    */   }
/*    */ }

/* Location:           C:\Users\na0aya2e\雑多なプログラム\jd-gui-0.3.5.windows\minecraftModsToAnalyze\BetterBoat-1.7.10-1.1.0.deobf.jar
 * Qualified Name:     de.sanandrew.mods.betterboat.client.RenderBetterBoat
 * JD-Core Version:    0.6.2
 */