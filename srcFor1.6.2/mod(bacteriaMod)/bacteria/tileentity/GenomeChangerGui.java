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

public class GenomeChangerGui extends GuiContainer {
	
	private GuiButton[] adder,reducer;
	private GuiButton lifter,lowerer;
	protected GenomeChangerTileEntity tileEntity;
	private byte pageNum=0;
	
	public GenomeChangerGui (InventoryPlayer inventoryPlayer,
            GenomeChangerTileEntity tileEntity) {
    //the container is instanciated and passed to the superclass for handling
		
    super(new GenomeChangerContainer(inventoryPlayer, tileEntity));
    this.tileEntity=tileEntity;
    
}
	/*
	public void writeToPacket(DataOutputStream dos){
		
		ItemStack slot1ItemStack = this.inventorySlots.getSlot(0).getStack();
		try{
			
		}catch(IOException e) {
 			e.printStackTrace();
 		}
	}*/
	
	@Override
	public void initGui(){
		super.initGui();
		adder=new GuiButton[3];
		reducer = new GuiButton[3];
		for(int i=0;i<3;i++){
			this.buttonList.add(this.adder[i]=new GuiButton(0+2*i,this.guiLeft+110,this.guiTop+16+20*i,16,8,"+"));
			this.buttonList.add(this.reducer[i]=new GuiButton(1+2*i,this.guiLeft+110,this.guiTop+24+20*i,16,8,"-"));
			
		}
		
		this.buttonList.add(this.lifter=new GuiButton(6,this.guiLeft+this.xSize-28,this.guiTop+30,24,15,"UP"));
		this.buttonList.add(this.lifter=new GuiButton(7,this.guiLeft+this.xSize-28,this.guiTop+66,24,15,"DOWN"));
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
            //draw text and stuff here
            //the parameters for drawString are: string, x, y, color
		
            fontRenderer.drawStringWithShadow("GenomeChanger", 8, 6, 0x00ffffff);
            
            ItemStack slot1ItemStack = this.inventorySlots.getSlot(0).getStack();
            
            if (slot1ItemStack!=null){
            	if(slot1ItemStack.getItemName().equals("item.bacteria")){
            		byte[] status=new byte[10];
             		NBTTagCompound nbt=slot1ItemStack.getTagCompound();
             		status[0]=nbt.getByte("str");
             		status[1]=nbt.getByte("spd");
             		status[2]=nbt.getByte("inc");
             		status[3]=nbt.getByte("therm");
             		status[4]=nbt.getByte("envUp");
             		status[5]=nbt.getByte("envDown");
             		status[6]=nbt.getByte("air");
             		status[7]=nbt.getByte("effID");
             		status[8]=nbt.getByte("effLevel");
             		
        			for(int i=0;i<3;i++){
        				String string="";
        				switch(i+pageNum){
        				case 0:
        					string="str";break;
        				case 1:
        					string="spd";break;
        				case 2:
        					string="inc";break;
        				case 3:
        					string="therm";break;
        				case 4:
        					string="envUp";break;
        				case 5:
        					string="envDown";break;
        				case 6:
        					string="air";break;
        				case 7:
        					string="effID";break;
        				case 8:
        					string="effLevel";break;
        					
        				}
        				fontRenderer.drawStringWithShadow(string+" "+status[i+pageNum], 24, 19+i*21, 0x00ffffff);
        			}
            		
            		
            	}
            	
            	
            }
            
            //draws "Inventory" or your regional equivalent
            fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
            
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //draw your Gui here, only thing you need to change is the path
	this.mc.func_110434_K().func_110577_a(new ResourceLocation("bacteriamod","textures/gui/GenomeChanger.png"));
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
	}
	
	@Override
	 protected void actionPerformed(GuiButton guibutton) {
         //id is the id you give your button
		 ItemStack slot1ItemStack = this.inventorySlots.getSlot(0).getStack();
		 if(guibutton.id<6){
         if (slot1ItemStack!=null){
         	if(slot1ItemStack.getItemName().equals("item.bacteria")){
         		byte[] status=new byte[10];
         		NBTTagCompound nbt=slot1ItemStack.getTagCompound();
         		status[0]=nbt.getByte("str");
         		status[1]=nbt.getByte("spd");
         		status[2]=nbt.getByte("inc");
         		status[3]=nbt.getByte("therm");
         		status[4]=nbt.getByte("envUp");
         		status[5]=nbt.getByte("envDown");
         		status[6]=nbt.getByte("air");
         		status[7]=nbt.getByte("effID");
         		status[8]=nbt.getByte("effLevel");
         	
         		byte nbtNum=(byte) ((guibutton.id-guibutton.id%2)/2+pageNum);
         		switch(guibutton.id%2) {
                case 0:
                	
                	status[nbtNum]++;
                    break;
                case 1:
                	status[nbtNum]--;
                    break;
                }
         		
         		
         		this.mc.thePlayer.sendQueue.addToSendQueue(PacketHandler.sendPacketBacteriaStatus(nbtNum,status[nbtNum],this.tileEntity));
         		
         	}
         }
         	
         }else if(guibutton.id==6){
        	 if(pageNum>0)pageNum--;
        	 
         }else if(guibutton.id==7){
        	 if(pageNum<6)pageNum++;
         }
         
	 }

	public void readFromPacket(byte[] status){
		ItemStack slot1ItemStack = this.inventorySlots.getSlot(0).getStack();
		if (slot1ItemStack!=null){
         	if(slot1ItemStack.getItemName().equals("item.bacteria")){
         		NBTTagCompound nbt=slot1ItemStack.getTagCompound();
         		nbt.setByte("str",status[0]);
         		nbt.setByte("spd",status[1]);
         		nbt.setByte("inc",status[2]);
         		nbt.setByte("therm",status[3]);
         		nbt.setByte("envUp",status[4]);
         		nbt.setByte("envDown",status[5]);
         		nbt.setByte("air",status[6]);
         		nbt.setByte("effID",status[7]);
         		nbt.setByte("effLevel",status[8]);
         	}
         }
	}
	
	@Override
	public void handleMouseInput(){
		super.handleMouseInput();
	}

}
