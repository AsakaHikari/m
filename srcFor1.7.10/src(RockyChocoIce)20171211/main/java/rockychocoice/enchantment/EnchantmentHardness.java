package rockychocoice.enchantment;

import rockychocoice.core.RockyChocoCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;

public class EnchantmentHardness extends Enchantment {

	public EnchantmentHardness(int id, int weight,
			EnumEnchantmentType type) {
		super(id, weight, type);
		this.setName("hardness");
	}
	
	/**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int p_77321_1_)
    {
        return 15 + 6 * (p_77321_1_ - 1);
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int p_77317_1_)
    {
        return super.getMinEnchantability(p_77317_1_) + 50;
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 5;
    }
    

    public boolean canApply(ItemStack p_92089_1_)
    {
        return p_92089_1_.getItem()==RockyChocoCore.itemRockyChoco;
    }
    
    public float func_152376_a(int p_152376_1_, EnumCreatureAttribute p_152376_2_)
    {
        return p_152376_1_*1.5f;
    }

}
