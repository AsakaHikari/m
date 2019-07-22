package mods.railcraft.common.carts;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;

public class Train
  implements Iterable<EntityMinecart>
{
  public static final String TRAIN_HIGH = "rcTrainHigh";
  public static final String TRAIN_LOW = "rcTrainLow";
  private static final Map<UUID, Train> trains = new HashMap();
  private final UUID uuid;
  private final LinkedList<UUID> carts = new LinkedList();
  private final List<UUID> safeCarts = Collections.unmodifiableList(this.carts);
  private final Set<UUID> lockingTracks = new HashSet();
  private TrainState trainState = TrainState.NORMAL;

  public Train(EntityMinecart cart) {
    this.uuid = UUID.randomUUID();

    buildTrain(null, cart);
  }

  public static Train getTrain(EntityMinecart cart) {
    if (cart == null)
      return null;
    Train train = (Train)trains.get(getTrainUUID(cart));
    if ((train != null) && (!train.containsCart(cart))) {
      train.releaseTrain();
      trains.remove(train.getUUID());
      train = null;
    }
    if (train != null) {
      train.validate();
      if (train.isEmpty()) {
        train = null;
      }
    }
    if (train == null) {
      train = new Train(cart);
      trains.put(train.getUUID(), train);
    }
    return train;
  }

  private static Train getTrainUnsafe(EntityMinecart cart) {
    if (cart == null)
      return null;
    return (Train)trains.get(getTrainUUID(cart));
  }

  public static UUID getTrainUUID(EntityMinecart cart) {
    NBTTagCompound nbt = cart.getEntityData();
    if (nbt.hasKey("rcTrainHigh")) {
      long high = nbt.getLong("rcTrainHigh");
      long low = nbt.getLong("rcTrainLow");
      return new UUID(high, low);
    }
    return null;
  }

  public static void resetTrain(EntityMinecart cart) {
    Train train = (Train)trains.remove(getTrainUUID(cart));
    if (train != null)
      train.releaseTrain();
  }

  public static void resetTrain(UUID uuid) {
    Train train = (Train)trains.remove(uuid);
    if (train != null)
      train.releaseTrain();
  }

  public static boolean areInSameTrain(EntityMinecart cart1, EntityMinecart cart2) {
    if ((cart1 == null) || (cart2 == null))
      return false;
    if (cart1 == cart2) {
      return true;
    }
    UUID train1 = getTrainUUID(cart1);
    UUID train2 = getTrainUUID(cart2);

    return (train1 != null) && (train1.equals(train2));
  }

  public static Train getLongestTrain(EntityMinecart cart1, EntityMinecart cart2) {
    Train train1 = getTrain(cart1);
    Train train2 = getTrain(cart2);

    if (train1 == train2)
      return train1;
    if (train1.size() >= train2.size())
      return train1;
    return train2;
  }

  public static void removeTrainTag(EntityMinecart cart) {
    cart.getEntityData().removeTag("rcTrainHigh");
    cart.getEntityData().removeTag("rcTrainLow");
  }

  public static void addTrainTag(EntityMinecart cart, Train train) {
    UUID trainId = train.getUUID();
    cart.getEntityData().setLong("rcTrainHigh", trainId.getMostSignificantBits());
    cart.getEntityData().setLong("rcTrainLow", trainId.getLeastSignificantBits());
  }

  public Train getTrain(UUID cartUUID) {
    if (cartUUID == null)
      return null;
    EntityMinecart cart = LinkageManager.instance().getCartFromUUID(cartUUID);
    if (cart == null)
      return null;
    return getTrain(cart);
  }

  public Iterator<EntityMinecart> iterator()
  {
    return new Iterator() {
      private final Iterator<UUID> it = Train.this.carts.iterator();
      private final LinkageManager lm = LinkageManager.instance();

      public boolean hasNext()
      {
        return this.it.hasNext();
      }

      public EntityMinecart next()
      {
        return this.lm.getCartFromUUID((UUID)this.it.next());
      }

      public void remove()
      {
        throw new UnsupportedOperationException("Removing not supported.");
      }
    };
  }


  private void buildTrain(EntityMinecart prev, EntityMinecart next) {
    _addLink(prev, next);

    LinkageManager lm = LinkageManager.instance();
    EntityMinecart linkA = lm.getLinkedCartA(next);
    EntityMinecart linkB = lm.getLinkedCartB(next);

    if ((linkA != null) && (linkA != prev) && (!containsCart(linkA))) {
      buildTrain(next, linkA);
    }
    if ((linkB != null) && (linkB != prev) && (!containsCart(linkB)))
      buildTrain(next, linkB);
  }

  private void dropCarts(EntityMinecart cart) {
    _removeCart(cart);

    LinkageManager lm = LinkageManager.instance();
    EntityMinecart linkA = lm.getLinkedCartA(cart);
    EntityMinecart linkB = lm.getLinkedCartB(cart);

    if ((linkA != null) && (containsCart(linkA))) dropCarts(linkA);
    if ((linkB != null) && (containsCart(linkB))) dropCarts(linkB); 
  }

  public void validate()
  {
    if (!isValid()) {
      UUID first = null;
      for (UUID id : this.carts) {
        if (isCartValid(id)) {
          first = id;
          break;
        }
      }
      releaseTrain();
      if (first == null)
        trains.remove(getUUID());
      else
        buildTrain(null, LinkageManager.instance().getCartFromUUID(first));
    }
  }

  private boolean isValid() {
    for (UUID id : this.carts) {
      if (!isCartValid(id))
        return false;
    }
    return true;
  }

  private boolean isCartValid(UUID cartId) {
    EntityMinecart cart = LinkageManager.instance().getCartFromUUID(cartId);
    return (cart != null) && (this.uuid.equals(getTrainUUID(cart)));
  }

  protected void releaseTrain() {
    LinkageManager lm = LinkageManager.instance();
    for (UUID id : this.carts) {
      EntityMinecart cart = lm.getCartFromUUID(id);
      if (cart != null) {
        removeTrainTag(cart);
      }
    }
    this.carts.clear();
    this.lockingTracks.clear();
  }

  public UUID getUUID() {
    return this.uuid;
  }

  public void removeAllLinkedCarts(EntityMinecart cart) {
    UUID cartId = cart.getPersistentID();
    if (this.carts.contains(cartId))
      dropCarts(cart);
  }

  public void addLink(EntityMinecart cart1, EntityMinecart cart2)
  {
    if (isTrainEnd(cart1))
      buildTrain(cart1, cart2);
    else if (isTrainEnd(cart2))
      buildTrain(cart2, cart1);
  }

  private void _addLink(EntityMinecart cartBase, EntityMinecart cartNew) {
    if ((cartBase == null) || (this.carts.getFirst() == cartBase.getPersistentID()))
      this.carts.addFirst(cartNew.getPersistentID());
    else if (this.carts.getLast() == cartBase.getPersistentID())
      this.carts.addLast(cartNew.getPersistentID());
    else
      return;
    Train train = getTrainUnsafe(cartNew);
    if ((train != null) && (train != this))
      train._removeCart(cartNew);
    addTrainTag(cartNew, this);
  }

  private boolean _removeCart(EntityMinecart cart) {
    boolean removed = _removeCart(cart.getPersistentID());
    if ((removed) && (this.uuid.equals(getTrainUUID(cart)))) {
      removeTrainTag(cart);
    }
    return removed;
  }

  private boolean _removeCart(UUID cart) {
    boolean removed = this.carts.remove(cart);
    if ((removed) && 
      (this.carts.isEmpty())) {
      releaseTrain();
      trains.remove(getUUID());
    }

    return removed;
  }

  public boolean containsCart(EntityMinecart cart) {
    if (cart == null)
      return false;
    return this.carts.contains(cart.getPersistentID());
  }

  public boolean isTrainEnd(EntityMinecart cart) {
    if (cart == null)
      return false;
    return getEnds().contains(cart.getPersistentID());
  }

  public Set<UUID> getEnds() {
    Set ends = new HashSet();
    ends.add(this.carts.getFirst());
    ends.add(this.carts.getLast());
    return ends;
  }

  public List<UUID> getCartsInTrain() {
    return this.safeCarts;
  }

  public int size() {
    return this.carts.size();
  }

  public boolean isEmpty() {
    return this.carts.isEmpty();
  }


  public void refreshMaxSpeed() {
    setMaxSpeed(getMaxSpeed());
  }

  public float getMaxSpeed() {
    float speed = 1.2F;
   
    return speed;
  }

  public void setMaxSpeed(float trainSpeed) {
    for (EntityMinecart c : this)
      c.setCurrentCartSpeedCapOnRail(trainSpeed);
  }

  public boolean isTrainLockedDown()
  {
    return !this.lockingTracks.isEmpty();
  }

  public void addLockingTrack(UUID track) {
    this.lockingTracks.add(track);
  }

  public void removeLockingTrack(UUID track) {
    this.lockingTracks.remove(track);
  }

  public boolean isIdle() {
    return (this.trainState == TrainState.IDLE) || (isTrainLockedDown());
  }

  public boolean isStopped() {
    return this.trainState == TrainState.STOPPED;
  }

  public void setTrainState(TrainState state) {
    this.trainState = state;
  }

  public static enum TrainState
  {
    STOPPED, 
    IDLE, 
    NORMAL;
  }
}