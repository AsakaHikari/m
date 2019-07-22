package cannonmod.core;

import cannonmod.entity.EntityCannon;
import cannonmod.entity.EntityFallingBlockEx;
import cannonmod.entity.RenderCannon;
import cannonmod.entity.RenderFallingBlockEx;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderProxyClient extends RenderProxy{
	public void init(){
		RenderingRegistry.registerEntityRenderingHandler(EntityCannon.class, new RenderCannon());
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockEx.class, new RenderFallingBlockEx());
	};
}
