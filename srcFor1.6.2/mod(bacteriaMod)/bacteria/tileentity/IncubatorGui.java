package mod.bacteria.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

public class IncubatorGui extends GuiContainer {
	protected IncubatorTileEntity tileEntity;
	private byte pageNum=0;
	
	public IncubatorGui (InventoryPlayer inventoryPlayer,
            IncubatorTileEntity tileEntity) {
    //the container is instanciated and passed to the superclass for handling
		
    super(new IncubatorContainer(inventoryPlayer, tileEntity));
    this.tileEntity=tileEntity;
    
}
	
	@Override
	public void initGui(){
		super.initGui();
		
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
            //draw text and stuff here
            //the parameters for drawString are: string, x, y, color
		
            fontRenderer.drawStringWithShadow("Incubator", 8, 6, 0x00ffffff);
            
            //draws "Inventory" or your regional equivalent
            fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
            
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //draw your Gui here, only thing you need to change is the path
	this.mc.func_110434_K().func_110577_a(new ResourceLocation("bacteriamod","textures/gui/Incubator.png"));
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
	}
	
	
	@Override
	public void handleMouseInput(){
		super.handleMouseInput();
	}

}
