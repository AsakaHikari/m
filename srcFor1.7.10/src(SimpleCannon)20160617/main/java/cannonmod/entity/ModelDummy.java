package cannonmod.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelDummy extends ModelBase{
	public RenderCannon renderer;
	
	
	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		((EntityCannon)entity).getThisModel(this.renderer).render(entity, f, f1, f2, f3, f4, f5);
	}

}
