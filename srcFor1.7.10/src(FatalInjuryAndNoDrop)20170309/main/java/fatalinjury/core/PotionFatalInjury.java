package fatalinjury.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionFatalInjury extends Potion{
	
	public PotionFatalInjury(int id, boolean isBad,
			int liquidColor) {
		super(id, isBad, liquidColor);
		this.setIconIndex(1, 2);
	}
	
	public void performEffect(EntityLivingBase entity, int id){
		if(entity.getHealth()>6){
			entity.setHealth(6);
		}
		/*
		if(entity instanceof EntityPlayer){
			((EntityPlayer)entity).getFoodStats().setFoodLevel(6);
		}
		*/
		super.performEffect(entity, id);
		
	}
	
	public boolean isReady(int p_76397_1_, int p_76397_2_){return true;}
}
