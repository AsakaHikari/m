package mod.core;

import mod.entity.*;
import mod.renderer.RenderShell;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShellCoreClient extends ShellCoreProxy{
	@Override
	public void registerRenderer(){
		//id is an internal mob id, you can start at 0 and continue adding them up.
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalShell.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigShell.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeShell.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/shell.png","shellmod:textures/entity/shell_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityRubberBall.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/rubberball.png","shellmod:textures/entity/rubberball_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalHE.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigHE.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeHE.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/HE.png","shellmod:textures/entity/HE_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalPointImpactHE.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/HE(PointImpact).png","shellmod:textures/entity/HE(PointImpact)_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigPointImpactHE.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/HE(PointImpact).png","shellmod:textures/entity/HE(PointImpact)_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugePointImpactHE.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/HE(PointImpact).png","shellmod:textures/entity/HE(PointImpact)_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNormalHEProximity.class, new RenderShell(1.0f,
				 "shellmod:textures/entity/HE(Proximity).png","shellmod:textures/entity/HE(Proximity)_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityBigHEProximity.class, new RenderShell(2.0f,
				 "shellmod:textures/entity/HE(Proximity).png","shellmod:textures/entity/HE(Proximity)_top.png"));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHugeHEProximity.class, new RenderShell(3.0f,
				 "shellmod:textures/entity/HE(Proximity).png","shellmod:textures/entity/HE(Proximity)_top.png"));
	}
}
