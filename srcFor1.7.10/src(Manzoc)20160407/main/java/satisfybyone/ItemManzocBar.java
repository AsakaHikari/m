package satisfybyone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemManzocBar extends ItemFood {

	public ItemManzocBar(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_) {
		super(p_i45339_1_, p_i45339_2_, p_i45339_3_);

	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 50;
	}
	
	@Override
	protected void onFoodEaten(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_){
		super.onFoodEaten(p_77849_1_, p_77849_2_, p_77849_3_);
		if(!p_77849_2_.isRemote){
			for (WorldServer w : MinecraftServer.getServer().worldServers) {
				for (Object o : w.playerEntities) {
					EntityPlayer p=(EntityPlayer)o;
					p.addChatMessage(new ChatComponentText(p_77849_3_.getDisplayName()+" は満足した"));
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
		if(p_77659_3_.canEat(false)){
			p_77659_2_.playSoundAtEntity(p_77659_3_,
					"manzoc:manzoc.eatbar", 0.5F,
					p_77659_2_.rand.nextFloat() * 0.1F + 0.9F);
		}
		return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }
}
