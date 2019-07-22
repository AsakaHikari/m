package gvcguns;

import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;


	public class GVCItemBox extends Item {

		public GVCItemBox() {
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
		}
		
		@Override
	    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
	     par3EntityPlayer.capabilities.allowFlying = !par3EntityPlayer.capabilities.allowFlying;
	     return par1ItemStack;
	    }
		
		public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	    {
			EntityPlayer entityplayer = (EntityPlayer)entity;
			int s;
			int li = getMaxDamage() - itemstack.getItemDamage();
			boolean var5 = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
			
			//world.getGameRules().getGameRuleBooleanValue("keepInventory");
			
			super.onUpdate(itemstack, world, entity, i, flag);
	    }
		
		/*public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
			//FMLClientHandler.instance().showGuiScreen(player);
			//player.openGui(GVCGunsPlus.INSTANCE, GVCGunsPlus.GUI_ID, world, MathHelper.ceiling_double_int(player.posX), MathHelper.ceiling_double_int(player.posY), MathHelper.ceiling_double_int(player.posZ));
		    return itemStack;
		}
		
		
		/*	
		public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	    {
		    for (int ix = 0; ix < 3; ++ix){
			 for (int iy = 0; iy < 3; ++iy){
    		 for (int iz = 0; iz < 3; ++iz){
			Block pBlock = par3World.getBlock(par4-1+ix, par5-1+iy, par6-1+iz);
			int pMetadata = par3World.getBlockMetadata(par4-1+ix, par5-1+iy, par6-1+iz);
			removeBlock(par4-1+ix, par5-1+iy, par6-1+iz, pBlock, pMetadata,par3World);
			pBlock.onBlockDestroyedByPlayer(par3World, par4-1+ix, par5-1+iy, par6-1+iz, pMetadata);
			pBlock.dropBlockAsItem(par3World, par4-1+ix, par5-1+iy, par6-1+iz, pMetadata, pMetadata);
			
			
    		 }
    		 }
			}
			
			return true;
	    }
		protected void removeBlock(int pX, int pY, int pZ, Block pBlock, int pMetadata, World par3World) {
			par3World.playAuxSFX(2001, pX, pY, pZ, Block.getIdFromBlock(pBlock) + (pMetadata << 12));
			par3World.setBlockToAir(pX, pY, pZ);	
		}
		
		/*public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
			//par3EntityPlayer.displayGUIWorkbench(MathHelper.ceiling_double_int(par3EntityPlayer.posX), MathHelper.ceiling_double_int(par3EntityPlayer.posY), MathHelper.ceiling_double_int(par3EntityPlayer.posZ));
			//par3EntityPlayer.openGui(mod, modGuiId, world, x, y, z)
			return par1ItemStack;
	    }*/
		
}
	