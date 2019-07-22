/*     */ package de.sanandrew.mods.betterboat.entity;
/*     */ 
/*     */ import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
/*     */ import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*     */ import cpw.mods.fml.relauncher.Side;
/*     */ import cpw.mods.fml.relauncher.SideOnly;
/*     */ import de.sanandrew.mods.betterboat.BetterBoat;
/*     */ import de.sanandrew.mods.betterboat.network.PacketSendBoatPos;

/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;

/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityBetterBoat extends Entity
/*     */ {
/*     */   private static final int DW_TIME_SINCE_HIT = 17;
/*     */   private static final int DW_FORWARD_DIRECTION = 18;
/*     */   private static final int DW_DMG_TAKEN = 19;
/*     */   private boolean isBoatEmpty;
/*     */   private double speedMultiplier;
/*     */   private int boatPosRotationIncrements;
/*     */   private double boatX;
/*     */   private double boatY;
/*     */   private double boatZ;
/*     */   private double boatYaw;
/*     */   private double boatPitch;
/*     */   private double velocityX;
/*     */   private double velocityY;
/*     */   private double velocityZ;
/*     */ 
/*     */   public EntityBetterBoat(World world)
/*     */   {
/*  52 */     super(world);
/*  53 */     this.isBoatEmpty = true;
/*  54 */     this.speedMultiplier = 0.07000000000000001D;
/*  55 */     this.preventEntitySpawning = true;
/*  56 */     setSize(1.5F, 0.6F);
/*  57 */     this.yOffset = (this.height / 2.0F);
/*     */   }
/*     */ 
/*     */   public EntityBetterBoat(World world, double posX, double posY, double posZ) {
/*  61 */     this(world);
/*  62 */     setPosition(posX, posY + this.yOffset, posZ);
/*  63 */     this.motionX = 0.0D;
/*  64 */     this.motionY = 0.0D;
/*  65 */     this.motionZ = 0.0D;
/*  66 */     this.prevPosX = posX;
/*  67 */     this.prevPosY = posY;
/*  68 */     this.prevPosZ = posZ;
/*     */   }
/*     */ 
/*     */   protected boolean canTriggerWalking()
/*     */   {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   protected void entityInit()
/*     */   {
/*  78 */     this.dataWatcher.addObject(17, Integer.valueOf(0));
/*  79 */     this.dataWatcher.addObject(18, Integer.valueOf(1));
/*  80 */     this.dataWatcher.addObject(19, Float.valueOf(0.0F));
/*     */   }
/*     */ 
/*     */   public AxisAlignedBB getCollisionBox(Entity entity)
/*     */   {
/*  85 */     return entity.boundingBox;
/*     */   }
/*     */ 
/*     */   public AxisAlignedBB getBoundingBox()
/*     */   {
/*  90 */     return this.boundingBox;
/*     */   }
/*     */ 
/*     */   public boolean canBePushed()
/*     */   {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   public double getMountedYOffset()
/*     */   {
/* 100 */     return -0.3D;
/*     */   }
/*     */ 
/*     */   public boolean attackEntityFrom(DamageSource dmgSource, float damage)
/*     */   {
/* 105 */     if (isEntityInvulnerable())
/* 106 */       return false;
/* 107 */     if ((!this.worldObj.isRemote) && (!this.isDead)) {
/* 108 */       setForwardDirection(-getForwardDirection());
/* 109 */       setTimeSinceHit(10);
/* 110 */       setDamageTaken(getDamageTaken() + damage * 10.0F);
/* 111 */       setBeenAttacked();
/* 112 */       boolean isRiddenPlayerCreative = ((dmgSource.getEntity() instanceof EntityPlayer)) && (((EntityPlayer)dmgSource.getEntity()).capabilities.isCreativeMode);
/*     */ 
/* 114 */       if ((isRiddenPlayerCreative) || (getDamageTaken() > 40.0F)) {
/* 115 */         if (this.riddenByEntity != null) {
/* 116 */           this.riddenByEntity.mountEntity(this);
/*     */         }
/*     */ 
/* 119 */         if (!isRiddenPlayerCreative) {
/* 120 */           func_145778_a(Items.boat, 1, 0.0F);
/*     */         }
/*     */ 
/* 123 */         setDead();
/*     */       }
/*     */ 
/* 126 */       return true;
/*     */     }
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void performHurtAnimation()
/*     */   {
/* 135 */     setForwardDirection(-getForwardDirection());
/* 136 */     setTimeSinceHit(10);
/* 137 */     setDamageTaken(getDamageTaken() * 11.0F);
/*     */   }
/*     */ 
/*     */   public boolean canBeCollidedWith()
/*     */   {
/* 142 */     return !this.isDead;
/*     */   }
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void setPositionAndRotation2(double posX, double posY, double posZ, float yaw, float pitch, int posRotIncr)
/*     */   {
/* 148 */     if (this.isBoatEmpty) {
/* 149 */       this.boatPosRotationIncrements = (posRotIncr + 5);
/*     */     } else {
/* 151 */       double deltaX = posX - this.posX;
/* 152 */       double deltaY = posY - this.posY;
/* 153 */       double deltaZ = posZ - this.posZ;
/* 154 */       double deltaVec = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
/*     */ 
/* 156 */       if (deltaVec <= 1.0D) {
/* 157 */         return;
/*     */       }
/*     */ 
/* 160 */       this.boatPosRotationIncrements = 3;
/*     */     }
/*     */ 
/* 163 */     this.boatX = posX;
/* 164 */     this.boatY = posY;
/* 165 */     this.boatZ = posZ;
/* 166 */     this.boatYaw = yaw;
/* 167 */     this.boatPitch = pitch;
/* 168 */     this.motionX = this.velocityX;
/* 169 */     this.motionY = this.velocityY;
/* 170 */     this.motionZ = this.velocityZ;
/*     */   }
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void setVelocity(double motionX, double motionY, double motionZ)
/*     */   {
/* 176 */     this.velocityX = (this.motionX = motionX);
/* 177 */     this.velocityY = (this.motionY = motionY);
/* 178 */     this.velocityZ = (this.motionZ = motionZ);
/*     */   }
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/* 183 */     super.onUpdate();
/*     */ 
/* 185 */     if (getTimeSinceHit() > 0) {
/* 186 */       setTimeSinceHit(getTimeSinceHit() - 1);
/*     */     }
/*     */ 
/* 189 */     if (getDamageTaken() > 0.0F) {
/* 190 */       setDamageTaken(getDamageTaken() - 1.0F);
/*     */     }
/*     */ 
/* 193 */     this.prevPosX = this.posX;
/* 194 */     this.prevPosY = this.posY;
/* 195 */     this.prevPosZ = this.posZ;
/* 196 */     double motionAmount = 0.0D;
/*     */ 
/* 198 */     for (int i = 0; i < 5; i++) {
/* 199 */       double minY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * i / 5.0D - 0.125D;
/* 200 */       double maxY = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (i + 1) / 5.0D - 0.125D;
/* 201 */       AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(this.boundingBox.minX, minY, this.boundingBox.minZ, this.boundingBox.maxX, maxY, this.boundingBox.maxZ);
/*     */ 
/* 203 */       if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
/* 204 */         motionAmount += 0.2D;
/*     */       }
/*     */     }
/*     */ 
/* 208 */     double horizMotion = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */ 
/* 210 */     if (horizMotion > 0.2625D) {
/* 211 */       double motionDirCos = Math.cos(this.rotationYaw * 3.141592653589793D / 180.0D);
/* 212 */       double motionDirSin = Math.sin(this.rotationYaw * 3.141592653589793D / 180.0D);
/*     */ 
/* 214 */       for (double d = 0.0D; d < 1.0D + horizMotion * 60.0D; d += 1.0D) {
/* 215 */         double particlePosRnd1 = this.rand.nextFloat() * 2.0F - 1.0F;
/* 216 */         double particlePosRnd2 = (this.rand.nextInt(2) * 2 - 1) * 0.7D;
/*     */         double particleZ;
/*     */         double particleX;
/* 220 */         if (this.rand.nextBoolean()) {
/* 221 */           particleX = this.posX - motionDirCos * particlePosRnd1 * 0.8D + motionDirSin * particlePosRnd2;
/* 222 */           particleZ = this.posZ - motionDirSin * particlePosRnd1 * 0.8D - motionDirCos * particlePosRnd2;
/*     */         } else {
/* 224 */           particleX = this.posX + motionDirCos + motionDirSin * particlePosRnd1 * 0.7D;
/* 225 */           particleZ = this.posZ + motionDirSin - motionDirCos * particlePosRnd1 * 0.7D;
/*     */         }
/*     */ 
/* 228 */         this.worldObj.spawnParticle("splash", particleX, this.posY - 0.125D, particleZ, this.motionX, this.motionY, this.motionZ);
/*     */       }
/*     */     }
/*     */ 
/* 232 */     if ((this.worldObj.isRemote) && (this.isBoatEmpty))
/*     */     {
/* 237 */       if (this.boatPosRotationIncrements > 0) {
/* 238 */         float yawAngle = MathHelper.wrapAngleTo180_float((float)(this.boatYaw - this.rotationYaw));
/*     */ 
/* 240 */         double newPosX = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
/* 241 */         double newPosY = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
/* 242 */         double newPosZ = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
/* 243 */         this.rotationYaw += yawAngle / this.boatPosRotationIncrements;
/* 244 */         this.rotationPitch = ((float)(this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements));
/* 245 */         this.boatPosRotationIncrements -= 1;
/*     */ 
/* 247 */         setPosition(newPosX, newPosY, newPosZ);
/* 248 */         setRotation(this.rotationYaw, this.rotationPitch);
/*     */       } else {
/* 250 */         double newPosX = this.posX + this.motionX;
/* 251 */         double newPosY = this.posY + this.motionY;
/* 252 */         double newPosZ = this.posZ + this.motionZ;
/*     */ 
/* 254 */         setPosition(newPosX, newPosY, newPosZ);
/*     */ 
/* 256 */         if (this.onGround) {
/* 257 */           this.motionX *= 0.5D;
/* 258 */           this.motionY *= 0.5D;
/* 259 */           this.motionZ *= 0.5D;
/*     */         }
/*     */ 
/* 262 */         this.motionX *= 0.99D;
/* 263 */         this.motionY *= 0.95D;
/* 264 */         this.motionZ *= 0.99D;
/*     */       }
/*     */     }
/*     */     else {
/* 268 */       if (motionAmount < 1.0D) {
/* 269 */         double motionVal = motionAmount * 2.0D - 1.0D;
/* 270 */         this.motionY += 0.04D * motionVal;
/*     */       } else {
/* 272 */         if (this.motionY < 0.0D) {
/* 273 */           this.motionY /= 2.0D;
/*     */         }
/*     */ 
/* 276 */         this.motionY += 0.007000000216066837D;
/*     */       }
/*     */ 
/* 279 */       if ((this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityLivingBase))) {
/* 280 */         EntityLivingBase riddenLiving = (EntityLivingBase)this.riddenByEntity;
/* 281 */         float sideMotion = this.riddenByEntity.rotationYaw + -riddenLiving.moveStrafing * 90.0F;
/* 282 */         this.motionX += -Math.sin(sideMotion * 3.141593F / 180.0F) * this.speedMultiplier * riddenLiving.moveForward * 0.05D;
/* 283 */         this.motionZ += Math.cos(sideMotion * 3.141593F / 180.0F) * this.speedMultiplier * riddenLiving.moveForward * 0.05D;
/*     */       }
/*     */ 
/* 286 */       double motionVal = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */ 
/* 288 */       if (motionVal > 0.35D) {
/* 289 */         double motionValRecur = 0.35D / motionVal;
/* 290 */         this.motionX *= motionValRecur;
/* 291 */         this.motionZ *= motionValRecur;
/* 292 */         motionVal = 0.35D;
/*     */       }
/*     */ 
/* 295 */       if ((motionVal > horizMotion) && (this.speedMultiplier < 0.35D)) {
/* 296 */         this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
/*     */ 
/* 298 */         if (this.speedMultiplier > 0.35D)
/* 299 */           this.speedMultiplier = 0.35D;
/*     */       }
/*     */       else {
/* 302 */         this.speedMultiplier -= (this.speedMultiplier - 0.07000000000000001D) / 35.0D;
/*     */ 
/* 304 */         if (this.speedMultiplier < 0.07000000000000001D) {
/* 305 */           this.speedMultiplier = 0.07000000000000001D;
/*     */         }
/*     */       }
/*     */ 
/* 309 */       for (int i = 0; i < 4; i++) {
/* 310 */         int blockX = MathHelper.floor_double(this.posX + (i % 2 - 0.5D) * 0.8D);
/* 311 */         int blockZ = MathHelper.floor_double(this.posZ + (i / 2 - 0.5D) * 0.8D);
/*     */ 
/* 313 */         for (int j = 0; j < 2; j++) {
/* 314 */           int blockY = MathHelper.floor_double(this.posY) + j;
/* 315 */           Block block = this.worldObj.getBlock(blockX, blockY, blockZ);
/*     */ 
/* 317 */           if (block == Blocks.snow_layer) {
/* 318 */             this.worldObj.setBlockToAir(blockX, blockY, blockZ);
/* 319 */             this.isCollidedHorizontally = false;
/* 320 */           } else if (block == Blocks.waterlily) {
/* 321 */             this.worldObj.func_147480_a(blockX, blockY, blockZ, true);
/* 322 */             this.isCollidedHorizontally = false;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 327 */       if (this.onGround) {
/* 328 */         this.motionX *= 0.5D;
/* 329 */         this.motionY *= 0.5D;
/* 330 */         this.motionZ *= 0.5D;
/*     */       }
/*     */ 
/* 333 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */ 
/* 335 */       this.rotationPitch = 0.0F;
/* 336 */       double yaw = this.rotationYaw;
/* 337 */       double deltaPosX = this.prevPosX - this.posX;
/* 338 */       double deltaPosZ = this.prevPosZ - this.posZ;
/*     */ 
/* 340 */       if (deltaPosX * deltaPosX + deltaPosZ * deltaPosZ > 0.001D) {
/* 341 */         yaw = (float)(Math.atan2(deltaPosZ, deltaPosX) * 180.0D / 3.141592653589793D);
/*     */       }
/*     */ 
/* 344 */       double yawAngle = MathHelper.wrapAngleTo180_double(yaw - this.rotationYaw);
/*     */ 
/* 346 */       if (yawAngle > 20.0D) {
/* 347 */         yawAngle = 20.0D;
/*     */       }
/*     */ 
/* 350 */       if (yawAngle < -20.0D) {
/* 351 */         yawAngle = -20.0D;
/*     */       }
/*     */ 
/* 354 */       this.rotationYaw = ((float)(this.rotationYaw + yawAngle));
/* 355 */       setRotation(this.rotationYaw, this.rotationPitch);
/*     */ 
/* 357 */       if (!this.worldObj.isRemote) {
/* 358 */         List collidedEntities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2D, 0.0D, 0.2D));
/*     */         Iterator localIterator;
/* 360 */         if ((collidedEntities != null) && (!collidedEntities.isEmpty())) {
/* 361 */           for (localIterator = collidedEntities.iterator(); localIterator.hasNext(); ) { Object entityObj = localIterator.next();
/* 362 */             Entity entity = (Entity)entityObj;
/*     */ 
/* 364 */             if (entity != this.riddenByEntity) {
/* 365 */               if ((entity.canBePushed()) && ((entity instanceof EntityBoat))) {
/* 366 */                 entity.applyEntityCollision(this);
/* 367 */               } else if ((entity instanceof EntityLivingBase)) {
/* 368 */                 entity.applyEntityCollision(this);
/* 369 */                 this.isCollidedHorizontally = false;
/* 370 */                 if (horizMotion > 0.2D) {
/* 371 */                   entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.riddenByEntity != null ? this.riddenByEntity : this), (float)(horizMotion * 5.0D));
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 378 */         if ((this.riddenByEntity != null) && (this.riddenByEntity.isDead)) {
/* 379 */           this.riddenByEntity = null;
/*     */         }
/*     */ 
/* 382 */         
/*     */       }else{
	if (this.ticksExisted % 20 == 0) {
		/* 383 */           BetterBoat.network.sendToServer(new PacketSendBoatPos(this));
	/*     */         }
}
/*     */ 
/* 387 */       if ((this.isCollidedHorizontally) && (horizMotion > 0.2D)) {
/* 388 */         if ((!this.worldObj.isRemote) && (!this.isDead)) {
/* 389 */           setDead();
/*     */ 
/* 391 */           if ((!(this.riddenByEntity instanceof EntityPlayer)) || (!((EntityPlayer)this.riddenByEntity).capabilities.isCreativeMode))
/* 392 */             func_145778_a(Items.boat, 1, 0.0F);
/*     */         }
/*     */       }
/*     */       else {
/* 396 */         this.motionX *= 0.99D;
/* 397 */         this.motionY *= 0.95D;
/* 398 */         this.motionZ *= 0.99D;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateRiderPosition()
/*     */   {
/* 405 */     if (this.riddenByEntity != null) {
/* 406 */       double xOffset = Math.cos(this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
/* 407 */       double zOffset = Math.sin(this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
/* 408 */       this.riddenByEntity.setPosition(this.posX + xOffset, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + zOffset);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeEntityToNBT(NBTTagCompound nbt)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void readEntityFromNBT(NBTTagCompound nbt) {
/*     */   }
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public float getShadowSize() {
/* 421 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public boolean interactFirst(EntityPlayer player)
/*     */   {
/* 426 */     if ((this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityPlayer)) && (this.riddenByEntity != player)) {
/* 427 */       return true;
/*     */     }
/* 429 */     if (!this.worldObj.isRemote) {
/* 430 */       player.mountEntity(this);
/*     */     }
/*     */ 
/* 433 */     return true;
/*     */   }
/*     */ 
/*     */   protected void updateFallState(double tickDistance, boolean onGround)
/*     */   {
/* 439 */     int blockX = MathHelper.floor_double(this.posX);
/* 440 */     int blockY = MathHelper.floor_double(this.posY);
/* 441 */     int blockZ = MathHelper.floor_double(this.posZ);
/*     */ 
/* 443 */     if (onGround) {
/* 444 */       if (this.fallDistance > 3.0F) {
/* 445 */         fall(this.fallDistance);
/*     */ 
/* 447 */         if ((!this.worldObj.isRemote) && (!this.isDead)) {
/* 448 */           setDead();
/*     */ 
/* 450 */           if ((!(this.riddenByEntity instanceof EntityPlayer)) || (!((EntityPlayer)this.riddenByEntity).capabilities.isCreativeMode)) {
/* 451 */             func_145778_a(Items.boat, 1, 0.0F);
/*     */           }
/*     */         }
/*     */ 
/* 455 */         this.fallDistance = 0.0F;
/*     */       }
/* 457 */     } else if ((this.worldObj.getBlock(blockX, blockY - 1, blockZ).getMaterial() != Material.water) && (tickDistance < 0.0D))
/* 458 */       this.fallDistance = ((float)(this.fallDistance - tickDistance));
/*     */   }
/*     */ 
/*     */   public void setDamageTaken(float damage)
/*     */   {
/* 463 */     this.dataWatcher.updateObject(19, Float.valueOf(damage));
/*     */   }
/*     */ 
/*     */   public float getDamageTaken() {
/* 467 */     return this.dataWatcher.getWatchableObjectFloat(19);
/*     */   }
/*     */ 
/*     */   public void setTimeSinceHit(int time) {
/* 471 */     this.dataWatcher.updateObject(17, Integer.valueOf(time));
/*     */   }
/*     */ 
/*     */   public int getTimeSinceHit() {
/* 475 */     return this.dataWatcher.getWatchableObjectInt(17);
/*     */   }
/*     */ 
/*     */   public void setForwardDirection(int direction) {
/* 479 */     this.dataWatcher.updateObject(18, Integer.valueOf(direction));
/*     */   }
/*     */ 
/*     */   public int getForwardDirection() {
/* 483 */     return this.dataWatcher.getWatchableObjectInt(18);
/*     */   }
/*     */ }

/* Location:           C:\Users\na0aya2e\雑多なプログラム\jd-gui-0.3.5.windows\minecraftModsToAnalyze\BetterBoat-1.7.10-1.1.0.deobf.jar
 * Qualified Name:     de.sanandrew.mods.betterboat.entity.EntityBetterBoat
 * JD-Core Version:    0.6.2
 */