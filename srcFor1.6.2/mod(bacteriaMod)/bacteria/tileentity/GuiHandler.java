package mod.bacteria.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	//returns an instance of the Container you made earlier
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof GenomeChangerTileEntity){
                return new GenomeChangerContainer(player.inventory, (GenomeChangerTileEntity) tileEntity);
            }
            if(tileEntity instanceof U_MChangerTileEntity){
                return new U_MChangerContainer(player.inventory, (U_MChangerTileEntity) tileEntity);
            }
            return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                    int x, int y, int z) {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if(tileEntity instanceof GenomeChangerTileEntity){
                return new GenomeChangerGui(player.inventory, (GenomeChangerTileEntity) tileEntity);
            }
            if(tileEntity instanceof U_MChangerTileEntity){
                return new U_MChangerGui(player.inventory, (U_MChangerTileEntity) tileEntity);
            }
            return null;

    }
}
