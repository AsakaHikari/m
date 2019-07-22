package cannonmod.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFallingBlockEx extends Render<EntityFallingBlockEx>
{
    public RenderFallingBlockEx(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityFallingBlockEx entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.getBlock() != null)
        {
            this.bindTexture(TextureMap.locationBlocksTexture);
            IBlockState iblockstate = entity.getBlock();
            Block block = iblockstate.getBlock();
            BlockPos blockpos = new BlockPos(entity);
            World world = entity.worldObj;

            if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1)
            {
                if (block.getRenderType() == 3)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)x, (float)y, (float)z);
                    GlStateManager.disableLighting();
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                    int i = blockpos.getX();
                    int j = blockpos.getY();
                    int k = blockpos.getZ();
                    worldrenderer.setTranslation((double)((float)(-i) - 0.5F), (double)(-j), (double)((float)(-k) - 0.5F));
                    BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                    IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, (BlockPos)null);
                    blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                    worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GlStateManager.enableLighting();
                    GlStateManager.popMatrix();
                    super.doRender(entity, x, y, z, entityYaw, partialTicks);
                }
            }
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFallingBlockEx entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}