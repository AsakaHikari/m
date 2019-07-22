package nooremod.core;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class EventCaller {
	@SubscribeEvent
	public void oreSpawnEvent(OreGenEvent.GenerateMinable e){
		for(int i:NoOreCore.dimensions){
			if(i==e.world.provider.dimensionId){
				e.setResult(Result.DENY);
			}
		}
	}
}
