package mod;

import static cpw.mods.fml.common.eventhandler.Event.Result.DENY;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid="MINE_SPAWNER", name="MineSpawner", version="1.0")
public class Core
{
   @EventHandler
   public void init(FMLInitializationEvent event)
   {
	   MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onBlockBreak(BlockEvent.BreakEvent event)
   {
      if (event.isCancelable() && event.block instanceof BlockMobSpawner) {
         if (EnchantmentHelper.getSilkTouchModifier(event.getPlayer())) {
            TileEntity tile = (TileEntity)event.world.getTileEntity(event.x, event.y, event.z);
            if (tile instanceof TileEntityMobSpawner) {
               TileEntityMobSpawner spawner = (TileEntityMobSpawner)tile;
               ItemStack itemstack = new ItemStack(Blocks.mob_spawner);
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               itemstack.writeToNBT(nbttagcompound);
               NBTTagCompound spawner_nbttag = new NBTTagCompound();
               spawner.writeToNBT(spawner_nbttag);
               nbttagcompound.setString("EntityId", spawner_nbttag.getString("EntityId"));
               {
                  NBTTagCompound localnbt;
                  if (nbttagcompound.hasKey("display")) {
                     localnbt = nbttagcompound.getCompoundTag("display");
                  } else {
                     localnbt = new NBTTagCompound();
                     nbttagcompound.setTag("display", localnbt);
                  }
                  localnbt.setString("Name", EnumChatFormatting.LIGHT_PURPLE + spawner_nbttag.getString("EntityId") + " Spawner");
               }
               if (spawner_nbttag.hasKey("SpawnData")) {
                  nbttagcompound.setTag("SpawnData", spawner_nbttag.getCompoundTag("SpawnData"));
               }
               itemstack.setTagCompound(nbttagcompound);
               EntityItem entityitem = new EntityItem(event.world, event.x, event.y, event.z, itemstack);
               event.world.spawnEntityInWorld(entityitem);
               event.world.setBlockToAir(event.x, event.y, event.z);
               event.setCanceled(true);
            }
         }
      }
   }
   

   @SubscribeEvent
   public void playerInteract(PlayerInteractEvent event)
   {
      if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.isCancelable() && !event.world.isRemote) {
    	  World world = net.minecraft.server.MinecraftServer.getServer().worldServerForDimension(event.entityPlayer.dimension);
          EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(event.entityPlayer.getCommandSenderName());
          int lookingBlockId = Block.getIdFromBlock(world.getBlock(event.x, event.y, event.z));
          Vec3 lookvec = player.getLookVec();
          float fx = (float)lookvec.xCoord - event.x;
          float fy = (float)lookvec.yCoord - event.y;
          float fz = (float)lookvec.zCoord - event.z;
          int x = event.x;
          int y = event.y;
          int z = event.z;
          if(lookingBlockId != 106 && lookingBlockId != 31 && lookingBlockId != 32) {
             x += (event.face == 5 ? 1 : (event.face == 4 ? -1 : 0));
             y += (event.face == 1 ? 1 : (event.face == 0 ? -1 : 0));
             z += (event.face == 3 ? 1 : (event.face == 2 ? -1 : 0));
          }

          ItemStack itemstack = player.getHeldItem();
          if (itemstack != null && Item.getIdFromItem(itemstack.getItem()) == 52 && itemstack.getTagCompound() != null)
          {
             NBTTagCompound nbttagcompound = itemstack.getTagCompound();
             String entity_name = nbttagcompound.getString("EntityId");
             if (entity_name == null || entity_name.equals("")){
                entity_name = EntityList.getStringFromID(itemstack.getItemDamage());
             }
             if (entity_name == null || entity_name.equals("")){
                entity_name = "Pig";
             }
             //System.out.println(nbttagcompound.toString());

             if (world != null && world.setBlock(x, y, z, Block.getBlockById(52), 0, 3)) {
                TileEntity tile = (TileEntity)world.getTileEntity(x, y, z);
                if (tile instanceof TileEntityMobSpawner) {
                   TileEntityMobSpawner spawner = (TileEntityMobSpawner)tile;
                   spawner.func_145881_a().setEntityName(entity_name);
                   NBTTagCompound localdh = new NBTTagCompound();
                   spawner.writeToNBT(localdh);
                   for (Object key : nbttagcompound.func_150296_c()) {
                	   String str = (String)key;
                	   NBTBase tag = nbttagcompound.getTag(str);
                	   localdh.setTag(str, tag);
                   }
                   spawner.readFromNBT(localdh);
                   //spawner.func_145881_a().readFromNBT(nbttagcompound);
                   if (!player.capabilities.isCreativeMode) {
                      itemstack.stackSize --;
                      event.useItem = DENY;
                   }
                   event.setCanceled(true);
                }
             }
             
          }
       }
   }
}
