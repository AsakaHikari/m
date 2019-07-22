package regulararmy.core;

import regulararmy.entity.command.RegularArmyLeader;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemLetterOfPeace extends Item {

	public ItemLetterOfPeace() {
		super();
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		if(!par3World.isRemote){
			if(par3World.getBlock(par4, par5, par6)==MonsterRegularArmyCore.blockBase&&par3World.getBlockMetadata(par4, par5, par6)==1){
				for(int i=0;i<MonsterRegularArmyCore.leaders.length;i++){
					RegularArmyLeader leader=MonsterRegularArmyCore.leaders[i];
					
					if(leader!=null && leader.x==par4 && leader.y==par5 && leader.z==par6){
						leader.onEnd(false);
						par2EntityPlayer.addChatMessage(new ChatComponentText("The war has ended.At wave "+leader.wave));
						par3World.setBlockMetadataWithNotify(par4, par5, par6, 0, 2);
						par1ItemStack.splitStack(1);
						return true;
					}
				}
				par2EntityPlayer.addChatMessage(new ChatComponentText("Error! Did you replaced this block with an external tool or deleted \"MRAData\" file?"));
				par3World.setBlockMetadataWithNotify(par4, par5, par6, 0, 2);
				par1ItemStack.splitStack(1);
				return true;
			}
		}
		return false;
	}
}
