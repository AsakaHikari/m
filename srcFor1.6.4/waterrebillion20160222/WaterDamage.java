package waterrebillion;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class WaterDamage implements IScheduledTickHandler{

	 @Override
	 public void tickStart(EnumSet<TickType> type, Object... tickData) {
	  
	 }

	 @Override
	 public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		 World world=MinecraftServer.getServer().worldServers[0];
		 List players=world.playerEntities;
		 for(int i=0;i<players.size();i++){
			 EntityPlayer player=(EntityPlayer) players.get(i);
			 if(player.isWet()){
				player.attackEntityFrom(DamageSource.drown, 2.0f); 
			 }
		 }
	  }

	 @Override
	 public EnumSet<TickType> ticks() {
	  return EnumSet.of(TickType.SERVER);
	 }

	 @Override
	 public String getLabel() {
	  return "damager";
	 }

	 @Override
	 public int nextTickSpacing() {
	  return 1;
	 }
	 
	 @ForgeSubscribe
	 public void load(Load e){

	  
	 }

}
