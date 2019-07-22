package gvcguns.client;
import gvcguns.CommonSideProxyGVC;
import gvcguns.GVCEntityBox;
import gvcguns.GVCEntityBullet;
import gvcguns.GVCEntityBulletDart;
import gvcguns.GVCEntityBulletFrag;
import gvcguns.GVCEntityBulletG;
import gvcguns.GVCEntityBullettest;
import gvcguns.GVCEntityC4;
import gvcguns.GVCEntityGrenade;
import gvcguns.GVCEntityGrenade_HundF;
import gvcguns.GVCEntityGurenadeBullet;
import gvcguns.GVCEntityLaser;
import gvcguns.GVCEntityMissile;
import gvcguns.GVCEntityPlus;
import gvcguns.GVCEntityRPG;
import gvcguns.GVCEntitySentry;
import gvcguns.GVCEntitySentryAAG;
import gvcguns.GVCEntitySentryBullet;
import gvcguns.GVCEntitySentryBulletAAG;
import gvcguns.GVCEntityTarget;
import gvcguns.GVCEntitygustorch;
import gvcguns.GVCEntityparas;
import gvcguns.GVCGunsPlus;
import gvcguns.GVCTileEntityBlockIED;
import gvcguns.GVCTileEntityGunBox;
import gvcguns.GVCZoomEvent;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.java.games.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
public class ClientProxyGVC extends CommonSideProxyGVC {
    //�L�[��UnlocalizedName�A�o�C���h����L�[�̑Ή������l�iKeyboard�N���X�Q�Ƃ̂��Ɓj�A�J�e�S���[��
    public static final KeyBinding Speedreload = new KeyBinding("Key.reload", Keyboard.KEY_R, "GVCGunsPlus");
    public static final KeyBinding Firetype = new KeyBinding("Key.firetype", Keyboard.KEY_G, "GVCGunsPlus");
    public static final KeyBinding ADStype = new KeyBinding("Key.adstype", Keyboard.KEY_V, "GVCGunsPlus");
	private static final IItemRenderer ItemRenderAK74 = new GVCRenderItemAK74();
    //public static final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
    
    
    @Override
	public World getCilentWorld(){
		return FMLClientHandler.instance().getClient().theWorld;
		}
    
    @Override
    public void registerClientInfo() {
        //ClientRegistry.registerKeyBinding(Speedreload);
    }
    
    @Override
    public Minecraft getCilentMinecraft(){
		return FMLClientHandler.instance().getClient();
		}
    
