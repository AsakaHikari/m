package gvcguns.client;
 
import gvcguns.GVCGunsPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
 
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
 
public class GVCRenderItemM134 implements IItemRenderer
{
    private GVCModelItemM134 modeling;
    private static final ResourceLocation skeletonTextures = new ResourceLocation("gvcguns:textures/model/modelM134.png"); 
    private static final ResourceLocation skeletonTextures2 = new ResourceLocation("gvcguns:textures/model/modelM134.png"); 
    public GVCRenderItemM134()
    {
        modeling = new GVCModelItemM134();
    }
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
    	
        switch(type.ordinal())
        {
        //case 1: //entity
        case 3: //item
        	return false;
        case 2: //handitem
        //case 3: 
        //case 4: //?
        case 1: 
        	return true;
        }
        return false;
    }
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
    	
    	switch(type.ordinal())
        {
        //case 1: 
        case 3: 
        	return false;
        case 2: 
        //case 3: 
        //case 4: 
        case 1: 
        	return true;
        }
        return false;
    }
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
    	float scala = 2.0F;
    	float scala2 = 2.5F;
    	switch(type.ordinal())
        {
        case 4: 
           glMatrixForRenderInInventory(); break;
          case 2: 
          //case 3: 
        	  
        	  Entity entity = Minecraft.getMinecraft().renderViewEntity;
        	  EntityPlayer entityplayer = (EntityPlayer)entity;
        	  if(entityplayer.isSneaking()|| GVCGunsPlus.adstype == 1){
        		  glMatrixForRenderInEquippedADS();
        	  }else{
                  glMatrixForRenderInEquipped();
        	  }
  			
            GL11.glPushMatrix();
            GL11.glScalef(scala, scala, scala);
            
            if(item.getItemDamage() == item.getMaxDamage()){
            Minecraft.getMinecraft().renderEngine.bindTexture(skeletonTextures2);
            }else{
            Minecraft.getMinecraft().renderEngine.bindTexture(skeletonTextures);
            }

            modeling.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

            GL11.glPopMatrix();
            break;
          case 1: 
        	  if ((data[1] != null) && ((data[1] instanceof EntityPlayer)))
              {
        		  glMatrixForRenderInEntityPlayer();
        		  GL11.glPushMatrix();
                  GL11.glScalef(scala2, scala2, scala2);
              }else{
            	  glMatrixForRenderInEntity();
            	  GL11.glPushMatrix();
                  GL11.glScalef(scala, scala, scala);
              }
            
            
            if(item.getItemDamage() == item.getMaxDamage()){
                Minecraft.getMinecraft().renderEngine.bindTexture(skeletonTextures2);
                }else{
                Minecraft.getMinecraft().renderEngine.bindTexture(skeletonTextures);
                }
            modeling.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

            GL11.glPopMatrix();
            break;
          case 3: 
              break;
        }
    	
    	
    	
    	
    }
    
    private void glMatrixForRenderInInventory()
    {
    	GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-30F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(1900F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(-0.8F, -1.2F, -0.1F);
    }
    
    private void glMatrixForRenderInEquipped()
    {
    	GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-30F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(190F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(-1.0F, -1.0F, -0.1F);
    }
    
    private void glMatrixForRenderInEquippedADS()
    {
    	GL11.glRotatef(-3.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-45F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(175.55F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.668F, -1.290F, 1.4F);
    }
    
    private void glMatrixForRenderInEntity()
    {
    	GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-30F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(190F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(-0.8F, -1.0F, -0.0F);
    }
    
    private void glMatrixForRenderInEntityPlayer()
    {
    	GL11.glRotatef(-60F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(270F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
}
    
