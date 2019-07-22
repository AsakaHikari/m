
package mods.railcraft.common.items;

import com.google.common.collect.MapMaker;

import java.util.Map;

import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.core.items.IToolCrowbar;
import mods.railcraft.common.carts.LinkageManager;
import mods.railcraft.common.plugins.forge.ChatPlugin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrowbarHandler
{
  public static final float SMACK_VELOCITY = 0.07F;
  private static final Map<EntityPlayer, EntityMinecart> linkMap = new MapMaker().weakKeys().weakValues().makeMap();
  private static CrowbarHandler instance;

  public static CrowbarHandler instance()
  {
    if (instance == null)
      instance = new CrowbarHandler();
    return instance;
  }

  @SubscribeEvent
  public void onEntityInteract(EntityInteract event) {
    EntityPlayer thePlayer = event.getEntityPlayer();
    Entity entity = event.getTarget();

    ItemStack stack = thePlayer.getHeldItemMainhand();
    if ((stack != null) && ((stack.getItem() instanceof IToolCrowbar))) {
      thePlayer.swingArm(EnumHand.MAIN_HAND);
    }
   
    boolean used = false;
    if ((stack != null) && ((stack.getItem() instanceof IToolCrowbar))) {
      IToolCrowbar crowbar = (IToolCrowbar)stack.getItem();
      if ((entity instanceof EntityMinecart)) {
        EntityMinecart cart = (EntityMinecart)entity;

        if (crowbar.canLink(thePlayer, stack, cart)) {
          boolean linkable = cart instanceof ILinkableCart;
          if ((!linkable) || (((ILinkableCart)cart).isLinkable())) {
            EntityMinecart last = (EntityMinecart)linkMap.remove(thePlayer);
            if ((last != null) && (!last.isDead)) {
              LinkageManager lm = LinkageManager.instance();
              if (lm.areLinked(cart, last, false)) {
                lm.breakLink(cart, last);
                used = true;
                ChatPlugin.sendLocalizedChatFromServer(thePlayer, "railcraft.gui.link.broken", new Object[0]);
                LinkageManager.printDebug("Reason For Broken Link: User removed link.", new Object[0]);
              } else {
                used = lm.createLink(last, (EntityMinecart)entity);
                if (used)
                  ChatPlugin.sendLocalizedChatFromServer(thePlayer, "railcraft.gui.link.created", new Object[0]);
              }
              if (!used)
                ChatPlugin.sendLocalizedChatFromServer(thePlayer, "railcraft.gui.link.failed", new Object[0]);
            } else {
              linkMap.put(thePlayer, (EntityMinecart)entity);
              ChatPlugin.sendLocalizedChatFromServer(thePlayer, "railcraft.gui.link.started", new Object[0]);
            }
          }
          if (used)
            crowbar.onLink(thePlayer, stack, cart);
        } else if (crowbar.canBoost(thePlayer, stack, cart)) {
          thePlayer.addExhaustion(1.0F);

          if (thePlayer.getRidingEntity() == null)
          {
            
              {
                if (cart.posX < thePlayer.posX)
                  cart.motionX -= 0.07000000029802322D;
                else
                  cart.motionX += 0.07000000029802322D;
                if (cart.posZ < thePlayer.posZ)
                  cart.motionZ -= 0.07000000029802322D;
                else
                  cart.motionZ += 0.07000000029802322D; 
              }
            }
          
          crowbar.onBoost(thePlayer, stack, cart);
        }
      }
    }
    if (used)
      event.setCanceled(true);
  }
}