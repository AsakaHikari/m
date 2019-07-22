package regulararmy.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import regulararmy.analysis.DataAnalyzerOneToOne;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.command.RegularArmyLeader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;

public class RegularArmyEventHandler {
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent e){
	}
	
	@SubscribeEvent
	public void onWorldLoaded(WorldEvent.Load e){
		/*
		int binary=e.world.getWorldInfo().getNBTTagCompound().getInteger("leadersListBinary");
		System.out.println("binary="+binary);
		for(int i=0;i<32;i++){
			if((binary&(1<<i))!=0){
				MonsterRegularArmyCore.leaders[i]=new RegularArmyLeader(e.world,e.world.getWorldInfo().getNBTTagCompound().getCompoundTag("leader"+i),(byte)i);
			}
		}
		*/
		if(e.world.isRemote)return;
		File file2=new File(((SaveHandler) (e.world.getSaveHandler())).getWorldDirectory(),"MRAdata");
			if(!file2.exists()){
				file2.mkdir();
				return;
			}
			File fileIDMap=new File(file2,"monsterRegularArmy_IDs.dat");
			if(!fileIDMap.exists()){
				try {
					fileIDMap.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}else{
				FileInputStream fileinputstream;
				NBTTagCompound nbttagcompound=null;
				try {
					fileinputstream = new FileInputStream(fileIDMap);
					nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
					fileinputstream.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if(nbttagcompound!=null){
					for(int i=0;nbttagcompound.hasKey("name"+i);i++){
						MonsterRegularArmyCore.entityIDList.add(nbttagcompound.getString("name"+i));
						//System.out.println(MonsterRegularArmyCore.entityIDList.get(i)+" id:"+i);
					}
				}
			}
			
			File file1=new File(file2,"leaderDIM"+e.world.provider.dimensionId+".dat");
			if(!file1.exists()){
				try {
					file1.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
            FileInputStream fileinputstream;
            NBTTagCompound nbttagcompound=null;
			try {
				fileinputstream = new FileInputStream(file1);
				nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
	            fileinputstream.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(nbttagcompound==null)return;
			if(!nbttagcompound.hasKey("data"))System.out.println("Tag\"data\"does not exist");
            NBTTagCompound nbt=nbttagcompound.getCompoundTag("data");
            if(!nbt.hasKey("leadersListBinary"))System.out.println("Tag\"leadersListBinary\"does not exist");
            int binary=nbt.getInteger("leadersListBinary");
    		//System.out.println("binary="+binary);
    		for(int i=0;i<32;i++){
    			if((binary&(1<<i))!=0){
    				MonsterRegularArmyCore.leaders[i]=new RegularArmyLeader(e.world,nbt.getCompoundTag("leader"+i),(byte)i);
    				MonsterRegularArmyCore.leadersNum=i;
    			}
    		}
		
			
		
		
	}
	
	@SubscribeEvent
	public void onWorldSaved(WorldEvent.Save e){
		/*
		int binary=0;
		for(int i=0;i<MonsterRegularArmyCore.leadersNum+1;i++){
			if(MonsterRegularArmyCore.leaders[i]!=null&&MonsterRegularArmyCore.leaders[i].theWorld==e.world){
				RegularArmyLeader leader=MonsterRegularArmyCore.leaders[i];
				
				e.world.getWorldInfo().getNBTTagCompound().setCompoundTag("leader"+i, leader.writeToNBT(new NBTTagCompound()));
				binary+=(1<<i);
			}
		}
		
		e.world.getWorldInfo().getNBTTagCompound().setInteger("leadersListBinary", binary);
		*/
		if(e.world.isRemote)return;
		int binary=0;
		NBTTagCompound nbt1=new NBTTagCompound();
		for(int i=0;i<MonsterRegularArmyCore.leadersNum+1;i++){
			if(MonsterRegularArmyCore.leaders[i]!=null&&MonsterRegularArmyCore.leaders[i].theWorld==e.world){
				nbt1.setTag("leader"+i, MonsterRegularArmyCore.leaders[i].writeToNBT(new NBTTagCompound()));
				//MonsterRegularArmyCore.leaders[i]=null;
				binary+=(1<<i);
			}
		}
		//System.out.println("binary="+binary);
		nbt1.setInteger("leadersListBinary", binary);
		NBTTagCompound nbt=new NBTTagCompound();
		nbt.setTag("data", nbt1);
		
		File file1=new File(((SaveHandler) (e.world.getSaveHandler())).getWorldDirectory(),"MRAdata");
		
		if(file1!=null){
			try {
				if(!file1.exists()){
					file1.mkdir();
				}
				File file2=new File(file1,"leaderDIM"+e.world.provider.dimensionId+".dat");
				if(file2!=null){
					if(!file2.exists())file2.createNewFile();
					FileOutputStream fis=new FileOutputStream(file2);
					CompressedStreamTools.writeCompressed(nbt, fis);
					fis.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		File fileIDMap=new File(file1,"monsterRegularArmy_IDs.dat");
		if(!fileIDMap.exists()){
			try {
				fileIDMap.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		NBTTagCompound nbttagcompound=new NBTTagCompound();
		for(int i=0;i<MonsterRegularArmyCore.entityIDList.size();i++){
			nbttagcompound.setString("name"+i,MonsterRegularArmyCore.entityIDList.get(i));
			//System.out.println(MonsterRegularArmyCore.entityIDList.get(i)+" id:"+i);
		}
		try {
			FileOutputStream fileinputstream=new FileOutputStream(fileIDMap);
			CompressedStreamTools.writeCompressed(nbt, fileinputstream);
			fileinputstream.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	
	@SubscribeEvent
	public void onWorldUnloaded(WorldEvent.Unload e){
		if(e.world.isRemote)return;
		for(int i=0;i<MonsterRegularArmyCore.leadersNum+1;i++){
			if(MonsterRegularArmyCore.leaders[i]!=null&&MonsterRegularArmyCore.leaders[i].theWorld==e.world){
				MonsterRegularArmyCore.leaders[i]=null;
			}
		}
	}

	@SubscribeEvent
	public void tickStart(TickEvent.ServerTickEvent e) {
		
		if(e.phase!=Phase.START)return;
		//System.out.println(e.side.toString()+","+e.type.toString());
		if(MonsterRegularArmyCore.noAttackWhenLaggy>0){
			//System.out.println("Laggy timer:"+MonsterRegularArmyCore.laggyTimer);
			long time=System.currentTimeMillis();
			if(time-MonsterRegularArmyCore.lastTimeMillis>(1000/MonsterRegularArmyCore.noAttackWhenLaggy)){
				MonsterRegularArmyCore.laggyTimer=20;
				//System.out.println("Laggy!");
			}
			MonsterRegularArmyCore.lastTimeMillis=time;
		}
		MonsterRegularArmyCore.laggyTimer--;
		
		for(int i=0;i<MonsterRegularArmyCore.leadersNum+1;i++){
			if(MonsterRegularArmyCore.leaders[i]!=null&&MonsterRegularArmyCore.leaders[i].theWorld==MinecraftServer.getServer().getEntityWorld()){
				MonsterRegularArmyCore.leaders[i].onUpdate();
			}
		}
		if(MonsterRegularArmyCore.leadersNum>-1&&MonsterRegularArmyCore.leaders[MonsterRegularArmyCore.leadersNum]==null){
			MonsterRegularArmyCore.leadersNum--;
		}
	}
	/*
	@SubscribeEvent
	public void onAttack(LivingHurtEvent e){
		if(e.source.getSourceOfDamage() instanceof EntityRegularArmy){
			EntityRegularArmy entity=(EntityRegularArmy)e.source.getSourceOfDamage();
			double rx=entity.posX-e.entityLiving.posX;
			double ry=entity.posY-e.entityLiving.posY;
			double rz=entity.posZ-e.entityLiving.posZ;
			entity.addDistanceNode(new DataAnalyzerOneToOne.DataNode(MathHelper.sqrt_double(rx*rx+ry*ry+rz*rz), new int[]{(int) e.ammount+1}));
		}
	}
	*/

}
