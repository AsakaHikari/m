package gvcguns;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GVCItemGrenade extends Item
{
    private static final String __OBFID = "CL_00000069";

    public GVCItemGrenade()
    {
        this.maxStackSize = 4;
        setCreativeTab(GVCGunsPlus.tabgvc);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "gvcguns:gvcguns.grenade", 1.0F, 2.0F);

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new GVCEntityGrenade(par2World, par3EntityPlayer));
        }

        return par1ItemStack;
    }
}
