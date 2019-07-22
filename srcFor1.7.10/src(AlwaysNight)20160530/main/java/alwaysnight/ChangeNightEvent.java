package alwaysnight;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ChangeNightEvent {
	@SubscribeEvent
	 public void onServerTick(TickEvent.ServerTickEvent event) {
		for (int j = 0; j < MinecraftServer.getServer().worldServers.length; ++j)
        {
			if(MinecraftServer.getServer().worldServers[j].getWorldTime()>22000
					&&MinecraftServer.getServer().worldServers[j].getTotalWorldTime()>AlwaysNightCore.day)
            MinecraftServer.getServer().worldServers[j].setWorldTime(14000);
        }
	}
}
