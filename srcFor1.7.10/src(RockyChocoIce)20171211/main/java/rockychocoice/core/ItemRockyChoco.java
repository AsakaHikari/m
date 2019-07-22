package rockychocoice.core;

import java.util.Set;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemRockyChoco extends ItemFood {

	public IIcon[] icons;
	public float damageBase;
	public ItemRockyChoco() {
		super(1,4,false);
		this.maxStackSize = 1;
        this.setMaxDamage(511);
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.damageBase =6.0f;
	}
	public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_)
    {
        p_77644_1_.damageItem(1, p_77644_3_);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_)
    {
        if ((double)p_150894_3_.getBlockHardness(p_150894_2_, p_150894_4_, p_150894_5_, p_150894_6_) != 0.0D)
        {
            p_150894_1_.damageItem(2, p_150894_7_);
        }

        return true;
    }
    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }
    
    public int getItemEnchantability()
    {
        return 16;
    }
    
    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.damageBase, 0));
        return multimap;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
    	this.icons=new IIcon[4];
        this.itemIcon = p_94581_1_.registerIcon(this.getIconString()+"_0");
        for(int i=0;i<4;i++){
        	this.icons[i]=p_94581_1_.registerIcon(this.getIconString()+"_"+i);
        }
    }
    
    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.icons[p_77617_1_/128];
    }
    
    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
    {
    	int hardnessLvl=EnchantmentHelper.getEnchantmentLevel(RockyChocoCore.hardness.effectId, p_77654_1_);
    	if(hardnessLvl>0){
    		p_77654_3_.attackEntityFrom(RockyChocoCore.tooHardIce, hardnessLvl);
    		p_77654_1_.damageItem(6-hardnessLvl,p_77654_3_);
    	}else{
    		p_77654_1_.damageItem(6,p_77654_3_);
    	}
    	
        p_77654_3_.getFoodStats().func_151686_a(this, p_77654_1_);
        p_77654_2_.playSoundAtEntity(p_77654_3_, "random.burp", 0.5F, p_77654_2_.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(p_77654_1_, p_77654_2_, p_77654_3_);
        return p_77654_1_;
    }
    

    public int func_150905_g(ItemStack p_150905_1_)
    {
        return 1+EnchantmentHelper.getEnchantmentLevel(RockyChocoCore.sweatness.effectId, p_150905_1_);
    }
}
