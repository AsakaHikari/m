package gvcguns;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Multimap;

import cpw.mods.fml.client.FMLClientHandler;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.inventory.Slot;


	public class GVCItemBayonet extends Item {
		private float field_150934_a;
		
		public GVCItemBayonet() {
			setMaxDamage(30);
			
			this.maxStackSize = 1;
			setCreativeTab(GVCGunsPlus.tabgvc);
	        this.field_150934_a = 5.0F;
	       
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
	        return EnumAction.bow;
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
		
	    public Multimap getItemAttributeModifiers()
	    {
	        Multimap var1 = super.getItemAttributeModifiers();
	        var1.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.field_150934_a, 0));
	        return var1;
	    }
}
