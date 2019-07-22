package de.sanandrew.mods.betterboat;

import net.minecraft.entity.Entity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import de.sanandrew.mods.betterboat.entity.EntityBetterBoat;
import de.sanandrew.mods.betterboat.network.PacketSendBoatPos;

public class BoatPosMessageHandler implements IMessageHandler<PacketSendBoatPos, IMessage>{
	@Override//IMessageHandlerのメソッド
	public IMessage onMessage(PacketSendBoatPos pkt, MessageContext ctx) {
		//クライアントへ送った際に、EntityPlayerインスタンスはこのように取れる。
		//EntityPlayer player = SamplePacketMod.proxy.getEntityPlayerInstance();
		//サーバーへ送った際に、EntityPlayerインスタンス（EntityPlayerMPインスタンス）はこのように取れる。
		//EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
		//Do something.
		Entity e=ctx.getServerHandler().playerEntity.worldObj.getEntityByID(pkt.boatId);
		if(e instanceof EntityBetterBoat){
			((EntityBetterBoat)e).setPositionAndRotation(pkt.posX, pkt.posY, pkt.posZ, pkt.rotationYaw, pkt.rotationPitch);
		}
		return null;
		//本来は返答用IMessageインスタンスを返すのだが、旧来のパケットの使い方をするなら必要ない。
	}
}
