package regulararmy.entity.ai;

import java.lang.reflect.Field;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.MathHelper;

public class EntityMoveHelperEx extends EntityMoveHelper {

	public Field Fentity;
	public Field FposX;
	public Field FposY;
	public Field FposZ;
	
	public Field Fspeed;
	public Field Fupdate;
	
	public float angleMovementLimit;
	public EntityMoveHelperEx(EntityLiving p_i1614_1_,float angleMovementLimit) {
		super(p_i1614_1_);
		try {
			this.Fentity=ReflectionHelper.findField(EntityMoveHelper.class,ObfuscationReflectionHelper.remapFieldNames(EntityMoveHelper.class.getName(),"entity","field_75648_a"));
			this.FposX=ReflectionHelper.findField(EntityMoveHelper.class,ObfuscationReflectionHelper.remapFieldNames(EntityMoveHelper.class.getName(),"posX","field_75646_b"));
			this.FposY=ReflectionHelper.findField(EntityMoveHelper.class,ObfuscationReflectionHelper.remapFieldNames(EntityMoveHelper.class.getName(),"posY","field_75647_c"));
			this.FposZ=ReflectionHelper.findField(EntityMoveHelper.class,ObfuscationReflectionHelper.remapFieldNames(EntityMoveHelper.class.getName(),"posZ","field_75644_d"));
			this.Fspeed=ReflectionHelper.findField(EntityMoveHelper.class,ObfuscationReflectionHelper.remapFieldNames(EntityMoveHelper.class.getName(),"speed","field_75645_e"));
			this.Fupdate=ReflectionHelper.findField(EntityMoveHelper.class,ObfuscationReflectionHelper.remapFieldNames(EntityMoveHelper.class.getName(),"update","field_75643_f"));
		
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		this.angleMovementLimit=angleMovementLimit;
	}

    public void onUpdateMoveHelper()
    {
    	EntityLiving entity=null;
    	boolean update=false;
    	double speed=0;
    	double posX=0;
    	double posY=0;
    	double posZ=0;
		try {
			entity = (EntityLiving) Fentity.get(this);
			update=this.Fupdate.getBoolean(this);
			speed=this.Fspeed.getDouble(this);
			posX=this.FposX.getDouble(this);
			posY=this.FposY.getDouble(this);
			posZ=this.FposZ.getDouble(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    	
        entity.setMoveForward(0.0F);
        
        if (update)
        {
            try {
				Fupdate.setBoolean(this, false);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
            int i = MathHelper.floor_double(entity.boundingBox.minY + 0.5D);
            double d0 = posX - entity.posX;
            double d1 = posZ - entity.posZ;
            double d2 = posY - (double)i;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;

            if (d3 >= 2.500000277905201E-7D)
            {
                float f = (float)(Math.atan2(d1, d0) * 180.0D / Math.PI) - 90.0F;
                entity.rotationYaw = this.limitAngle(entity.rotationYaw, f, this.angleMovementLimit);
                entity.setAIMoveSpeed((float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));

                //System.out.println("dist:"+d0 * d0 + d1 * d1);
                if (d2 > 0.0D && d0 * d0 + d1 * d1 < entity.width*entity.width+1)
                {
                    entity.getJumpHelper().setJumping();
                }
            }
        }
    }
    
    /**
     * Limits the given angle to a upper and lower limit.
     */
    public float limitAngle(float p_75639_1_, float p_75639_2_, float p_75639_3_)
    {
        float f3 = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);

        if (f3 > p_75639_3_)
        {
            f3 = p_75639_3_;
        }

        if (f3 < -p_75639_3_)
        {
            f3 = -p_75639_3_;
        }

        return p_75639_1_ + f3;
    }

}
