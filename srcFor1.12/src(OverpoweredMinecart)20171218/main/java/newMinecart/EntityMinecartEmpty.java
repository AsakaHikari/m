package newMinecart;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecartRapid
{
    private static final String __OBFID = "CL_00001677";
    

    public EntityMinecartEmpty(World p_i1722_1_)
    {
        super(p_i1722_1_);
        maxSpeedAirLateral = 0.8f;        
    }

    public EntityMinecartEmpty(World p_i1723_1_, double p_i1723_2_, double p_i1723_4_, double p_i1723_6_)
    {
        super(p_i1723_1_, p_i1723_2_, p_i1723_4_, p_i1723_6_);
        maxSpeedAirLateral = 0.8f;
    }

    /**
     * First layer of player interaction
     */
    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
    {
        if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, player,hand))){
        	return EnumActionResult.PASS;
        }
        if(this.getPassengers().size()>0){
        	Entity passenger=this.getPassengers().get(0);
        	if (passenger != null && passenger instanceof EntityPlayer && passenger != player)
        	{
        		return EnumActionResult.PASS;
        	}
        	else if (passenger != null && passenger != player)
        	{
        		return EnumActionResult.PASS;
        	}
        }
        else
        {
            if (!this.world.isRemote)
            {
                this.addPassenger(player);
            }

            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    public Type getType()
    {
        return Type.RIDEABLE;
    }
    
    public void killMinecart(DamageSource p_94095_1_)
    {
    	this.setDead();
        ItemStack itemstack = new ItemStack(Core.minecartItem, 1);

        this.entityDropItem(itemstack, 0.0F);
    }
    
    public ItemStack getCartItem(){
    	return new ItemStack(Core.minecartItem);
    }
}