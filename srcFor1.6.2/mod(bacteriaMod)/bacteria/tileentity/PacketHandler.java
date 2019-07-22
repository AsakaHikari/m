package mod.bacteria.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
 



import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
 



import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
 



import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
 
public class PacketHandler implements IPacketHandler {
 
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
	
		
		if(packet.channel.equals("GCCont")){
			/*
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
		
		
 
			TileEntity tileentity = null;
 
			World worldClient = FMLClientHandler.instance().getClient().theWorld;
			World worldServer = ((EntityPlayer)player).worldObj;
 
			if(worldClient != null && worldServer == null){
				tileentity = worldClient.getBlockTileEntity(x, y, z);
			}
			if(worldServer != null){
				tileentity = worldServer.getBlockTileEntity(x, y, z);
			}
			if (tileentity != null) {
				
				if(tileentity instanceof GenomeChangerTileEntity) ((GenomeChangerTileEntity)tileentity).readToPacket(data);
			}
			*/
		}
		
		if(packet.channel.equals("GCBact")){
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
		
		
 
			TileEntity tileentity = null;
 
			World worldClient = FMLClientHandler.instance().getClient().theWorld;
			World worldServer = ((EntityPlayer)player).worldObj;
 
			if(worldClient != null && worldServer == null){
				tileentity = worldClient.getBlockTileEntity(x, y, z);
			}
			if(worldServer != null){
				tileentity = worldServer.getBlockTileEntity(x, y, z);
			}
			if(tileentity!=null){
				if(tileentity instanceof GenomeChangerTileEntity) ((GenomeChangerTileEntity)tileentity).receiveItemData(data);
			}
		}
		if(packet.channel.equals("UMBact")){
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
		
		
 
			TileEntity tileentity = null;
 
			World worldClient = FMLClientHandler.instance().getClient().theWorld;
			World worldServer = ((EntityPlayer)player).worldObj;
 
			if(worldClient != null && worldServer == null){
				tileentity = worldClient.getBlockTileEntity(x, y, z);
			}
			if(worldServer != null){
				tileentity = worldServer.getBlockTileEntity(x, y, z);
			}
			if(tileentity!=null){
				if(tileentity instanceof U_MChangerTileEntity) ((U_MChangerTileEntity)tileentity).receiveItemData(data);
			}
		}
	}
 
	/*
	 public static Packet getPacketTileEntity(GenomeChangerTileEntity tileentity)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		
		int x = tileentity.xCoord;
		int y = tileentity.yCoord;
		int z = tileentity.zCoord;
		
 
		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			tileentity.writeToPacket(dos);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
 
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "GCCont";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = true;
 
		return packet;
	}
	*/
	
	public static Packet sendPacketBacteriaStatus(byte nbtNum,byte nbtValue,TileEntity tileentity){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
 		try
		{
			
			dos.writeInt(tileentity.xCoord);
			dos.writeInt(tileentity.yCoord);
			dos.writeInt(tileentity.zCoord);
			dos.writeByte(nbtNum);
			dos.writeByte(nbtValue);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "GCBact";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = false;
 
		return packet;
	}
	public static Packet sendPacketU_MStatus(byte nbtNum,int nbtValue,TileEntity tileentity){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
 		try
		{
			
			dos.writeInt(tileentity.xCoord);
			dos.writeInt(tileentity.yCoord);
			dos.writeInt(tileentity.zCoord);
			dos.writeByte(nbtNum);
			dos.writeInt(nbtValue);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "UMBact";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = false;
 
		return packet;
	}
 
}