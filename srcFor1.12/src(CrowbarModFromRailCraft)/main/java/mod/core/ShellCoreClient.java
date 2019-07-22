package mod.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ShellCoreClient extends ShellCoreProxy{
	@Override
	public void registerRenderer(){
		//id is an internal mob id, you can start at 0 and continue adding them up.
		Map<Class<? extends Entity>, Render<? extends Entity>> map=new HashMap<Class<? extends Entity>, Render<? extends Entity>>();
		 RenderingRegistry.registerEntityRenderingHandler
		 (EntityShell.class, new RenderShell("crowbarmodfromrailcraft:textures/entity/chunk_loader.png",Minecraft.getMinecraft().getRenderManager()));
		 RenderingRegistry.loadEntityRenderers(map);

	}
}
