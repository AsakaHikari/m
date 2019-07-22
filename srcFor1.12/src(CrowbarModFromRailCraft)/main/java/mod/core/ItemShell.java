package mod.core;

import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.creativetab.CreativeTabs;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShell extends Item{

	public ItemShell()
	{
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.TRANSPORTATION);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}


	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(!world.isRemote){
			
			//EntityShell entityshell=new EntityShell(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));;
			/*
                    switch(this.getDamage(par1ItemStack)){
                    case 0:
                    	EntityNormalShell entityshell = new EntityNormalShell(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityshell.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityshell, entityshell.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityshell);
                    	break;
                    case 1:
                    	EntityBigShell entitybigshell = new EntityBigShell(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entitybigshell.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entitybigshell, entitybigshell.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entitybigshell);
                    	break;
                    case 2:
                    	EntityHugeShell entityhugeshell = new EntityHugeShell(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityhugeshell.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityhugeshell, entityhugeshell.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityhugeshell);
                    	break;
                    case 3:
                    	EntityRubberBall entityrubber = new EntityRubberBall(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityrubber.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityrubber, entityrubber.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityrubber);
                    	break;
                    case 4:
                    	EntityNormalHE entityNormalHE = new EntityNormalHE(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityNormalHE.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityNormalHE, entityNormalHE.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityNormalHE);
                    	break;
                    case 5:
                    	EntityBigHE entityBigHE = new EntityBigHE(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityBigHE.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityBigHE, entityBigHE.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityBigHE);
                    	break;
                    case 6:
                    	EntityHugeHE entityHugeHE = new EntityHugeHE(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityHugeHE.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityHugeHE, entityHugeHE.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityHugeHE);
                    	break;
                    case 7:
                    	EntityNormalPointImpactHE entityNormalPointImpactHE = new EntityNormalPointImpactHE(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityNormalPointImpactHE.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityNormalPointImpactHE, entityNormalPointImpactHE.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityNormalPointImpactHE);
                    	break;
                    case 8:
                    	EntityBigPointImpactHE entityBigPointImpactHE = new EntityBigPointImpactHE(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityBigPointImpactHE.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityBigPointImpactHE, entityBigPointImpactHE.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityBigPointImpactHE);
                    	break;
                    case 9:
                    	EntityHugePointImpactHE entityHugePointImpactHE = new EntityHugePointImpactHE(par3World, (double)((float)par4 + 0.5F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.5F+var3));
                    	entityHugePointImpactHE.rotationYaw = (float)(((MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par3World.getCollidingBoundingBoxes(entityHugePointImpactHE, entityHugePointImpactHE.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return false;
                        }
                            par3World.spawnEntityInWorld(entityHugePointImpactHE);
                    	break;

                    }
			 */
			EntityShell entity=new EntityShell(world);
			entity.setLocationAndAngles(pos.getX()+0.5+(double)facing.getFrontOffsetX()*(entity.width/2+0.5)
					,pos.getY()+0.5+facing.getFrontOffsetY()*(entity.height/2+0.5)-entity.height/2
					,pos.getZ()+0.5+facing.getFrontOffsetZ()*(entity.width/2+0.5),0.0f,0.0f);
			if (!world.getCollisionBoxes(entity, entity.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty())
			{
				return EnumActionResult.PASS;
			}
			world.spawnEntity(entity);


			if (!player.capabilities.isCreativeMode)
			{
				player.getHeldItem(hand).grow(-1);
			}


			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
}
