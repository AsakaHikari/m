package cannonmod.item;

import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.creativetab.CreativeTabs;

import java.math.BigDecimal;
import java.util.List;

import cannonmod.entity.EntityCannon;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemArty extends Item{
	
	public static String[] camoList={
		"None","Woodland","Desert","Urban","Snow"
	};

	public ItemArty()
    {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        //this.setMaxDamage(0);
        //this.setHasSubtypes(true);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advanced) {
		NBTTagCompound nbt=itemStack.getTagCompound();
		if(nbt!=null){
			int calibre=nbt.getInteger("Calibre");
			int barrel=nbt.getInteger("Barrel");
			if(calibre==0){
				list.add("Calibre : calibre size of Cannon item");
				list.add("Length : barrel length of Cannon item");
				list.add("Engine : engine type of Chassis item");
				list.add("Motor : motor type of Chassis item");
			}else if(calibre==0xfe){
				list.add("Thicken armor.");
			}else if(calibre==0xff){
				int design=nbt.getInteger("Design");
				if(design==0xff){
				}else{
					list.add("Camouflage:"+camoList[design]);
				}
			}else {
				list.add("Calibre "+calibre*10+"cm");
				list.add("Length "+barrel*10+"cm");
				int engine=nbt.getInteger("Engine");
				if(engine>0){
					list.add("Engine Mk."+engine);
				}
				int motor=nbt.getInteger("Motor");
				if(motor>0){
					list.add("Motor Mk."+motor);
				}
				int design=nbt.getInteger("Design");
				list.add("Camouflage:"+camoList[design]);
				int armor=(int)((nbt.getInteger("Armor")+1)*10/((2*(double)calibre+(double)barrel/2+1)/40));
				list.add("Armor "+armor+"mm");
			}
		}
	}
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
                if(!worldIn.isRemote){
					
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
                	EntityCannon entity=new EntityCannon(worldIn);
                	NBTTagCompound nbt=stack.getTagCompound();
                	
                	if(nbt!=null){
                		entity.calibre=nbt.getInteger("Calibre");
                		entity.lengthOfBarrel=nbt.getInteger("Barrel");
                		entity.engine=nbt.getInteger("Engine");
                		entity.motor=nbt.getInteger("Motor");
                		entity.armor=nbt.getInteger("Armor")+1;
                		entity.design=nbt.getInteger("Design");
                	}
                	entity.setStats();
                	entity.updateDataWatcher();
                	entity.setLocationAndAngles(pos.getX()+0.5+side.getFrontOffsetX()*(entity.width/2+0.5)
                			, pos.getY()+0.5+side.getFrontOffsetY()*(entity.height/2+0.5)-entity.height/2
                			,pos.getZ()+0.5+side.getFrontOffsetZ()*(entity.width/2+0.5),0.0f,0.0f);
                	if (!worldIn.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                    {
                        return false;
                    }
                	worldIn.spawnEntityInWorld(entity);

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }
                

                return true;
                }
                return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List par3List) {
		{
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 5);
			nbt.setInteger("Barrel",10);
			nbt.setInteger("Armor",1);
			ItemStack result=new ItemStack(this);
			result.setTagCompound(nbt);
			par3List.add(result);
		}

		{
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 10);
			nbt.setInteger("Barrel",30);
			nbt.setInteger("Armor",1);
			ItemStack result=new ItemStack(this);
			result.setTagCompound(nbt);
			par3List.add(result);
		}
		
		{
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 20);
			nbt.setInteger("Barrel",60);
			nbt.setInteger("Armor",1);
			ItemStack result=new ItemStack(this);
			result.setTagCompound(nbt);
			par3List.add(result);
		}
		
		{
			NBTTagCompound nbt=new NBTTagCompound();
			nbt.setInteger("Calibre", 30);
			nbt.setInteger("Barrel",90);
			nbt.setInteger("Armor",1);
			ItemStack result=new ItemStack(this);
			result.setTagCompound(nbt);
			par3List.add(result);
		}
	}
	
}
