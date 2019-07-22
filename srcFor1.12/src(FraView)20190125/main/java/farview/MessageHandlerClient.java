package farview;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerClient implements IMessageHandler<MessageHandlerClient.Message, IMessage>{
	
	public static class Message implements IMessage{
		public int chunkX;
		public int chunkZ;
	    public int availableSections;
	    public byte[] buffer;
	    public List<NBTTagCompound> tileEntityTags;
	    public boolean fullChunk;
	    public int dimension;
		public Message(){}
		
		public Message(Chunk chunkIn, int changedSectionFilter){
			this.dimension=chunkIn.getWorld().provider.getDimension();
			this.chunkX = chunkIn.x;
	        this.chunkZ = chunkIn.z;
	        this.fullChunk = changedSectionFilter == 65535;
	        boolean flag = chunkIn.getWorld().provider.hasSkyLight();
	        this.buffer = new byte[this.calculateChunkSize(chunkIn, flag, changedSectionFilter)];
	        this.availableSections = this.extractChunkData(new PacketBuffer(this.getWriteBuffer()), chunkIn, flag, changedSectionFilter);
	        this.tileEntityTags = Lists.<NBTTagCompound>newArrayList();

	        for (Entry<BlockPos, TileEntity> entry : chunkIn.getTileEntityMap().entrySet())
	        {
	            BlockPos blockpos = entry.getKey();
	            TileEntity tileentity = entry.getValue();
	            int i = blockpos.getY() >> 4;

	            if (this.fullChunk || (changedSectionFilter & 1 << i) != 0)
	            {
	                NBTTagCompound nbttagcompound = tileentity.getUpdateTag();
	                this.tileEntityTags.add(nbttagcompound);
	            }
	        }
			
		}

	    /**
	     * Reads the raw packet data from the data stream.
	     */
		@Override
	    public void fromBytes(ByteBuf buf)
	    {
			this.dimension=buf.readInt();
	        this.chunkX = buf.readInt();
	        this.chunkZ = buf.readInt();
	        this.fullChunk = buf.readBoolean();
	        this.availableSections = buf.readInt();
	        int i = buf.readInt();

	        if (i > 2097152)
	        {
	            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
	        }
	        else
	        {
	            this.buffer = new byte[i];
	            buf.readBytes(this.buffer);
	            int j = buf.readInt();
	            this.tileEntityTags = Lists.<NBTTagCompound>newArrayList();

	            for (int k = 0; k < j; ++k)
	            {
	                try {
						this.tileEntityTags.add(this.readCompoundTag(buf));
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	        }
	    }
		 /**
	     * Reads a compressed NBTTagCompound from this buffer
	     */
	    @Nullable
	    public NBTTagCompound readCompoundTag(ByteBuf buf) throws IOException
	    {
	        int i = buf.readerIndex();
	        byte b0 = buf.readByte();

	        if (b0 == 0)
	        {
	            return null;
	        }
	        else
	        {
	        	buf.readerIndex(i);

	            try
	            {
	                return CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L));
	            }
	            catch (IOException ioexception)
	            {
	                throw new EncoderException(ioexception);
	            }
	        }
	    }
	    /**
	     * Writes the raw packet data to the data stream.
	     */
		@Override
	    public void toBytes(ByteBuf buf)
	    {
			buf.writeInt(this.dimension);
	        buf.writeInt(this.chunkX);
	        buf.writeInt(this.chunkZ);
	        buf.writeBoolean(this.fullChunk);
	        buf.writeInt(this.availableSections);
	        buf.writeInt(this.buffer.length);
	        buf.writeBytes(this.buffer);
	        buf.writeInt(this.tileEntityTags.size());

	        for (NBTTagCompound nbttagcompound : this.tileEntityTags)
	        {
	            this.writeCompoundTag(nbttagcompound,buf);
	        }
	    }

	    /**
	     * Writes a compressed NBTTagCompound to this buffer
	     */
	    public ByteBuf writeCompoundTag(@Nullable NBTTagCompound nbt,ByteBuf buf)
	    {
	        if (nbt == null)
	        {
	            buf.writeByte(0);
	        }
	        else
	        {
	            try
	            {
	                CompressedStreamTools.write(nbt, new ByteBufOutputStream(buf));
	            }
	            catch (IOException ioexception)
	            {
	                throw new EncoderException(ioexception);
	            }
	        }

	        return buf;
	    }

	    //@SideOnly(Side.CLIENT)
	    public PacketBuffer getReadBuffer()
	    {
	        return new PacketBuffer(Unpooled.wrappedBuffer(this.buffer));
	    }

	    private ByteBuf getWriteBuffer()
	    {
	        ByteBuf bytebuf = Unpooled.wrappedBuffer(this.buffer);
	        bytebuf.writerIndex(0);
	        return bytebuf;
	    }

	    public int extractChunkData(PacketBuffer buf, Chunk chunkIn, boolean writeSkylight, int changedSectionFilter)
	    {
	        int i = 0;
	        ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
	        int j = 0;

	        for (int k = aextendedblockstorage.length; j < k; ++j)
	        {
	            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];

	            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!this.fullChunk || !extendedblockstorage.isEmpty()) && (changedSectionFilter & 1 << j) != 0)
	            {
	                i |= 1 << j;
	                extendedblockstorage.getData().write(buf);
	                buf.writeBytes(extendedblockstorage.getBlockLight().getData());

	                if (writeSkylight)
	                {
	                    buf.writeBytes(extendedblockstorage.getSkyLight().getData());
	                }
	            }
	        }

	        if (this.fullChunk)
	        {
	            buf.writeBytes(chunkIn.getBiomeArray());
	        }

	        return i;
	    }

	    protected int calculateChunkSize(Chunk chunkIn, boolean p_189556_2_, int p_189556_3_)
	    {
	        int i = 0;
	        ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
	        int j = 0;

	        for (int k = aextendedblockstorage.length; j < k; ++j)
	        {
	            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];

	            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!this.fullChunk || !extendedblockstorage.isEmpty()) && (p_189556_3_ & 1 << j) != 0)
	            {
	                i = i + extendedblockstorage.getData().getSerializedSize();
	                i = i + extendedblockstorage.getBlockLight().getData().length;

	                if (p_189556_2_)
	                {
	                    i += extendedblockstorage.getSkyLight().getData().length;
	                }
	            }
	        }

	        if (this.fullChunk)
	        {
	            i += chunkIn.getBiomeArray().length;
	        }

	        return i;
	    }

		
	}

	public IMessage onMessage(Message message, MessageContext ctx) {
		if(ctx.side.isServer()){
			
			//Core.INSTANCE.sendTo(message, ctx.getServerHandler().player);
		}else {
			IndependentChunk c=new IndependentChunk(Minecraft.getMinecraft().world,message.chunkX,message.chunkZ);
			//c.markLoaded(true);
			c.read(message.getReadBuffer(), message.availableSections, message.fullChunk);
			EventReceiver.getRenderer(message.dimension).receivedChunkMap.put(c.getPos(),c);
			//System.out.println("client "+message.chunkX+" "+message.chunkZ);
		}
		return null;
	}

}
