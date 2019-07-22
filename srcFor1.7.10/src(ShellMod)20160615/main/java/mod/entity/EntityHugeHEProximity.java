package mod.entity;

import mod.core.ShellCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityHugeHEProximity extends EntityHEProximityFuse{
	public EntityHugeHEProximity(World par1World) {
		super(par1World);
        
        this.setSize(2.98F, 2.98F);
        this.setPosition(this.posX, this.posY+=this.height/2, this.posZ);
        this.stepHeight=0.6f;
        reflection=0.7f;
        destruction=6;
        damage=20.0f;
        this.explosive=7.0f;
        this.maxfuse=0;
	}
	public EntityHugeHEProximity(World par1World, double par2, double par4, double par6)
    {
		this(par1World);
        this.setLocationAndAngles(par2, par4, par6, this.rotationYaw, this.rotationPitch);
    }
	
}
