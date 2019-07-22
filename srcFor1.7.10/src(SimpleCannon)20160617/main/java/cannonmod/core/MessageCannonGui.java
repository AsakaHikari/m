package cannonmod.core;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
 
public class MessageCannonGui implements IMessage {
 
    public int entityId;
    public byte type;
 
    public MessageCannonGui(){}
 
    public MessageCannonGui(int par1,byte type) {
        this.entityId= par1;
        this.type=type;
    }
 
    @Override//IMessageのメソッド。ByteBufからデータを読み取る。
    public void fromBytes(ByteBuf buf) {
        this.entityId= buf.readInt();
        this.type=buf.readByte();
    }
 
    @Override//IMessageのメソッド。ByteBufにデータを書き込む。
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.type);
    }
}