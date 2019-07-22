package gvcguns;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
 
public class GVCMessageKeyPressedHandler implements IMessageHandler<GVCMessageKeyPressed, IMessage> {
 
    @Override
    public IMessage onMessage(GVCMessageKeyPressed message, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        //受け取ったMessageクラスのkey変数の数字をチャットに出力
        
        ItemStack itemstack = ((EntityPlayer)(entityPlayer)).getCurrentEquippedItem();
        int li = itemstack.getMaxDamage() - itemstack.getItemDamage();
	    itemstack.damageItem(li, entityPlayer);
 
        //entityPlayer.addChatComponentMessage(new ChatComponentText(String.format("Received byte %d", message.key)));
        return null;
    }
}