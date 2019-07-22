package regulararmy.core;

import regulararmy.entity.*;
import regulararmy.entity.model.ModelZombieLongSpearer;
import regulararmy.entity.model.ModelZombieSpearer;
import regulararmy.entity.render.*;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderClient extends RenderProxy{
	
	public void init(){
		RenderingRegistry.registerEntityRenderingHandler(EntitySniperSkeleton.class,new RenderSkeletonR());
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonR.class,new RenderSkeletonR());
		RenderingRegistry.registerEntityRenderingHandler(EntityEngineer.class, new RenderZombie());
		RenderingRegistry.registerEntityRenderingHandler(EntityScouter.class, new RenderZombie());
		RenderingRegistry.registerEntityRenderingHandler(EntityFastZombie.class, new RenderZombie());
		RenderingRegistry.registerEntityRenderingHandler(EntityCreeperR.class,new RenderCreeperR());
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieSpearer.class,new RenderZombieSpearer(new ModelZombieSpearer()));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieLongSpearer.class,new RenderZombieSpearer(new ModelZombieLongSpearer()));
		RenderingRegistry.registerEntityRenderingHandler(EntityCatapult.class,new RenderCatapult());
		RenderingRegistry.registerEntityRenderingHandler(EntityCannon.class,new RenderCannon());
		RenderingRegistry.registerEntityRenderingHandler(EntityStone.class,new RenderStone());
	}

}
