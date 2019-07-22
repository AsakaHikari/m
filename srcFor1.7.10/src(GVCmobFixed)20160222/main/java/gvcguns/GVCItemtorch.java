package gvcguns;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;


	public class GVCItemtorch extends GVCItemGunBase {
		private float field_150934_a;
		
		public GVCItemtorch() {
			setMaxDamage(60);
			
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
	        this.field_150934_a = 0F;
	        
		}
		
		public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	    {
			EntityPlayer entityplayer = (EntityPlayer)entity;
			int s;
			int li = getMaxDamage() - itemstack.getItemDamage();
			boolean lflag = cycleBolt(itemstack);
			boolean var5 = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
			
			if (entity != null && entity instanceof EntityPlayer) {
				if (entityplayer.isUsingItem() && itemstack == entityplayer.getCurrentEquippedItem()) {
					
					if (lflag) {
						if(li>0)
						 {
						/*switch (this.bulletcount)
			             {
			                 case 0:
			                	 FireBullet(itemstack,world,entityplayer);
			            	     this.bulletcount = 1;
		                         break;
			                 case 1:
				            	 this.bulletcount = 0;
			                     break;
		                     
			             }*/
							FireBullet(itemstack,world,entityplayer);
							resetBolt(itemstack);
						GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
						 }
					}else{
						GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
					}
					
				}
			}
			
			super.onUpdate(itemstack, world, entity, i, flag);
	    }
		
		@Override
	    public byte getCycleCount(ItemStack pItemstack)
	    {
	        return 1/2;
	    }
		
		public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
			int s;
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

	        
	         if(par1ItemStack.getItemDamage() == this.getMaxDamage())
			 {
	        	 //this.isreload = 1;
			 }
	        	else	 
			 {
	        		//FireBullet(par1ItemStack,par2World,par3EntityPlayer);
	        		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			 }
			
	        return par1ItemStack;
	    }
				
        public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4){}

		public void FireBullet(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			//par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 1.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            GVCEntitygustorch var8 = new GVCEntitygustorch(par2World,(EntityLivingBase) par3EntityPlayer);
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
               }
            
		}
		/**
	     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	     */
	    public Multimap getItemAttributeModifiers()
	    {
	        Multimap var1 = super.getItemAttributeModifiers();
	        var1.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.field_150934_a, 0));
	        return var1;
	    }
}
