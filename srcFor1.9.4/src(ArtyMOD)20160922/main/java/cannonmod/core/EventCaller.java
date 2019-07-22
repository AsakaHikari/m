package cannonmod.core;

import org.lwjgl.opengl.GL11;

import cannonmod.entity.EntityCannon;
import cannonmod.entity.RenderCannon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class EventCaller {
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Pre e){
		if(e.getType()==RenderGameOverlayEvent.ElementType.CROSSHAIRS){
			
			Entity entity=Minecraft.getMinecraft().thePlayer.getRidingEntity();
			if(Minecraft.getMinecraft().gameSettings.thirdPersonView==0&&
					Minecraft.getMinecraft().thePlayer.getRidingEntity()!=null &&
					entity instanceof EntityCannon){
				int f1=Math.min(Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().displayWidth);
				Minecraft.getMinecraft().getTextureManager().bindTexture(RenderCannon.textureCrossbar);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(false);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableAlpha();
				//GL11.glDisable(GL11.GL_ALPHA_TEST);
				Tessellator tessellator = Tessellator.getInstance();
				 VertexBuffer worldrenderer = tessellator.getBuffer();
				int widthDiff=(Minecraft.getMinecraft().displayWidth-f1)/2;
				int heightDiff=(Minecraft.getMinecraft().displayHeight-f1)/2;
				f1/=2;
				//System.out.println("widthdiff:"+widthDiff+" heightDiff:"+heightDiff+" f1:"+f1);
				EntityPlayer player=Minecraft.getMinecraft().thePlayer;
				EntityCannon cannon=(EntityCannon)entity;
				
				float yaw=player.rotationYawHead-cannon.rotationYawHead;
				float pitch=player.rotationPitch-cannon.rotationPitch;
				
				widthDiff/=2;
				heightDiff/=2;
				worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
				worldrenderer.pos(widthDiff-yaw*3, heightDiff+(double)f1-pitch*3, 0).tex( 0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
				worldrenderer.pos(widthDiff+(double)f1-yaw*3, heightDiff+(double)f1-pitch*3, 0).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
				worldrenderer.pos(widthDiff+(double)f1-yaw*3, heightDiff-pitch*3, 0).tex( 1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
				worldrenderer.pos(widthDiff-yaw*3, heightDiff-pitch*3, 0).tex( 0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
				tessellator.draw();
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				//GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if(CannonCore.buttonCannonGui.isPressed()){
			Entity e=Minecraft.getMinecraft().thePlayer.getRidingEntity();
			if(e instanceof EntityCannon){
				((EntityCannon)e).openGUI();
			}
		}
		/*
		if(CannonCore.buttonCannonFire.isPressed()){
			Entity e=Minecraft.getMinecraft().thePlayer.ridingEntity;
			if(e instanceof EntityCannon){
				((EntityCannon)e).openFire();
			}
		}
		*/
	}
	
	@SubscribeEvent
	public void onPlayerRightClick(RightClickEmpty event){
		if(event.getEntityPlayer().getRidingEntity() instanceof EntityCannon){
			((EntityCannon)event.getEntityPlayer().getRidingEntity()).openFire();
		}
	}

}
