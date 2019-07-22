/*    */ package de.sanandrew.mods.betterboat.network;
/*    */ 
/*    */ import cpw.mods.fml.common.network.simpleimpl.IMessage;
import de.sanandrew.mods.betterboat.BetterBoat;
/*    */ import de.sanandrew.mods.betterboat.CommonProxy;
/*    */ import de.sanandrew.mods.betterboat.entity.EntityBetterBoat;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class PacketSendBoatPos implements IMessage
/*    */ {
/*    */   public int boatId;
/*    */   public double posX;
/*    */   public double posY;
/*    */   public double posZ;
/*    */   public float rotationYaw;
/*    */   public float rotationPitch;
/*    */ 
/*    */   public PacketSendBoatPos()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PacketSendBoatPos(EntityBetterBoat boat)
/*    */   {
/* 30 */     this.boatId = boat.getEntityId();
/* 31 */     this.posX = boat.posX;
/* 32 */     this.posY = boat.posY;
/* 33 */     this.posZ = boat.posZ;
/* 34 */     this.rotationYaw = boat.rotationYaw;
/* 35 */     this.rotationPitch = boat.rotationPitch;
/*    */   }
/*    */ 
/*    */   public void fromBytes(ByteBuf buf)
/*    */   {
/* 50 */     this.boatId = buf.readInt();
/* 51 */     this.posX = buf.readDouble();
/* 52 */     this.posY = buf.readDouble();
/* 53 */     this.posZ = buf.readDouble();
/* 54 */     this.rotationYaw = buf.readFloat();
/* 55 */     this.rotationPitch = buf.readFloat();
/*    */   }
/*    */ 
/*    */   public void toBytes(ByteBuf buf)
/*    */   {
/* 60 */     buf.writeInt(this.boatId);
/* 61 */     buf.writeDouble(this.posX);
/* 62 */     buf.writeDouble(this.posY);
/* 63 */     buf.writeDouble(this.posZ);
/* 64 */     buf.writeFloat(this.rotationYaw);
/* 65 */     buf.writeFloat(this.rotationPitch);
/*    */   }
/*    */ }

/* Location:           C:\Users\na0aya2e\雑多なプログラム\jd-gui-0.3.5.windows\minecraftModsToAnalyze\BetterBoat-1.7.10-1.1.0.deobf.jar
 * Qualified Name:     de.sanandrew.mods.betterboat.network.PacketSendBoatPos
 * JD-Core Version:    0.6.2
 */