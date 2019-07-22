package satisfybyone;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class CoreProxyClient extends CoreProxy {
	@Override
	public void init() {
		RenderingRegistry.registerEntityRenderingHandler(
				EntityManzocCart.class, new RenderMinecart());
		// EVENTS
		ManzocEventHandler eventHandler = new ManzocEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		FMLCommonHandler.instance().bus().register(eventHandler);
	}
}
