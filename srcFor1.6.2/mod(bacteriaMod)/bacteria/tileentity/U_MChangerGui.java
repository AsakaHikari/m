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

public class U_MChangerGui extends GuiContainer {
	private int pageNum=0;

	protected U_MChangerTileEntity tileEntity;
	protected boolean isBacteria=false;
	
	public U_MChangerGui (InventoryPlayer inventoryPlayer,
            U_MChangerTileEntity tileEntity) {
    //the container is instanciated and passed to the superclass for handling
		
    super(new U_MChangerContainer(inventoryPlayer, tileEntity));
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
		GuiButton[] adder,reducer,adder10,reducer10;
		GuiButton up,down,ok;
		super.initGui();
		adder=new GuiButton[3];
		reducer = new GuiButton[3];
		adder10=new GuiButton[3];
		reducer10=new GuiButton[3];
		
		for(int i=0;i<3;i++){
			this.buttonList.add(adder[i]=new GuiButton(0+4*i,this.guiLeft+65,this.guiTop+15+i*20,16,10,"+1"));
			this.buttonList.add(reducer[i]=new GuiButton(1+4*i,this.guiLeft+65,this.guiTop+25+i*20,16,10,"-1"));
			this.buttonList.add(adder10[i]=new GuiButton(2+4*i,this.guiLeft+81,this.guiTop+15+i*20,18,10,"+10"));
			this.buttonList.add(reducer10[i]=new GuiButton(3+4*i,this.guiLeft+81,this.guiTop+25+i*20,18,10,"-10"));
			
		}
		this.buttonList.add(up=new GuiButton(12,this.guiLeft+150,this.guiTop+30,20,14,"UP"));
		this.buttonList.add(down=new GuiButton(13,this.guiLeft+150,this.guiTop+60,20,14,"DOWN"));
		this.buttonList.add(ok=new GuiButton(14,this.guiLeft+101,this.guiTop+35,20,10,"OK"));
		
		
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
            //draw text and stuff here
            //the parameters for drawString are: string, x, y, color
		if(pageNum<4)fontRenderer.drawStringWithShadow("Make"+pageNum, 8, 6, 0x00ffffff);
		else   fontRenderer.drawStringWithShadow("Use", 8, 6, 0x00ffffff);
            
            
            ItemStack slot1ItemStack = this.inventorySlots.getSlot(0).getStack();
            
            if (slot1ItemStack!=null){
            	if(slot1ItemStack.getItemName().equals("item.bacteria")){
            		if(pageNum<4){
            		int make,mrate,mmin,mmax;
            		
             		NBTTagCompound nbt=slot1ItemStack.getTagCompound();
             		
    				make=nbt.getInteger("make"+pageNum);
    				mrate=nbt.getInteger("mrate"+pageNum);
    				mmin=nbt.getInteger("mmin"+pageNum);
    				mmax=nbt.getInteger("mmax"+pageNum);
    				
    				fontRenderer.drawStringWithShadow("mrate", 13, 22, 0x00ffffff);
        			fontRenderer.drawStringWithShadow("mmax", 13, 42, 0x00ffffff);
        			fontRenderer.drawStringWithShadow("mmin", 13, 62, 0x00ffffff);
        			fontRenderer.drawStringWithShadow("item", 101, 8, 0x00ffffff);
             		
    				fontRenderer.drawStringWithShadow(mrate+"%", 47, 22, 0x00ffffff);
        			fontRenderer.drawStringWithShadow(""+mmax, 47, 42, 0x00ffffff);
        			fontRenderer.drawStringWithShadow(""+mmin, 47, 62, 0x00ffffff);
        			if(!this.isBacteria){
        				if(make!=0){
        					this.tileEntity.inv[1]=new ItemStack(make,1,0);
        				}else{
        					this.tileEntity.inv[1]=null;
        				}
        				this.isBacteria=true;
        			}
        			
            		}else{
            			int use,urate;
            			NBTTagCompound nbt=slot1ItemStack.getTagCompound();
            			use=nbt.getInteger("use");
            			urate=nbt.getInteger("urate");
            			
            			fontRenderer.drawStringWithShadow("urate", 13, 22, 0x00ffffff);
            			fontRenderer.drawStringWithShadow("N/A", 13, 42, 0x00ffffff);
            			fontRenderer.drawStringWithShadow("N/A", 13, 62, 0x00ffffff);
            			fontRenderer.drawStringWithShadow("item", 101, 8, 0x00ffffff);
            			
            			fontRenderer.drawStringWithShadow(urate+"", 47, 22, 0x00ffffff);
            			if(!this.isBacteria){
            				if(use!=0){
            					this.tileEntity.inv[1]=new ItemStack(use,1,0);
            				}else{
            					this.tileEntity.inv[1]=null;
            				}
            				this.isBacteria=true;
            			}
            		}
            	}else this.isBacteria=false;
            }else this.isBacteria=false;
            //draws "Inventory" or your regional equivalent
            fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
            
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //draw your Gui here, only thing you need to change is the path
	this.mc.func_110434_K().func_110577_a(new ResourceLocation("bacteriamod","textures/gui/U_MChanger.png"));
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
	}
	 
