package mods.railcraft.api.carts;

import java.util.UUID;
import net.minecraft.entity.item.EntityMinecart;

public abstract interface ILinkageManager
{
  public static final float LINKAGE_DISTANCE = 1.25F;
  public static final float OPTIMAL_DISTANCE = 0.78F;

  public abstract boolean setAutoLink(EntityMinecart paramEntityMinecart, boolean paramBoolean);

  public abstract boolean hasAutoLink(EntityMinecart paramEntityMinecart);

  public abstract boolean tryAutoLink(EntityMinecart paramEntityMinecart1, EntityMinecart paramEntityMinecart2);

  public abstract boolean createLink(EntityMinecart paramEntityMinecart1, EntityMinecart paramEntityMinecart2);

  public abstract boolean hasFreeLink(EntityMinecart paramEntityMinecart);

  public abstract EntityMinecart getLinkedCartA(EntityMinecart paramEntityMinecart);

  public abstract EntityMinecart getLinkedCartB(EntityMinecart paramEntityMinecart);

  public abstract boolean areLinked(EntityMinecart paramEntityMinecart1, EntityMinecart paramEntityMinecart2);

  public abstract void breakLink(EntityMinecart paramEntityMinecart1, EntityMinecart paramEntityMinecart2);

  public abstract void breakLinks(EntityMinecart paramEntityMinecart);

  public abstract void breakLinkA(EntityMinecart paramEntityMinecart);

  public abstract void breakLinkB(EntityMinecart paramEntityMinecart);

  public abstract int countCartsInTrain(EntityMinecart paramEntityMinecart);

  public abstract Iterable<EntityMinecart> getCartsInTrain(EntityMinecart paramEntityMinecart);

  public abstract EntityMinecart getCartFromUUID(UUID paramUUID);
}