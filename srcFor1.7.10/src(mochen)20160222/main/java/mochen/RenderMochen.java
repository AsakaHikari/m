package mochen;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderMochen extends RenderLiving {
   public RenderMochen(ModelBase p_i1262_1_, float p_i1262_2_) {
		super(p_i1262_1_, p_i1262_2_);
		
	}

public static final ResourceLocation texture = new ResourceLocation("mochen:textures/entity/mochen1.png");
public static final ResourceLocation textureSleep = new ResourceLocation("mochen:textures/entity/mochen1_sleep.png");

@Override
protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
	return p_110775_1_.getDataWatcher().getWatchableObjectByte(17)==0?texture:textureSleep;
}
}