package nooremod.core;

import handmadeguns.HMGItemGunBase;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;


public class EventCaller {
	@SubscribeEvent
	public void onPlayerFire(PlayerUseItemEvent e){
		ItemStack item=e.item;
		EntityPlayer player=e.entityPlayer;
		if(!player.capabilities.isCreativeMode && item.getItem() instanceof HMGItemGunBase){
			System.out.println("fire");
			
			List<Entity> list=player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().expand(16, 16, 16));
			for(Entity entity:list){
				if(entity instanceof EntityMob){
					EntityMob em=(EntityMob)entity;
					if(em.getAttackTarget() ==null){
						em.setAttackTarget(player);
					}
				}
			}
		}
	}
}
