package mochen;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
 
 
public class EntityMochen extends EntityAnimal {
	public EntityAIBase aiWander=new EntityAIWander(this,0.5);
	public EntityAIBase aiLookIdle=new EntityAILookIdle(this);
	public EntityAIBase aiWatchMochen= new EntityAIWatchClosest(this, EntityMochen.class, 8.0f);
	public EntityAIBase aiWatchPlayer= new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f);
	public EntityAIBase aiMakeMochen= new EntityAITurnToMochen(this,100);
	
	public boolean isSleeping=false;
	
    public EntityMochen(World world) {
        super(world);
        this.setSize(0.9F, 0.6F);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(1, this.aiMakeMochen);
		this.setTasks();
    }
    
    public EntityMochen(World w,double x,double y,double z){
    	this(w);
    	this.setPosition(x, y, z);
    }
    
    @Override
    public void entityInit(){
    	super.entityInit();
    	this.dataWatcher.addObject(16, (byte)0);
    	this.dataWatcher.addObject(17, (byte)0);
    }
    
    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2.0D);
    }

    
    @Override
    public void onUpdate(){
    	super.onUpdate();
    	//this.updateArmSwingProgress();

    	Entity e=this;
    	byte i=0;
    	while(e.riddenByEntity!=null && i<6){
    		e=e.riddenByEntity;
    		i++;
    	}
    	this.dataWatcher.updateObject(16, i);
    	
    	this.setSize(0.9f+0.2f*i, 0.5f-0.07f*i);
    	
    	if(this.ticksExisted%256==0&&!this.getMoveHelper().isUpdating()){
    		if(this.worldObj.rand.nextInt(32)<10){
    			if(this.isSleeping){
    				this.setTasks();
    				this.isSleeping=false;
    				this.dataWatcher.updateObject(17,(byte)0);
    			}else{
    				this.removeTasks();
    				this.isSleeping=true;
    				this.dataWatcher.updateObject(17,(byte)1);
    			}
    		}
    	}
    	//NBTTagCompound nbt=this.getEntityData().getCompoundTag("Riding");
    	//if(nbt!=null)System.out.println(nbt.getString("id"));
    }
 
    @Override
    public boolean isAIEnabled() { return true; }
    
    @Override
    public boolean interact(EntityPlayer p){
    	if(!this.worldObj.isRemote){
    		ItemStack item=p.getCurrentEquippedItem();
    		if(item==null){
    			Entity e;
    			for(e=this;e.riddenByEntity!=null;e=e.riddenByEntity){
    			}
    			if(e instanceof EntityMochen){
    				e.setDead();
    				EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY , this.posZ, new ItemStack(MochenCore.itemMochen));
    				this.worldObj.spawnEntityInWorld(entityitem);
    			}
    			return true;
    		}
    	}
    	return false;
    }
 
   @Override
    public EnumCreatureAttribute getCreatureAttribute() { return EnumCreatureAttribute.UNDEFINED; }

@Override
public EntityMochen createChild(EntityAgeable p_90011_1_) {
	return new EntityMochen(this.worldObj);
}

@Override
public ItemStack getPickedResult(MovingObjectPosition target){
	return new ItemStack(MochenCore.itemMochen);
}

public void setTasks(){
	this.tasks.addTask(1, this.aiWander);
	this.tasks.addTask(2, this.aiLookIdle);
	this.tasks.addTask(3, this.aiWatchMochen);
	this.tasks.addTask(4, this.aiWatchPlayer);
	
}

public void removeTasks(){
	this.tasks.removeTask(this.aiWander);
	this.tasks.removeTask(this.aiLookIdle);
	this.tasks.removeTask(this.aiWatchMochen);
	this.tasks.removeTask(this.aiWatchPlayer);
}

@SideOnly(Side.CLIENT)
public byte getAmountNum(){
	return this.dataWatcher.getWatchableObjectByte(16);
}

}