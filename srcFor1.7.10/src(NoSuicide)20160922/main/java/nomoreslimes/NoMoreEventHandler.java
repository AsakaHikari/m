package nomoreslimes;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Set;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

public class NoMoreEventHandler
{
  @SubscribeEvent
  public void spawnCheck(LivingHurtEvent event)
  {
	  if(event.entityLiving instanceof EntityPlayer && !event.source.canHarmInCreative()){
		  if(event.source.getSourceOfDamage()==null || event.source.getSourceOfDamage()==event.entityLiving){
			  if(event.ammount>=event.entityLiving.getHealth() && event.ammount<100){
				  event.ammount=event.entityLiving.getHealth()-1;
			  }
		  }
	  }
    
  }

}