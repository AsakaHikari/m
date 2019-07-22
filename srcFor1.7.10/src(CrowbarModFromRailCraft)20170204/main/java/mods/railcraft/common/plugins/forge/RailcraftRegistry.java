package mods.railcraft.common.plugins.forge;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.railcraft.api.core.items.TagList;
import mods.railcraft.common.core.Railcraft;
import mods.railcraft.common.util.misc.MiscTools;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public final class RailcraftRegistry
{
  public static ItemStack getItem(String tag, int qty)
  {
    tag = MiscTools.cleanTag(tag);
    return GameRegistry.findItemStack(Railcraft.getModId(), tag, qty);
  }

  public static void register(String tag, ItemStack stack)
  {
    if (stack == null)
      throw new RuntimeException("Don't register null items!");
    tag = MiscTools.cleanTag(tag);
    TagList.addTag(tag);

    Item existingItem = GameRegistry.findItem(Railcraft.getModId(), tag);
    Block existingBlock = GameRegistry.findBlock(Railcraft.getModId(), tag);
    if ((existingItem == null) && (existingBlock == null))
      GameRegistry.registerCustomItemStack(tag, stack);
    else
      throw new RuntimeException("ItemStack registrations must be unique!");
  }

  public static void register(ItemStack stack)
  {
    if (stack == null)
      throw new RuntimeException("Don't register null items!");
    register(stack.getUnlocalizedName(), stack);
  }

  public static void register(Item item)
  {
    _register(item);
  }

  public static void registerInit(Item item) {
    _register(item);
  }

  private static void _register(Item item) {
    String tag = item.getUnlocalizedName();
    tag = MiscTools.cleanTag(tag);
    TagList.addTag(tag);
    GameRegistry.registerItem(item, tag);
  }

  public static void register(Block block)
  {
    register(block, ItemBlock.class);
  }

  public static void register(Block block, Class<? extends ItemBlock> itemclass)
  {
    String tag = block.getUnlocalizedName();
    tag = MiscTools.cleanTag(tag);
    TagList.addTag(tag);
    GameRegistry.registerBlock(block, itemclass, tag);
  }
}