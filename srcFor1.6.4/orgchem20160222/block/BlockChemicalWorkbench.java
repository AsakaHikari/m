package orgchem.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockChemicalWorkbench extends Block {
	
	public Icon workbenchIconTop;

	public BlockChemicalWorkbench(int par1) {
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	   @SideOnly(Side.CLIENT)

	    /**
	     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	     */
	    public Icon getIcon(int par1, int par2)
	    {
	        return par1 == 1 ? this.workbenchIconTop : (par1 == 0 ? Block.planks.getBlockTextureFromSide(par1) : this.blockIcon );
	    }

	    @SideOnly(Side.CLIENT)

	    /**
	     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	     * is the only chance you get to register icons.
	     */
	    public void registerIcons(IconRegister par1IconRegister)
	    {
	        this.blockIcon = par1IconRegister.registerIcon("planks_oak");
	        this.workbenchIconTop = par1IconRegister.registerIcon("orgchem:chemicalWorkbench_top");
	    }
	    
	    @Override
	    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float par7, float par8, float par9){
	    	
	    	return true;
	    }
}
