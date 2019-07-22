/*    */ package torchboots;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*    */ import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemTorchBoots extends ItemArmor
/*    */ {
	/*    */   public ItemTorchBoots(ItemArmor.ArmorMaterial p_i45325_1_, int p_i45325_2_, EntityEquipmentSlot p_i45325_3_)
	/*    */   {
		/* 22 */     super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
	/*    */   }
	/*    */ 
	/*    */   public void onArmorTick(World world, EntityPlayer entity, ItemStack stack)
	/*    */   {
		BlockPos pos=new BlockPos(MathHelper.floor_double(entity.posX),MathHelper.floor_double(entity.posY-0.05)+1,MathHelper.floor_double(entity.posZ));
		/* 28 */    
		/* 31 */     InventoryPlayer inv = entity.inventory;
		/* 32 */     if (world.getWorldInfo().getWorldTotalTime()%10==0 && (world.getBlockState(pos).getBlock() == Blocks.AIR) && (world.getLightFromNeighbors(pos) < 7))
			/* 33 */       for (int i = 0; i < inv.mainInventory.length; i++)
				/* 34 */         if ((inv.mainInventory[i] != null) && (TorchBootsCore.triggerItems.contains(inv.mainInventory[i].getItem()))) {
					/* 35 */          
					/* 36 */           if(inv.mainInventory[i].onItemUse(entity, world, pos, EnumHand.MAIN_HAND, EnumFacing.UP, 0,-1,0)==EnumActionResult.SUCCESS){
						//inv.decrStackSize(i, 1);
					}
					/* 37 */           break;
				/*    */         }
	/*    */   }
	/*    */ 
	/*    */   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	/*    */   {
		/* 45 */     return "torchboots:textures/models/armor/torch_boots_layer_1.png";
	/*    */   }
/*    */ }
