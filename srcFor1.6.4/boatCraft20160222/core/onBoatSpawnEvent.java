package boatCraft.core;
import java.util.EnumSet;




import boatCraft.entity.EntityBoatWithBelayingPin;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
public class onBoatSpawnEvent {
	@ForgeSubscribe
	public void entityJoinWorldEvent(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityBoatWithBelayingPin && !event.world.isRemote){
			EntityBoatWithBelayingPin boatentity=(EntityBoatWithBelayingPin)event.entity;
			boatentity.addToUUIDMap();
		}
	}
}
