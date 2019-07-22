package mod.bacteria.block;

import java.util.Random;

import mod.bacteria.ModBacteria;
import mod.bacteria.tileentity.GenomeChangerTileEntity;
import mod.bacteria.tileentity.IncubatorTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
 
public class BlockIncubator extends BlockContainer {
	public Icon iconTop,iconBottom,iconSide1,iconSide2;
 
	public BlockIncubator(int par1, Material par2Material) {
		super(par1, par2Material);
		 setHardness(2.0F);
         setResistance(5.0F);
		this.setCreativeTab(CreativeTabs.tabDecorations);	//クリエイティブのタブ
		this.setUnlocalizedName("incubator");	//システム名の登録
		this.func_111022_d("bacteriamod:Incubator_1");
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister){
		this.iconTop=par1IconRegister.registerIcon("bacteriamod:Incubator_0");
		this.iconBottom=par1IconRegister.registerIcon("bacteriamod:Incubator_2");
		this.iconSide1=par1IconRegister.registerIcon("bacteriamod:Incubator_1");
		this.iconSide2=par1IconRegister.registerIcon("bacteriamod:Incubator_3");
		super.registerIcons(par1IconRegister);
	}
	
	@Override
	public Icon getIcon(int par1, int par2){
		if(par1==0){
			return this.iconBottom;
		}else if(par1==1){
			return this.iconTop;
		}else if(par1==2||par1==3){
			return this.iconSide1;
		}else{
			return this.iconSide2;
		}
	}
	
	@Override
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return this.getIcon(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
 /*
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

 */
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
		return new IncubatorTileEntity();
	}
}