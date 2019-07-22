package gvcguns;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;



	public class GVCItemUZI extends GVCItemGunBase {
		private float field_150934_a;
		
		public GVCItemUZI() {
			//setMaxDamage(32);
			
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
	        this.field_150934_a = 2.0F;
	        //this.retime = 30;
	        this.powor = 6;
	        this.speed = 3f;
	        this.bure = 20f;
	        this.recoil = 1.0;
	        this.bureads = 4f;
	        this.recoilads = 0.6;
	        this.reloadtime = 30;
		}
		
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
		    par3List.add(EnumChatFormatting.YELLOW + "MagazimeType " + StatCollector.translateToLocal("HGMagazine"));
		    par3List.add(EnumChatFormatting.WHITE + "FirePowor " + StatCollector.translateToLocal("600"));
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
							if(entityplayer.isSneaking()){
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
		
		public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	    {
			int s;
			int li = getMaxDamage() - par1ItemStack.getItemDamage();
			boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

	        if (var5 || par3EntityPlayer.inventory.hasItem(GVCGunsPlus.fn_magazinehg))
	        {
	         if(par1ItemStack.getItemDamage() == this.getMaxDamage())
			 {
	        	 //this.isreload = 1;
			 }
	        	else	 
			 {
	        		//FireBullet(par1ItemStack,par2World,par3EntityPlayer);
	        		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			 }
			}
	        return par1ItemStack;
	    }
				
        public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4){}

		public void FireBullet(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) 
		{
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.firehg", 1.0F, 1.0F);

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
			par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.firehg", 1.0F, 1.0F);

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
			
			        par1ItemStack.damageItem(li, par3EntityPlayer);
					setDamage(par1ItemStack, -this.getMaxDamage());
					par3EntityPlayer.inventory.consumeInventoryItem(GVCGunsPlus.fn_magazinehg);
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
