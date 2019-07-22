package satisfybyone;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityManzocCart extends EntityMinecart {
	private static final String __OBFID = "CL_00001677";

	public EntityManzocCart(World p_i1722_1_) {
		super(p_i1722_1_);
		maxSpeedAirLateral = 0.8f;
	}

	public EntityManzocCart(World p_i1723_1_, double p_i1723_2_,
			double p_i1723_4_, double p_i1723_6_) {
		super(p_i1723_1_, p_i1723_2_, p_i1723_4_, p_i1723_6_);
		maxSpeedAirLateral = 0.8f;
	}

	/**
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS
				.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(
						this, p_130002_1_)))
			return true;
		if (this.riddenByEntity != null
				&& this.riddenByEntity instanceof EntityPlayer
				&& this.riddenByEntity != p_130002_1_) {
			return true;
		} else if (this.riddenByEntity != null
				&& this.riddenByEntity != p_130002_1_) {
			return false;
		} else {
			if (!this.worldObj.isRemote) {
				p_130002_1_.mountEntity(this);
			}

			return true;
		}
	}

	public double getMountedYOffset() {
		return 1.2;
	}

	public int getMinecartType() {
		return 0;
	}

	public void killMinecart(DamageSource p_94095_1_) {
		this.setDead();
		ItemStack itemstack = new ItemStack(ManzocCore.itemCart, 1);

		this.entityDropItem(itemstack, 0.0F);
	}

	public ItemStack getCartItem() {
		return new ItemStack(ManzocCore.itemCart);
	}
}