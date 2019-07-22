package farview;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerServer implements IMessageHandler<MessageHandlerServer.Message, IMessage>{

	public static class Message implements IMessage{
		public int chunkX;
		public int chunkZ;
		public int dim;
		public Message(){}

		public Message(int chunkX,int chunkY,int dim){
			this.chunkX=chunkX;
			this.chunkZ=chunkY;
			this.dim=dim;

		}

		public void fromBytes(ByteBuf buf) {
			this.chunkX=buf.readInt();
			this.chunkZ=buf.readInt();
			this.dim=buf.readInt();
		}

		public void toBytes(ByteBuf buf) {
			buf.writeInt(this.chunkX);
			buf.writeInt(this.chunkZ);
			buf.writeInt(this.dim);
		}


	}

	public IMessage onMessage(Message message, MessageContext ctx) {
		if(ctx.side.isServer()){
			//((ChunkProviderServer)(ctx.getServerHandler().player.world.getChunkProvider())).loadChunk(message.chunkX, message.chunkZ);
			//System.out.println("server "+message.chunkX+" "+message.chunkZ);
			WorldServer worldIn=(WorldServer)ctx.getServerHandler().player.world;
			Object[] objs = null;
			try {
				objs=((AnvilChunkLoader)worldIn.getChunkProvider().chunkLoader).loadChunk__Async(worldIn, message.chunkX, message.chunkZ);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(objs!=null && objs.length>0) {
				Core.INSTANCE.sendTo(new MessageHandlerClient.Message((Chunk)objs[0], 65535), ctx.getServerHandler().player);
			}
			//Core.INSTANCE.sendTo(message, ctx.getServerHandler().player);
		}else {
			//System.out.println("client "+message.chunkX+" "+message.chunkZ);
		}
		return null;
	}
}
