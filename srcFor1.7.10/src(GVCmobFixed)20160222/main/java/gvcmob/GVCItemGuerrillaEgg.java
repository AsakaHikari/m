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

public class GVCItemGuerrillaEgg extends Item
{
	public int mob ;
	
	public GVCItemGuerrillaEgg(int i)
    {
        super();
        this.mob = i;
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
        	if(this.mob == 0){
            ++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityGuerrilla entityskeleton = new GVCEntityGuerrilla(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_ak74));
            
            /*GVCEntityparas entityskeleton1 = new GVCEntityparas(par3World);
            entityskeleton1.setLocationAndAngles(par4, par5, par6, var12, 0.0F);
            entityskeleton1.onSpawnWithEgg((IEntityLivingData)null);
            par3World.spawnEntityInWorld(entityskeleton1);*/
            
            
            par3World.spawnEntityInWorld(entityskeleton);
            //entityskeleton.mountEntity(entityskeleton1);
                    --par1ItemStack.stackSize;
                    return true;
                
        }else if(this.mob == 1){
        	 ++par5;
             int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
             GVCEntityGuerrillaBM entityskeleton = new GVCEntityGuerrillaBM(par3World);
             entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
             //entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_ak74));
             par3World.spawnEntityInWorld(entityskeleton);
                     --par1ItemStack.stackSize;
                     return true;
        }else if(this.mob == 2){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityGuerrillaSP entityskeleton = new GVCEntityGuerrillaSP(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_svd));
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 3){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityGuerrillaRPG entityskeleton = new GVCEntityGuerrillaRPG(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_rpg7));
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 4){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityGuerrillaSG entityskeleton = new GVCEntityGuerrillaSG(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_m870));
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 5){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityGuerrillaMG entityskeleton = new GVCEntityGuerrillaMG(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_pkm));
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 6){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityGK entityskeleton = new GVCEntityGK(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            //entityskeleton.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_ak74));
            
            /*GVCEntityparas entityskeleton1 = new GVCEntityparas(par3World);
            entityskeleton1.setLocationAndAngles(par4, par5, par6, var12, 0.0F);
            entityskeleton1.onSpawnWithEgg((IEntityLivingData)null);
            par3World.spawnEntityInWorld(entityskeleton1);*/
            
            
            par3World.spawnEntityInWorld(entityskeleton);
            //entityskeleton.mountEntity(entityskeleton1);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 7){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityTank entityskeleton = new GVCEntityTank(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 8){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityAPC entityskeleton = new GVCEntityAPC(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 9){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityHeli entityskeleton = new GVCEntityHeli(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 10){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityJeep entityskeleton = new GVCEntityJeep(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            par3World.spawnEntityInWorld(entityskeleton);
            //if(!par3World.isRemote)
            if(par3World.rand.nextInt(9)== 0){
            	GVCEntityAAG entityskeleton1 = new GVCEntityAAG(par3World);
                entityskeleton1.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
                entityskeleton1.onSpawnWithEgg((IEntityLivingData)null);
                par3World.spawnEntityInWorld(entityskeleton1);
                entityskeleton1.mountEntity(entityskeleton);
            }else
            {
            GVCEntityGuerrillaMG entityskeleton1 = new GVCEntityGuerrillaMG(par3World);
            entityskeleton1.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            entityskeleton1.setCurrentItemOrArmor(0, new ItemStack(GVCGunsPlus.fn_pkm));
            entityskeleton1.onSpawnWithEgg((IEntityLivingData)null);
            par3World.spawnEntityInWorld(entityskeleton1);
            entityskeleton1.mountEntity(entityskeleton);
            }
                    --par1ItemStack.stackSize;
                    return true;
        }else if(this.mob == 11){
        	++par5;
            int var12 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            GVCEntityAAG entityskeleton = new GVCEntityAAG(par3World);
            entityskeleton.setLocationAndAngles(par4+0.5, par5, par6+0.5, var12, 0.0F);
            par3World.spawnEntityInWorld(entityskeleton);
                    --par1ItemStack.stackSize;
                    return true;
        }else{
        	return false;
        }
            
        }
    }
}