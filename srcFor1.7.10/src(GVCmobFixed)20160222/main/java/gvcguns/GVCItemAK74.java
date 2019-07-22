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


	public class GVCItemAK74 extends GVCItemGunBase {
		private float field_150934_a;
		public static String ads;
		
		public GVCItemAK74(int p, float s, float b, double r, int rt, float at, float cz, String adss) {
			super();
			
			
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
	        this.field_150934_a = at;
	        //this.retime = 30;
	        this.powor = p;
	        this.speed = s;
	        this.bure = b;
	        this.recoil = r;
	        this.bureads = b/5;
	        this.recoilads = r/2;
	        this.reloadtime = rt;
	        this.scopezoom = cz;
	        this.ads = adss;
		}
		
		/*public void addInformation(ItemStack itemstack, List list)
		{
			NBTTagCompound nbt = itemstack.getTagCompound();
			if(nbt == null)
				return;
			
			powor = nbt.getInteger("PosX");
			speed = nbt.getFloat("PosY");
			reloadtime = nbt.getInteger("PosZ");
			list.add("[" + powor + "," + speed + "," + reloadtime + "]");
		}*/

		public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
		  {
			String powor = String.valueOf(this.powor + EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack));
			String speed = String.valueOf(this.speed);
			String bure = String.valueOf(this.bure);
			String recoil = String.valueOf(this.recoil);
			String retime = String.valueOf(this.reloadtime);
            String nokori = String.valueOf(getMaxDamage() - par1ItemStack.getItemDamage());
			
			par3List.add(EnumChatFormatting.RED + "RemainingBullet " + StatCollector.translateToLocal(nokori));
		    par3List.add(EnumChatFormatting.WHITE + "FireDamege " + "+" + StatCollector.translateToLocal(powor));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpeed " + "+" + StatCollector.translateToLocal(speed));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpread "+ "+" + StatCollector.translateToLocal(bure));
		    par3List.add(EnumChatFormatting.WHITE + "Recoil " + "+" + StatCollector.translateToLocal(recoil));
		    par3List.add(EnumChatFormatting.YELLOW + "ReloadTime " + "+" + StatCollector.translateToLocal(retime));
		    par3List.add(EnumChatFormatting.YELLOW + "MagazimeType " + StatCollector.translateToLocal("ARMagazine"));
		    if(!(this.scopezoom == 1.0f)){
				String scopezoom = String.valueOf(this.scopezoom);
				par3List.add(EnumChatFormatting.WHITE + "ScopeZoom " + "x" + StatCollector.translateToLocal(scopezoom));
			}
		    par3List.add(EnumChatFormatting.WHITE + "FirePowor " + StatCollector.translateToLocal("600"));
		  }
		
		
		
		public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	    {
			EntityPlayer entityplayer = (EntityPlayer)entity;
			int s;
			int li = getMaxDamage() - itemstack.getItemDamage();
			boolean lflag = cycleBolt(itemstack);
			boolean var5 = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
			Item item = itemstack.getItem();
			
			if (entity != null && entity instanceof EntityPlayer) {
				if (entityplayer.isUsingItem() && itemstack == entityplayer.getCurrentEquippedItem()) {
				//if (itemstack == entityplayer.getCurrentEquippedItem()) {
					//if(itemstack =! entityplayer.isUsingItem()){
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
						 }else{
							 GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
						 }
					}else{
						GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
					}
				//}
				}
			}
			
			boolean laser = EnchantmentHelper.getEnchantmentLevel(GVCGunsPlus.lasersight.effectId, itemstack) > 0;
			if(flag){
				if (laser) {
				GVCEntityLaser var8 = new GVCEntityLaser(world,(EntityLivingBase) entityplayer);
	               if (!world.isRemote)
	               {
	                world.spawnEntityInWorld(var8);
	               }
				}
			}
			
			/*if(itemstack == entityplayer.getCurrentEquippedItem()) {
				if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
				{
				Minecraft minecraft = Minecraft.getMinecraft();
				if(flag){
				 if(entity.isSneaking()){
					 GuiIngameForge.renderCrosshairs = false;
				   }else{
						 GuiIngameForge.renderCrosshairs = true;
				   }
				  }
		        }
			}*/
			
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
			
			/*if(itemstack == entityplayer.getCurrentEquippedItem()) {
				if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
				{
				Minecraft minecraft = Minecraft.getMinecraft();
				if(flag){
				 if(entity.isSneaking()){
					 
					if (world.isRemote)
					   {
					      ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 1.0D*this.scopezoom, "cameraZoom", "field_78503_V"); 
						//minecraft.field_71474_y.field_74334_X = 8.0F;
						//minecraft.gameSettings.fovSetting = 70.0F / this.scopezoom;
						GuiIngameForge.renderCrosshairs = false;
					   }
					 
				   }else{
					 if (world.isRemote)
					   {
					      ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 1.0D, "cameraZoom", "field_78503_V");
						 //GVCEntityRenderTest.cameraZoom = 1.0D;
						 //minecraft.gameSettings.fovSetting = 70.0F;
						 GuiIngameForge.renderCrosshairs = true;
					   }
				   }
				  }
		        }
			}*/
			
			
			super.onUpdate(itemstack, world, entity, i, flag);
	    }
		
		@Override
	    public byte getCycleCount(ItemStack pItemstack)
	    {
	        return 1;
	    }
		
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
	        	 /*++this.retime;
	        	 if(this.retime == this.reloadtime){
	        		 this.retime = 0;
						
	        		 getReload(par1ItemStack,par2World,par3EntityPlayer);
						
	        	 }*/
	        	// par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			 }
	        	else	 
			 {
	        		
	        		//FireBullet(par1ItemStack,par2World,par3EntityPlayer);
	        		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			 }
			}
	        return par1ItemStack;
	    }
				
        public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4){
        	/*int s;
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			int j = getMaxItemUseDuration(par1ItemStack) - par4;
			float f = (float)j / 20F;
			f = (f * f + f * 2.0F) / 3F;
			
			if(par1ItemStack.getItemDamage() == this.getMaxDamage()){
			   if(f >= getMaxItemUseDuration(par1ItemStack)){
				   getReload(par1ItemStack,par2World,par3EntityPlayer);
				   return;
			   } 
			}*/
        }

		public void FireBullet(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 1.5F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            int pluspower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor+pluspower, this.speed, this.bure);
            //GVCEntityBullettest var8 = new GVCEntityBullettest(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, this.speed, this.bure);
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
					//return par1ItemStack;
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
