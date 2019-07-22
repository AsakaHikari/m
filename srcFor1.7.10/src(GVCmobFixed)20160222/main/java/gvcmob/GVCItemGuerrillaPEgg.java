package gvcmob;

import gvcguns.GVCEntityparas;
import gvcguns.GVCGunsPlus;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GVCItemGuerrillaPEgg extends Item
{
	public GVCItemGuerrillaPEgg()
    {
        super();
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabs.tabMisc);
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
            ++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityGuerrillaP entityskeleton = new GVCEntityGuerrillaP(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_aks74u));
            
            /*GVCEntityparas entityskeleton1 = new GVCEntityparas(par3World);
            entityskeleton1.setLocationAndAngles(par4, par5, par6, var12, 0.0F);
            entityskeleton1.onSpawnWithEgg((IEntityLivingData)null);
            par3World.spawnEntityInWorld(entityskeleton1);*/
            
            
            par3World.spawnEntityInWorld(entityskeleton);
            //entityskeleton.mountEntity(entityskeleton1);
                    --par1ItemStack.stackSize;
                    return true;
                }
            
        }
}