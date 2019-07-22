package buildingboots;

/*    */ import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*    */ import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
/*    */ import net.minecraft.world.World;

public class ItemBuildingBoots extends ItemArmor
{
	public int type;
	public ItemBuildingBoots(ItemArmor.ArmorMaterial p_i45325_1_, int p_i45325_2_, EntityEquipmentSlot slot,int type)
	{
		super(p_i45325_1_, p_i45325_2_, slot);
		this.type=type;
	}
	@Override
	public void onArmorTick(World world, EntityPlayer entity, ItemStack stack)
	{
		if(!world.isRemote)return;
		//System.out.println("x:"+entity.motionX+" y:"+entity.motionY+" z:"+entity.motionZ+" forward:"+entity.moveForward);
		//System.out.println("x:"+entity.posX+" y:"+entity.posY+" z:"+entity.posZ+" forward:"+entity.moveForward);
		int x = MathHelper.floor_double(entity.posX+2*entity.motionX);
		int y = MathHelper.floor_double(entity.posY-0.95)-this.type;
		int z = MathHelper.floor_double(entity.posZ+2*entity.motionZ);
		InventoryPlayer inv=entity.inventory;
		BuildingBootsCore.INSTANCE.sendToServer(new MessageCannonGui(x,y,z,this.type));

	}

	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return "buildingbootsmod:textures/models/armor/building_boots_"+this.type+".png";
	}

	
	public static double getMaxY(World w,Entity e,int x,int y,int z){
		BlockPos pos=new BlockPos(x,y,z);
		Block id=w.getBlockState(pos).getBlock();
		if(id==Blocks.AIR){
			return y;
		}
		if(id.isOpaqueCube(w.getBlockState(pos))){
			return y;
		}
		List<AxisAlignedBB> list=new ArrayList();
		id.addCollisionBoxToList(w.getBlockState(pos),w,pos, new AxisAlignedBB(x, y, z, x+1, y+1, z+1),list,e);
		if(list.isEmpty()){
			return y-1;
		}
		double maxY=list.get(0).maxY;
		double maxYNow;
		for(int i=1;i<list.size();i++){
			maxYNow=list.get(i).maxY;
			if(maxYNow<maxY){
				maxY=maxYNow;
			}
		}
		//System.out.println("maxY at "+x+","+y+","+z+" is "+maxY);
		return maxY;
	}
}
