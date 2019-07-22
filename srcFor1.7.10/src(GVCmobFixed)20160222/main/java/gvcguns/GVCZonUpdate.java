package gvcguns;


import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class GVCZonUpdate {

	public String ads;
	
	public boolean zoomtype;
	
	  @SubscribeEvent
	  public void onEvent(RenderGameOverlayEvent.Pre event)
	  {
		  if(FMLCommonHandler.instance().getSide() == Side.CLIENT) 
  		{
		  Minecraft minecraft = FMLClientHandler.instance().getClient();
		  World world = FMLClientHandler.instance().getWorldClient();
			Entity entity = minecraft.renderViewEntity;
			EntityPlayer entityplayer = (EntityPlayer)entity;
			//EntityPlayer entityplayer = event.player;
			ItemStack itemstack = ((EntityPlayer)(entityplayer)).getCurrentEquippedItem();
            ItemStack itemstack2 = entityplayer.inventory.armorItemInSlot(3);
            if(minecraft.gameSettings.thirdPersonView == 0 && itemstack2 != null && itemstack2.getItem() == GVCGunsPlus.fn_mugenb){
            	if(itemstack.getItemDamage() > 0){
        			itemstack.damageItem(-1, entityplayer);
            	}
            }
	        
  		}
	  }

}
