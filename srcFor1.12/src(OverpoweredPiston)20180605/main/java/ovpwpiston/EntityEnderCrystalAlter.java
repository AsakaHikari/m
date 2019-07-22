package ovpwpiston;

import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.world.World;

public class EntityEnderCrystalAlter extends EntityEnderCrystal {

	public EntityEnderCrystalAlter(World worldIn) {
		super(worldIn);
		this.setSize(1.98F, 1.98F);
	}
	public EntityEnderCrystalAlter(World worldIn, double x, double y, double z){
		
		this(worldIn);
		 this.setPosition(x, y, z);
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		//System.out.println("mean me");
	}
	
	/**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }
}
