package entityidchecker;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CheckerItem extends Item {
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity)
    {
		player.addChatMessage(new ChatComponentText("ClassID:"+EntityList.getEntityID(entity)+" UniqueID:"+entity.getEntityId()));
        return true;
    }
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		Set entrySet=EntityList.IDtoClassMapping.entrySet();
		for(Object entryO:entrySet){
			Map.Entry<Integer, Class<Entity>> entry=(Entry<Integer, Class<Entity>>) entryO;
			player.addChatMessage(new ChatComponentText(entry.getValue().getSimpleName()+" ID:"+entry.getKey()));
		}
		return stack;
	}
}
