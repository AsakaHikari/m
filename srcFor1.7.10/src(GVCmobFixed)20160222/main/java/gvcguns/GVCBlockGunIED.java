package gvcguns;
 
import java.util.Random;
 
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
 
public class GVCBlockGunIED extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon TopIcon;
 
    @SideOnly(Side.CLIENT)
    private IIcon SideIcon;
 
    public GVCBlockGunIED() {
        super(Material.rock);
        setCreativeTab(GVCGunsPlus.tabgvc);
        setHardness(1.5F);
        setResistance(1.0F);
        setStepSound(Block.soundTypeStone);
        setLightOpacity(1);
    }
 
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
            this.setBlockBounds(0.3F, 0.0F, 0.1F, 0.6F, 0.1F, 0.9F);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
    	if (!par1World.isRemote)
        {
    	par1World.createExplosion(null, par2, par3+1, par4, 3.0F, false);
    	par1World.setBlockToAir(par2, par3, par4);
        }
    	
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ){
        return true;
    }
    
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player){
    }
 
    @Override
    public void onNeighborBlockChange(World par1World, int x, int y, int z, Block neighborBlock){
    }
 
    @Override
    public int quantityDropped(int meta, int fortune, Random random){
        return quantityDroppedWithBonus(fortune, random);
    }
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    /*@Override
	public TileEntity createTileEntity(World world, int a) {
 
		return new GVCTileEntityBlockIED();
	}*/
    
}