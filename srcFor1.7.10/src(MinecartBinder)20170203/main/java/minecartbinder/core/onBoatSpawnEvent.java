package minecartbinder.core;
import java.util.EnumSet;



import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
public class onBoatSpawnEvent {
	@SubscribeEvent
	public void entityJoinWorldEvent(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityMinecart && !event.world.isRemote){
			BoatCraftCore.UUIDMap.put(event.entity.getUniqueID(), event.entity.getEntityId());
			
		}
	}
}
