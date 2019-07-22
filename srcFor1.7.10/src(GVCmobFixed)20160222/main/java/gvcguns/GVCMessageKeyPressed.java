package gvcguns;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
 
public class GVCMessageKeyPressed implements IMessage {
 
    public int key;
 
    public GVCMessageKeyPressed(){}
 
    public GVCMessageKeyPressed(int i) {
        this.key = i;
    }
 
    @Override
    public void fromBytes(ByteBuf buf) {
        this.key = buf.readByte();
    }
 
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.key);
    }
}