	@Override
	 protected void actionPerformed(GuiButton guibutton) {
         //id is the id you give your button
		 ItemStack slot1ItemStack = this.inventorySlots.getSlot(0).getStack();
		 if (slot1ItemStack!=null){
         	if(slot1ItemStack.getItemName().equals("item.bacteria")){
         		if(guibutton.id<12){
         			if(pageNum<4){
         				NBTTagCompound nbt=slot1ItemStack.getTagCompound();
         				int nbtValue=0;
         				byte nbtNum=0;
         				int mrate,mmin,mmax;
         				
         				mrate=nbt.getInteger("mrate"+pageNum);
    					mmin=nbt.getInteger("mmin"+pageNum);
    					mmax=nbt.getInteger("mmax"+pageNum);
    					int add=0;
    					switch(guibutton.id%4){
     					case 0:
     						add=1;
     						break;
     					case 1:
     						add=-1;
     						break;
     					case 2:
     						add=10;
     						break;
     					case 3:
     						add=-10;
     						break;
         				}
         				switch((guibutton.id-guibutton.id%4)/4){
         				case 0:
         					mrate+=add;
         					nbtNum=(byte) (6+pageNum);
         					nbtValue=mrate;
         					break;
         				case 1:
         					mmax+=add;
         					nbtNum=(byte) (10+pageNum);
         					nbtValue=mmax;
         					break;
         				case 2:
         					mmin+=add;
         					nbtNum=(byte) (14+pageNum);
         					nbtValue=mmin;
         					break;
         				}
         				nbt.setInteger("mrate"+pageNum, mrate);
         				nbt.setInteger("mmax"+pageNum,mmax);
         				nbt.setInteger("mmin"+pageNum,mmin);
         				slot1ItemStack.setTagCompound(nbt);
         				this.mc.thePlayer.sendQueue.addToSendQueue(PacketHandler.sendPacketU_MStatus(nbtNum,nbtValue,this.tileEntity));
         			}else{
         				NBTTagCompound nbt=slot1ItemStack.getTagCompound();
             			int nbtValue=0;
             			byte nbtNum=0;
             			
             			int urate;
             			
             			urate=nbt.getInteger("urate");
             			
             			int add=0;
    					switch(guibutton.id%4){
     					case 0:
     						add=1;
     						break;
     					case 1:
     						add=-1;
     						break;
     					case 2:
     						add=10;
     						break;
     					case 3:
     						add=-10;
     						break;
         				}
    					switch((guibutton.id-guibutton.id%4)/4){
    					case 0:
    						urate+=add;
    						nbtNum=1;
    						nbtValue=urate;
    						
    						break;
    					case 1:
    					case 2:
    						nbtNum=-127;
    						break;
    					}
    					
    					nbt.setInteger("urate", urate);
    					
    					slot1ItemStack.setTagCompound(nbt);
    					this.mc.thePlayer.sendQueue.addToSendQueue(PacketHandler.sendPacketU_MStatus
    							(nbtNum,nbtValue,this.tileEntity));
         			}
         		}else if(guibutton.id<14){
         			switch(guibutton.id){
         			case 12:
         				pageNum--;
         				break;
         			case 13:
         				pageNum++;
         				break;
         			}
         			int item;
         			NBTTagCompound nbt=slot1ItemStack.getTagCompound();
         			if(pageNum<4){
         				item=nbt.getInteger("make"+pageNum);
         			}else{
         				item=nbt.getInteger("use");
         			}
         			if(item!=0){
    					this.tileEntity.inv[1]=new ItemStack(item,1,0);
    				}else{
    					this.tileEntity.inv[1]=null;
    				}
         			if(pageNum<0)pageNum=0;
         			if(pageNum>4)pageNum=4;
         		}else if(guibutton.id<15){
         			int nbtNum,nbtValue;
         			nbtValue=this.tileEntity.inv[1]==null?0:this.tileEntity.inv[1].itemID;
         			nbtNum=this.pageNum<4?2+this.pageNum:0;
         			this.mc.thePlayer.sendQueue.addToSendQueue(PacketHandler.sendPacketU_MStatus
         					((byte)nbtNum,nbtValue,this.tileEntity));
         		}
         	}
		 }
         
	 }

	public void readFromPacket(int use,int urate,int[] make,int[] mrate){
		ItemStack slot1ItemStack = this.inventorySlots.getSlot(0).getStack();
		if (slot1ItemStack!=null){
         	if(slot1ItemStack.getItemName().equals("item.bacteria")){
         		NBTTagCompound nbt=slot1ItemStack.getTagCompound();
         		nbt.setInteger("use"+pageNum, use);
         		nbt.setInteger("urate"+pageNum, urate);
         		nbt.setIntArray("make"+pageNum,make);
         		nbt.setIntArray("mrate"+pageNum, mrate);
         	}
         }
	}
	
	@Override
	public void handleMouseInput(){
		super.handleMouseInput();
	}

}
