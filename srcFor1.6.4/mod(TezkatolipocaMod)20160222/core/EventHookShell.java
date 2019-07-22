package mod.core;

import java.util.EnumSet;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;
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
import net.minecraftforge.event.world.*;
import net.minecraftforge.event.world.WorldEvent.Load;

public class EventHookShell implements IScheduledTickHandler{

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(MinecraftServer.getServer().worldServers[0] != null){
			World world=MinecraftServer.getServer().worldServers[0];
			NBTTagCompound nbt=world.getWorldInfo().getNBTTagCompound();
			if(world.getTotalWorldTime()>100&&!nbt.getBoolean("isTezSpawned")){
				System.out.println("called");
				Entity e=EntityList.createEntityByName("Tezkatlipoca", world);
				e.setLocationAndAngles(world.getWorldInfo().getSpawnX()+100,world.getWorldInfo().getSpawnY()+30,world.getWorldInfo().getSpawnZ(),0,0);
				world.spawnEntityInWorld(e);
				
				nbt.setBoolean("isTezSpawned", true);
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "tez";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}
	
	@ForgeSubscribe
	public void load(Load e){
		NBTTagCompound nbt=e.world.getWorldInfo().getNBTTagCompound();
		
	}
	
}
