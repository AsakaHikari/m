package mod.bacteria.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayerFactory;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EmptySchale extends Item{
	
		 
		public EmptySchale(int par1)
		{
			super(par1);
			this.setCreativeTab(CreativeTabs.tabMaterials);	//クリエイティブのタブ
			this.setUnlocalizedName("empty schale");	//システム名の登録
			this.func_111206_d("bacteriamod:Empty Schale");	//テクスチャの指定
			this.setMaxStackSize(64);	//スタックできる量
		}
}
