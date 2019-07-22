package mod.core;

import mod.entity.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.creativetab.CreativeTabs;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemShell extends Item{

	public IIcon[] icons;
	public ItemShell()
    {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
	
	@Override
	public void registerIcons(IIconRegister ir){
		this.icons=new IIcon[5];
		this.icons[0]=ir.registerIcon("shellmod:shell");
		this.icons[1]=ir.registerIcon("shellmod:rubberball");
		this.icons[2]=ir.registerIcon("shellmod:HE");
		this.icons[3]=ir.registerIcon("shellmod:HE(PointImpact)");
		this.icons[4]=ir.registerIcon("shellmod:HE(Proximity)");
	}
	
	@Override
	public IIcon getIconFromDamage(int damage){
		switch(damage){
		case 0:
		case 1:
		case 2:
			return this.icons[0];
		case 3:
			return this.icons[1];
		case 4:
		case 5:
		case 6:
			return this.icons[2];
		case 7:
		case 8:
		case 9:
			return this.icons[3];
		case 10:
		case 11:
		case 12:
			return this.icons[4];
		}
		return this.icons[0];
	}
	
	public EntityShell getEntityFromDamage(int damage,World w){
		switch(damage){
			case 0:
				return new EntityNormalShell(w);
			case 1:
				return new EntityBigShell(w);
			case 2:
				return new EntityHugeShell(w);
			case 3:
				return new EntityRubberBall(w);
			case 4:
				return new EntityNormalHE(w);
			case 5:
				return new EntityBigHE(w);
			case 6:
				return new EntityHugeHE(w);
			case 7:
				return new EntityNormalPointImpactHE(w);
			case 8:
				return new EntityBigPointImpactHE(w);
			case 9:
				return new EntityHugePointImpactHE(w);
			case 10:
				return new EntityNormalHEProximity(w);
			case 11:
				return new EntityBigHEProximity(w);
			case 12:
				return new EntityHugeHEProximity(w);
		}
		return new EntityNormalShell(w);
	}
	
	public static int getMetadataFromEntity(Entity e){
		if(e instanceof EntityNormalShell)return 0;
		if(e instanceof EntityBigShell)return 1;
		if(e instanceof EntityHugeShell)return 2;
		if(e instanceof EntityRubberBall)return 3;
		if(e instanceof EntityNormalHE)return 4;
		if(e instanceof EntityBigHE)return 5;
		if(e instanceof EntityHugeHE)return 6;
		if(e instanceof EntityNormalPointImpactHE)return 7;
		if(e instanceof EntityBigPointImpactHE)return 8;
		if(e instanceof EntityHugePointImpactHE)return 9;
		if(e instanceof EntityNormalHEProximity)return 10;
		if(e instanceof EntityBigHEProximity)return 11;
		if(e instanceof EntityHugeHEProximity)return 12;
		return 0;
	}
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
                if(!par3World.isRemote){
					float var1=0,var2=0,var3=0;
                    switch(par7){
                    case 0:
                    	var2=-1.0f;
                    	break;
                    case 1:
                    	var2=1.0f;
                    	break;
                    case 2:
                    	var3=-1.0f;
                    	break;
                    case 3:
                    	var3=1.0f;
                    	break;
                    case 4:
                    	var1=-1.0f;
                    	break;
                    case 5:
                    	var1=1.0f;
                    	break;
                    }
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
                	EntityShell entity=this.getEntityFromDamage(this.getDamage(par1ItemStack),par3World);
                	entity.setLocationAndAngles(par4+0.5+(double)var1*(entity.width/2+0.5)
                			, par5+0.5+var2*(entity.height/2+0.5)-entity.height/2
                			,par6+0.5+var3*(entity.width/2+0.5),0.0f,0.0f);
                	if (!par3World.getCollidingBoundingBoxes(entity, entity.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                    {
                        return false;
                    }
                	entity.setter=par2EntityPlayer;
                	par3World.spawnEntityInWorld(entity);
                    

                    if (!par2EntityPlayer.capabilities.isCreativeMode)
                    {
                        --par1ItemStack.stackSize;
                    }
                

                return true;
                }
                return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i=0;i<=12;i++){
			par3List.add(new ItemStack(this, 1, i));
		}
	}
    
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.getUnlocalizedName() + "_" + par1ItemStack.getItemDamage();
	}
	
}
