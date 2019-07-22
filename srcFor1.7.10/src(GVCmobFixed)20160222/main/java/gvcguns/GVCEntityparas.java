package gvcguns;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GVCEntityparas extends EntityAnimal
{
    public float field_70886_e;
    public float destPos;
    public float field_70884_g;
    public float field_70888_h;
    public float field_70889_i = 1.0F;

    /** The time until the next egg is spawned. */
    public int timeUntilNextEgg;

    public GVCEntityparas(World par1World)
    {
        super(par1World);
        this.setSize(1.0F, 1.0F);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 0D));
        this.tasks.addTask(2, new EntityAIMate(this, 0D));
        //this.tasks.addTask(3, new EntityAITempt(this, 1.0D, Item.seeds.itemID, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 0D));
        this.tasks.addTask(5, new EntityAIWander(this, 0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1.0D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.field_70888_h = this.field_70886_e;
        this.field_70884_g = this.destPos;
        this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3D);

        if (this.destPos < 0.0F)
        {
            this.destPos = 0.0F;
        }

        if (this.destPos > 1.0F)
        {
            this.destPos = 1.0F;
        }

        if (!this.onGround && this.field_70889_i < 1.0F)
        {
            this.field_70889_i = 1.0F;
        }

        this.field_70889_i = (float)((double)this.field_70889_i * 0.9D);

        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }

        this.field_70886_e += this.field_70889_i * 2.0F;

        
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    //protected void fall(float par1) {}

    

    

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public GVCEntityparas spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        return null;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.getItem() instanceof ItemSeeds;
    }

    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return null;
    }

	public static float getMobScale() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
	}
}
