package mochen;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class EntityAITurnToMochen extends EntityAIBase{
	public EntityLivingBase host;
	public int frequency;
	public int timer;
	
	public EntityAITurnToMochen(EntityLivingBase hostEntity,int frequencyTicks){
		this.host=hostEntity;
		this.frequency=frequencyTicks;
	}

	@Override
	public boolean shouldExecute() {
		this.timer++;
		if(this.timer%frequency!=0)return false;
		/*
		List<Coord> mochiList=new ArrayList();
		for(int x=MathHelper.floor_double(host.boundingBox.minX)-2;x<host.boundingBox.maxX+2;x++){
			for(int y=MathHelper.floor_double(host.boundingBox.minY)-2;y<host.boundingBox.maxY+2;y++){
				for(int z=MathHelper.floor_double(host.boundingBox.minZ)-2;z<host.boundingBox.maxZ+2;z++){
					if(host.worldObj.getBlock(x, y, z)==MochenCore.blockMochi){
						mochiList.add(new Coord(x,y,z));
					}
				}
			}
		}
		if(mochiList.isEmpty())return false;
		Coord theCoord=mochiList.get(host.worldObj.rand.nextInt(mochiList.size()));
		host.worldObj.func_147480_a(theCoord.x,theCoord.y,theCoord.z,false);
		EntityMochen mochen=new EntityMochen(host.worldObj);
		mochen.setPosition((double)theCoord.x+0.5, (double)theCoord.y, (double)theCoord.z+0.5);
		host.worldObj.spawnEntityInWorld(mochen);
		*/
		int x=MathHelper.floor_double(host.boundingBox.minX)-2+this.host.worldObj.rand.nextInt(5);
		int y=MathHelper.floor_double(host.boundingBox.minY)-2+this.host.worldObj.rand.nextInt(5);
		int z=MathHelper.floor_double(host.boundingBox.minZ)-2+this.host.worldObj.rand.nextInt(5);
		if(host.worldObj.getBlock(x, y, z)==MochenCore.blockMochi){
			host.worldObj.setBlock(x, y, z, Blocks.air, 0,3);
			EntityMochen mochen=new EntityMochen(host.worldObj);
			mochen.setPosition((double)x+0.5, (double)y, (double)z+0.5);
			host.worldObj.spawnEntityInWorld(mochen);
		}
		return true;
	}

}
