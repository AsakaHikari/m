/*    */ package torchboots;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemArmor.ArmorMaterial;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemTorchBoots extends ItemArmor
/*    */ {
	/*    */   public ItemTorchBoots(ItemArmor.ArmorMaterial p_i45325_1_, int p_i45325_2_, int p_i45325_3_)
	/*    */   {
		/* 22 */     super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
	/*    */   }
	/*    */ 
	/*    */   public void onArmorTick(World world, EntityPlayer entity, ItemStack stack)
	/*    */   {
		/* 28 */     int x = MathHelper.floor_double(entity.posX);
		/* 29 */     int y = MathHelper.floor_double(entity.posY-0.05)+1;
		/* 30 */     int z = MathHelper.floor_double(entity.posZ);
		/* 31 */     InventoryPlayer inv = entity.inventory;
		/* 32 */     if (!world.isRemote && world.getWorldInfo().getWorldTotalTime()%10==0 && (world.getBlock(x, y, z) == Blocks.air) && (world.getBlockLightValue(x, y, z) < 7))
			/* 33 */       for (int i = 0; i < inv.mainInventory.length; i++)
				/* 34 */         if ((inv.mainInventory[i] != null) && (TorchBootsCore.triggerItems.contains(inv.mainInventory[i].getItem()))) {
					/* 35 */          
					/* 36 */           if(inv.mainInventory[i].tryPlaceItemIntoWorld(entity, world, x, y, z, 5, 0, -1, 0)){
						//inv.decrStackSize(i, 1);
					}
					/* 37 */           break;
				/*    */         }
	/*    */   }
	/*    */ 
	/*    */   public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	/*    */   {
		/* 45 */     return "torchboots:textures/models/armor/torch_layer_1.png";
	/*    */   }
/*    */ }
