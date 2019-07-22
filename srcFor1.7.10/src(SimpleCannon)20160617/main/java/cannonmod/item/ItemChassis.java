package cannonmod.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemChassis extends Item {
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advanced) {
		if(((itemStack.getItemDamage()&0x0000ff00)>>8)==0xfe){
			list.add("Motor : crafted motor of the carriage.");
		}else if(((itemStack.getItemDamage()&0x0000ff00)>>8)==0xff){
			list.add("Motor : Any");
		}else if(((itemStack.getItemDamage()&0x0000ff00)>>8)>0){
			list.add("Motor Mk."+((itemStack.getItemDamage()&0x0000ff00)>>8));
		}
		if((itemStack.getItemDamage()&0x000000ff)==0xfe){
			list.add("Engine : crafted engine of the carriage.");
		}else if((itemStack.getItemDamage()&0x000000ff)==0xff){
			list.add("Engine : Any");
		}else if((itemStack.getItemDamage()&0x000000ff)>0){
			list.add("Engine Mk."+(itemStack.getItemDamage()&0x000000ff));
		}
		
	}
}
