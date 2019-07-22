package gvcguns;
 
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
 
public class GVCItemArmorMugen extends GVCItemGunBase {
 
	public static final String armor_layer1 = "gvcguns:textures/armor/mugen_layer1.png";
    public static final String armor_layer2 = "gvcguns:textures/armor/mugen_layer2.png";

	
	public GVCItemArmorMugen() {
		super();
	}
 
	/*@Override
	public String getArmorTexture(ItemStack itemStack, Entity entity, int slot, String type) {
		if (this.armorType == 2) {
			return armor_layer2;
		}
		return armor_layer1;
	}*/
 
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {

		EntityPlayer entityplayer = (EntityPlayer)entity;
		ItemStack itemstack1 = ((EntityPlayer)(entityplayer)).getCurrentEquippedItem();
		
		int s;
		int li = getMaxDamage() - itemstack.getItemDamage();
		int id = itemstack1.getItemDamage();
		
		//if(entityplayer.inventory.armorItemInSlot(3) != null && entityplayer.inventory.armorItemInSlot(3) == new ItemStack(GVCGunsPlus.fn_mugenb))
		if (entity != null && entity instanceof EntityPlayer) 
    	{
			if (!world.isRemote)
            {
				/*if(itemstack1.getItemDamage() > 0)
				{
					itemstack1.damageItem(-1, entityplayer);
				}*/
				//id = 0;
            }
			GVCItemGunBase.updateCheckinghSlot(entity, itemstack);
    	}
		super.onUpdate(itemstack, world, entity, i, flag);
    }
	
	@Override
    public byte getCycleCount(ItemStack pItemstack)
    {
        return 1/2;
    }
}
