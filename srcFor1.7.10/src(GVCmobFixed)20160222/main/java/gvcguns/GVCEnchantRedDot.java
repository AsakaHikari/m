package gvcguns;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class GVCEnchantRedDot extends Enchantment {
	public GVCEnchantRedDot(int id, int weight) {
		super(id, weight, EnumEnchantmentType.bow);
	}

	@Override
	public int getMinEnchantability(int par1) {
		return 10;
	}

	@Override
	public int getMaxEnchantability(int par1) {
		return super.getMinEnchantability(par1) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
}
