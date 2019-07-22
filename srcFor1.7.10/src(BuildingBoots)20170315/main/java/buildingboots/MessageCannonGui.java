package buildingboots;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
 
public class MessageCannonGui implements IMessage {
 
    public int x,y,z,playerId;
 
    public MessageCannonGui(){}
 
    public MessageCannonGui(int x,int y,int z,int playerId) {
        this.x=x;
        this.y=y;
        this.z=z;
        this.playerId=playerId;
    }
 
    @Override//IMessage
    public void fromBytes(ByteBuf buf) {
        this.x=buf.readInt();
        this.y=buf.readInt();
        this.z=buf.readInt();
        this.playerId=buf.readInt();
    }
 
    @Override//IMessage
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.playerId);
    }
}