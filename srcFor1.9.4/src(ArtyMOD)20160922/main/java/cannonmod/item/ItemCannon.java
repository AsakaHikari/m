package cannonmod.item;

import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemCannon extends Item {
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advanced) {
		NBTTagCompound nbt=itemStack.getTagCompound();
		int calibre=0;
		int barrel=0;
		if(nbt!=null){
			calibre=nbt.getInteger("Calibre");
			barrel=nbt.getInteger("Barrel");
			//list.add("Calibre "+calibre*10+"cm");
			//list.add("Length "+barrel*10+"cm");
		}
		if(barrel==0){
			list.add("Barrel : crafted length of the barrel.");
		}else if(barrel==0xff){
			list.add("Barrel : Any");
		}else{
			list.add("Barrel "+barrel*10+"cm");
		}
		if(calibre==0){
			list.add("Calibre : crafted calibre of barrel.");
		}else if(calibre==0xff){
			list.add("Calibre : Any");
		}else{
			list.add("Calibre "+calibre*10+"cm");
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List par3List) {
		ItemStack stack=new ItemStack(this);
		NBTTagCompound nbt=new NBTTagCompound();
		nbt.setInteger("Calibre", 5);
		nbt.setInteger("Barrel", 10);
		stack.setTagCompound(nbt);
		par3List.add(stack);
	}
}
