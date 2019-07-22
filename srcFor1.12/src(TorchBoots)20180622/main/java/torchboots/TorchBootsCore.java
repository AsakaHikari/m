/*    */package torchboots;

/*    */
/*    */import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
/*    */
import net.minecraftforge.fml.common.Mod.EventHandler;
/*    */
import net.minecraftforge.fml.common.Mod.Instance;
/*    */
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
/*    */
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.GameData;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
/*    */
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
/*    */
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
/*    */
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
/*    */
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBlock;
/*    */
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
/*    */
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
/*    */
import net.minecraftforge.common.util.EnumHelper;

/*    */
/*    */@Mod(modid = "torchbootsmod", name = "torchbootsmod", version = "Alpha1_0")
/*    */public class TorchBootsCore
/*    */{
	/*    */
	/*    */@Mod.Instance("torchbootsmod")
	/*    */public static TorchBootsCore instance;
			public static List<Item> triggerItems;
			@NetworkCheckHandler
		    public boolean accept(Map<String, String>modList, Side side) {
		       return true;
		    }
	/*    */
	/*    */@Mod.EventHandler
	/*    */public void preInit(FMLPreInitializationEvent e)
	/*    */{
		triggerItems=new ArrayList();
		Configuration cfg=new Configuration(e.getSuggestedConfigurationFile());
		cfg.load();
		Property itemsP=cfg.get("system", "ITEMS to put automatically when you wearing torch boots", new String[]{"minecraft:torch"},
				"seperate itemIDs with Newline");
		cfg.save();
		for(String s:itemsP.getStringList()){
			Item item=tryFindingItem(s);
			if(item !=null){
				System.out.println(item.getUnlocalizedName()+" put");
				triggerItems.add(item);
			}
		}
		
		MinecraftForge.EVENT_BUS.register(this);
		
		/*    */}

	/*    */
	/*    */@Mod.EventHandler
	/*    */public void init(FMLInitializationEvent e)
	/*    */{
		}
	public Item tryFindingItem(String s){
		 Item item = Item.getByNameOrId(s);
	        if (item != null)
	        {
	            return item;
	        }
	        
	        Block block = Block.getBlockFromName(s);
	        if (block != Blocks.AIR)
	        {
	            return Item.getItemFromBlock(block);
	        }
	        return null;
	 }
	/*    */
		
	@SubscribeEvent
	public void update(LivingEvent.LivingUpdateEvent e){
		if(!e.getEntityLiving().world.isRemote && e.getEntityLiving() instanceof EntityPlayer){

			for(ItemStack stack:e.getEntityLiving().getArmorInventoryList()){
				if(stack!=null && stack.getItem()==Items.LEATHER_BOOTS && stack.getTagCompound()!=null){
					NBTTagCompound nbt=stack.getTagCompound().getCompoundTag("display");
					if(nbt!=null && nbt.getInteger("color")==0xF9801D){
				EntityPlayer entity=(EntityPlayer) e.getEntityLiving();
				World world=e.getEntityLiving().world;
				BlockPos pos=new BlockPos(MathHelper.floor(entity.posX),MathHelper.floor(entity.posY-0.05)+1,MathHelper.floor(entity.posZ));
				//System.out.println("called1");
				InventoryPlayer inv = entity.inventory;
				if (world.getWorldInfo().getWorldTotalTime()%10==0 && (world.getBlockState(pos).getBlock() == Blocks.AIR) && (world.getLightFor(EnumSkyBlock.BLOCK, pos) < 9)){
					//System.out.println("called2");   
					for (ItemStack s:inv.offHandInventory){
						//System.out.println("called3");  
						if ((s != null) && (TorchBootsCore.triggerItems.contains(s.getItem()))) {
							//System.out.println("called4");  
							if(s.getItem() instanceof ItemBlock){
								world.setBlockState(pos, ((ItemBlock)s.getItem()).getBlock().getStateForPlacement(world, pos, EnumFacing.DOWN, 0, 0, 0, s.getMetadata(), entity, EnumHand.OFF_HAND));
								if(!entity.isCreative()){
									s.setCount(s.getCount()-1);
								}
							}else{
								s.onItemUse(entity, world, pos, EnumHand.OFF_HAND, EnumFacing.DOWN, 0,0,0);
							}
							//inv.decrStackSize(i, 1);
							
							break;
						}
					}
				}
				}
			}
			}
		}
	}
}
