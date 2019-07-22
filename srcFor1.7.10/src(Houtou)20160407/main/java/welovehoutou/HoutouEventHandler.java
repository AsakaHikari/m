package welovehoutou;

import com.maverick.minekawaii.MainTest;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class HoutouEventHandler {
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent e){
		if(e.world.isRemote)return;
		Block b=e.world.getBlock(e.x,e.y,e.z);
		if(e.entityPlayer!=null &&e.entityPlayer.inventory.getCurrentItem()!=null){
			if(b==Blocks.cauldron){
				if(e.entityPlayer.inventory.getCurrentItem().getItem()==HoutouCore.itemHoutou){
					if (!e.entityPlayer.capabilities.isCreativeMode)
					{
						ItemStack itemstack1 = new ItemStack(Items.glass_bottle, 1, 0);

						if (!e.entityPlayer.inventory.addItemStackToInventory(itemstack1))
						{
							e.world.spawnEntityInWorld(new EntityItem(e.world, (double)e.x + 0.5D, (double)e.y + 1.5D, (double)e.z + 0.5D, itemstack1));
						}
						else if (e.entityPlayer instanceof EntityPlayerMP)
						{
							((EntityPlayerMP)e.entityPlayer).sendContainerToPlayer(e.entityPlayer.inventoryContainer);
						}
						--e.entityPlayer.inventory.getCurrentItem().stackSize;

						if (e.entityPlayer.inventory.getCurrentItem().stackSize <= 0)
						{
							e.entityPlayer.inventory.setInventorySlotContents(e.entityPlayer.inventory.currentItem, (ItemStack)null);
						}
					}
					e.world.setBlock(e.x,e.y,e.z, HoutouCore.houtouedCauldron, 0,2);
				}else if(e.entityPlayer.inventory.getCurrentItem().getItem()==MainTest.cookednoodles){
					if (!e.entityPlayer.capabilities.isCreativeMode)
					{
						ItemStack itemstack1 = new ItemStack(Items.glass_bottle, 1, 0);

						if (!e.entityPlayer.inventory.addItemStackToInventory(itemstack1))
						{
							e.world.spawnEntityInWorld(new EntityItem(e.world, (double)e.x + 0.5D, (double)e.y + 1.5D, (double)e.z + 0.5D, itemstack1));
						}
						else if (e.entityPlayer instanceof EntityPlayerMP)
						{
							((EntityPlayerMP)e.entityPlayer).sendContainerToPlayer(e.entityPlayer.inventoryContainer);
						}
						--e.entityPlayer.inventory.getCurrentItem().stackSize;

						if (e.entityPlayer.inventory.getCurrentItem().stackSize <= 0)
						{
							e.entityPlayer.inventory.setInventorySlotContents(e.entityPlayer.inventory.currentItem, (ItemStack)null);
						}
					}
					e.world.setBlock(e.x,e.y,e.z, HoutouCore.houtouedCauldron, 8,2);
				}
			}else if(b==HoutouCore.houtouedCauldron){
				EntityPlayer player=e.entityPlayer;
				ItemStack itemstack=player.inventory.getCurrentItem();
				World world=e.world;
				int x=e.x;
				int y=e.y;
				int z=e.z;
				int metadata=world.getBlockMetadata(x, y, z);
				int level=BlockHoutouedCauldron.func_150027_b(metadata);
				if (itemstack.getItem() == HoutouCore.itemHoutou)
                {
                    if (level < 3&&metadata<8)
                    {
                    	if (!player.capabilities.isCreativeMode)
                        {
                            ItemStack itemstack1 = new ItemStack(Items.glass_bottle, 1, 0);

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

                        HoutouCore.houtouedCauldron.func_150024_a(world, x, y, z, metadata+1);
                    }

                }else if (itemstack.getItem() == MainTest.cookednoodles)
                {
                    if (level < 3&&metadata>7)
                    {
                    	if (!player.capabilities.isCreativeMode)
                        {
                            ItemStack itemstack1 = new ItemStack(Items.glass_bottle, 1, 0);

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

                    	HoutouCore.houtouedCauldron.func_150024_a(world, x, y, z, metadata+1);
                    }

                }
			}
		}
	}
}
