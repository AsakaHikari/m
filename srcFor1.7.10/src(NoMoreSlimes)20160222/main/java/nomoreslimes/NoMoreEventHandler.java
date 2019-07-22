package nomoreslimes;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Set;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;

public class NoMoreEventHandler
{
  @SubscribeEvent
  public void spawnCheck(LivingSpawnEvent.CheckSpawn event)
  {
	  if(EntityList.getEntityString(event.entityLiving).equals("Slime")){
		  event.setResult(Result.DENY);
	  }
    
  }

}