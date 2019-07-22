package rockychocoice.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class EventReceiver {
	@SubscribeEvent
	public void onAttackWithRockyChoco(LivingAttackEvent e){
		Entity sourceEntity=e.source.getSourceOfDamage();
		if(sourceEntity instanceof EntityLivingBase ){
			ItemStack stack=((EntityLivingBase)sourceEntity).getHeldItem();
			if(stack!=null && stack.getItem()==RockyChocoCore.itemRockyChoco){
				int icyLvl=EnchantmentHelper.getEnchantmentLevel(RockyChocoCore.icy.effectId, stack);
				if(icyLvl>0){
					e.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,icyLvl*80,1));
				}
				
			}
		}
	}
}
