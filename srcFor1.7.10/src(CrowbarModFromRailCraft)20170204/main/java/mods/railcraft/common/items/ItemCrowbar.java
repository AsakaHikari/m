package mods.railcraft.common.items;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.railcraft.api.core.items.IToolCrowbar;
import mods.railcraft.common.plugins.forge.RailcraftRegistry;
import mods.railcraft.common.util.misc.MiscTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemCrowbar extends ItemTool
  implements IToolCrowbar
{
  public static final byte BOOST_DAMAGE = 3;
  private static final String ITEM_TAG = "railcraft.tool.crowbar";
  private static Item item;
  private final Set<Class<? extends Block>> shiftRotations = new HashSet();
  private final Set<Class<? extends Block>> bannedRotations = new HashSet();

  public static void registerItem() {
    if ((item == null)) {
      item = new ItemCrowbar(Item.ToolMaterial.IRON);
      item.setUnlocalizedName("railcraft.tool.crowbar");
      RailcraftRegistry.register(item);
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(item), new Object[] { " RI", "RIR", "IR ", 
        Character.valueOf('I'), 
        "ingotIron", 
        Character.valueOf('R'), 
        "dyeRed" }));
    }
  }

  public static ItemStack getItem() {
    if (item == null)
      return null;
    return new ItemStack(item);
  }

  public static Item getItemObj() {
    return item;
  }

  protected ItemCrowbar(Item.ToolMaterial material) {
    super(3.0F, material, new HashSet(Arrays.asList(new Block[] { Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail })));

    setCreativeTab(CreativeTabs.tabTransport);
    this.shiftRotations.add(BlockLever.class);
    this.shiftRotations.add(BlockButton.class);
    this.shiftRotations.add(BlockChest.class);
    this.bannedRotations.add(BlockRailBase.class);

    setHarvestLevel("crowbar", 2);
  }

  public float getDigSpeed(ItemStack stack, Block block, int meta)
  {
    if (block instanceof BlockRailBase)
      return this.efficiencyOnProperMaterial;
    return super.getDigSpeed(stack, block, meta);
  }

  public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
  {
    return true;
  }

  public void registerIcons(IIconRegister iconRegister)
  {
    this.itemIcon = iconRegister.registerIcon("railcraft:" + MiscTools.cleanTag(getUnlocalizedName()));
  }

  private boolean isShiftRotation(Class<? extends Block> cls) {
    for (Class shift : this.shiftRotations) {
      if (shift.isAssignableFrom(cls))
        return true;
    }
    return false;
  }

  private boolean isBannedRotation(Class<? extends Block> cls) {
    for (Class banned : this.bannedRotations) {
      if (banned.isAssignableFrom(cls))
        return true;
    }
    return false;
  }

  public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
  {
    Block block = world.getBlock(x, y, z);

    if (block == null) {
      return false;
    }
    if (player.isSneaking() != isShiftRotation(block.getClass())) {
      return false;
    }
    if (isBannedRotation(block.getClass())) {
      return false;
    }
    if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
      player.swingItem();
      return !world.isRemote;
    }
    return false;
  }

  public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
  {
    if ((!world.isRemote) && 
      ((entity instanceof EntityPlayer))) {
      EntityPlayer player = (EntityPlayer)entity;
      
    }
    return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
  }

  public EnumAction getItemUseAction(ItemStack stack)
  {
    return EnumAction.block;
  }

  public int getMaxItemUseDuration(ItemStack par1ItemStack)
  {
    return 72000;
  }

  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
  {
    player.setItemInUse(stack, getMaxItemUseDuration(stack));
    return stack;
  }

  public boolean canBeStoredInToolbox(ItemStack itemstack)
  {
    return true;
  }

  public boolean canWrench(EntityPlayer player, int x, int y, int z)
  {
    return true;
  }

  public void wrenchUsed(EntityPlayer player, int x, int y, int z)
  {
    player.getCurrentEquippedItem().damageItem(1, player);
    player.swingItem();
  }

  public boolean canWhack(EntityPlayer player, ItemStack crowbar, int x, int y, int z)
  {
    return true;
  }

  public void onWhack(EntityPlayer player, ItemStack crowbar, int x, int y, int z)
  {
    crowbar.damageItem(1, player);
    player.swingItem();
  }

  public boolean canLink(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
  {
    return player.isSneaking();
  }

  public void onLink(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
  {
    crowbar.damageItem(1, player);
    player.swingItem();
  }

  public boolean canBoost(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
  {
    return !player.isSneaking();
  }

  public void onBoost(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
  {
    crowbar.damageItem(3, player);
    player.swingItem();
  }

  private void removeAndDrop(World world, int x, int y, int z, Block block) {
    int meta = world.getBlockMetadata( x, y, z);
    List<ItemStack> drops = block.getDrops(world, x, y, z, meta, 0);
    for (ItemStack stack : drops)
        spewItem(stack, world, x, y, z);
    world.setBlockToAir(x, y, z);
  }
  private static void spewItem(ItemStack stack, World world, int x, int y, int z)
  {
    if (stack != null) {
      float xOffset = MiscTools.getRand().nextFloat() * 0.8F + 0.1F;
      float yOffset = MiscTools.getRand().nextFloat() * 0.8F + 0.1F;
      float zOffset = MiscTools.getRand().nextFloat() * 0.8F + 0.1F;
      while (stack.stackSize > 0) {
        int numToDrop = MiscTools.getRand().nextInt(21) + 10;
        if (numToDrop > stack.stackSize)
          numToDrop = stack.stackSize;
        ItemStack newStack = stack.copy();
        newStack.stackSize = numToDrop;
        stack.stackSize -= numToDrop;
        EntityItem entityItem = new EntityItem(world, x + xOffset, y + yOffset, z + zOffset, newStack);
        float variance = 0.05F;
        entityItem.motionX = ((float)MiscTools.getRand().nextGaussian() * variance);
        entityItem.motionY = ((float)MiscTools.getRand().nextGaussian() * variance + 0.2F);
        entityItem.motionZ = ((float)MiscTools.getRand().nextGaussian() * variance);
        world.spawnEntityInWorld(entityItem);
      }
    }
  }


  private void removeExtraBlocks(World world, int level, int x, int y, int z, Block block) {
    if (level > 0) {
      removeAndDrop(world, x, y, z, block);
      checkBlocks(world, level, x, y, z);
    }
  }

  private void checkBlock(World world, int level, int x, int y, int z) {
    Block block = world.getBlock( x, y, z);
    if ((block instanceof BlockRail) ||  (block.isToolEffective("crowbar", world.getBlockMetadata( x, y, z))))
      removeExtraBlocks(world, level - 1, x, y, z, block);
  }

  private void checkBlocks(World world, int level, int x, int y, int z)
  {
    checkBlock(world, level, x, y, z - 1);
    checkBlock(world, level, x, y + 1, z - 1);
    checkBlock(world, level, x, y - 1, z - 1);

    checkBlock(world, level, x, y, z + 1);
    checkBlock(world, level, x, y + 1, z + 1);
    checkBlock(world, level, x, y - 1, z + 1);

    checkBlock(world, level, x + 1, y, z);
    checkBlock(world, level, x + 1, y + 1, z);
    checkBlock(world, level, x + 1, y - 1, z);

    checkBlock(world, level, x - 1, y, z);
    checkBlock(world, level, x - 1, y + 1, z);
    checkBlock(world, level, x - 1, y - 1, z);

    checkBlock(world, level, x, y + 1, z);
    checkBlock(world, level, x, y - 1, z);
  }
}