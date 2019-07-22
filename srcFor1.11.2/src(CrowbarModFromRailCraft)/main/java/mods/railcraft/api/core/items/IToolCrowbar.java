package mods.railcraft.api.core.items;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract interface IToolCrowbar
{
  public abstract boolean canWhack(EntityPlayer paramEntityPlayer, ItemStack paramItemStack, int paramInt1, int paramInt2, int paramInt3);

  public abstract void onWhack(EntityPlayer paramEntityPlayer, ItemStack paramItemStack, int paramInt1, int paramInt2, int paramInt3);

  public abstract boolean canLink(EntityPlayer paramEntityPlayer, ItemStack paramItemStack, EntityMinecart paramEntityMinecart);

  public abstract void onLink(EntityPlayer paramEntityPlayer, ItemStack paramItemStack, EntityMinecart paramEntityMinecart);

  public abstract boolean canBoost(EntityPlayer paramEntityPlayer, ItemStack paramItemStack, EntityMinecart paramEntityMinecart);

  public abstract void onBoost(EntityPlayer paramEntityPlayer, ItemStack paramItemStack, EntityMinecart paramEntityMinecart);
}