package regulararmy.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.command.RegularArmyLeader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;

public class RegularArmyEventHandler implements IScheduledTickHandler{
	@ForgeSubscribe
	public void onEntityJoinWorld(EntityJoinWorldEvent e){
	}
	
	@ForgeSubscribe
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
	
	@ForgeSubscribe
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
				nbt1.setCompoundTag("leader"+i, MonsterRegularArmyCore.leaders[i].writeToNBT(new NBTTagCompound()));
				//MonsterRegularArmyCore.leaders[i]=null;
				binary+=(1<<i);
			}
		}
		//System.out.println("binary="+binary);
		nbt1.setInteger("leadersListBinary", binary);
		NBTTagCompound nbt=new NBTTagCompound();
		nbt.setCompoundTag("data", nbt1);
		
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
	}
	
	@ForgeSubscribe
	public void onWorldUnloaded(WorldEvent.Unload e){
		if(e.world.isRemote)return;
		for(int i=0;i<MonsterRegularArmyCore.leadersNum+1;i++){
			if(MonsterRegularArmyCore.leaders[i]!=null&&MonsterRegularArmyCore.leaders[i].theWorld==e.world){
				MonsterRegularArmyCore.leaders[i]=null;
			}
		}
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		for(int i=0;i<MonsterRegularArmyCore.leadersNum+1;i++){
			if(MonsterRegularArmyCore.leaders[i]!=null&&MonsterRegularArmyCore.leaders[i].theWorld==tickData[0]){
				MonsterRegularArmyCore.leaders[i].onUpdate();
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		
		return "MRA";
	}

	@Override
	public int nextTickSpacing() {
		return 1;
	}
}
