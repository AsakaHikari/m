package gvcmob;
 
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
 
public class WorldGenSample extends WorldGenerator {
 
	private boolean flag = false;
/*
 * this.flagは変数名を意味します。
 * this.flag = private boolean flag = false;の部分
 * flag = 引数のboolean flag
 */
	public WorldGenSample(boolean flag){
		this.flag = flag;
	}
 
	@Override
	public boolean generate(World par1World, Random rand, int par2, int par3,
			int par4) {
		//ここでTileEntityChestをキャストします。
		TileEntityChest Chest;
		//Chestを配置します。
		par1World.setBlock(par2, par3, par4, Blocks.chest);
		//TileEntityChest型変数にTileEntityをWorldクラスから取得し、代入します。
		Chest = (TileEntityChest) par1World.getTileEntity(par2, par3, par4);
		if(Chest != null){//Null以外なら
			//引数はスロット番号(-1),ItemStackです。
			Chest.setInventorySlotContents(0, new ItemStack(Items.redstone));
		}else{
			//nullだったらコンソールに文字列を表示して知らせます。
			System.out.println("== TileEntityChest=null ==");
		}
		return flag;
	}
 
}