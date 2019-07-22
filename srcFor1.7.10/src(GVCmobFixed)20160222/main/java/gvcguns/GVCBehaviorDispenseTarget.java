package gvcguns;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GVCBehaviorDispenseTarget extends BehaviorProjectileDispense
{
	protected ItemStack fitemstack;

	@Override
	public ItemStack dispenseStack(IBlockSource par1iBlockSource, ItemStack par2ItemStack) {
		fitemstack = par2ItemStack;
		return super.dispenseStack(par1iBlockSource, par2ItemStack);
	}

	@Override
	protected IProjectile getProjectileEntity(World var1, IPosition var2) {
		return (IProjectile) new GVCEntityTarget(var1);
	}
}
