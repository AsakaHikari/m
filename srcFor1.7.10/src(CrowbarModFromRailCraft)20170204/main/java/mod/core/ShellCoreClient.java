package mod.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShellCoreClient extends ShellCoreProxy{
	@Override
	public void registerRenderer(){
		//id is an internal mob id, you can start at 0 and continue adding them up.
		 RenderingRegistry.registerEntityRenderingHandler(EntityShell.class, new RenderShell("railcraft:textures/entity/chunk_loader.png"));

	}
}
