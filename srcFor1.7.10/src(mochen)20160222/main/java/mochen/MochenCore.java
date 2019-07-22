package mochen;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
 
@Mod(modid = "mochen", name = "mochen", version = "1.2.0")
public class MochenCore {
	public static Item itemMochen;
	public static Block blockMochi;
	@SidedProxy(clientSide="mochen.RenderRegister",serverSide="mochen.RenderProxy")
	public static RenderProxy proxy;
	
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	itemMochen = new ItemMochen();
    	GameRegistry.registerItem(itemMochen, "mochen");
    	
    	blockMochi = new BlockMochi();
    	GameRegistry.registerBlock(blockMochi, "mochi");
    	
    	EntityRegistry.registerGlobalEntityID(EntityMochen.class,"mochen", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityMochen.class, "mochen", 0, this, 250, 1, false);
        List<BiomeGenBase> list=new ArrayList();
        for(BiomeGenBase i:BiomeGenBase.getBiomeGenArray()){
        	if(i!=null)
        	{
        		list.add(i);
        	}
        }
        proxy.init();
        EntityRegistry.addSpawn(EntityMochen.class, 20, 1, 4, EnumCreatureType.creature, list.toArray(new BiomeGenBase[0]));
       
        GameRegistry.addRecipe(new ItemStack(blockMochi), "WWW","WXW",'W',Items.wheat,'X',Items.water_bucket);
        GameRegistry.addRecipe(new ItemStack(blockMochi)," S ","WXW",'W',Items.wheat,'X',Items.water_bucket,'S',Items.slime_ball);
    }
}