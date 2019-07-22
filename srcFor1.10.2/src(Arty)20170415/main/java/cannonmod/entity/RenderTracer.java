package cannonmod.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTracer extends Render<EntityTracer>
{
	public RenderTracer(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityTracer entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityTracer entity)
    {
        return TextureMap.LOCATION_MISSING_TEXTURE;
    }
}