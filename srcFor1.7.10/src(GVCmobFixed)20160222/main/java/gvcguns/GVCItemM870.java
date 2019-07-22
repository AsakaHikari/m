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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;


	public class GVCItemM870 extends GVCItemGunBaseSG {
		private float field_150934_a;
	    

		public GVCItemM870() {
			setMaxDamage(8);
			
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
	        this.field_150934_a = 2.0F;
	        //this.retime = 30;
	        this.powor = 5;
	        this.powordart = 3;
	        this.poworfrag = 8;
	        this.poworsrag = 20;
	        this.speed = 3f;
	        this.bure = 40f;
	        this.buresrag = 5f;
	        this.recoil = 2;
	        this.reloadtime = 50;
		}
		
		public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
		  {
			String powor = String.valueOf(this.powor + EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack));
			String speed = String.valueOf(this.speed);
			String bure = String.valueOf(this.bure);
			String recoil = String.valueOf(this.recoil);
			String retime = String.valueOf(this.reloadtime);
			String nokori = String.valueOf(getMaxDamage() - par1ItemStack.getItemDamage());
			
		    //super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
			if(this.firetype ==0){
				par3List.add(EnumChatFormatting.GRAY + "Firetype " + StatCollector.translateToLocal("BackShot"));
				 }else if(this.firetype ==1){
					 par3List.add(EnumChatFormatting.GOLD + "Firetype " + StatCollector.translateToLocal("Dart"));
        		}else if(this.firetype ==2){
        			par3List.add(EnumChatFormatting.WHITE + "Firetype " + StatCollector.translateToLocal("Frag"));
        		}else if(this.firetype ==3){
        			par3List.add(EnumChatFormatting.BLUE + "Firetype " + StatCollector.translateToLocal("Srag"));
        	}
			
			par3List.add(EnumChatFormatting.RED + "RemainingBullet " + StatCollector.translateToLocal(nokori));
		    par3List.add(EnumChatFormatting.WHITE + "FireDamege " + "+" + StatCollector.translateToLocal(powor));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpeed " + "+" + StatCollector.translateToLocal(speed));
		    par3List.add(EnumChatFormatting.WHITE + "BlletSpread "+ "+" + StatCollector.translateToLocal(bure));
		    par3List.add(EnumChatFormatting.WHITE + "Recoil " + "+" + StatCollector.translateToLocal(recoil));
		    par3List.add(EnumChatFormatting.YELLOW + "ReloadTime " + "+" + StatCollector.translateToLocal(retime));
		    par3List.add(EnumChatFormatting.YELLOW + "MagazimeType " + StatCollector.translateToLocal("ShotSell"));
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
			/*if(this.isreload == 1){
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
			}*/
            }
			
			super.onUpdate(itemstack, world, entity, i, flag);
	    }
		
		public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
	    {
			int s;
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			int j = getMaxItemUseDuration(par1ItemStack) - par4;
			float f = (float)j / 20F;
			f = (f * f + f * 2.0F) / 3F;
			boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

	        if (var5 || par3EntityPlayer.inventory.hasItem(GVCGunsPlus.fn_shell))
	        {
	         if(par1ItemStack.getItemDamage() == this.getMaxDamage())
			 {
	        	 //this.isreload = 1;
	        	 par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.reload", 1.0F, 1.0F);
			 }
	        	else	 
			 {
				 //if(!par3EntityPlayer.isSneaking())
	        		if( f > 0.2F){
					 if(this.firetype ==0){
	        		FireBullet(par1ItemStack,par2World,par3EntityPlayer);
					 }else if(this.firetype ==1){
						 FireBulletDart(par1ItemStack,par2World,par3EntityPlayer);
	        		}else if(this.firetype ==2){
	        			FireBulletFrag(par1ItemStack,par2World,par3EntityPlayer);
	        		}else if(this.firetype ==3){
	        			FireBulletSrag(par1ItemStack,par2World,par3EntityPlayer);
	        		}
	        		
				 }
			  }
			}
	        
	    }
		
		public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			
	        if (par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(GVCGunsPlus.fn_shell))
	        {
	        	/*if(par3EntityPlayer.isSneaking()){
	        		if(this.firetype<4){
	        		this.firetype = this.firetype +1;
	        		}else{
		        		this.firetype = 0;
		        	}
	        	}*/
	        	if(!(par1ItemStack.getItemDamage() == 0)){
	        	  /*try {
	        	   if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
				   {
				   Minecraft minecraft = Minecraft.getMinecraft();
				     if(minecraft.gameSettings.keyBindAttack.getIsKeyPressed()){
					  getReload(par1ItemStack,par2World,par3EntityPlayer);
				     }
				   }else{
					//MinecraftServer server = MinecraftServer.getServer();
					//server.
				   }
	        	   } catch (Error e) {
				}*/
	        		if(par3EntityPlayer.isSneaking()){
	        			getReload(par1ItemStack,par2World,par3EntityPlayer);
	        		}
	        		/*if(GVCGunsPlus.proxy.getCilentMinecraft().gameSettings.keyBindAttack.getIsKeyPressed()){
	        			getReload(par1ItemStack,par2World,par3EntityPlayer);
	        		}*/
	            }
	        	
	            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
	        }

	        return par1ItemStack;
	    }
		
		public void FireBullet(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.8F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            //GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, this.speed, this.bure);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            for(int i = 0; i < 8 ; ++i)
            {
            	int pluspower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
                GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor+pluspower, this.speed, this.bure);
              if (!par2World.isRemote)
              {
               par2World.spawnEntityInWorld(var8);
              }
            }
            
		}
		
		public void FireBulletDart(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.8F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            //GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, this.speed, this.bure);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            for(int i = 0; i < 8 ; ++i)
            {
            	int pluspower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
                GVCEntityBulletDart var8 = new GVCEntityBulletDart(par2World,(EntityLivingBase) par3EntityPlayer, this.powor+pluspower, this.speed, this.bure);
              if (!par2World.isRemote)
              {
               par2World.spawnEntityInWorld(var8);
              }
            }
            
		}
		
		public void FireBulletFrag(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.8F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            //GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, this.speed, this.bure);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            int pluspower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            GVCEntityBulletFrag var8 = new GVCEntityBulletFrag(par2World,(EntityLivingBase) par3EntityPlayer, this.poworfrag+pluspower, this.speed, this.buresrag);
              if (!par2World.isRemote)
              {
               par2World.spawnEntityInWorld(var8);
              }
            
            
		}
		
		public void FireBulletSrag(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.fire", 1.0F, 0.8F);

            par1ItemStack.damageItem(1, par3EntityPlayer);
            
            //GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.powor, this.speed, this.bure);
            par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * this.recoil;
            //resetBolt(par1ItemStack);
            int pluspower = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            GVCEntityBullet var8 = new GVCEntityBullet(par2World,(EntityLivingBase) par3EntityPlayer, this.poworsrag+pluspower, this.speed, this.buresrag);
              if (!par2World.isRemote)
              {
               par2World.spawnEntityInWorld(var8);
              }
            
            
		}
		
		public void getReload(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			
			        par1ItemStack.damageItem(-1, par3EntityPlayer);
					//setDamage(par1ItemStack, -1);
					//for(int i = 0; i < this.getMaxDamage() ; ++i)
			        boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
					
			        //par1ItemStack.damageItem(li, par3EntityPlayer);
					//setDamage(par1ItemStack, -this.getMaxDamage());
					if (!linfinity) {
					par3EntityPlayer.inventory.consumeInventoryItem(GVCGunsPlus.fn_shell);
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
