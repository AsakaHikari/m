package buildingboots;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import io.netty.buffer.ByteBuf;

public class MessageCannonGuiHandler implements IMessageHandler<MessageCannonGui,IMessage> {

	@Override//IMessageHandler
	public IMessage onMessage(MessageCannonGui message, MessageContext ctx) {
		
		EntityPlayer entity=ctx.getServerHandler().playerEntity;
		//InventoryPlayer inv=entity.inventory;
		World world=entity.worldObj;
		int type=message.playerId;
		int x=message.x;
		int y=message.y;
		int z=message.z;
		//System.out.println("x:"+x+" y:"+y+" z:"+z+" type:"+type);
		for(int i=0;i<=type;i++){
			int solid=0;
			if(this.solidBlockExists(world, x+1, y, z, EnumFacing.WEST))solid++;
			if(this.solidBlockExists(world, x-1, y, z, EnumFacing.EAST))solid++;
			if(this.solidBlockExists(world, x, y+1, z, EnumFacing.DOWN))solid++;
			if(this.solidBlockExists(world, x, y-1, z, EnumFacing.UP))solid++;
			if(this.solidBlockExists(world, x, y, z+1, EnumFacing.NORTH))solid++;
			if(this.solidBlockExists(world, x, y, z-1, EnumFacing.SOUTH))solid++;
			
			if ( (world.getBlockState(new BlockPos(x, y, z)).getBlock().isReplaceable(world, new BlockPos(x, y, z))) && solid>=2){
				ItemStack stackToUse=entity.getHeldItemMainhand();
				if(stackToUse!=null && 
						stackToUse.onItemUse(entity, entity.worldObj, new BlockPos(x, y, z),EnumHand.MAIN_HAND,
								EnumFacing.DOWN , 0, 0, 0)==EnumActionResult.SUCCESS){
					if(stackToUse.stackSize<1){
						entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
					}
				}else {
					stackToUse=entity.getHeldItemOffhand();
					if(stackToUse!=null && 
							stackToUse.onItemUse(entity, entity.worldObj, new BlockPos(x, y, z),EnumHand.OFF_HAND,
									EnumFacing.DOWN , 0, 0, 0)==EnumActionResult.SUCCESS){
						if(stackToUse.stackSize<1){
							entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
						}
					}
				}
				
			}
			y++;
		}
		
		return null;
	}
	
	public boolean solidBlockExists(World w,int x,int y,int z,EnumFacing side){
		IBlockState state=w.getBlockState(new BlockPos(x,y,z));
		return state.getBlock().getCollisionBoundingBox(state,w, new BlockPos(x, y, z))!=null;
	}
}