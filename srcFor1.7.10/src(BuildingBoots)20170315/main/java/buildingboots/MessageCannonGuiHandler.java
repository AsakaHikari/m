package buildingboots;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import io.netty.buffer.ByteBuf;

public class MessageCannonGuiHandler implements IMessageHandler<MessageCannonGui,IMessage> {

	@Override//IMessageHandler
	public IMessage onMessage(MessageCannonGui message, MessageContext ctx) {
		
		EntityPlayer entity=ctx.getServerHandler().playerEntity;
		InventoryPlayer inv=entity.inventory;
		World world=entity.worldObj;
		int type=message.playerId;
		int x=message.x;
		int y=message.y;
		int z=message.z;
		//System.out.println("x:"+x+" y:"+y+" z:"+z+" type:"+type);
		for(int i=0;i<=type;i++){
			int solid=0;
			if(this.solidBlockExists(world, x+1, y, z, ForgeDirection.WEST))solid++;
			if(this.solidBlockExists(world, x-1, y, z, ForgeDirection.EAST))solid++;
			if(this.solidBlockExists(world, x, y+1, z, ForgeDirection.DOWN))solid++;
			if(this.solidBlockExists(world, x, y-1, z, ForgeDirection.UP))solid++;
			if(this.solidBlockExists(world, x, y, z+1, ForgeDirection.NORTH))solid++;
			if(this.solidBlockExists(world, x, y, z-1, ForgeDirection.SOUTH))solid++;
			if ( (world.getBlock(x, y, z).isReplaceable(world, x, y, z)) && solid>=2){
				if (entity.getHeldItem() != null) {
					if(entity.getHeldItem().tryPlaceItemIntoWorld(entity, entity.worldObj, x,y,z, 5, 0, 0, 0)){
						if(entity.getHeldItem().stackSize<1){
							inv.mainInventory[inv.currentItem]=null;
						}

					}
				}
			}
			y++;
		}
		
		return null;
	}
	
	public boolean solidBlockExists(World w,int x,int y,int z,ForgeDirection side){
		return w.getBlock(x, y, z).getCollisionBoundingBoxFromPool(w, x, y, z)!=null;
	}
}