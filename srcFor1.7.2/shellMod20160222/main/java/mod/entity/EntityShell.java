package mod.entity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class EntityShell extends Entity
{
    private double prevMotionX,prevMotionY,prevMotionZ;
    public byte destruction;
    public float reflection;
    public float damage;

    public EntityShell(World par1World)
    {
        super(par1World);
        
    }


    @Override
	public boolean canBePushed(){
    	return true;
    }
    
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }
    
    public AxisAlignedBB getBoundingBox(){
    	return this.boundingBox;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
    	
    if(!worldObj.isRemote){
    	
       	this.prevMotionX=this.motionX;
       	this.prevMotionY=this.motionY;
       	this.prevMotionZ=this.motionZ;
           
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
            
     	if(this.posY<-1000)this.setDead();
     	
        
        int j=1+
        		(int)((Math.max(Math.max(Math.abs(motionX),Math.abs(motionY)),Math.max(Math.abs(motionY),Math.abs(motionZ)))
        				-Math.max(Math.max(Math.abs(motionX),Math.abs(motionY)),Math.max(Math.abs(motionY),Math.abs(motionZ)))%0.9)/0.9);
        //System.out.println("j="+j);
        
        //System.out.println("position:"+posX+","+posY+","+posZ);
        for(int i=1;i<=j;i++){
        
    	int minX = MathHelper.floor_double(this.boundingBox.minX - 0.0D);
        int minY = MathHelper.floor_double(this.boundingBox.minY - 0.0D);
        int minZ = MathHelper.floor_double(this.boundingBox.minZ - 0.0D);
        int maxX = MathHelper.floor_double(this.boundingBox.maxX + 0.0D);
        int maxY = MathHelper.floor_double(this.boundingBox.maxY + 0.0D);
        int maxZ = MathHelper.floor_double(this.boundingBox.maxZ + 0.0D);
        
        
        //System.out.println(maxX+","+maxY+","+maxZ+","+minX+","+minY+","+minZ+",");        
        boolean xflag=false;
        boolean yflag=false;
        boolean zflag=false;
        

        if (this.worldObj.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ))
        {
            for (int k1 = minX; k1 <= maxX; ++k1)
            {
                for (int l1 = minY; l1 <= maxY; ++l1)
                {
                    for (int i2 = minZ; i2 <= maxZ; ++i2)
                    {
                    	if(!(this.worldObj.isAirBlock(k1, l1, i2))){
                    		
                    		Block target=this.worldObj.getBlock(k1, l1, i2);
                    		target.setBlockBoundsBasedOnState(worldObj, k1, l1, i2);
                    		AxisAlignedBB tAABB=target.getCollisionBoundingBoxFromPool(worldObj, 0, 0, 0);
                    		if(tAABB!=null){
                    		if(this.boundingBox.intersectsWith(AxisAlignedBB.getBoundingBox(tAABB.minX+k1,tAABB.minY+l1,tAABB.minZ+i2,
                    				tAABB.maxX+k1,tAABB.maxY+l1,tAABB.maxZ+i2))){
                    			double relativeX,relativeY,relativeZ;
                    			relativeX=this.posX-k1-(tAABB.maxX+tAABB.minX)/2;
                    			relativeY=this.posY-l1-(tAABB.maxY+tAABB.minY)/2;
                    			relativeZ=this.posZ-i2-(tAABB.maxZ+tAABB.minZ)/2;
                    			//System.out.println(relativeX+","+relativeY+","+relativeZ);
                    			
                    			relativeX*=(tAABB.maxY-tAABB.minY)*(tAABB.maxZ-tAABB.minZ);
                    			relativeY*=(tAABB.maxX-tAABB.minX)*(tAABB.maxZ-tAABB.minZ);
                    			relativeZ*=(tAABB.maxY-tAABB.minY)*(tAABB.maxX-tAABB.minX);
                    			
                    			float hardness=(target.getBlockHardness(worldObj, k1, l1, i2)+(2.0f-target.getBlockHardness(worldObj, k1, l1, i2))/2)/this.destruction;
                    			if(hardness<0.3f)hardness=Float.MAX_VALUE;
                    			try{
                    				if(Math.abs(relativeX)>=Math.abs(relativeY)&&Math.abs(relativeX)>=Math.abs(relativeZ)){
                    					//System.out.println("CollidedX("+k1+","+l1+","+i2+")");
                    					if(!((target instanceof BlockDynamicLiquid)||(target instanceof BlockStaticLiquid))&&!xflag){
                    						if(motionX>0&&relativeX<0){
                    							this.onCollidedWithBlock(k1, l1, i2, ForgeDirection.WEST);
                    							if(this.motionX>hardness){
                    								this.motionX-=hardness;
                    								this.worldObj.func_147480_a(k1, l1, i2, true);
                    								this.onBreakBlock(k1, l1, i2, ForgeDirection.WEST);
                    							}else{
                    								xflag=true;
                    								this.boundingBox.minX=k1+tAABB.minX-(this.boundingBox.maxX-this.boundingBox.minX);
                    								this.boundingBox.maxX=k1+tAABB.minX;
                    								this.motionX*=-this.reflection;
                    								this.onFailedToBreakBlock(k1, l1, i2, ForgeDirection.WEST);
                    							}
                    						}else if(motionX<=0&&relativeX>=0){
                    							this.onCollidedWithBlock(k1, l1, i2, ForgeDirection.EAST);
                    							if(-this.motionX>hardness){
                    								this.motionX+=hardness;
                    								this.worldObj.func_147480_a(k1, l1, i2, true);
                    								this.onBreakBlock(k1, l1, i2, ForgeDirection.EAST);
                    							}else{
                    								xflag=true;
                    								this.boundingBox.maxX=k1+tAABB.maxX+(this.boundingBox.maxX-this.boundingBox.minX);
                    								this.boundingBox.minX=k1+tAABB.maxX;
                    								this.motionX*=-this.reflection;
                    								this.onFailedToBreakBlock(k1, l1, i2, ForgeDirection.EAST);
                    							}
                    						}
                    					}
                    				}else if(Math.abs(relativeY)>=Math.abs(relativeX)&&Math.abs(relativeY)>=Math.abs(relativeZ)){
                    					//System.out.println("CollidedY("+k1+","+l1+","+i2+")");
                    					if(!((target instanceof BlockDynamicLiquid)||(target instanceof BlockStaticLiquid))&&!yflag){
                    						if(motionY>0&&relativeY<0){
                    							this.onCollidedWithBlock(k1, l1, i2, ForgeDirection.DOWN);
                    							if(this.motionY>hardness){
                    								this.motionY-=hardness;
                    								this.worldObj.func_147480_a(k1, l1, i2, true);
                    								this.onBreakBlock(k1, l1, i2, ForgeDirection.DOWN);
                    							}else{
                    								yflag=true;
                    								this.boundingBox.minY=l1+tAABB.minY-(this.boundingBox.maxY-this.boundingBox.minY);
                    								this.boundingBox.maxY=l1+tAABB.minY;
                    								this.motionY*=-this.reflection;
                    								this.onFailedToBreakBlock(k1, l1, i2, ForgeDirection.DOWN);
                    							}
                    						}else if(motionY<=0&&relativeY>=0){
                    							this.onCollidedWithBlock(k1, l1, i2, ForgeDirection.UP);
                    							if(-this.motionY>hardness){
                    								this.motionY+=hardness;
                    								this.worldObj.func_147480_a(k1, l1, i2, true);
                    								this.onBreakBlock(k1, l1, i2, ForgeDirection.UP);
                    							}else{
                    								yflag=true;
                    								this.boundingBox.maxY=l1+tAABB.maxY+(this.boundingBox.maxY-this.boundingBox.minY);
                    								this.boundingBox.minY=l1+tAABB.maxY;
                    								this.motionY*=-this.reflection;
                    								this.onFailedToBreakBlock(k1, l1, i2, ForgeDirection.UP);
                    							}
                    						}
                    					}
                    				}else if(Math.abs(relativeZ)>=Math.abs(relativeY)&&Math.abs(relativeZ)>=Math.abs(relativeX)){
                    			
                    					//System.out.println("CollidedZ("+k1+","+l1+","+i2+")");
                    					if(!((target instanceof BlockDynamicLiquid)||(target instanceof BlockStaticLiquid))&&!zflag){
                    						if(motionZ>0&&relativeZ<0){
                    							this.onCollidedWithBlock(k1, l1, i2, ForgeDirection.NORTH);
                    							if(this.motionZ>hardness){
                    								this.motionZ-=hardness;
                    								this.worldObj.func_147480_a(k1, l1, i2, true);
                    								this.onBreakBlock(k1, l1, i2, ForgeDirection.NORTH);
                    							}else{
                    								zflag=true;
                    								this.boundingBox.minZ=i2+tAABB.minZ-(this.boundingBox.maxZ-this.boundingBox.minZ);
                    								this.boundingBox.maxZ=i2+tAABB.minZ;
                    								this.motionZ*=-this.reflection;
                    								this.onFailedToBreakBlock(k1, l1, i2, ForgeDirection.NORTH);
                    							}
                    						}else if(motionZ<=0&&relativeZ>=0){
                    							this.onCollidedWithBlock(k1, l1, i2, ForgeDirection.SOUTH);
                    							if(-this.motionZ>hardness){
                    								this.motionZ+=hardness;
                    								this.worldObj.func_147480_a(k1, l1, i2, true);
                    								this.onBreakBlock(k1, l1, i2, ForgeDirection.SOUTH);
                    							}else{
                    								zflag=true;
                    								this.boundingBox.maxZ=i2+tAABB.maxZ+(this.boundingBox.maxZ-this.boundingBox.minZ);
                    								this.boundingBox.minZ=i2+tAABB.maxZ;
                    								this.motionZ*=-this.reflection;
                    								this.onFailedToBreakBlock(k1, l1, i2, ForgeDirection.SOUTH);
                    							}
                    						}
                    					}
                    				}
                    			
                    			}catch(NullPointerException e){
                    			
                    			}
                    		}
                    		}
                    	}
                    }
                }
            }
        }
        
        
        this.boundingBox.offset(motionX/j, motionY/j, motionZ/j);
        this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
        this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
        this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
        List entityList=worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
        if(!entityList.isEmpty()){
        	
        	for(int i1=0;i1<entityList.size();i1++){
        		if(entityList.get(i1) instanceof Entity){
        			
        			Entity entity=(Entity)entityList.get(i1);
        			//System.out.println("collided with"+entity.getClass().getSimpleName());
        			if(MathHelper.sqrt_double(Math.pow(motionX, 2.0)+Math.pow(motionY, 2.0)+Math.pow(motionZ, 2.0))>0.5){
        				
        				double damage=this.damage*(MathHelper.sqrt_double(Math.pow(motionX, 2.0)+Math.pow(motionY, 2.0)+Math.pow(motionZ, 2.0))-0.5);
        				entity.attackEntityFrom(DamageSource.generic, (float)damage);
        				
        			}
        			if (entity != this.riddenByEntity && entity.canBePushed())
                    {
        				if(entity instanceof EntityShell){
        					double motionX_=this.motionX;
        					double motionY_=this.motionY;
        					double motionZ_=this.motionZ;
        					this.motionX=entity.motionX;
        					this.motionY=entity.motionY;
        					this.motionZ=entity.motionZ;
        					entity.motionX=motionX_;
        					entity.motionY=motionY_;
        					entity.motionZ=motionZ_;
        				}else{
        					double motionX_=this.motionX;
        					double motionY_=this.motionY;
        					double motionZ_=this.motionZ;
        					this.motionX=this.motionX/5*4+entity.motionX/5;
        					this.motionY=this.motionY/5*4+entity.motionY/5;
        					this.motionZ=this.motionZ/5*4+entity.motionZ/5;
        					entity.motionX=motionX_;
        					entity.motionY=motionY_;
        					entity.motionZ=motionZ_;
        				}
    				
                        entity.applyEntityCollision(this);
                    }
        		}
        	}
        	
        }
        try
        {
            this.func_145775_I();
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity tile collision");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
        }
        this.motionY -= 0.03;
        if(this.posY<192){
     	   this.motionX *= 0.98D;
     	   this.motionY *= 0.98D;
     	   this.motionZ *= 0.98D;
        }
       // System.out.println("motion:"+motionX+","+motionY+","+motionZ);
        
        }
    this.setPosition(this.posX, this.posY, this.posZ);
    }

    
    
    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */
    protected float getMotionFactor()
    {
        return 0.98F;
    }

   
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
    	nbt.setFloat("reflection", this.reflection);
    	nbt.setFloat("damage", this.damage);
    	nbt.setByte("destruction", this.destruction);
    	
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
    	this.reflection=nbt.getFloat("reflection");
    	this.damage=nbt.getFloat("damage");
    	this.destruction=nbt.getByte("destruction");
    }
   
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    
    /**
     * Called when the entity is attacked.
     */
    

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }


	@Override
	protected void entityInit() {
		
	}
	
	/**
	 * called when this broke a block
	 * @param x block xcoord
	 * @param y block ycoord
	 * @param z block zcoord
	 * @param fd side which this collided
	 */
	public void onBreakBlock(int x,int y,int z,ForgeDirection fd){
	}
	
	/**
	 * called when this failed to break a block
	 * @param x block xcoord
	 * @param y block ycoord
	 * @param z block zcoord
	 * @param fd side which this collided
	 */
	public void onFailedToBreakBlock(int x,int y,int z,ForgeDirection fd){
	}
	
	/**
	 * called when this collided with a block
	 * @param x block xcoord
	 * @param y block ycoord
	 * @param z block zcoord
	 * @param fd side which this collided
	 */
	public void onCollidedWithBlock(int x,int y,int z,ForgeDirection fd){
	}
}
