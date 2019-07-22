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
 * this.flag�͕ϐ������Ӗ����܂��B
 * this.flag = private boolean flag = false;�̕���
 * flag = ������boolean flag
 */
	public WorldGenSample(boolean flag){
		this.flag = flag;
	}
 
	@Override
	public boolean generate(World par1World, Random rand, int par2, int par3,
			int par4) {
		//������TileEntityChest���L���X�g���܂��B
		TileEntityChest Chest;
		//Chest��z�u���܂��B
		par1World.setBlock(par2, par3, par4, Blocks.chest);
		//TileEntityChest�^�ϐ���TileEntity��World�N���X����擾���A������܂��B
		Chest = (TileEntityChest) par1World.getTileEntity(par2, par3, par4);
		if(Chest != null){//Null�ȊO�Ȃ�
			//�����̓X���b�g�ԍ�(-1),ItemStack�ł��B
			Chest.setInventorySlotContents(0, new ItemStack(Items.redstone));
		}else{
			//null��������R���\�[���ɕ������\�����Ēm�点�܂��B
			System.out.println("== TileEntityChest=null ==");
		}
		return flag;
	}
 
}