package gvcguns;
 
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
 
public class GVCGuiADSserver extends Container {
    //���W��GUI���J�������肷�邽�߂̂��́B
    int xCoord, yCoord, zCorrd;
    public GVCGuiADSserver(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCorrd = z;
    }
 
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        //�����A�u���b�N�Ƃ̈ʒu�֌W��GUI���䂵�����Ȃ�A��������g��
//        return player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCorrd + 0.5D) <= 64D;
        return true;
    }
}