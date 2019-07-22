package gvcguns;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiStreamIndicator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GVCGuiADSClient extends GuiIngame
{
    protected static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    protected static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    protected static final RenderItem itemRenderer = new RenderItem();
    protected final Random rand = new Random();
    protected final Minecraft mc;
    /** ChatGUI instance that retains all previous chat data */
    protected final GuiNewChat persistantChatGUI;
    protected final GuiStreamIndicator field_152127_m;
    protected int updateCounter;
    /** The string specifying which record music is playing */
    protected String recordPlaying = "";
    /** How many ticks the record playing message will be displayed */
    protected int recordPlayingUpFor;
    protected boolean recordIsPlaying;
    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    public float prevVignetteBrightness = 1.0F;
    /** Remaining ticks the item highlight should be visible */
    protected int remainingHighlightTicks;
    /** The ItemStack that is currently being highlighted */
    protected ItemStack highlightingItemStack;
    private static final String __OBFID = "CL_00000661";

    public GVCGuiADSClient(Minecraft p_i1036_1_)
    {
    	super(p_i1036_1_);
        this.mc = p_i1036_1_;
        this.persistantChatGUI = new GuiNewChat(p_i1036_1_);
        this.field_152127_m = new GuiStreamIndicator(this.mc);
    }

    /**
     * Render the ingame overlay with quick icon bar, ...
     */
    public void renderGameOverlay(float p_73830_1_, boolean p_73830_2_, int p_73830_3_, int p_73830_4_)
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = this.mc.fontRenderer;
        this.mc.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);

        if (Minecraft.isFancyGraphicsEnabled())
        {
            this.renderVignette(this.mc.thePlayer.getBrightness(p_73830_1_), k, l);
        }
        else
        {
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }

        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);

        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
        {
            this.renderPumpkinBlur(k, l);
        }

        if (!this.mc.thePlayer.isPotionActive(Potion.confusion))
        {
            float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * p_73830_1_;

            if (f1 > 0.0F)
            {
                this.func_130015_b(f1, k, l);
            }
        }

        
        
    }

    public void renderPumpkinBlur(int p_73836_1_, int p_73836_2_)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, (double)p_73836_2_, -90.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)p_73836_1_, (double)p_73836_2_, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)p_73836_1_, 0.0D, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    public static void renderADS(int p_73836_1_, int p_73836_2_, Minecraft minecraft)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        minecraft.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, (double)p_73836_2_, -90.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double)p_73836_1_, (double)p_73836_2_, -90.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV((double)p_73836_1_, 0.0D, -90.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}