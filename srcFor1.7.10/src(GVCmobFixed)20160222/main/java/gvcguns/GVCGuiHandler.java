package gvcguns;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IGuiHandler;

public class GVCGuiHandler implements IGuiHandler {
	
	

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		GVCTileEntityGunBox tileEntity = (GVCTileEntityGunBox)world.getTileEntity(x, y, z);
		
		if (ID == GVCGunsPlus.GUI_ID) {
            return new GVCContainer(player.inventory,x,y,z, tileEntity);
        }
		/*if (ID == GVCGunsPlus.GUI_ID2) {
            return new GVCGuiADSserver(x,y,z);
        }*/

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		GVCTileEntityGunBox tileEntity = (GVCTileEntityGunBox)world.getTileEntity(x, y, z);
		
		if (ID == GVCGunsPlus.GUI_ID) {
            return new GVCGuiContainer(player.inventory,x,y,z, tileEntity);
        }
		
		/*Minecraft minecraft = Minecraft.getMinecraft();
		if (ID == GVCGunsPlus.GUI_ID2) {
            return new GVCGuiADSClient(minecraft);
        }*/

		return null;
	}

}
