package regulararmy.entity.ai;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import regulararmy.core.MonsterRegularArmyCore;
import regulararmy.entity.EntityRegularArmy;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EntityAILearnedTarget extends EntityRegularAIBase
{
    private final int targetChance;
    public EntityRegularArmy taskOwner;
    public boolean shouldCheckSight;
    public byte timer=0;
    /**
     * This filter is applied to the Entity search.  Only matching entities will be targetted.  (null -> no
     * restrictions)
     */
    private EntityLivingBase targetEntity;
    
    public IEntitySelector selector;
    public Comparator comparator;

    public EntityAILearnedTarget(EntityRegularArmy p_i1663_1_,int p_i1663_3_, boolean p_i1663_4_)
    {
        this(p_i1663_1_,  p_i1663_3_, p_i1663_4_, (IEntitySelector)null);
    }

    public EntityAILearnedTarget(EntityRegularArmy hostEntity, int targetChance, boolean shouldCheckSight, final IEntitySelector selector)
    {
    	super();
        this.targetChance = targetChance;
        this.taskOwner=hostEntity;
        this.shouldCheckSight=shouldCheckSight;
        this.setMutexBits(1);
        if(selector==null){
        	this.selector=new EnemySelector(shouldCheckSight,hostEntity);
        }
        this.comparator=new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				EntityRegularArmy owner=EntityAILearnedTarget.this.taskOwner;
				Vec3 vec3=Vec3.createVectorHelper(owner.posX, owner.posY, owner.posZ);
				Entity e1=(Entity)o1;
				Entity e2=(Entity)o2;
				Integer costIE1=owner.getSettings().dangerousSupporter.get(EntityRegularArmy.getCustomEntitySharedID(e1));
				Integer costIE2=owner.getSettings().dangerousSupporter.get(EntityRegularArmy.getCustomEntitySharedID(e2));
				
				int costE1=0,costE2=0;
				if(costIE1!=null){
					costE1=costIE1/10;
				}
				if(costIE2!=null){
					costE2=costIE2/10;
				}
				if(e1 instanceof EntityPlayer){
					costE1+=20+costE2;
				}
				if(e2 instanceof EntityPlayer){
					costE2+=20+costE1;
				}
				//System.out.println(e1.toString()+" cost:"+costIE1);
				//System.out.println(e2.toString()+" cost:"+costIE2);
				int scoreE1=(int)-e1.getDistanceToEntity(owner)+costE1;
				int scoreE2=(int)-e2.getDistanceToEntity(owner)+costE2;
				if(scoreE2<scoreE1){
					return 1;
				}else{
					return -1;
				}
			}
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else
        {
        	double d0=taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
        	double d1=4;
        	this.timer++;
        	if(this.timer==30){
        		switch(this.taskOwner.worldObj.rand.nextInt(3)){
        		case 0:
        			d0*=2;
        			break;
        		case 1:
        			d1=30;
        			break;
        		case 2:
        			d0*=2;
        			d1=30;
        			break;
        		}
        		this.timer=0;
        	}
        	
        	AxisAlignedBB sight=this.taskOwner.boundingBox.expand(d0, d1, d0).offset(d0*0.8*this.taskOwner.getLookVec().xCoord, 0, d0*0.8*this.taskOwner.getLookVec().zCoord);
            Entity[] array = (Entity[]) this.taskOwner.worldObj.getEntitiesWithinAABBExcludingEntity(this.taskOwner,sight ,this.selector).toArray(new Entity[0]);
            if(!this.taskOwner.worldObj.isRemote){
        		//System.out.println("boundingBox:"+sight.toString());
            }

            if (array.length==0)
            {
            	/*
            	if(!this.taskOwner.worldObj.isRemote){
            	System.out.println("target:null");
            	}
            	*/
                return false;
            }
            try{
            	Arrays.sort(array,this.comparator);
            }catch(Exception e){
            	
            }
            /*
            if(!this.taskOwner.worldObj.isRemote){
            System.out.println("target:"+array[0].getClass().getSimpleName());
            }
            */
            Integer costIE1=this.taskOwner.getSettings().dangerousSupporter.get(EntityRegularArmy.getCustomEntitySharedID(array[0]));
            if(costIE1==null || costIE1>2){
            	this.targetEntity = (EntityLivingBase)array[0];
            	return true;
            }else{
            	return false;
            }
            
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
    
    public static class EnemySelector implements IEntitySelector{
    	public boolean shouldCheckSight;
    	public EntityRegularArmy taskOwner;
    	public EnemySelector(boolean shouldCheckSight,EntityRegularArmy taskOwner){
    		this.shouldCheckSight=shouldCheckSight;
    		this.taskOwner=taskOwner;
    	}
		@Override
		public boolean isEntityApplicable(Entity e){
    		if(!(e instanceof EntityLivingBase)||
    				e.isEntityInvulnerable()||
    				e.isDead||
    				(e instanceof EntityRegularArmy)||
    				(e.riddenByEntity instanceof EntityRegularArmy) ||
    				(this.shouldCheckSight&&!this.taskOwner.getEntitySenses().canSee(e))||
    				(!MonsterRegularArmyCore.doTargetPlayers&&(e instanceof EntityPlayer))||
    				((e instanceof EntityHorse) && e.riddenByEntity==null)){
    			return false;
    		}
    		return ((e instanceof EntityPlayer )&&!((EntityPlayer)e).capabilities.disableDamage)
    				||( e.riddenByEntity instanceof EntityPlayer && !((EntityPlayer)e.riddenByEntity).capabilities.disableDamage)
    				||(this.taskOwner.getSettings().dangerousSupporter.containsKey(EntityRegularArmy.getCustomEntitySharedID(e)));
    	}
    	
    }

}