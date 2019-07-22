package nocave;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class CancelCaveEvent {
	@SubscribeEvent
	 public void onGenerateCave(LivingSpawnEvent.CheckSpawn e){
		if(e.world.getWorldInfo().getVanillaDimension()==0 && !(e.entityLiving instanceof EntitySlime) && e.y<60){
			e.setResult(Result.DENY);
		}
		
	}
}
