package gvcguns;



import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
//import net.minecraft.client.Minecraft;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;
import net.minecraftforge.event.ForgeEventFactory;



	public class GVCItemGunBase extends ItemBow {

		public static final String[] bowPullIconNameArray = new String[] {"reload", "reload", "0"};
		public String[] iconNames;
		protected IIcon[] icons;
	    @SideOnly(Side.CLIENT)
	    private IIcon[] iconArray;
		
	    //private final Item.ToolMaterial field_150933_b;
	    private static final String __OBFID = "CL_00000072";
	    public static final String Tag_Cycle		= "Cycle";
	    public static final String Tag_CycleCount	= "CycleCount";
	    
	    public float f;
	    public int j;
	    
	    public int isreload;
	    public int retime;
	    public int reloadtime;
	    
	    public static int maxdamege;
	    
	    public static boolean canreload;
	    
	    public int firetime;
	    public int firegrenadetime;
	    public int retimegrenade;

		
		public int powor;
		public int powordart;
		public int poworfrag;
		public int poworsrag;
	    public float speed;
	    public float bure;
	    public double recoil;
	    public float scopezoom;
	    
	    public float bureads;
	    public double recoilads;
	    
	    public static int bulletcount;
	    
	    public static String ads;
	    
	    public int aaa;
	    public static boolean grenadekey;
		
		public GVCItemGunBase() {
			//iconNames = new String[] {"reload", ""};
			//maxdamege = this.getMaxDamage();
		}
		
		public boolean checkTags(ItemStack pitemstack) {
			if (pitemstack.hasTagCompound()) {
				return true;
			}
			NBTTagCompound ltags = new NBTTagCompound();
			pitemstack.setTagCompound(ltags);
			ltags.setInteger("Reload", 0x0000);
			ltags.setByte("Bolt", (byte)0);
			NBTTagCompound lammo = new NBTTagCompound();
			for (int li = 0; li < getMaxDamage(); li++) {
				lammo.setLong(Integer.toString(li), 0L);
			}
			//ltags.setCompoundTag("Ammo", lammo);
			return false;
		}
		
		protected boolean cycleBolt(ItemStack pItemstack) {
			checkTags(pItemstack);
			NBTTagCompound lnbt = pItemstack.getTagCompound();
			byte lb = lnbt.getByte("Bolt");
			if (lb <= 0) {
//				if (pReset) resetBolt(pItemstack);
				return true;
			} else {
				lnbt.setByte("Bolt", --lb);
				return false;
			}
		}
		
		protected void resetBolt(ItemStack pItemstack) {
			checkTags(pItemstack);
			pItemstack.getTagCompound().setByte("Bolt", getCycleCount(pItemstack));
		}
		
		public byte getCycleCount(ItemStack pItemstack) {
			return (byte)1;
		}
		
		public static void updateCheckinghSlot(Entity pEntity, ItemStack pItemstack) {
			if (pEntity instanceof EntityPlayerMP) {
				EntityPlayerMP lep = (EntityPlayerMP)pEntity;
				Container lctr = lep.openContainer;
				for (int li = 0; li < lctr.inventorySlots.size(); li++) {
					ItemStack lis = ((Slot)lctr.getSlot(li)).getStack(); 
					if (lis == pItemstack) {
						lctr.inventoryItemStacks.set(li, pItemstack.copy());
						break;
					}
				}
			}
		}
		
		
		
		
		/**/

		
		
    
		/*private void renderPumpkinBlur(int par1, int par2)
	    {
			Minecraft minecraft = FMLClientHandler.instance().getClient();
			
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        GL11.glDepthMask(false);
	        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_ALPHA_TEST);
	        minecraft.getTextureManager().bindTexture(new ResourceLocation("gvcguns:textures/misc/Horo.png"));
	        Tessellator var3 = Tessellator.instance;
	        var3.startDrawingQuads();
	        var3.addVertexWithUV(0.0D, (double)par2, -90.0D, 0.0D, 1.0D);
	        var3.addVertexWithUV((double)par1, (double)par2, -90.0D, 1.0D, 1.0D);
	        var3.addVertexWithUV((double)par1, 0.0D, -90.0D, 1.0D, 0.0D);
	        var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
	        var3.draw();
	        GL11.glDepthMask(true);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glEnable(GL11.GL_ALPHA_TEST);
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    }*/
    
    /*public float func_150931_i()
    {
        return this.field_150933_b.getDamageVsEntity();
    }*/
		
		/*
		@SideOnly(Side.CLIENT)(鬯ｮ�ｱ�ｽ�ｽJavadoc)
		@see net.minecraft.item.Item#onUpdate(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.Entity, int, boolean)
		@Override
	    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	    {
			Minecraft minecraft = FMLClientHandler.instance().getClient();
			if(flag){
			 if(entity.isSneaking()){
				if (world.isRemote)
				   {
				      //ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 8.0D, "cameraZoom", "field_78503_V");
					
						/*ScaledResolution var21 = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
						int par1 = var21.getScaledWidth();
						int par2 = var21.getScaledHeight();
						
						this.renderPumpkinBlur(par1, par2);
						 
				   }
				 /*if (world.isRemote){
				ScaledResolution var21 = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
				int x1 = var21.getScaledWidth();
				int y1 = var21.getScaledHeight();
				
				GL11.glEnable(GL11.GL_BLEND);
            	GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                minecraft.getTextureManager().bindTexture(new ResourceLocation("gvcguns:textures/misc/Horo.png"));
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(0.0D, (double)y1, -90.0D, 0.0D, 1.0D);
                tessellator.addVertexWithUV((double)x1, (double)y1, -90.0D, 1.0D, 1.0D);
                tessellator.addVertexWithUV((double)x1, 0.0D, -90.0D, 1.0D, 0.0D);
                tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
                tessellator.draw();
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				 }
			 }else{
				 if (world.isRemote)
				   {
				      ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, minecraft.entityRenderer, 1.0D, "cameraZoom", "field_78503_V");
				   }
			 }
			}
			super.onUpdate(itemstack, world, entity, i, flag);
	    }
	    */
		
		/*@Override
	    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
	    {
			if (entity != null && entity instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer)entity;
				if (entityplayer.isUsingItem() && itemstack == entityplayer.getCurrentEquippedItem()) {
					if (flag) {
						if (world.rand.nextInt(1) == 0)
				        {
						this.onItemRightClick(itemstack, world, entityplayer);
				        }
					}
				}
			}
			super.onUpdate(itemstack, world, entity, i, flag);
	    }*/
		
		


	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_)
    {
        
            return 1.0F;
        
        
    }
    
    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /*public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
    {
    	if(p_77654_1_.getItemDamage() == this.getMaxDamage()){
    		p_77654_3_.setItemInUse(p_77654_1_, getMaxItemUseDuration(p_77654_1_));
    	}
    	
        return p_77654_1_;
    }*/
    
    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
    {
        return p_77654_1_;
    }
    
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
      /*Entity entity = Minecraft.getMinecraft().renderViewEntity;
  	  EntityPlayer entityplayer = (EntityPlayer)entity;
	  if(entityplayer.isSneaking()){
        return null;
	  }else{
		  return EnumAction.bow;
	  }*/
    	if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{
    		Entity entity = Minecraft.getMinecraft().renderViewEntity;
    	  	  EntityPlayer entityplayer = (EntityPlayer)entity;
    		  if(entityplayer.isSneaking()|| GVCGunsPlus.adstype == 1){
    	        return null;
    		  }else{
    			  return EnumAction.bow;
    		  }
		}else{
			if(par1ItemStack.getItemDamage() == this.getMaxDamage()){
	    		return EnumAction.block;
	    	    }else{
	    	    return null;
	    	    }
		}
    }

    /**
     * How long it takes to use or consume an item
     */
    /*public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
    	if(par1ItemStack.getItemDamage() == this.getMaxDamage()){
    		return 20;
    		
    	}
    	return 7200;
    	
    }*/
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
    	return 7200;
    }

    

    public boolean func_150897_b(Block p_150897_1_)
    {
        return p_150897_1_ == Blocks.web;
    }

    @SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
    	if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
		{
		icons = new IIcon[3];
		icons[0] = par1IconRegister.registerIcon(this.getIconString() + "");
		icons[1] = par1IconRegister.registerIcon(this.getIconString() + "_reload");
		icons[2] = par1IconRegister.registerIcon("gvcguns:null");
		itemIcon = icons[0];
		}
	}

    @SideOnly(Side.CLIENT)
	public IIcon getItemIconForUseDuration(int par1) {
		
		return itemIcon;
	}

    @SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		//if(f >3f){
			//return icons[0];
	    //}else
    	Entity entity = Minecraft.getMinecraft().renderViewEntity;
	  	  EntityPlayer entityplayer = (EntityPlayer)entity;
		  if(entityplayer.isSneaking()|| GVCGunsPlus.adstype == 1){
			  return icons[2];
		  }else if (stack.getItemDamage() == this.getMaxDamage()){
			  return icons[1];
			} else {
				return icons[0];
			}
	}
	/*public TileEntity createNewTileEntity(World world, int a) {
 
		return new GVCTileEntityItemG36();
	}*/
}
