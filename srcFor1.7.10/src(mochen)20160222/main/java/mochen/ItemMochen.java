package mochen;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemMochen extends ItemFood
{
 
	public ItemMochen()
	{
		super(5,7.0f,true);
		this.setUnlocalizedName("mochen");
		this.setTextureName("mochen:mochen");
		this.setMaxStackSize(64);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack,EntityPlayer player,EntityLivingBase entity){
		if(entity.worldObj.isRemote)return false;
		EntityMochen entityshell = new EntityMochen(entity.worldObj, entity.posX, entity.posY, entity.posZ);
		entity.worldObj.spawnEntityInWorld(entityshell);
		Entity e;
		for(e=entity;e.riddenByEntity!=null;e=e.riddenByEntity){
		}
		e.riddenByEntity=entityshell;
		entityshell.ridingEntity=e;
		itemstack.splitStack(1);
		return true;
	}

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
                    EntityMochen entityshell = new EntityMochen(par3World, (double)((float)par4 + 0.75F+var1), (double)((float)par5 +var2), (double)((float)par6 + 0.75F+var3));

                    if (!par3World.getCollidingBoundingBoxes(entityshell, entityshell.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                    {
                        return false;
                    }
                        par3World.spawnEntityInWorld(entityshell);
                        par1ItemStack.splitStack(1);
                        return true;
                }
                return false;
	}
}