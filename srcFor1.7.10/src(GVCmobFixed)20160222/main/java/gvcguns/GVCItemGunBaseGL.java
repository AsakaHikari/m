package gvcguns;

import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;



	public class GVCItemGunBaseGL extends ItemBow {

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
	    
	    public int firetime;
	    public int firegrenadetime;
	    public int retimegrenade;

	    public static int firetype;
	    
		public int powor;
		public int powordart;
		public int poworfrag;
		public int poworsrag;
	    public float speed;
	    public float bure;
	    public float buresrag;
	    public double recoil;
	    
	    public float bureads;
	    public double recoilads;
	    
	    public static int bulletcount;
	    
	    public int aaa;
	    public static boolean grenadekey;
		
		public GVCItemGunBaseGL() {
			//iconNames = new String[] {"reload", ""};
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
		
		
		
		

	public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_)
    {
        
            return 15.0F;
        
        
    }
    
    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
    {
        return p_77654_1_;
    }
    
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        //return EnumAction.bow;
    	return null;
    }

    /**
     * How long it takes to use or consume an item
     */
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
		
		icons = new IIcon[6];
		icons[0] = par1IconRegister.registerIcon(this.getIconString() + "");

		
		icons[1] = par1IconRegister.registerIcon(this.getIconString() + "_dart");
		icons[2] = par1IconRegister.registerIcon(this.getIconString() + "_frag");
		icons[3] = par1IconRegister.registerIcon(this.getIconString() + "_srag");
		icons[4] = par1IconRegister.registerIcon(this.getIconString() + "_reload");
		icons[5] = par1IconRegister.registerIcon("gvcguns:null");
		itemIcon = icons[0];
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
			  return icons[5];
		  }else if (stack.getItemDamage() == this.getMaxDamage()){
			return icons[4];
		} else if(this.firetype==1){
			return icons[1];
		}else if(this.firetype==2){
			return icons[2];
		}else if(this.firetype==3){
			return icons[3];
		}else {
			return icons[0];
		}
	}
		
}
