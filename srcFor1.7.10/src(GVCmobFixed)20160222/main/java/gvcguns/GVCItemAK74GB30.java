package gvcguns;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.GuiIngameForge;


	public class GVCItemAK74GB30 extends GVCItemGunBaseGL {
		private float field_150934_a;
		
		public GVCItemAK74GB30() {
			setMaxDamage(30);
			
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
	        this.field_150934_a = 2.0F;
	        //this.retime = 30;
	        this.powor = 8;
	        this.powordart = 3;
	        this.speed = 4f;
	        this.bure = 15f;
	        this.recoil = 1.0;
	        this.bureads = 3f;
	        this.recoilads = 0.5;
	        this.reloadtime = 50;
		}
		
		@SideOnly(Side.CLIENT)
	    public boolean hasEffect(ItemStack par1ItemStack)
	    {
			if(grenadekey == true){
	        return true;
			}else{
				return false;
			}
	    }
		
		public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
		  {
			String powor = String.valueOf(this.powor + EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack));
			String speed = String.valueOf(this.speed);
			String bure = String.valueOf(this.bure);
			String recoil = String.valueOf(this.recoil);
			String retime = String.valueOf(this.reloadtime);
			String nokori = String.valueOf(getMaxDamage() - par1ItemStack.getItemDamage());
			
			if(this.firetype ==0){
				par3List.add(EnumChatFormatting.GRAY + "GrenadType " + StatCollector.translateToLocal("HE"));
				 }else if(this.firetype ==1){
					 par3List.add(EnumChatFormatting.GOLD + "GrenadType " + StatCollector.translateToLocal("Dart"));
        		}else if(this.firetype ==2){
        			par3List.add(EnumChatFormatting.GREEN + "GrenadType " + StatCollector.translateToLocal("LVG"));
        		}else if(this.firetype ==3){
        			par3List.add(EnumChatFormatting.YELLOW + "GrenadType " + StatCollector.translateToLocal("FB"));
        	}
            
			par3List.add(EnumChatFormatting.RED + "RemainingBullet " + StatCollector.translateToLocal(nokori));
		    par3List.add(EnumChatFormatting.WHITE + "FireDamege " + "+" + StatCollector.translateToLocal(powor));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpeed " + "+" + StatCollector.translateToLocal(speed));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpread "+ "+" + StatCollector.translateToLocal(bure));
		    par3List.add(EnumChatFormatting.WHITE + "Recoil " + "+" + StatCollector.translateToLocal(recoil));
		    par3List.add(EnumChatFormatting.YELLOW + "ReloadTime " + "+" + StatCollector.translateToLocal(retime));
		    par3List.add(EnumChatFormatting.YELLOW + "MagazimeType " + StatCollector.translateToLocal("ARMagazine&RPGBullet"));
		    par3List.add(EnumChatFormatting.WHITE + "FirePowor " + StatCollector.translateToLocal("600"));
		  }
		
	    /*public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	    {
	    	if(aaa != 1){
	    		grenadekey = true;
	    		aaa = aaa +1;
	    	}else if(aaa == 1){
	    		grenadekey = false;
	    		aaa = 0;
	    	}
	    	
	        return false;
	    }
	    /*public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player)
	    {
	    	if(aaa != 1){
	    		grenadekey = true;
	    		aaa = aaa +1;
	    	}else if(aaa == 1){
	    		grenadekey = false;
	    		aaa = 0;
	    	}
	        return false;
	    }*/
	    
		
		public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	    {
			EntityPlayer entityplayer = (EntityPlayer)entity;
			int s;
			int li = getMaxDamage() - itemstack.getItemDamage();
			boolean lflag = cycleBolt(itemstack);
			boolean var5 = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
			
			if (entity != null && entity instanceof EntityPlayer) {
				if (entityplayer.isUsingItem() && itemstack == entityplayer.getCurrentEquippedItem()) {
					if(grenadekey == false){
					if (lflag) {
						if(li>0)
						 {
							if(entityplayer.isSneaking()|| GVCGunsPlus.adstype == 1){
								FireBulletADS(itemstack,world,entityplayer);
							}else{
								FireBullet(itemstack,world,entityplayer);
							}
							resetBolt(itemstack);
						GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
						 }
						else{
							 GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
						 }
					}else{
						GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
					}
					}
					
					
					
				}
			}
			
			if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
			{
			Minecraft minecraft = Minecraft.getMinecraft();
			  if(minecraft.gameSettings.keyBindAttack.getIsKeyPressed()){
			    grenadekey = true;
			  }else{
				grenadekey = false;
			  }
			}
			
			
			if(itemstack.getItemDamage() == this.getMaxDamage())
			 {
	        	 this.isreload = 1;
			 }
			if (!world.isRemote)
            {
			//if(this.isreload == 1 ||GVCItemGunBase.isreload == 1){
			if(this.isreload == 1){
				this.isreload = 0;
			if (entity != null && entity instanceof EntityPlayer)
            {
			     if(flag){
					 {
			    		 ++this.retime;
			        	 if(this.retime == this.reloadtime){
			        		 this.retime = 0;
								
			        		 getReload(itemstack,world,entityplayer);
								
			        	 }
					 }
			     }
            }
			}
            }
			
			
			
			
			
			super.onUpdate(itemstack, world, entity, i, flag);
	    }
		
		@Override
	    public byte getCycleCount(ItemStack pItemstack)
	    {
	        return 1;
	    }
		
		public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4){}
		
		public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
			int s;
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

			
	        if (var5 || par3EntityPlayer.inventory.hasItem(GVCGunsPlus.fn_magazine))
	        {
	          if(par1ItemStack.getItemDamage() == this.getMaxDamage())
			  {
	        	 //this.isreload = 1;
			  }
	          else{
	        	  //if(par3EntityPlayer.isSneaking() == true)
				  { 
		        	  if (var5 || par3EntityPlayer.inventory.hasItem(GVCGunsPlus.fn_rpg))
		  	        {
		        		  if(grenadekey == true){
		        	 //FireGrenadeBullet(par1ItemStack,par2World,par3EntityPlayer);
		        			  if(this.firetype ==0){
		        				  FireGrenadeBullet(par1ItemStack,par2World,par3EntityPlayer);
		      					 }else if(this.firetype ==1){
		      						FireGrenadeBulletDart(par1ItemStack,par2World,par3EntityPlayer);
		      	        		}else if(this.firetype ==2){
		      	        			FireGrenadeBulletLVG(par1ItemStack,par2World,par3EntityPlayer);
		      	        		}else if(this.firetype ==3){
		      	        			FireGrenadeBulletFB(par1ItemStack,par2World,par3EntityPlayer);
		      	        		}
		        	 par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		        		  }
		  	        }
				  }
				 //FireBullet(par1ItemStack,par2World,par3EntityPlayer);
				  par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			  }
			}
	        return par1ItemStack;
	    }
				
		public void FireBullet(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 1.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            int pluspower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            
            
            GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor+pluspower, this.speed, this.bure);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
               }
            
		}
		
		public void FireBulletADS(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 1.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            int pluspower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            
            
            GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor+pluspower, this.speed, this.bureads);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoilads;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
               }
            
		}
		
		
		public void FireGrenadeBullet(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			//par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 1.5F);
			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 0.5F);

            //par1ItemStack.damageItem(1, par3EntityPlayer);
			boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
			if (!linfinity) {
			par3EntityPlayer.inventory.consumeInventoryItem(GVCGunsPlus.fn_rpg);
			}
            GVCEntityGurenadeBullet var8 = new GVCEntityGurenadeBullet(par2World,(EntityLivingBase) par3EntityPlayer);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
               }
               //this.firegrenadetime = 1;
		}
		
		public void FireGrenadeBulletDart(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.8F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            //GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, this.speed, this.bure);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            for(int i = 0; i < 8 ; ++i)
            {
            	GVCEntityBulletDart var8 = new GVCEntityBulletDart(par2World,(EntityLivingBase) par3EntityPlayer, this.powordart, this.speed, this.bureads*10);
              if (!par2World.isRemote)
              {
               par2World.spawnEntityInWorld(var8);
              }
            }
            
		}
		
		public void FireGrenadeBulletLVG(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			//par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 1.5F);
			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 0.5F);

            //par1ItemStack.damageItem(1, par3EntityPlayer);
			boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
			if (!linfinity) {
			par3EntityPlayer.inventory.consumeInventoryItem(GVCGunsPlus.fn_rpg);
			}
            GVCEntityGurenadeBulletLVG var8 = new GVCEntityGurenadeBulletLVG(par2World,(EntityLivingBase) par3EntityPlayer);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
               }
               //this.firegrenadetime = 1;
		}
		
		public void FireGrenadeBulletFB(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			//par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 1.5F);
			par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 0.5F);

            //par1ItemStack.damageItem(1, par3EntityPlayer);
			boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
			if (!linfinity) {
			par3EntityPlayer.inventory.consumeInventoryItem(GVCGunsPlus.fn_rpg);
			}
            GVCEntityGurenadeBulletFB var8 = new GVCEntityGurenadeBulletFB(par2World,(EntityLivingBase) par3EntityPlayer);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
               }
               //this.firegrenadetime = 1;
		}
		
		public void getReload(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
			
	        par1ItemStack.damageItem(li, par3EntityPlayer);
			setDamage(par1ItemStack, -this.getMaxDamage());
			if (!linfinity) {
			par3EntityPlayer.inventory.consumeInventoryItem(GVCGunsPlus.fn_magazine);
			}
					//par2World.playSoundAtEntity(par3EntityPlayer, "random.click", 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
					par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.reload", 1.0F, 1.0F);
		 
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
