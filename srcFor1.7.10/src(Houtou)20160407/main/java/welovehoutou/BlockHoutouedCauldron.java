package welovehoutou;

import org.lwjgl.util.Color;

import com.maverick.minekawaii.MainTest;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockHoutouedCauldron extends BlockCauldron{
	@SideOnly(Side.CLIENT)
	public IIcon houtou;
	public IIcon ramen;

	public BlockHoutouedCauldron()
	{
		super();
		setHardness(2.0F);
	}
	@Override
	public void func_150024_a(World p_150024_1_, int p_150024_2_, int p_150024_3_, int p_150024_4_, int p_150024_5_)
	{
		p_150024_1_.setBlockMetadataWithNotify(p_150024_2_, p_150024_3_, p_150024_4_,p_150024_5_ , 2);
		p_150024_1_.func_147453_f(p_150024_2_, p_150024_3_, p_150024_4_, this);
	}

	public static int func_150027_b(int metadata){
		return metadata<8?metadata+1:metadata-7;
	}

	public static IIcon getContentsIcon(int metadata){
		return metadata<8?HoutouCore.houtouedCauldron.houtou:HoutouCore.houtouedCauldron.ramen;
	}

	public void registerBlockIcons(IIconRegister p_149651_1_)
	{
		super.registerBlockIcons(p_149651_1_);
		this.houtou = p_149651_1_.registerIcon("houtou:houtou");
		this.ramen = p_149651_1_.registerIcon("houtou:cookednoodles");
	}
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			ItemStack itemstack = player.inventory.getCurrentItem();

			if (itemstack == null)
			{
				return true;
			}
			else
			{
				int metadata = world.getBlockMetadata(x, y, z);
				int level = func_150027_b(metadata);

				
				{
					if (itemstack.getItem() == Items.glass_bottle)
					{
						if (level > 0)
						{
							if (!player.capabilities.isCreativeMode)
							{
								ItemStack itemstack1;
								if(metadata<8){
									itemstack1=new ItemStack(HoutouCore.itemHoutou, 1, 0);
								}else{
									itemstack1=new ItemStack(MainTest.cookednoodles, 1, 0);
								}

								if (!player.inventory.addItemStackToInventory(itemstack1))
								{
									world.spawnEntityInWorld(new EntityItem(world, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, itemstack1));
								}
								else if (player instanceof EntityPlayerMP)
								{
									((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
								}

								--itemstack.stackSize;

								if (itemstack.stackSize <= 0)
								{
									player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
								}
							}

							if(metadata ==0||metadata==8){
								world.setBlock(x,y,z, Blocks.cauldron, 0,2);
							}else{
								this.func_150024_a(world, x, y, z,metadata - 1);
							}
						}
					}
					else if (level > 0 && itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.CLOTH)
					{
						ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
						itemarmor.func_82813_b(itemstack ,0x00cab13c);
						if(metadata ==0||metadata==8){
							world.setBlock(x,y,z, Blocks.cauldron, 0,2);
						}else{
							this.func_150024_a(world, x, y, z,metadata - 1);
						}
						return true;
					}

					return false;
				}
			}
		}
	}

	@Override
	public int getRenderType()
	{
		return HoutouCore.houtouedCauldronRenderID;
	}

	@SideOnly(Side.CLIENT)
	public static float getRenderLiquidLevel(int p_150025_0_)
	{
		int j = func_150027_b(p_150025_0_);
		return (float)(6 + 3 * j) / 16.0F;
	}

	@Override
	public void fillWithRain(World p_149639_1_, int p_149639_2_, int p_149639_3_, int p_149639_4_){}
}
