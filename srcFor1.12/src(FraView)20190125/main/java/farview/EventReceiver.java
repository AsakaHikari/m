package farview;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@Mod.EventBusSubscriber
public class EventReceiver {
	
	public static Map<World,Renderer> dimensionToRenderer=new HashMap();

	@SubscribeEvent
	public void fogSettings(RenderFogEvent e){
		World w=e.getEntity().getEntityWorld();
		Renderer r=this.getRenderer(w);
		r.fogSettings(e);
	}
	/*
	@SubscribeEvent
	public void render(CameraSetup e){
		World w=e.getEntity().getEntityWorld();
		Renderer r=this.getRenderer(w);
		r.render(e);
	}*/
	@SubscribeEvent
	public void render(RenderWorldLastEvent e){
		World w=Minecraft.getMinecraft().player.world;
		Renderer r=this.getRenderer(w);
		r.render(e);
	}
	
	public static Renderer getRenderer(World w) {
		Renderer r=dimensionToRenderer.get(w);
		if(r==null) {
			r=new Renderer();
			dimensionToRenderer.put(w, r);
		}
		return r;
	}
	
	public static Renderer getRenderer(int dimension) {
		for(Map.Entry<World,Renderer> e:dimensionToRenderer.entrySet()) {
			if(e.getKey().provider.getDimension()==dimension) {
				return e.getValue();
			}
		}
		return null;
	}
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Core.modid)) {
			ConfigManager.sync(Core.modid, Type.INSTANCE);
		}
	}
}
