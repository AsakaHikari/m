package waterrebillion;

import java.util.EnumSet;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.*;
import net.minecraftforge.event.world.WorldEvent.Load;

public class WaterInfect implements IScheduledTickHandler{

 @Override
 public void tickStart(EnumSet<TickType> type, Object... tickData) {
  
 }

 @Override
 public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	 World world=MinecraftServer.getServer().worldServers[0];
	 
	 for(int i=0;i<world.activeChunkSet.size();i++){
		 Object obj=world.activeChunkSet.toArray()[i];
		 if(obj!=null&&obj instanceof ChunkCoordIntPair){
			 ChunkCoordIntPair ccip=(ChunkCoordIntPair) obj;
			 Chunk chunk = world.getChunkFromChunkCoords(ccip.chunkXPos, ccip.chunkZPos);
			 for(int j=0;j<30;j++){
			 int x=((int)Math.floor(Math.random()*15.99));
			 int y=((int)Math.floor(Math.random()*(chunk.heightMap.length-0.1)));
			 int z=((int)Math.floor(Math.random()*15.99));
			 
			 if(chunk.getBlockID(x, y, z)==Block.waterStill.blockID){
				 x+=chunk.xPosition*16;
				 z+=chunk.zPosition*16;
				 if(world.getBlockId(x+1, y, z)==0||world.getBlockId(x+1, y, z)==Block.waterMoving.blockID){
					 world.setBlock(x+1, y, z, Block.waterStill.blockID);
			 	}else if(world.getBlockId(x, y, z+1)==0||world.getBlockId(x, y, z+1)==Block.waterMoving.blockID){
			 		world.setBlock(x, y, z+1, Block.waterStill.blockID);
				}else if(world.getBlockId(x-1, y, z)==0||world.getBlockId(x-1, y, z)==Block.waterMoving.blockID){
					world.setBlock(x-1, y, z, Block.waterStill.blockID);
				}else if(world.getBlockId(x, y, z-1)==0||world.getBlockId(x, y, z-1)==Block.waterMoving.blockID){
					world.setBlock(x, y, z-1, Block.waterStill.blockID);
				}else if(world.getBlockId(x, y+1, z)==0||world.getBlockId(x, y+1, z)==Block.waterMoving.blockID){
					world.setBlock(x, y+1, z, Block.waterStill.blockID);
				}else if(world.getBlockId(x, y-1, z)==0||world.getBlockId(x, y-1, z)==Block.waterMoving.blockID){
					world.setBlock(x, y-1, z, Block.waterStill.blockID);
				}
			 }
			 }
			 
		 //System.out.println(x+","+y+","+z);
		 }
	 }
  }

 @Override
 public EnumSet<TickType> ticks() {
  return EnumSet.of(TickType.SERVER);
 }

 @Override
 public String getLabel() {
  return "infector";
 }

 @Override
 public int nextTickSpacing() {
  return 10;
 }
 
 @ForgeSubscribe
 public void load(Load e){

  
 }
 
}