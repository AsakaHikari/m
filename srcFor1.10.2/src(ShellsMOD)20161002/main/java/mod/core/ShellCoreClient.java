package mod.core;

import java.util.HashMap;
import java.util.Map;

import mod.entity.*;
import mod.renderer.RenderShell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShellCoreClient extends ShellCoreProxy{
	@Override
	public void registerRenderer(){
		RenderManager rm=Minecraft.getMinecraft().getRenderManager();
		Map<Class<? extends Entity>, Render<? extends Entity>> map=new HashMap<Class<? extends Entity>, Render<? extends Entity>>();
		
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalShell.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigShell.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeShell.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityRubberBall.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/rubberball.png","shellmod:textures/entity/rubberball_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalHE.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigHE.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeHE.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalPointImpactHE.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/HE(PointImpact).png","shellmod:textures/entity/HE(PointImpact)_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigPointImpactHE.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/HE(PointImpact).png","shellmod:textures/entity/HE(PointImpact)_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugePointImpactHE.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/HE(PointImpact).png","shellmod:textures/entity/HE(PointImpact)_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalHEProximity.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/HE(Proximity).png","shellmod:textures/entity/HE(Proximity)_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigHEProximity.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/HE(Proximity).png","shellmod:textures/entity/HE(Proximity)_top.png",rm));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeHEProximity.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/HE(Proximity).png","shellmod:textures/entity/HE(Proximity)_top.png",rm));
		 RenderingRegistry.loadEntityRenderers(map);
	}
}
