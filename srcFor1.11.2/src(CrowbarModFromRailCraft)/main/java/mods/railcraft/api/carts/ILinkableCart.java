package mods.railcraft.api.carts;

import net.minecraft.entity.item.EntityMinecart;

public abstract interface ILinkableCart
{
  public abstract boolean isLinkable();

  public abstract boolean canLinkWithCart(EntityMinecart paramEntityMinecart);

  public abstract boolean hasTwoLinks();

  public abstract float getLinkageDistance(EntityMinecart paramEntityMinecart);

  public abstract float getOptimalDistance(EntityMinecart paramEntityMinecart);

  public abstract boolean canBeAdjusted(EntityMinecart paramEntityMinecart);

  public abstract void onLinkCreated(EntityMinecart paramEntityMinecart);

  public abstract void onLinkBroken(EntityMinecart paramEntityMinecart);
}