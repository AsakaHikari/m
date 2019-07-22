package farview;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

public class CoreProxyClient extends CoreProxy {

	@Override
	public void init() {
		Core.FrenderContainer=ReflectionHelper.findField(RenderGlobal.class, "renderContainer","field_174996_N");
		Core.FrenderContainer.setAccessible(true);
		Core.FrenderChunks=ReflectionHelper.findField(ChunkRenderContainer.class,"renderChunks","field_178009_a");
		Core.FrenderChunks.setAccessible(true);
		Core.FrenderInfos=ReflectionHelper.findField(RenderGlobal.class,"renderInfos","field_72755_R");
		Core.FrenderInfos.setAccessible(true);
		Core.FviewFrustum=ReflectionHelper.findField(RenderGlobal.class,"viewFrustum","field_175008_n");
		Core.FviewFrustum.setAccessible(true);
		Core.FrendererUpdateCount=ReflectionHelper.findField(EntityRenderer.class,"rendererUpdateCount","field_78529_t");
		Core.FrendererUpdateCount.setAccessible(true);
    	/*
    	try {
			CLRIClazz=Class.forName("net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation");
		} catch (ClassNotFoundException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

    	FrenderChunk=ReflectionHelper.findField(CLRIClazz,"renderChunk");
    	FrenderChunk.setAccessible(true);*/
		Core.MsetupArrayPointers=ReflectionHelper.findMethod(VboRenderList.class,"setupArrayPointers","func_178010_a");
		Core.MsetupArrayPointers.setAccessible(true);
		Core.MgetFOVModifier=ReflectionHelper.findMethod(EntityRenderer.class,"getFOVModifier","func_78481_a",float.class,boolean.class);
		Core.MgetFOVModifier.setAccessible(true);
		Core.MapplyBobbing=ReflectionHelper.findMethod(EntityRenderer.class,"applyBobbing","func_78475_f",float.class);
		Core.MapplyBobbing.setAccessible(true);
		Core.MhurtCameraEffect=ReflectionHelper.findMethod(EntityRenderer.class,"hurtCameraEffect","func_78482_e",float.class);
		Core.MhurtCameraEffect.setAccessible(true);
		Core.MorientCamera=ReflectionHelper.findMethod(EntityRenderer.class,"orientCamera","func_78467_g",float.class);
		Core.MorientCamera.setAccessible(true);

        MinecraftForge.EVENT_BUS.register(new EventReceiver());
        Core.INSTANCE.registerMessage(MessageHandlerClient.class, MessageHandlerClient.Message.class, 1, Side.CLIENT);
	}
}
