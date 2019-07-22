package nooremod.core;

import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class NoiseOfItem {
	public Item item;
	public int damage=-1;
	public NBTTagCompound nbt;
	public double probability=0.5;
	
	public boolean makeFromString(String str){
		String[] strs=str.split(";");
		Item item;
		if(strs[0].contains(":")){
			String modid=str.substring(0,str.indexOf(':'));
			String name=str.substring(str.indexOf(':')+1);
			item=GameRegistry.findItem(modid, name);
		}else{
			item=GameRegistry.findItem("minecraft",strs[0]);
		}
		if(item == null)return false;
		this.item=item;
		if(strs.length<2 || strs[3].equals("DEFAULT"))return true;
		this.probability=Double.parseDouble(strs[1]);
		
		if(strs.length<3 || strs[3].equals("ANY"))return true;
		
		this.damage=Integer.parseInt(strs[3]);
		
		if(strs.length<4)return true;
		
		try
        {
            NBTBase nbtbase = JsonToNBT.func_150315_a(strs[4]);

            if (!(nbtbase instanceof NBTTagCompound))
            {
                System.err.println("\" "+strs[4]+" \" is not valid");
                return false;
            }

            this.nbt = (NBTTagCompound)nbtbase;
        }
        catch (NBTException nbtexception)
        {
        	System.err.println("\" "+strs[4]+" \" is not valid");
            return false;
        }
		return  true;
	}
	
	public boolean isValidItem(ItemStack stack){
		if(stack.getItem()==this.item){
			if(this.damage<0 || stack.getItemDamage()==this.damage){
				if(this.nbt!=null){
					Set tags=this.nbt.func_150296_c();
					for(Object obj:tags){
						if(obj instanceof NBTBase){
							NBTBase tag=(NBTBase)obj;
						NBTTagCompound stacknbt=stack.getTagCompound();
						if(stacknbt.hasKey(tag.))
						}
					}
				}else{
					return true;
				}
			}
		}
	}
}
