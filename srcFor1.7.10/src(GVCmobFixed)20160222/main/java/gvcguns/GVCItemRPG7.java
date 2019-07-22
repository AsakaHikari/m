package gvcguns;

import java.util.List;

import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
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
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.GuiIngameForge;


	public class GVCItemRPG7 extends GVCItemGunBaseGL {
		private float field_150934_a;
	    

		public GVCItemRPG7() {
			setMaxDamage(1);
			
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
	        this.field_150934_a = 0F;
	        //this.retime = 30;
	        this.powor = 99;
	        this.powordart = 120;
	        this.poworfrag = 50;
	        this.poworsrag = 20;
	        this.speed = 3f;
	        this.bure = 5f;
	        this.recoil = 0.5;
	        this.reloadtime = 50;
		}
		
		public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
		  {
			String powor = String.valueOf(this.powor);
			String speed = String.valueOf(this.speed);
			String bure = String.valueOf(this.bure);
			String recoil = String.valueOf(this.recoil);
			String retime = String.valueOf(this.reloadtime);
			String nokori = String.valueOf(getMaxDamage() - par1ItemStack.getItemDamage());
			
			
			if(this.firetype ==0){
				par3List.add(EnumChatFormatting.GRAY + "GrenadType " + StatCollector.translateToLocal("HEAT"));
				 }else if(this.firetype ==1){
					 par3List.add(EnumChatFormatting.GOLD + "GrenadType " + StatCollector.translateToLocal("Tandem"));
      		}else if(this.firetype ==2){
      			par3List.add(EnumChatFormatting.GREEN + "GrenadType " + StatCollector.translateToLocal("FB"));
      		}else if(this.firetype ==3){
      			par3List.add(EnumChatFormatting.YELLOW + "GrenadType " + StatCollector.translateToLocal("ThermoBaric"));
      	}
			par3List.add(EnumChatFormatting.RED + "RemainingBullet " + StatCollector.translateToLocal(nokori));
		    par3List.add(EnumChatFormatting.WHITE + "FireDamege " + "+" + StatCollector.translateToLocal(powor));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpeed " + "+" + StatCollector.translateToLocal(speed));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpread "+ "+" + StatCollector.translateToLocal(bure));
		    par3List.add(EnumChatFormatting.WHITE + "Recoil " + "+" + StatCollector.translateToLocal(recoil));
		    par3List.add(EnumChatFormatting.YELLOW + "ReloadTime " + "+" + StatCollector.translateToLocal(retime));
		    par3List.add(EnumChatFormatting.YELLOW + "MagazimeType " + StatCollector.translateToLocal("RPGBullet"));
		    //par3List.add(EnumChatFormatting.WHITE + "FirePowor " + StatCollector.translateToLocal("600"));
		  }
		
		public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	    {
			EntityPlayer entityplayer = (EntityPlayer)entity;
			int s;
			int li = getMaxDamage() - itemstack.getItemDamage();
			boolean var5 = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
			
			if(itemstack.getItemDamage() == this.getMaxDamage())
			 {
	        	 this.isreload = 1;
			 }
			if (!world.isRemote)
            {
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
		
		public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
	    {
			int s;
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

	        if (var5 || par3EntityPlayer.inventory.hasItem(GVCGunsPlus.fn_rpg))
	        {
	         if(par1ItemStack.getItemDamage() == this.getMaxDamage())
			 {
	        	 //this.isreload = 1;
	        	 par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.reload", 1.0F, 1.0F);
			 }
	        	else	 
			 {
				 
	        		if(this.firetype ==0){
	      				  FireBulletVL(par1ItemStack,par2World,par3EntityPlayer);
	    					 }else if(this.firetype ==1){
	    						FireBulletVR(par1ItemStack,par2World,par3EntityPlayer);
	    	        		}else if(this.firetype ==2){
	    	        			FireBulletVH(par1ItemStack,par2World,par3EntityPlayer);
	    	        		}else if(this.firetype ==3){
	    	        			FireBulletVS(par1ItemStack,par2World,par3EntityPlayer);
	    	        		}
			  }
			}
	        
	    }
		
		public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			
	        if (par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(GVCGunsPlus.fn_rpg))
	        {
	        	
	            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
	        }

	        return par1ItemStack;
	    }
		
		public void FireBulletVL(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            GVCEntityRPG var8 = new GVCEntityRPG(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, this.speed-1, this.bure, 3.0F, false, true);
            //GVCEntityMissile var8 = new GVCEntityMissile(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, 0.8F, this.bure, 3.0F, false, true);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
                par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY+2, par3EntityPlayer.posZ, 0.0F, false);
               }
            
		}
		
		public void FireBulletVR(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            GVCEntityRPGVR var8 = new GVCEntityRPGVR(par2World,(EntityLivingBase) par3EntityPlayer, this.powordart, this.speed-1.5F, this.bure, 2.5F, false, true);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
                par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY+2, par3EntityPlayer.posZ, 0.0F, false);
               }
            
		}
		
		public void FireBulletVH(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            GVCEntityRPG var8 = new GVCEntityRPG(par2World,(EntityLivingBase) par3EntityPlayer, this.poworfrag, this.speed, this.bure, 3.0F, true, false);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
                par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY+2, par3EntityPlayer.posZ, 0.0F, false);
               }
            
		}
		
		public void FireBulletVS(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            GVCEntityRPG var8 = new GVCEntityRPG(par2World,(EntityLivingBase) par3EntityPlayer, this.poworsrag, this.speed-1.5F, this.bure, 4.0F, false, false);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            
               if (!par2World.isRemote)
               {
                par2World.spawnEntityInWorld(var8);
                par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY+2, par3EntityPlayer.posZ, 0.0F, false);
               }
            
		}
		
		public void getReload(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
			
	        par1ItemStack.damageItem(li, par3EntityPlayer);
			setDamage(par1ItemStack, -this.getMaxDamage());
			if (!linfinity) {
			par3EntityPlayer.inventory.consumeInventoryItem(GVCGunsPlus.fn_rpg);
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
