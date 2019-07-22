package boatCraft.item;

import java.util.UUID;

import boatCraft.entity.EntityBoatWithBelayingPin;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemBoatRope extends Item {

	public ItemBoatRope(int par1) {
		super(par1);
		this.maxStackSize=64;
		this.setCreativeTab(CreativeTabs.tabTransport);
	}
	
	public void onUseForBoat(EntityBoatWithBelayingPin entity,EntityPlayer entityplayer,ItemStack itemstack,World world){
		if(!world.isRemote){
			NBTTagCompound nbt=itemstack.getTagCompound();
			if(nbt==null){
				nbt=new NBTTagCompound();
				itemstack.setTagCompound(nbt);
			}
				if(!nbt.hasKey("UUIDMost")){
				
					nbt.setLong("UUIDMost", entity.getUniqueID().getMostSignificantBits());
					nbt.setLong("UUIDLeast", entity.getUniqueID().getLeastSignificantBits());
					if(world instanceof WorldServer){
						ChatMessageComponent chat=new ChatMessageComponent();
						chat.addText("Binding...");
						((WorldServer)world).getMinecraftServer().getConfigurationManager().sendChatMsg(chat);
					}
				}else {
					if(entity.getUniqueID().getMostSignificantBits()==nbt.getLong("UUIDMost")&&
							entity.getUniqueID().getLeastSignificantBits()==nbt.getLong("UUIDLeast")){
						ChatMessageComponent chat=new ChatMessageComponent();
						chat.addText("Canceled.");
						((WorldServer)world).getMinecraftServer().getConfigurationManager().sendChatMsg(chat);
						
					}else if(entity.bindingEntity!=null&&
							entity.bindingEntity.getMostSignificantBits()==nbt.getLong("UUIDMost")&&
							entity.bindingEntity.getLeastSignificantBits()==nbt.getLong("UUIDLeast")){
						Entity entity1=world.getEntityByID((Integer) EntityBoatWithBelayingPin.UUIDMap.get
								(new UUID(nbt.getLong("UUIDMost"),nbt.getLong("UUIDLeast"))));
						if(entity1 instanceof EntityBoatWithBelayingPin){
							EntityBoatWithBelayingPin boatentity=(EntityBoatWithBelayingPin)entity1;
							entity.bindingEntity=null;
							boatentity.boundByEntity=null;
							ChatMessageComponent chat=new ChatMessageComponent();
							chat.addText("Unbound.");
							((WorldServer)world).getMinecraftServer().getConfigurationManager().sendChatMsg(chat);
						}
						
					}else{
						Entity entity1=world.getEntityByID((Integer) EntityBoatWithBelayingPin.UUIDMap.get
								(new UUID(nbt.getLong("UUIDMost"),nbt.getLong("UUIDLeast"))));
						if(entity1 instanceof EntityBoatWithBelayingPin){
							EntityBoatWithBelayingPin boatentity=(EntityBoatWithBelayingPin)entity1;
							boatentity.boundByEntity=entity.getUniqueID();
							entity.bindingEntity=boatentity.getUniqueID();
							if(entity.bindingEntity!=null){
								if(entity.bindingEntity.equals(entity.boundByEntity)){
									entity.boundByEntity=null;
									boatentity.bindingEntity=null;
									
								}
							}
							AxisAlignedBB beAABB=boatentity.getBoundingBox();
							AxisAlignedBB eAABB=entity.getBoundingBox();
							boatentity.posX=entity.posX+((beAABB.maxX-beAABB.minX)/2+(eAABB.maxX-eAABB.minX)/2+0.8)*Math.cos(entity.rotationYaw*Math.PI/180.0);
							boatentity.posZ=entity.posZ+((beAABB.maxZ-beAABB.minZ)/2+(eAABB.maxZ-eAABB.minZ)/2+0.8)*Math.sin(entity.rotationYaw*Math.PI/180.0);
							System.out.println("cos="+Math.cos(entity.rotationYaw*Math.PI/180.0)+"sin="+Math.sin(entity.rotationYaw*Math.PI/180.0));
							boatentity.rotationYaw=entity.rotationYaw;
							boatentity.setPositionAndRotation(boatentity.posX, boatentity.posY, boatentity.posZ, boatentity.rotationYaw, boatentity.rotationPitch);
							ChatMessageComponent chat=new ChatMessageComponent();
							chat.addText("Succeeded to binding.");
							((WorldServer)world).getMinecraftServer().getConfigurationManager().sendChatMsg(chat);
						}
						
						
					}
					nbt.removeTag("UUIDMost");
					nbt.removeTag("UUIDLeast");
					
				}
		}
	}

}
