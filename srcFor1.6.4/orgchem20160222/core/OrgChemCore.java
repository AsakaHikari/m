package orgchem.core;

import orgchem.block.BlockChemicalWorkbench;
import orgchem.item.ItemCompoundsContainer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.*;
@Mod(modid="organicChemistry",name="organicChemistry")
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class OrgChemCore {
	@Instance("organicChemistry")
	protected OrgChemCore instance;
	public static ItemCompoundsContainer itemBeaker;
	public static Block blockChemicalWorkbench;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		itemBeaker=new ItemCompoundsContainer(19001);
		GameRegistry.registerItem(itemBeaker, "beaker");
		blockChemicalWorkbench=(new BlockChemicalWorkbench(1301)).setUnlocalizedName("chemicalWorkbench");
		GameRegistry.registerBlock(blockChemicalWorkbench, "chemicalWorkbench");
		LanguageRegistry.instance().addStringLocalization("tile.chemicalWorkbench.name", "chemical workbench");
		
		LanguageRegistry.instance().addStringLocalization("item.beaker.name", "beaker");
		LanguageRegistry.instance().addStringLocalization("item.emptyBeaker.name", "empty beaker");
		
		GameRegistry.addRecipe(new ItemStack(itemBeaker,4,1), "X X","X X","XXX",'X',Block.glass);
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		
		
	}
}
