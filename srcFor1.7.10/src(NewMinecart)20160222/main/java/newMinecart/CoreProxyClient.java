package newMinecart;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class CoreProxyClient extends CoreProxy{
	@Override
	public void init(){
		RenderingRegistry.registerEntityRenderingHandler(EntityMinecartEmpty.class, new RenderMinecart());
		RenderingRegistry.registerEntityRenderingHandler(EntityMinecartChest.class, new RenderMinecart());
	}
}
