package regulararmy.core;

import regulararmy.entity.command.RegularArmyLeader;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemLetterOfProclamation extends Item {

	public ItemLetterOfProclamation() {
		super();
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		if(!par3World.isRemote){
			if(par3World.getBlock(par4, par5, par6)==MonsterRegularArmyCore.blockBase&&par3World.getBlockMetadata(par4, par5, par6)==0){
				if(MonsterRegularArmyCore.leadersNum>32){
					par2EntityPlayer.addChatMessage(new ChatComponentText("Too many bases!"));
				}
				RegularArmyLeader theLeader=new RegularArmyLeader(par3World,par4,par5,par6,(byte) ++MonsterRegularArmyCore.leadersNum);
				MonsterRegularArmyCore.leaders[MonsterRegularArmyCore.leadersNum]=theLeader;
				par1ItemStack.splitStack(1);
				par3World.setBlockMetadataWithNotify(par4, par5, par6, 1, 2);
				System.out.println(MonsterRegularArmyCore.leadersNum);
				theLeader.onStart();
				return true;

			}
		}
		return false;
	}
}
