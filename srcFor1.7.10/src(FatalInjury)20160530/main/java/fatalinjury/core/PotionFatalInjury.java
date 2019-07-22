package fatalinjury.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionFatalInjury extends Potion{
	
	public PotionFatalInjury(int id, boolean isBad,
			int liquidColor) {
		super(id, isBad, liquidColor);
		this.setIconIndex(1, 2);
	}
	
	public void performEffect(EntityLivingBase entity, int id){
		entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,2400,2));
		entity.addPotionEffect(new PotionEffect(Potion.weakness.id,2400,2));
		entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id,2400,2));
		
		super.performEffect(entity, id);
		
	}
	
	public boolean isReady(int p_76397_1_, int p_76397_2_){return true;}
}
