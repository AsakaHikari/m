package mod.bacteria.block;

import java.util.Random;

import mod.bacteria.ModBacteria;
import mod.bacteria.tileentity.GenomeChangerTileEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
 
public class BlockGenomeChanger extends BlockContainer {
 

 
	public BlockGenomeChanger(int par1, Material par2Material) {
		super(par1, par2Material);
		 setHardness(2.0F);
         setResistance(5.0F);
		this.setCreativeTab(CreativeTabs.tabDecorations);	//クリエイティブのタブ
		this.setUnlocalizedName("genome changer");	//システム名の登録
		this.func_111022_d("bacteriamod:GenomeChanger");	//テクスチャの指定
	}
 
	//右クリックされた時の処理
	 @Override
     public boolean onBlockActivated(World world, int x, int y, int z,
                     EntityPlayer player, int metadata, float what, float these, float are) {
             TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
             if (tileEntity == null || player.isSneaking()) {
                     return false;
             }
     //code to open gui explained later
             
     player.openGui(ModBacteria.instance, 0, world, x, y, z);
             return true;
     }

 
	//ブロックが壊れた時の処理
	 @Override
     public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
             dropItems(world, x, y, z);
             super.breakBlock(world, x, y, z, par5, par6);
     }
	 
	 private void dropItems(World world, int x, int y, int z){
         Random rand = new Random();

         TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
         if (!(tileEntity instanceof IInventory)) {
                 return;
         }
         IInventory inventory = (IInventory) tileEntity;

         for (int i = 0; i < inventory.getSizeInventory(); i++) {
                 ItemStack item = inventory.getStackInSlot(i);

                 if (item != null && item.stackSize > 0) {
                         float rx = rand.nextFloat() * 0.8F + 0.1F;
                         float ry = rand.nextFloat() * 0.8F + 0.1F;
                         float rz = rand.nextFloat() * 0.8F + 0.1F;

                         EntityItem entityItem = new EntityItem(world,
                                         x + rx, y + ry, z + rz,
                                         new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                         if (item.hasTagCompound()) {
                                 entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                         }

                         float factor = 0.05F;
                         entityItem.motionX = rand.nextGaussian() * factor;
                         entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                         entityItem.motionZ = rand.nextGaussian() * factor;
                         world.spawnEntityInWorld(entityItem);
                         item.stackSize = 0;
                 }
         }
 }

 
	@Override
	public TileEntity createNewTileEntity(World world) {
		// TileEntityの生成
		return new GenomeChangerTileEntity();
	}
 
}