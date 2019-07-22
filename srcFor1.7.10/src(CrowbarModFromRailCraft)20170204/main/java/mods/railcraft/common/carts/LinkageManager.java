package mods.railcraft.common.carts;

import com.google.common.collect.MapMaker;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import mods.railcraft.api.carts.CartTools;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.carts.ILinkageManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;

public class LinkageManager
  implements ILinkageManager
{
  public static final String AUTO_LINK = "rcAutoLink";
  public static final String LINK_A_HIGH = "rcLinkAHigh";
  public static final String LINK_A_LOW = "rcLinkALow";
  public static final String LINK_B_HIGH = "rcLinkBHigh";
  public static final String LINK_B_LOW = "rcLinkBLow";
  private final Map<UUID, EntityMinecart> carts = new MapMaker().weakValues().makeMap();

  public static LinkageManager instance()
  {
    return (LinkageManager)CartTools.linkageManager;
  }

  public static void printDebug(String msg, Object[] args) {
    //if (RailcraftConfig.printLinkingDebug())
      //Game.log(Level.DEBUG, msg, args);
  }

  public static void reset() {
    CartTools.linkageManager = new LinkageManager();
  }

  public void removeLinkageId(EntityMinecart cart)
  {
    this.carts.remove(getLinkageId(cart));
  }

  public UUID getLinkageId(EntityMinecart cart)
  {
    UUID id = cart.getPersistentID();
    if (!cart.isDead)
      this.carts.put(id, cart);
    return id;
  }

  public EntityMinecart getCartFromUUID(UUID id)
  {
    EntityMinecart cart = (EntityMinecart)this.carts.get(id);
    if ((cart != null) && (cart.isDead)) {
      this.carts.remove(id);
      return null;
    }
    return (EntityMinecart)this.carts.get(id);
  }

  private float getLinkageDistanceSq(EntityMinecart cart1, EntityMinecart cart2)
  {
    float dist = 0.0F;
    if ((cart1 instanceof ILinkableCart))
      dist += ((ILinkableCart)cart1).getLinkageDistance(cart2);
    else
      dist += 1.25F;
    if ((cart2 instanceof ILinkableCart))
      dist += ((ILinkableCart)cart2).getLinkageDistance(cart1);
    else
      dist += 1.25F;
    return dist * dist;
  }

  public boolean setAutoLink(EntityMinecart cart, boolean autoLink)
  {
    if ((autoLink) && (hasFreeLink(cart))) {
      cart.getEntityData().setBoolean("rcAutoLink", true);
      printDebug("Cart {0}({1}) Set To Auto Link With First Collision.", new Object[] { getLinkageId(cart), cart });
      return true;
    }
    if (!autoLink) {
      cart.getEntityData().removeTag("rcAutoLink");
      return true;
    }
    return false;
  }

  public boolean hasAutoLink(EntityMinecart cart)
  {
    if (!hasFreeLink(cart))
      cart.getEntityData().removeTag("rcAutoLink");
    return cart.getEntityData().getBoolean("rcAutoLink");
  }

  public boolean tryAutoLink(EntityMinecart cart1, EntityMinecart cart2)
  {
    if (((hasAutoLink(cart1)) || (hasAutoLink(cart2))) && 
      (createLink(cart1, cart2)))
    {
      cart1.getEntityData().removeTag("rcAutoLink");
      cart2.getEntityData().removeTag("rcAutoLink");
      printDebug("Automatically Linked Carts {0}({1}) and {2}({3}).", new Object[] { getLinkageId(cart1), cart1, getLinkageId(cart2), cart2 });
     
      return true;
    }
    return false;
  }

  private boolean canLinkCarts(EntityMinecart cart1, EntityMinecart cart2)
  {
    if ((cart1 == null) || (cart2 == null)) {
      return false;
    }
    if (cart1 == cart2) {
      return false;
    }
    if ((cart1 instanceof ILinkableCart)) {
      ILinkableCart link = (ILinkableCart)cart1;
      if ((!link.isLinkable()) || (!link.canLinkWithCart(cart2))) {
        return false;
      }
    }
    if ((cart2 instanceof ILinkableCart)) {
      ILinkableCart link = (ILinkableCart)cart2;
      if ((!link.isLinkable()) || (!link.canLinkWithCart(cart1))) {
        return false;
      }
    }
    if (areLinked(cart1, cart2)) {
      return false;
    }
    if (cart1.getDistanceSqToEntity(cart2) > getLinkageDistanceSq(cart1, cart2)) {
      return false;
    }
    if (Train.areInSameTrain(cart1, cart2)) {
      return false;
    }
    return (hasFreeLink(cart1)) && (hasFreeLink(cart2));
  }

  public boolean createLink(EntityMinecart cart1, EntityMinecart cart2)
  {
    if (canLinkCarts(cart1, cart2)) {
      Train train = Train.getLongestTrain(cart1, cart2);

      setLink(cart1, cart2);
      setLink(cart2, cart1);

      train.addLink(cart1, cart2);

      if ((cart1 instanceof ILinkableCart))
        ((ILinkableCart)cart1).onLinkCreated(cart2);
      if ((cart2 instanceof ILinkableCart)) {
        ((ILinkableCart)cart2).onLinkCreated(cart1);
      }

      return true;
    }
    return false;
  }

  public boolean hasFreeLink(EntityMinecart cart)
  {
    return (getLinkedCartA(cart) == null) || ((hasLink(cart, LinkType.LINK_B)) && (getLinkedCartB(cart) == null));
  }

  public boolean hasLink(EntityMinecart cart, LinkType linkType) {
    if ((linkType == LinkType.LINK_B) && ((cart instanceof ILinkableCart)))
      return ((ILinkableCart)cart).hasTwoLinks();
    return true;
  }

  private void setLink(EntityMinecart cart1, EntityMinecart cart2) {
    if (getLinkedCartA(cart1) == null)
      setLink(cart1, cart2, LinkType.LINK_A);
    else if ((hasLink(cart1, LinkType.LINK_B)) && (getLinkedCartB(cart1) == null))
      setLink(cart1, cart2, LinkType.LINK_B);
  }

  public UUID getLink(EntityMinecart cart, LinkType linkType) {
    long high = cart.getEntityData().getLong(linkType.tagHigh);
    long low = cart.getEntityData().getLong(linkType.tagLow);
    return new UUID(high, low);
  }

  public UUID getLinkA(EntityMinecart cart) {
    return getLink(cart, LinkType.LINK_A);
  }

  public UUID getLinkB(EntityMinecart cart) {
    return getLink(cart, LinkType.LINK_B);
  }

  private void setLink(EntityMinecart cart1, EntityMinecart cart2, LinkType linkType) {
    if (!hasLink(cart1, linkType))
      return;
    UUID id = getLinkageId(cart2);
    cart1.getEntityData().setLong(linkType.tagHigh, id.getMostSignificantBits());
    cart1.getEntityData().setLong(linkType.tagLow, id.getLeastSignificantBits());
  }

  public EntityMinecart getLinkedCartA(EntityMinecart cart)
  {
    return getLinkedCart(cart, LinkType.LINK_A);
  }

  public EntityMinecart getLinkedCartB(EntityMinecart cart)
  {
    return getLinkedCart(cart, LinkType.LINK_B);
  }

  public EntityMinecart getLinkedCart(EntityMinecart cart, LinkType type) {
    return getCartFromUUID(getLink(cart, type));
  }

  @Deprecated
  public Train getTrain(EntityMinecart cart) {
    return Train.getTrain(cart);
  }

  @Deprecated
  public Train getTrain(UUID cartUUID) {
    if (cartUUID == null)
      return null;
    EntityMinecart cart = getCartFromUUID(cartUUID);
    if (cart == null)
      return null;
    return getTrain(cart);
  }

  @Deprecated
  public UUID getTrainUUID(EntityMinecart cart) {
    return Train.getTrainUUID(cart);
  }

  @Deprecated
  public void resetTrain(EntityMinecart cart) {
    Train.resetTrain(cart);
  }

  @Deprecated
  public boolean areInSameTrain(EntityMinecart cart1, EntityMinecart cart2) {
    return Train.areInSameTrain(cart1, cart2);
  }

  public boolean areLinked(EntityMinecart cart1, EntityMinecart cart2)
  {
    return areLinked(cart1, cart2, true);
  }

  public boolean areLinked(EntityMinecart cart1, EntityMinecart cart2, boolean strict)
  {
    if ((cart1 == null) || (cart2 == null))
      return false;
    if (cart1 == cart2) {
      return false;
    }

    boolean cart1Linked = false;
    UUID id1 = getLinkageId(cart1);
    UUID id2 = getLinkageId(cart2);
    if ((id2.equals(getLinkA(cart1))) || (id2.equals(getLinkB(cart1)))) {
      cart1Linked = true;
    }
    boolean cart2Linked = false;
    if ((id1.equals(getLinkA(cart2))) || (id1.equals(getLinkB(cart2)))) {
      cart2Linked = true;
    }
    if (strict) {
      return (cart1Linked) && (cart2Linked);
    }
    return (cart1Linked) || (cart2Linked);
  }

  public void breakLink(EntityMinecart cart1, EntityMinecart cart2)
  {
    UUID link = getLinkageId(cart2);
    if (link.equals(getLinkA(cart1))) {
      breakLinkA(cart1);
    }
    if (link.equals(getLinkB(cart1)))
      breakLinkB(cart1);
  }

  public void breakLinks(EntityMinecart cart)
  {
    breakLink(cart, LinkType.LINK_A);
    breakLink(cart, LinkType.LINK_B);
  }

  public void breakLinkA(EntityMinecart cart)
  {
    breakLink(cart, LinkType.LINK_A);
  }

  public void breakLinkB(EntityMinecart cart)
  {
    breakLink(cart, LinkType.LINK_B);
  }

  private EntityMinecart breakLink(EntityMinecart cart, LinkType linkType) {
    Train.resetTrain(cart);
    UUID link = getLink(cart, linkType);
    cart.getEntityData().setLong(linkType.tagHigh, 0L);
    cart.getEntityData().setLong(linkType.tagLow, 0L);
    EntityMinecart other = getCartFromUUID(link);
    if (other != null) {
      breakLink(other, cart);
    }
    if ((cart instanceof ILinkableCart)) {
      ((ILinkableCart)cart).onLinkBroken(other);
    }
    printDebug("Carts {0}({1}) and {2}({3}) unlinked ({4}).", new Object[] { getLinkageId(cart), cart, link, other != null ? other : "null", linkType.name() });
    return other;
  }

  public int countCartsInTrain(EntityMinecart cart)
  {
    return Train.getTrain(cart).size();
  }

  private int numLinkedCarts(EntityMinecart prev, EntityMinecart next) {
    int count = 1;
    EntityMinecart linkA = getLinkedCartA(next);
    EntityMinecart linkB = getLinkedCartB(next);

    if ((linkA != null) && (linkA != prev))
      count += numLinkedCarts(next, linkA);
    if ((linkB != null) && (linkB != prev))
      count += numLinkedCarts(next, linkB);
    return count;
  }

  public Iterable<EntityMinecart> getCartsInTrain(EntityMinecart cart)
  {
    return Train.getTrain(cart);
  }

  public static enum LinkType {
    LINK_A("rcLinkAHigh", "rcLinkALow"), 
    LINK_B("rcLinkBHigh", "rcLinkBLow");

    public final String tagHigh;
    public final String tagLow;

    private LinkType(String tagHigh, String tagLow) { this.tagHigh = tagHigh;
      this.tagLow = tagLow;
    }
  }
}