package fatalinjury.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFirstAid extends ItemFood {

	public ItemFirstAid(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_) {
		super(p_i45339_1_, p_i45339_2_, p_i45339_3_);
	}
	
	protected void onFoodEaten(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer entity)
    {
        if (!p_77849_2_.isRemote)
        {
        	/*
        	PotionEffect[] effects=new PotionEffect[3];
    		effects[0]=new PotionEffect(Potion.moveSlowdown.id,1,3);
    		effects[1]=new PotionEffect(Potion.weakness.id,1,3);
    		effects[2]=new PotionEffect(Potion.digSlowdown.id,1,3);
    		List<ItemStack> list=new ArrayList<ItemStack>();
    		list.add(new ItemStack(NoOreCore.itemfirstaid));
    		for(int i=0;i<effects.length;i++){
    			entity.addPotionEffect(effects[i]);
    		}
    		*/
        	PotionEffect effect=new PotionEffect(NoOreCore.potionId,1,999);
    		entity.addPotionEffect(effect);
        }
        
    }
}
