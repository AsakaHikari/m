package regulararmy.entity.render;

import regulararmy.entity.model.ModelCannon;
import regulararmy.entity.model.ModelCatapult;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderCannon extends RenderLiving {
	public static ResourceLocation textureCatapult=new ResourceLocation("monsterregulararmy:textures/entity/cannon.png");

	public RenderCannon() {
		super(new ModelCannon(), 1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return textureCatapult;
	}

}
