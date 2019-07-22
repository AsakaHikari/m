package cannonmod.core;

import net.minecraft.entity.Entity;
import cannonmod.entity.EntityCannon;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageCannonGuiHandler implements IMessageHandler<MessageCannonGui, IMessage> {

	@Override//IMessageHandlerのメソッド
	public IMessage onMessage(MessageCannonGui message, MessageContext ctx) {
		//クライアントへ送った際に、EntityPlayerインスタンスはこのように取れる。
		//EntityPlayer player = SamplePacketMod.proxy.getEntityPlayerInstance();
		//サーバーへ送った際に、EntityPlayerインスタンス（EntityPlayerMPインスタンス）はこのように取れる。
		//EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
		//Do something.
		switch(message.type){
		case 0:
			ctx.getServerHandler().playerEntity.openGui(CannonCore.instance, GuiHandler.MOD_TILE_ENTITY_GUI, ctx.getServerHandler().playerEntity.worldObj, message.entityId, 0, 0);
			break;
		case 1:
			Entity e=ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityId);
			if(e instanceof EntityCannon){
				((EntityCannon)e).openFire();
			}
			break;
		}
		return null;
		//本来は返答用IMessageインスタンスを返すのだが、旧来のパケットの使い方をするなら必要ない。
	}
}