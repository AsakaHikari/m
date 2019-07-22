package fatalinjury.core;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class EventCaller {
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e){
		/*
		PotionEffect[] effects=new PotionEffect[3];
		effects[0]=new PotionEffect(Potion.moveSlowdown.id,24000,2);
		effects[1]=new PotionEffect(Potion.weakness.id,24000,2);
		effects[2]=new PotionEffect(Potion.digSlowdown.id,24000,2);
		List<ItemStack> list=new ArrayList<ItemStack>();
		for(int i=0;i<effects.length;i++){
			effects[i].setCurativeItems(list)
			e.player.addPotionEffect(effects[i]);
		}
		*/
		List<ItemStack> list=new ArrayList<ItemStack>();
		PotionEffect effect=new PotionEffect(NoOreCore.potionId,12000,0);
		effect.setCurativeItems(list);
		e.player.addPotionEffect(effect);
	}
	@SubscribeEvent
	public void cancelDrop(LivingDropsEvent e){
		if(!(e.entityLiving instanceof EntityPlayer) && !e.recentlyHit){
			e.setCanceled(true);
		}
	}
}
