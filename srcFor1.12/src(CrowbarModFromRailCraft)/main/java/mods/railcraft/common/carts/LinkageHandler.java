package mods.railcraft.common.carts;

import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.core.items.IToolCrowbar;
import mods.railcraft.api.tracks.RailTools;
import mods.railcraft.common.util.misc.Vec2D;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LinkageHandler
{
  public static final String LINK_A_TIMER = "linkA_timer";
  public static final String LINK_B_TIMER = "linkB_timer";
  public static final double LINK_DRAG = 0.95D;
  public static final float MAX_DISTANCE = 8.0F;
  private static final float STIFFNESS = 0.7F;
  private static final float HS_STIFFNESS = 0.7F;
  private static final float DAMPING = 0.4F;
  private static final float HS_DAMPING = 0.3F;
  private static final float FORCE_LIMITER = 6.0F;
  private static final int TICK_HISTORY = 200;
  private static LinkageHandler instance;

  public static LinkageHandler getInstance()
  {
    if (instance == null)
      instance = new LinkageHandler();
    return instance;
  }

  private float getOptimalDistance(EntityMinecart cart1, EntityMinecart cart2)
  {
    float dist = 0.0F;
    if ((cart1 instanceof ILinkableCart))
      dist += ((ILinkableCart)cart1).getOptimalDistance(cart2);
    else
      dist += 0.78F;
    if ((cart2 instanceof ILinkableCart))
      dist += ((ILinkableCart)cart2).getOptimalDistance(cart1);
    else
      dist += 0.78F;
    return dist;
  }

  private boolean canCartBeAdjustedBy(EntityMinecart cart1, EntityMinecart cart2) {
    if (cart1 == cart2)
      return false;
    if (((cart1 instanceof ILinkableCart)) && (!((ILinkableCart)cart1).canBeAdjusted(cart2)))
      return false;
    return true;
  }

  protected void adjustVelocity(EntityMinecart cart1, EntityMinecart cart2, char link)
  {
    String timer = "linkA_timer";
    if (link == 'B')
      timer = "linkB_timer";
    if (cart1.world.provider.getDimension() != cart2.world.provider.getDimension()) {
      short count = cart1.getEntityData().getShort(timer);
      count = (short)(count + 1);
      if (count > 200) {
        LinkageManager.instance().breakLink(cart1, cart2);
        LinkageManager.printDebug("Reason For Broken Link: Carts in different dimensions.", new Object[0]);
      }
      cart1.getEntityData().setShort(timer, count);
      return;
    }
    cart1.getEntityData().setShort(timer, (short)0);

    double dist = cart1.getDistanceToEntity(cart2);
    if (dist > 8.0D) {
      LinkageManager.instance().breakLink(cart1, cart2);
      LinkageManager.printDebug("Reason For Broken Link: Max distance exceeded.", new Object[0]);
      return;
    }

    boolean adj1 = canCartBeAdjustedBy(cart1, cart2);
    boolean adj2 = canCartBeAdjustedBy(cart2, cart1);

    Vec2D cart1Pos = new Vec2D(cart1.posX, cart1.posZ);
    Vec2D cart2Pos = new Vec2D(cart2.posX, cart2.posZ);

    Vec2D unit = Vec2D.subtract(cart2Pos, cart1Pos);
    unit.normalize();

    float optDist = getOptimalDistance(cart1, cart2);
    double stretch = dist - optDist;

    boolean highSpeed = cart1.getEntityData().getBoolean("HighSpeed");

    double stiffness = highSpeed ? 0.699999988079071D : 0.699999988079071D;
    double springX = stiffness * stretch * unit.getX();
    double springZ = stiffness * stretch * unit.getY();

    springX = limitForce(springX);
    springZ = limitForce(springZ);

    if (adj1) {
      cart1.motionX += springX;
      cart1.motionZ += springZ;
    }

    if (adj2) {
      cart2.motionX -= springX;
      cart2.motionZ -= springZ;
    }

    Vec2D cart1Vel = new Vec2D(cart1.motionX, cart1.motionZ);
    Vec2D cart2Vel = new Vec2D(cart2.motionX, cart2.motionZ);

    double dot = Vec2D.subtract(cart2Vel, cart1Vel).dotProduct(unit);

    double damping = highSpeed ? 0.300000011920929D : 0.4000000059604645D;
    double dampX = damping * dot * unit.getX();
    double dampZ = damping * dot * unit.getY();

    dampX = limitForce(dampX);
    dampZ = limitForce(dampZ);

    if (adj1) {
      cart1.motionX += dampX;
      cart1.motionZ += dampZ;
    }

    if (adj2) {
      cart2.motionX -= dampX;
      cart2.motionZ -= dampZ;
    }
  }

  private double limitForce(double force) {
    return Math.copySign(Math.min(Math.abs(force), 6.0D), force);
  }

  private void adjustCart(EntityMinecart cart, LinkageManager lm)
  {
    int launched = cart.getEntityData().getInteger("Launched");
    if (launched > 0) {
      return;
    }
    if (isOnElevator(cart)) {
      return;
    }
    boolean linked = false;

    EntityMinecart link_A = lm.getLinkedCartA(cart);
    if (link_A != null)
    {
      if (!Train.areInSameTrain(cart, link_A)) {
        lm.breakLink(cart, link_A);
        lm.createLink(cart, link_A);
        return;
      }
      launched = link_A.getEntityData().getInteger("Launched");
      if ((launched <= 0) && (!isOnElevator(link_A))) {
        linked = true;
        adjustVelocity(cart, link_A, 'A');
      }

    }

    EntityMinecart link_B = lm.getLinkedCartB(cart);
    if (link_B != null)
    {
      if (!Train.areInSameTrain(cart, link_B)) {
        lm.breakLink(cart, link_B);
        lm.createLink(cart, link_B);
        return;
      }
      launched = link_B.getEntityData().getInteger("Launched");
      if ((launched <= 0) && (!isOnElevator(link_B))) {
        linked = true;
        adjustVelocity(cart, link_B, 'B');
      }

    }


    if (((link_A == null) && (link_B != null)) || ((link_A != null) && (link_B == null)))
      Train.getTrain(cart).refreshMaxSpeed();
    else if (link_A == null)
      Train.getTrain(cart).setMaxSpeed(1.2F);
  }

  @SubscribeEvent
  public void onMinecartUpdate(MinecartUpdateEvent event)
  {
    EntityMinecart cart = event.getMinecart();

    LinkageManager lm = LinkageManager.instance();

    if (cart.isDead) {
      lm.removeLinkageId(cart);
      return;
    }

    lm.getLinkageId(cart);

    adjustCart(cart, lm);
  }

  @SubscribeEvent
  public void onMinecartInteract(MinecartInteractEvent event)
  {
    EntityPlayer player = event.getPlayer();
    if ((player.getHeldItemMainhand() != null) && ((player.getHeldItemMainhand().getItem() instanceof IToolCrowbar)))
      event.setCanceled(true);
  }

  private boolean isOnElevator(EntityMinecart cart) {
    int elevator = cart.getEntityData().getByte("elevator");
    return elevator > 0;
  }
}