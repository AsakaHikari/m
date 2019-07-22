package cannonmod.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cannonmod.entity.EntityCannon;

public class GuiCannon extends GuiContainer {

	public ResourceLocation gui;
	public EntityCannon cannon;

	public GuiCannon (InventoryPlayer inventoryPlayer,
			EntityCannon tileEntity) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerCannon(inventoryPlayer, tileEntity));
		this.gui=new ResourceLocation("simplecannon:textures/gui/cannon.png");
		this.xSize=227;
		this.ySize=192;
		this.cannon=tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {

		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		fontRendererObj.drawString("HP "+cannon.getHealth(),120,5, 4210752);
		fontRendererObj.drawString("Calibre "+cannon.calibre*10+"cm",120,15, 4210752);
		fontRendererObj.drawString("Barrel "+cannon.lengthOfBarrel*10+"cm",120,25, 4210752);
		fontRendererObj.drawString("Armor "+(int)(cannon.armor*10/this.cannon.size)+"mm",120,35, 4210752);
		if(cannon.engine>0){
			fontRendererObj.drawString("Engine mk."+cannon.engine,120,45, 4210752);
		}
		if(cannon.motor>0){
			fontRendererObj.drawString("Motor mk."+cannon.motor,120,55, 4210752);
		}
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		//draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(this.gui);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		int fuelleft=this.cannon.getBurnTimeRemainingScaled(13);
		this.drawTexturedModalRect(x+153,y+66,232,12-fuelleft,14,1+fuelleft);
		
		int reloadPro=this.cannon.getReloadTimeProceedingScaled(24);
		this.drawTexturedModalRect(x+45+reloadPro,y+10,231+reloadPro,31,25-reloadPro,19);
		
	}

}