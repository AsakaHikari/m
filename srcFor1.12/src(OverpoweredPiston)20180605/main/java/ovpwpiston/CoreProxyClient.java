package ovpwpiston;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class CoreProxyClient extends CoreProxy{
	@Override
	public void init(){
		RenderManager rm=Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityMinecartEmpty.class, new RenderMinecart(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityMinecartChest.class, new RenderMinecart(rm));
	}
}