    @Override
	public void reisterRenderers(){
    	ClientRegistry.registerKeyBinding(Speedreload);
    	ClientRegistry.registerKeyBinding(Firetype);
    	ClientRegistry.registerKeyBinding(ADStype);
    	
    	
    	
		//RenderingRegistry.registerEntityRenderingHandler(GVCEntityBullet.class, new RenderSnowball(fn_bullet));
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityBullet.class, new GVCRenderBullet());
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityBulletG.class, new RenderSnowball(GVCGunsPlus.fn_bullet));
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityGrenade.class, new RenderSnowball(GVCGunsPlus.fn_entitygrenade));
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityGrenade_HundF.class, new RenderSnowball(GVCGunsPlus.fn_hundframe));
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityGurenadeBullet.class, new GVCRenderGrenadeBullet());
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityRPG.class, new GVCRenderRPG());
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityLaser.class, new GVCRenderLaser());
				RenderingRegistry.registerEntityRenderingHandler(GVCEntitygustorch.class, new GVCRendergustorch());
				
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityMissile.class, new GVCRenderMissile());
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityC4.class, new RenderSnowball(GVCGunsPlus.fn_c4));
				
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityparas.class, new GVCRenderparas());
				
				//RenderingRegistry.registerEntityRenderingHandler(GVCEntityBullettest.class, new GVCRenderBullettest());
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityBulletDart.class, new GVCRenderBulletDart());
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityBulletFrag.class, new GVCRenderBulletFrag());
				
				//RenderingRegistry.registerBlockHandler(handler);
				//MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_ak74, ItemRenderAK74);
				if(GVCGunsPlus.cfg_GunModel3D){
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m320, new GVCRenderItemM320());
				
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_ak74, new GVCRenderItemAK74());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_aks74u, new GVCRenderItemAKS74U());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m1911, new GVCRenderItemM1911());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_ak74sp, new GVCRenderItemAK74sp());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_ak74gb30, new GVCRenderItemAK74GB30());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_ak74b, new GVCRenderItemAK74Bayonet());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_rpk74, new GVCRenderItemRPK74());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m10, new GVCRenderItemM10());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_uzi, new GVCRenderItemUZI());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_cbj, new GVCRenderItemCBJ());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_pkm, new GVCRenderItemPKM());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_pkmsp, new GVCRenderItemPKMsp());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_svd, new GVCRenderItemSVD());
				
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m870, new GVCRenderItemM870());
				
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m16a4, new GVCRenderItemM16A4());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m16a4sp, new GVCRenderItemM16A4sp());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m16a4gl, new GVCRenderItemM16A4M320());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m4a1, new GVCRenderItemM4A1());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m110, new GVCRenderItemM110());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m249, new GVCRenderItemM249());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m240b, new GVCRenderItemM240B());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m240bsp, new GVCRenderItemM240Bsp());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_r700, new GVCRenderItemR700());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_mp7, new GVCRenderItemMP7());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m9, new GVCRenderItemM9());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_p226, new GVCRenderItemP226());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_g17, new GVCRenderItemG17());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_g18, new GVCRenderItemG18());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m82a3, new GVCRenderItemM82A3());
				
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_g36, new GVCRenderItemG36());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_g36c, new GVCRenderItemG36C());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_mg36, new GVCRenderItemMG36());
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_g36m320, new GVCRenderItemG36M320());
				
				
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_m134, new GVCRenderItemM134());
				
				MinecraftForgeClient.registerItemRenderer(GVCGunsPlus.fn_rpg7, new GVCRenderItemRPG7());
				}
				//MinecraftForgeClient.

				
				//GVCEntityPlus
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityBox.class, new GVCRenderBox()); 
				RenderingRegistry.registerEntityRenderingHandler(GVCEntityTarget.class, new GVCRenderTarget()); 
				VillagerRegistry.instance().registerVillagerSkin(GVCEntityPlus.GVCVillagerProfession, new ResourceLocation("gvcguns:textures/entity/mob/gvcVillager.png"));
				RenderingRegistry.registerEntityRenderingHandler(GVCEntitySentry.class, new GVCRenderSentry()); 
				RenderingRegistry.registerEntityRenderingHandler(GVCEntitySentryBullet.class, new GVCRenderSentryBullet()); 
				RenderingRegistry.registerEntityRenderingHandler(GVCEntitySentryAAG.class, new GVCRenderSentryAAG()); 
				RenderingRegistry.registerEntityRenderingHandler(GVCEntitySentryBulletAAG.class, new GVCRenderSentryBulletAAG()); 
				
    
    
    }
    
    @Override
    public void registerTileEntity() {
    	//GameRegistry.registerTileEntity(GVCTileEntityBlockIED.class, "GVCTileEntityBlockIED");
    	//ClientRegistry.registerTileEntity(GVCTileEntityBlockIED.class, "GVCTileEntityBlockIED", new GVCRenderTileEntityBlockIED());
    	//TileEntitySpecialRenderer render1 = new GVCRenderTileEntityBlockIED();
    	//ClientRegistry.bindTileEntitySpecialRenderer(GVCTileEntityBlockIED.class, render1);
		
    }
    
    @Override
    public void InitRendering()
    {
    	//ClientRegistry.bindTileEntitySpecialRenderer(GVCTileEntityItemG36.class, new GVCRenderItemG36());
    }

    
    
 
}