package mochen;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderRegister extends RenderProxy{

	public void init(){
		RenderingRegistry.registerEntityRenderingHandler(EntityMochen.class, new RenderMochen(new ModelMochen(),0.6f));
	}
}
