package gvcguns;

import gvcguns.GVCGunsPlus;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GVCItemEggSentry extends Item
{
	
	public int mob ;
	public GVCItemEggSentry(int i)
    {
        super();
        this.maxStackSize = 64;
        this.mob = i;
        setCreativeTab(GVCGunsPlus.tabgvc);
    }

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par3World.isRemote)
        {
            return true;
        }
        else if (par7 != 1)
        {
            return false;
        }
        else
        {
        if(this.mob == 0){
            ++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntitySentry entityskeleton = new GVCEntitySentry(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
         }
        else if(this.mob == 1) {
        	 ++par5;
             int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
             GVCEntitySentryAAG entityskeleton = new GVCEntitySentryAAG(par3World);
             entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
             par3World.spawnEntityInWorld(entityskeleton);
                     --par1ItemStack.stackSize;
                     return true;
          }
        }
        return false;
   }
}