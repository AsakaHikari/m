package mod.entity;

import mod.core.ShellCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityNormalHEProximity extends EntityHEProximityFuse{
	public EntityNormalHEProximity(World par1World) {
		super(par1World);
        this.setSize(0.98F, 0.98F);
        this.stepHeight=0.6f;
        reflection=0.7;
        destruction=1.5;
        damage=20.0;
        this.explosive=3.5f;
        this.setPosition(this.posX, this.posY, this.posZ);
	}
	public EntityNormalHEProximity(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        this.setLocationAndAngles(par2, par4, par6, this.rotationYaw, this.rotationPitch);
    }
	
}
