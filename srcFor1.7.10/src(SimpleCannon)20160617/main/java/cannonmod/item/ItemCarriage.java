package cannonmod.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ItemCarriage extends Item {
	public IIcon[] iconItemSample;
	public ItemCarriage(){
		super();
		this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return iconItemSample[par1 % iconItemSample.length];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.getUnlocalizedName() + "_" + par1ItemStack.getItemDamage();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {

		this.iconItemSample = new IIcon[4];

		this.iconItemSample[0]=par1IconRegister.registerIcon("simplecannon:carriage");
		for (int i = 1; i < this.iconItemSample.length; ++i)
		{
			this.iconItemSample[i] = par1IconRegister.registerIcon("simplecannon:carriageM" + i);
		}

	}
}
