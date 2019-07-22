package cannonmod.core;

import net.minecraft.entity.Entity;
import cannonmod.entity.EntityCannon;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageCannonGuiHandler implements IMessageHandler<MessageCannonGui, IMessage> {

	@Override//IMessageHandler
	public IMessage onMessage(MessageCannonGui message, MessageContext ctx) {
		
		switch(message.type){
		case 0:
			ctx.getServerHandler().playerEntity.openGui(CannonCore.instance, GuiHandler.MOD_TILE_ENTITY_GUI, ctx.getServerHandler().playerEntity.worldObj, message.entityId, 0, 0);
			break;
		case 1:
			Entity e1=ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityId);
			if(e1 instanceof EntityCannon){
				/**a120*/
				((EntityCannon)e1).sendFireCommand();
			}
			break;
			/**a130*/
		case 2:
			Entity e2=ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.entityId);
			if(e2 instanceof EntityCannon){
				((EntityCannon)e2).dropArtyAndInventoryItems();
				e2.setDead();
			}
			break;
		}
		return null;
		
	}
}