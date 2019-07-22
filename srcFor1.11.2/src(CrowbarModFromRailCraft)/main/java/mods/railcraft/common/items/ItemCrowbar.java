package mods.railcraft.common.items;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mods.railcraft.api.core.items.IToolCrowbar;
import mods.railcraft.common.util.misc.MiscTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class ItemCrowbar extends ItemTool
implements IToolCrowbar
{
	public static final byte BOOST_DAMAGE = 3;
	private static final String ITEM_TAG = "railcraft.tool.crowbar";
	private static Item item;
	private final Set<Class<? extends Block>> shiftRotations = new HashSet();
	private final Set<Class<? extends Block>> bannedRotations = new HashSet();

	public static void registerItem(RegistryEvent.Register<Item> event) {
		if ((item == null)) {
			item = new ItemCrowbar(Item.ToolMaterial.IRON);
			item.setUnlocalizedName("railcraft.tool.crowbar").setRegistryName("tool.crowbar");
			event.getRegistry().register(item);
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
		super(3.0F, -2.8F, material, new HashSet(Arrays.asList(new Block[] { Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL })));

		setCreativeTab(CreativeTabs.TRANSPORTATION);
		this.shiftRotations.add(BlockLever.class);
		this.shiftRotations.add(BlockButton.class);
		this.shiftRotations.add(BlockChest.class);
		this.bannedRotations.add(BlockRailBase.class);

		setHarvestLevel("crowbar", 2);
	}


	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
	{
		return true;
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
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Block block = world.getBlockState(pos).getBlock();

		if (block == null) {
			return EnumActionResult.PASS;
		}
		if (player.isSneaking() != isShiftRotation(block.getClass())) {
			return EnumActionResult.PASS;
		}
		if (isBannedRotation(block.getClass())) {
			return EnumActionResult.PASS;
		}
		if (block.rotateBlock(world, pos, facing)) {
			player.swingArm(EnumHand.MAIN_HAND);
			return !world.isRemote?EnumActionResult.SUCCESS:EnumActionResult.PASS;
		}
		return EnumActionResult.PASS;
	}

	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase entity)
	{
		if ((!world.isRemote) && 
				((entity instanceof EntityPlayer))) {
			EntityPlayer player = (EntityPlayer)entity;

		}
		return super.onBlockDestroyed(stack, world, block,pos, entity);
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BLOCK;
	}

	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	public boolean canBeStoredInToolbox(ItemStack itemstack)
	{
		return true;
	}

	public boolean canWrench(EntityPlayer player, int x, int y, int z)
	{
		return true;
	}


	public boolean canWhack(EntityPlayer player, ItemStack crowbar, int x, int y, int z)
	{
		return true;
	}

	public void onWhack(EntityPlayer player, ItemStack crowbar, int x, int y, int z)
	{
		crowbar.damageItem(1, player);
		player.swingArm(EnumHand.MAIN_HAND);
	}

	public boolean canLink(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
	{
		return player.isSneaking();
	}

	public void onLink(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
	{
		crowbar.damageItem(1, player);
		player.swingArm(EnumHand.MAIN_HAND);
	}

	public boolean canBoost(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
	{
		return !player.isSneaking();
	}

	public void onBoost(EntityPlayer player, ItemStack crowbar, EntityMinecart cart)
	{
		crowbar.damageItem(3, player);
		player.swingArm(EnumHand.MAIN_HAND);
	}

	private void removeAndDrop(World world,BlockPos pos, Block block) {
		IBlockState meta = world.getBlockState(pos);
		List<ItemStack> drops = block.getDrops(world,pos, meta, 0);
		for (ItemStack stack : drops)
			spewItem(stack, world,pos);
		world.setBlockToAir(pos);
	}
	private static void spewItem(ItemStack stack, World world,BlockPos pos)
	{
		if (stack != null) {
			float xOffset = MiscTools.getRand().nextFloat() * 0.8F + 0.1F;
			float yOffset = MiscTools.getRand().nextFloat() * 0.8F + 0.1F;
			float zOffset = MiscTools.getRand().nextFloat() * 0.8F + 0.1F;
			while (stack.getCount() > 0) {
				int numToDrop = MiscTools.getRand().nextInt(21) + 10;
				if (numToDrop > stack.getCount())
					numToDrop = stack.getCount();
				ItemStack newStack = stack.copy();
				newStack.setCount(numToDrop);
				stack.setCount(numToDrop);
				EntityItem entityItem = new EntityItem(world, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, newStack);
				float variance = 0.05F;
				entityItem.motionX = ((float)MiscTools.getRand().nextGaussian() * variance);
				entityItem.motionY = ((float)MiscTools.getRand().nextGaussian() * variance + 0.2F);
				entityItem.motionZ = ((float)MiscTools.getRand().nextGaussian() * variance);
				world.spawnEntity(entityItem);
			}
		}
	}


	private void removeExtraBlocks(World world, int level,BlockPos pos, Block block) {
		if (level > 0) {
			removeAndDrop(world, pos, block);
			checkBlocks(world, level,pos.getX(),pos.getY(),pos.getZ());
		}
	}

	private void checkBlock(World world, int level,int x,int y,int z) {
		Block block = world.getBlockState(new BlockPos(x,y,z)).getBlock();
		if ((block instanceof BlockRailBase) ||  (block.isToolEffective("crowbar", world.getBlockState(new BlockPos(x,y,z)))))
			removeExtraBlocks(world, level - 1, new BlockPos(x,y,z), block);
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