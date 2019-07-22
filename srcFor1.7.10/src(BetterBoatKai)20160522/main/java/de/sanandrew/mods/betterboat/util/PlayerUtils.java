package de.sanandrew.mods.betterboat.util;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public final class PlayerUtils
{
  private static final Field eventHandler = ReflectionHelper.findField(InventoryCrafting.class, new String[] { "eventHandler", "field_70465_c" });

  private static final Field containerPlayer = ReflectionHelper.findField(ContainerPlayer.class, new String[] { "thePlayer", "field_82862_h" });

  private static final Field slotPlayer = ReflectionHelper.findField(SlotCrafting.class, new String[] { "thePlayer", "field_75238_b" });

  @SideOnly(Side.CLIENT)
  public static Field currentBlockDamage;

  public static boolean canPlayerSleep(EntityPlayer player)
  {
    return (!player.isPlayerSleeping()) && (player.isEntityAlive()) && (player.worldObj.getWorldTime() > 12541L) && (player.worldObj.getWorldTime() < 23458L);
  }
/*
  public static void tryAOEHarvest(EntityPlayer player, Material[] materials, int layers)
  {
    ItemStack stack = player.getHeldItem();

    if (stack != null)
    {
      ItemStackUtils.prepareDataTag(stack);
      MovingObjectPosition lookPos = MathsUtils.rayTrace(player, 4.5D);

      if ((lookPos != null) && ((player instanceof EntityPlayerMP)))
      {
        EntityPlayerMP playerMP = (EntityPlayerMP)player;
        NBTTagCompound dataTag = stack.stackTagCompound;

        int x = lookPos.blockX;
        int y = lookPos.blockY;
        int z = lookPos.blockZ;

        if ((!dataTag.hasKey("bookshelfBreaking")) || (!dataTag.getBoolean("bookshelfBreaking")))
        {
          dataTag.setBoolean("bookshelfBreaking", true);
          int rangeX = layers;
          int rangeY = layers;
          int rangeZ = layers;

          switch (lookPos.sideHit)
          {
          case 1:
            rangeY = 0;
            break;
          case 3:
            rangeZ = 0;
            break;
          case 5:
            rangeX = 0;
          case 2:
          case 4:
          }
          for (int posX = x - rangeX; posX <= x + rangeX; posX++)
          {
            for (int posY = y - rangeY; posY <= y + rangeY; posY++)
            {
              for (int posZ = z - rangeZ; posZ <= z + rangeZ; posZ++)
              {
                Block block = playerMP.worldObj.getBlock(posX, posY, posZ);

                for (Material mat : materials) {
                  if ((block != null) && (mat == block.getMaterial()) && (block.getPlayerRelativeBlockHardness(playerMP, playerMP.worldObj, x, posY, z) > 0.0F))
                    playerMP.theItemInWorldManager.tryHarvestBlock(posX, posY, posZ);
                }
              }
            }
          }
          dataTag.setBoolean("bookshelfBreaking", false);
        }
      }
    }
  }
*/
  public static boolean isPlayerReal(EntityPlayer player)
  {
    if ((player == null) || (player.worldObj == null) || (player.getClass() != EntityPlayerMP.class)) {
      return false;
    }
    return MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
  }

  public static EntityPlayer getPlayerFromUUID(World world, UUID playerID)
  {
    for (Iterator i$ = world.playerEntities.iterator(); i$.hasNext(); ) { Object playerEntry = i$.next();

      if ((playerEntry instanceof EntityPlayer))
      {
        EntityPlayer player = (EntityPlayer)playerEntry;

        if (player.getUniqueID().equals(playerID)) {
          return player;
        }
      }
    }
    return null;
  }

  public static EntityPlayer getPlayerFromCrafting(InventoryCrafting inventory)
  {
    try
    {
      Container container = (Container)eventHandler.get(inventory);

      if ((container instanceof ContainerPlayer)) {
        return (EntityPlayer)containerPlayer.get(container);
      }
      if ((container instanceof ContainerWorkbench)) {
        return (EntityPlayer)slotPlayer.get(container.getSlot(0));
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return null;
  }

  @SideOnly(Side.CLIENT)
  public static EntityPlayer getClientPlayer()
  {
    return Minecraft.getMinecraft().thePlayer;
  }

  @SideOnly(Side.CLIENT)
  public static float getBlockDamage()
  {
    if (currentBlockDamage == null) {
      return 0.0F;
    }
    try
    {
      return currentBlockDamage.getFloat(Minecraft.getMinecraft().playerController);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
    }

    return 0.0F;
  }
}