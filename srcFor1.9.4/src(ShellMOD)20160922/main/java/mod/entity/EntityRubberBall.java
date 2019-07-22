package mod.entity;

import mod.core.ShellCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityRubberBall extends EntityShell{
	public EntityRubberBall(World par1World) {
		super(par1World);
        
        this.setSize(0.98F, 0.98F);
        this.setPosition(this.posX, this.posY+=this.height/2, this.posZ);
        this.stepHeight=0.6f;
        reflection=0.9f;
        destruction=0;
        damage=10.0f;
	}
	public EntityRubberBall(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        this.setLocationAndAngles(par2, par4, par6, this.rotationYaw, this.rotationPitch);
    }
	
}
