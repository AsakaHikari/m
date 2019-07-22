package gvcguns;
 
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
 
@SideOnly(Side.CLIENT)
public class GVCGuiContainer extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("gvcguns:textures/gui/guitest.png");
    private GVCTileEntityGunBox tileEntity;
    /*public GVCGuiContainer(int x, int y, int z) {
        super(new GVCContainer(x, y, z));
    }*/
 
    
    public GVCGuiContainer(InventoryPlayer inventoryPlayer,int x, int y, int z, GVCTileEntityGunBox tileEntity) {
        super(new GVCContainer(inventoryPlayer, x, y, z, tileEntity));
        this.tileEntity = tileEntity; 
         		this.xSize = 176; 
         		this.ySize = 166; 
         	} 
         
         
         	@Override 
         	protected void drawGuiContainerForegroundLayer(int xMouse, int yMouse) 
         	{ 
         		this.fontRendererObj.drawString(StatCollector.translateToLocal(this.tileEntity.getInventoryName()), 8, 6, 0x404040); 
         		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 0x404040); 
         	} 
         
         
         	@Override 
         	protected void drawGuiContainerBackgroundLayer(float partialRenderTick, int xMouse, int yMouse) 
         	{ 
         		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); 
         		this.mc.getTextureManager().bindTexture(TEXTURE); 
         		int x = (this.width  - this.xSize) / 2; 
         		int y = (this.height - this.ySize) / 2; 
         		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize); 
         	} 
          

}