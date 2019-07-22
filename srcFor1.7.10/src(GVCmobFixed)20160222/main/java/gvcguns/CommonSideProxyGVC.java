package gvcguns;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

//ClientProxy�̂ݕK�v�����A�g�������l��CommonProxy���p�ӁBGUI���ǉ�����Ȃ�AIGuiHandler�������̂��ƁB
public class CommonSideProxyGVC {
 
	public void registerClientInfo(){}
	
	public void IGuiHandler(){}
	
    public void reisterRenderers(){}
	
	public World getCilentWorld(){
		return null;}

	public Minecraft getCilentMinecraft(){
		return null;}
	
	public void InitRendering() {
		
	}

	public void registerTileEntity() {		
		//GameRegistry.registerTileEntity(GVCTileEntityBlockIED.class, "GVCTileEntityBlockIED");
	}
	
	
 
}