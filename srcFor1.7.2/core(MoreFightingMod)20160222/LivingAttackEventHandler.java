package core;
import item.MoreFightingWeapons;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
public class LivingAttackEventHandler {
	@SubscribeEvent 
	public void onLivingDeathEvent(LivingAttackEvent event) {
		
		if(event.entityLiving instanceof EntityLiving) {
			System.out.println("called");
			if(!event.source.damageType.equals("lava")&&
					!event.source.damageType.equals("inFire")&&
					!event.source.damageType.equals("inWall")&&
					!event.source.damageType.equals("outOfWorld")&&
					!event.source.damageType.equals("onFire")&&
					!event.source.damageType.equals("drawn")&&
					!event.source.damageType.equals("starve")&&
					!event.source.damageType.equals("cactus")){
				
				event.entityLiving.hurtResistantTime=2;
				
				Entity cause=event.source.getEntity();
				/*if(event.entity!=null){
				double d1 = event.entity.posX - event.entityLiving.posX;
                double d0;

                for (d0 = event.entity.posZ - event.entityLiving.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                {
                    d1 = (Math.random() - Math.random()) * 0.01D;
                }
                d1*=-1;
                d0*=-1;
                event.entityLiving.knockBack(event.entity,0, d1, d0);
				}*/
				event.entityLiving.motionX=0.0f;
				event.entityLiving.motionZ=0.0f;
				event.entityLiving.motionY=-1.0f;
				if(cause instanceof EntityLiving){
					EntityLiving causeEntity=(EntityLiving) cause;
					if(causeEntity.getHeldItem()!=null){
						if(causeEntity.getHeldItem().getItem() instanceof MoreFightingWeapons){
							MoreFightingWeapons tool=(MoreFightingWeapons) causeEntity.getHeldItem().getItem();
							if(tool.isCoolTime(causeEntity.getHeldItem(), Minecraft.getMinecraft().theWorld, causeEntity)||
									tool.isReadyTime(causeEntity.getHeldItem(), Minecraft.getMinecraft().theWorld, causeEntity)){
								event.entityLiving.hurtResistantTime=3;
							}
						}
					
					}
				}
			}
		}
	}
}
