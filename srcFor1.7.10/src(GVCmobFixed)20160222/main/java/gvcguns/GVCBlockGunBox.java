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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
 
public class GVCBlockGunBox extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon TopIcon;
 
    @SideOnly(Side.CLIENT)
    private IIcon SideIcon;
 
    public GVCBlockGunBox() {
        super(Material.rock);
        setCreativeTab(GVCGunsPlus.tabgvc);
        
        setHardness(1.5F);
        setResistance(1.0F);
        setStepSound(Block.soundTypeStone);
	/*setBlockUnbreakable();*/
	/*setTickRandomly(true);*/
	/*disableStats();
        setLightOpacity(1);
        //setLightLevel(1.0F);1.0F = 15*/
    }
 
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ){
    	//world.setBlock(x+3, y+0, z-3, Blocks.fence);
    	
    	//player.openGui(GVCGunsPlus.INSTANCE, GVCGunsPlus.GUI_ID,  world, x, y, z);
    	
    	ItemStack itemstack = player.inventory.getCurrentItem();
    	//boolean var5 = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemstack) > 0;
/*
        if (itemstack.getItem() == Items.emerald)
        {
        	//itemstack.stackSize -= 1;
        	//for(int i = 0; i < 5; ++i)
        	{
        	//player.inventory.consumeInventoryItem(Items.emerald);
        	}
        	if (!world.isRemote)
            {
        		switch (world.rand.nextInt(4))
                {
                case 0:
        		this.dropBlockAsItem(world, x, y+1, z, new ItemStack(GVCGunsPlus.fn_ak74, 1));
        		break;
                case 1:
            		this.dropBlockAsItem(world, x, y+1, z, new ItemStack(GVCGunsPlus.fn_m16a4, 1));
            		break;
                case 2:
            		this.dropBlockAsItem(world, x, y+1, z, new ItemStack(GVCGunsPlus.fn_g36, 1));
            		break;
                case 3:
            		this.dropBlockAsItem(world, x, y+1, z, new ItemStack(GVCGunsPlus.fn_m1911, 1));
            		break;
                }
        	//player.dropItem(Items.apple, 2);
            }
        }else{}*/
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
 
    @Override
    public int quantityDropped(Random random){
        return 1;
    }
    
   

}