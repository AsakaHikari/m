package gvcmob;
import org.lwjgl.input.Keyboard;

//import net.java.games.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
public class ClientProxyGVCM extends CommonSideProxyGVCM {
    //�L�[��UnlocalizedName�A�o�C���h����L�[�̑Ή������l�iKeyboard�N���X�Q�Ƃ̂��Ɓj�A�J�e�S���[��
    //public static final KeyBinding Speedreload = new KeyBinding("Key.reload", Keyboard.KEY_R, "CategoryName");
	@Override
	public World getCilentWorld(){
		return FMLClientHandler.instance().getClient().theWorld;
		}
	
	public void reisterRenderers(){
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityBulletBase.class, new GVCRenderBulletBase());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityBulletGeRPG.class, new GVCRenderBulletBaseRPG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityHeliBullet.class, new GVCRenderHeliBullet());
		
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrilla.class, new GVCRenderGuerrilla());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaSP.class, new GVCRenderGuerrillaSP());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaRPG.class, new GVCRenderGuerrillaRPG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaSG.class, new GVCRenderGuerrillaSG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaMG.class, new GVCRenderGuerrillaMG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaBM.class, new GVCRenderGuerrillaBM());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGuerrillaP.class, new GVCRenderGuerrillaP());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityGK.class, new GVCRenderGK());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityTank.class, new GVCRenderTank());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityAPC.class, new GVCRenderAPC());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityHeli.class, new GVCRenderHeli());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityJeep.class, new GVCRenderJeep());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityAAG.class, new GVCRenderAAG());
		
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityMobSpawner.class, new GVCRenderMobSpawner());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityMobSpawner2.class, new GVCRenderMobSpawner2());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityFlag.class, new GVCRenderFlag());
		
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityPMC.class, new GVCRenderPMC());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityPMCSP.class, new GVCRenderPMCSP());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityPMCMG.class, new GVCRenderPMCMG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityPMCRPG.class, new GVCRenderPMCRPG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityPMCTank.class, new GVCRenderPMCTank());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntityPMCHeli.class, new GVCRenderPMCHeli());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntitySoldier.class, new GVCRenderSoldier());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntitySoldierSP.class, new GVCRenderSoldierSP());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntitySoldierMG.class, new GVCRenderSoldierMG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntitySoldierRPG.class, new GVCRenderSoldierRPG());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntitySoldierTank.class, new GVCRenderSoldierTank());
		RenderingRegistry.registerEntityRenderingHandler(GVCEntitySoldierHeli.class, new GVCRenderSoldierHeli());
		
		
	}
	
	
    @Override
    public void registerClientInfo() {
        //ClientRegistry.registerKeyBinding(Speedreload);
    }
 
}