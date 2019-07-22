package mods.railcraft.common.util.misc;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MiscTools
{
  public static final Random RANDOM = new Random();

  public static Random getRand()
  {
    return RANDOM;
  }


  public static String cleanTag(String tag) {
    return tag.replaceAll("[Rr]ailcraft\\p{Punct}", "").replaceFirst("^tile\\.", "").replaceFirst("^item\\.", "");
  }

  public static void writeUUID(NBTTagCompound data, String tag, UUID uuid) {
    if (uuid == null)
      return;
    NBTTagCompound nbtTag = new NBTTagCompound();
    nbtTag.setLong("most", uuid.getMostSignificantBits());
    nbtTag.setLong("least", uuid.getLeastSignificantBits());
    data.setTag(tag, nbtTag);
  }

  public static UUID readUUID(NBTTagCompound data, String tag) {
    if (data.hasKey(tag)) {
      NBTTagCompound nbtTag = data.getCompoundTag(tag);
      return new UUID(nbtTag.getLong("most"), nbtTag.getLong("least"));
    }
    return null;
  }

  public static <T extends Entity> List<T> getNearbyEntities(World world, Class<T> entityClass, float x, float minY, float maxY, float z, float radius) {
    AxisAlignedBB box = new AxisAlignedBB(x, minY, z, x + 1.0F, maxY, z + 1.0F);
    box = box.expand(radius, 0.0D, radius);
    return world.getEntitiesWithinAABB(entityClass, box);
  }

  public static <T extends Entity> List<T> getEntitiesAt(World world, Class<T> entityClass, int x, int y, int z) {
    AxisAlignedBB box = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
    return world.getEntitiesWithinAABB(entityClass, box);
  }


  public static RayTraceResult collisionRayTrace(Vec3d vec3d, Vec3d vec3d1, int i, int j, int k) {
    vec3d = vec3d.addVector(-i, -j, -k);
    vec3d1 = vec3d1.addVector(-i, -j, -k);
    Vec3d vec3d2 = vec3d.getIntermediateWithXValue(vec3d1, 0.0D);
    Vec3d vec3d3 = vec3d.getIntermediateWithXValue(vec3d1, 1.0D);
    Vec3d vec3d4 = vec3d.getIntermediateWithYValue(vec3d1, 0.0D);
    Vec3d vec3d5 = vec3d.getIntermediateWithYValue(vec3d1, 1.0D);
    Vec3d vec3d6 = vec3d.getIntermediateWithZValue(vec3d1, 0.0D);
    Vec3d vec3d7 = vec3d.getIntermediateWithZValue(vec3d1, 1.0D);
    if (!isVecInsideYZBounds(vec3d2))
      vec3d2 = null;
    if (!isVecInsideYZBounds(vec3d3))
      vec3d3 = null;
    if (!isVecInsideXZBounds(vec3d4))
      vec3d4 = null;
    if (!isVecInsideXZBounds(vec3d5))
      vec3d5 = null;
    if (!isVecInsideXYBounds(vec3d6))
      vec3d6 = null;
    if (!isVecInsideXYBounds(vec3d7))
      vec3d7 = null;
    Vec3d vec3d8 = null;
    if ((vec3d2 != null) && ((vec3d8 == null) || (vec3d.distanceTo(vec3d2) < vec3d.distanceTo(vec3d8))))
      vec3d8 = vec3d2;
    if ((vec3d3 != null) && ((vec3d8 == null) || (vec3d.distanceTo(vec3d3) < vec3d.distanceTo(vec3d8))))
      vec3d8 = vec3d3;
    if ((vec3d4 != null) && ((vec3d8 == null) || (vec3d.distanceTo(vec3d4) < vec3d.distanceTo(vec3d8))))
      vec3d8 = vec3d4;
    if ((vec3d5 != null) && ((vec3d8 == null) || (vec3d.distanceTo(vec3d5) < vec3d.distanceTo(vec3d8))))
      vec3d8 = vec3d5;
    if ((vec3d6 != null) && ((vec3d8 == null) || (vec3d.distanceTo(vec3d6) < vec3d.distanceTo(vec3d8))))
      vec3d8 = vec3d6;
    if ((vec3d7 != null) && ((vec3d8 == null) || (vec3d.distanceTo(vec3d7) < vec3d.distanceTo(vec3d8))))
      vec3d8 = vec3d7;
    if (vec3d8 == null)
      return null;
    byte byte0 = -1;
    if (vec3d8 == vec3d2)
      byte0 = 4;
    if (vec3d8 == vec3d3)
      byte0 = 5;
    if (vec3d8 == vec3d4)
      byte0 = 0;
    if (vec3d8 == vec3d5)
      byte0 = 1;
    if (vec3d8 == vec3d6)
      byte0 = 2;
    if (vec3d8 == vec3d7)
      byte0 = 3;
    return new RayTraceResult( vec3d8.addVector(i, j, k), EnumFacing.getFront(byte0),new BlockPos(i, j, k));
  }

  private static boolean isVecInsideYZBounds(Vec3d vec3d) {
    if (vec3d == null) {
      return false;
    }
    return (vec3d.yCoord >= 0.0D) && (vec3d.yCoord <= 1.0D) && (vec3d.zCoord >= 0.0D) && (vec3d.zCoord <= 1.0D);
  }

  private static boolean isVecInsideXZBounds(Vec3d vec3d) {
    if (vec3d == null) {
      return false;
    }
    return (vec3d.xCoord >= 0.0D) && (vec3d.xCoord <= 1.0D) && (vec3d.zCoord >= 0.0D) && (vec3d.zCoord <= 1.0D);
  }

  private static boolean isVecInsideXYBounds(Vec3d vec3d) {
    if (vec3d == null) {
      return false;
    }
    return (vec3d.xCoord >= 0.0D) && (vec3d.xCoord <= 1.0D) && (vec3d.yCoord >= 0.0D) && (vec3d.yCoord <= 1.0D);
  }

  public static RayTraceResult rayTracePlayerLook(EntityPlayer player) {
    double distance = player.capabilities.isCreativeMode ? 5.0D : 4.5D;
    Vec3d posVec = new Vec3d(player.posX, player.posY, player.posZ);
    Vec3d lookVec = player.getLook(1.0F);
    posVec.addVector(0,player.getEyeHeight(),0);
    lookVec = posVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
    return player.world.rayTraceBlocks(posVec, lookVec);
  }

  public static EnumFacing getCurrentMousedOverSide(EntityPlayer player)
  {
	  RayTraceResult mouseOver = rayTracePlayerLook(player);
    if (mouseOver != null)
      return mouseOver.sideHit;
    return EnumFacing.DOWN;
  }

  public static EnumFacing getSideClosestToPlayer(World world, int i, int j, int k, EntityLivingBase entityplayer)
  {
    if ((MathHelper.abs((float)entityplayer.posX - i) < 2.0F) && (MathHelper.abs((float)entityplayer.posZ - k) < 2.0F)) {
      double d = entityplayer.posY + 1.82D - entityplayer.getYOffset();
      if (d - j > 2.0D)
        return EnumFacing.UP;
      if (j - d > 0.0D)
        return EnumFacing.DOWN;
    }
    int dir = MathHelper.floor(entityplayer.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
    switch (dir) {
    case 0:
      return EnumFacing.NORTH;
    case 1:
      return EnumFacing.EAST;
    case 2:
      return EnumFacing.SOUTH;
    }
    return dir != 3 ? EnumFacing.DOWN : EnumFacing.WEST;
  }


  public static EnumFacing getHorizontalSideClosestToPlayer(World world, int x, int y, int z, EntityLivingBase player)
  {
    int dir = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
    switch (dir) {
    case 0:
      return EnumFacing.NORTH;
    case 1:
      return EnumFacing.EAST;
    case 2:
      return EnumFacing.SOUTH;
    case 3:
      return EnumFacing.WEST;
    }
    return EnumFacing.NORTH;
  }

  public static EnumFacing getOppositeSide(int side) {
    int s = side;
    s = s % 2 == 0 ? s + 1 : s - 1;
    return EnumFacing.getFront(s);
  }

  public static int getXOnSide(int x, EnumFacing side) {
    return x + side.getFrontOffsetX();
  }

  public static int getYOnSide(int y, EnumFacing side) {
    return y + side.getFrontOffsetY();
  }

  public static int getZOnSide(int z, EnumFacing side) {
    return z + side.getFrontOffsetZ();
  }

  public static boolean areCoordinatesOnSide(int x, int y, int z, EnumFacing side, int xCoord, int yCoord, int zCoord) {
    return (x + side.getFrontOffsetX() == xCoord) && (y + side.getFrontOffsetY() == yCoord) && (z + side.getFrontOffsetZ() == zCoord);
  }

  public static boolean isKillabledEntity(Entity entity) {
    if ((entity.getRidingEntity() instanceof EntityMinecart))
      return false;
    if (!(entity instanceof EntityLivingBase))
      return false;
    return ((EntityLivingBase)entity).getMaxHealth() < 100.0F;
  }

  public static enum ArmorSlots
  {
    BOOTS, 
    LEGS, 
    CHEST, 
    HELM;
  }
}