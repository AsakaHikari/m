package namapumpkin;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockPumpkinStairs extends BlockStairs{

	public BlockPumpkinStairs(Block p_i45428_1_, int p_i45428_2_) {
		super(p_i45428_1_, p_i45428_2_);
		this.setHarvestLevel("axe", -1);
	}
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
		int meta=0;
		switch(p_149691_2_&0x03){
		case 0:
			meta=1;
			break;
		case 1:
			meta=3;
			break;
		case 2:
			meta=2;
			break;
		case 3:
			meta=0;
			break;
		}
        return Blocks.pumpkin.getIcon(p_149691_1_, meta);
    }
}
