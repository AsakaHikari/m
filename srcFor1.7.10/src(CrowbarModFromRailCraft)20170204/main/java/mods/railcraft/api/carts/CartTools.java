package mods.railcraft.api.carts;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class CartTools
{
  private static final GameProfile railcraftProfile = new GameProfile(UUID.nameUUIDFromBytes("[Railcraft]".getBytes()), "[Railcraft]");
  public static ILinkageManager linkageManager;

  public static ILinkageManager getLinkageManager(World world)
  {
    return linkageManager;
  }

  public static void setCartOwner(EntityMinecart cart, EntityPlayer owner)
  {
    setCartOwner(cart, owner.getGameProfile());
  }

  public static void setCartOwner(EntityMinecart cart, GameProfile owner)
  {
    if (!cart.worldObj.isRemote) {
      NBTTagCompound data = cart.getEntityData();
      if (owner.getName() != null)
        data.setString("owner", owner.getName());
      if (owner.getId() != null)
        data.setString("ownerId", owner.getId().toString());
    }
  }

  public static GameProfile getCartOwner(EntityMinecart cart)
  {
    NBTTagCompound data = cart.getEntityData();
    String ownerName = "[Unknown]";
    if (data.hasKey("owner")) {
      ownerName = data.getString("owner");
    }
    UUID ownerId = null;
    if (data.hasKey("ownerId"))
      ownerId = UUID.fromString(data.getString("ownerId"));
    return new GameProfile(ownerId, ownerName);
  }

  public static boolean doesCartHaveOwner(EntityMinecart cart)
  {
    NBTTagCompound data = cart.getEntityData();
    return data.hasKey("owner");
  }


  public static boolean isMinecartOnRailAt(World world, int i, int j, int k, float sensitivity) {
    return isMinecartOnRailAt(world, i, j, k, sensitivity, null, true);
  }

  public static boolean isMinecartOnRailAt(World world, int i, int j, int k, float sensitivity, Class<? extends EntityMinecart> type, boolean subclass) {
    if (BlockRailBase.func_150049_b_(world, i, j, k))
      return isMinecartAt(world, i, j, k, sensitivity, type, subclass);
    return false;
  }


  public static boolean isMinecartAt(World world, int i, int j, int k, float sensitivity) {
    return isMinecartAt(world, i, j, k, sensitivity, null, true);
  }

  public static boolean isMinecartAt(World world, int i, int j, int k, float sensitivity, Class<? extends EntityMinecart> type, boolean subclass) {
    List<EntityMinecart> list = getMinecartsAt(world, i, j, k, sensitivity);

    if (type == null) {
      return !list.isEmpty();
    }
    for (EntityMinecart cart : list) {
      if (((subclass) && (type.isInstance(cart))) || (cart.getClass() == type))
        return true;
    }
    return false;
  }

  public static List<EntityMinecart> getMinecartsAt(World world, int i, int j, int k, float sensitivity)
  {
    sensitivity = Math.min(sensitivity, 0.49F);
    List entities = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox(i + sensitivity, j + sensitivity, k + sensitivity, i + 1 - sensitivity, j + 1 - sensitivity, k + 1 - sensitivity));
    List carts = new ArrayList();
    for (Iterator localIterator = entities.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
      EntityMinecart cart = (EntityMinecart)o;
      if (!cart.isDead)
        carts.add((EntityMinecart)o);
    }
    return carts;
  }

  public static List<EntityMinecart> getMinecartsIn(World world, int i1, int j1, int k1, int i2, int j2, int k2) {
    List entities = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox(i1, j1, k1, i2, j2, k2));
    List carts = new ArrayList();
    for (Iterator localIterator = entities.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
      EntityMinecart cart = (EntityMinecart)o;
      if (!cart.isDead)
        carts.add((EntityMinecart)o);
    }
    return carts;
  }

  public static double getCartSpeedUncapped(EntityMinecart cart)
  {
    return Math.sqrt(cart.motionX * cart.motionX + cart.motionZ * cart.motionZ);
  }

  public static boolean cartVelocityIsLessThan(EntityMinecart cart, float vel) {
    return (Math.abs(cart.motionX) < vel) && (Math.abs(cart.motionZ) < vel);
  }
}