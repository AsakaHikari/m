package mod.bacteria.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayerFactory;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SchaleWithAger extends Item {
	public SchaleWithAger(int par1){
		super(par1);
		setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabMaterials);	//クリエイティブのタブ
		this.setUnlocalizedName("schale with ager");	//システム名の登録
		this.func_111206_d("bacteriamod:Schale");	//テクスチャの指定
	}
	
	@Override
	public boolean onItemUse
	(ItemStack item, EntityPlayer player, World world,
			int x, int y, int z, int side, 
			float disX, float disY, float disZ)
	{
		switch(world.getBlockId(x, y, z)){
		case 1:
		case 4:
			item.itemID=GameRegistry.findItem("Bacteria Craft","bacteria" ).itemID;
			setStatus(item,player,world,x,y,z,side,disX,disY,disZ);
			break;
		}
		return false;
	}
	/**ブロックに対して使用した時、その種類に応じて特定のステータスを持ったbacteriaに変化させる
	 **/
	
	public void setStatus(ItemStack item,EntityPlayer player, World world,
			int x, int y, int z, int side, 
			float disX, float disY, float disZ){
		byte[] stsB=new byte[16];
		int[] stsI=new int[16];
		switch(world.getBiomeGenForCoords(x, z).biomeID){
		case 5:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 19:
			stsB[3]=0;
			break;
		case 2:
		case 17:
		case 21:
		case 22:
			stsB[3]=2;
			break;
		case 8:
			stsB[3]=3;
			break;
		default:
			stsB[3]=1;
		}
		switch(world.getBlockId(x, y, z)){
		case 1:
		case 4:
			item.itemID=GameRegistry.findItem("Bacteria Craft","bacteria" ).itemID;
			stsB[0]=(byte) (1+(Math.random()*3));
			stsB[1]=(byte) (1+(Math.random()*3));
			stsB[2]=(byte) (1+(Math.random()*3));
			stsB[4]=(byte) (Math.random()*2);
			stsB[5]=(byte) (Math.random()*2);
			stsB[6]=0;
			stsB[7]=Math.random()<0.5?(byte)0:19;
			stsB[8]=1;
			stsB[9]=0;
			stsI[0]=1000;
			switch((int)(Math.random()%2)){
			case 0:
				stsI[1]=4;
				break;
			case 1:
				stsI[1]=296;
				break;
			}
			
			break;
		}
		NBTTagCompound nbt = item.getTagCompound();
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
			item.setTagCompound(nbt);
		}
		nbt.setByte("str", stsB[0]);
		nbt.setByte("spd", stsB[1]);
		nbt.setByte("inc", stsB[2]);
		nbt.setByte("therm", stsB[3]);
		nbt.setByte("envUp", stsB[4]);
		nbt.setByte("envDown", stsB[5]);
		nbt.setByte("air", stsB[6]);
		nbt.setByte("effID", stsB[7]);
		nbt.setByte("effLevel", stsB[8]);
		nbt.setByte("biohazard", stsB[9]);
		nbt.setInteger("nut",stsI[0]);
		nbt.setInteger("use",stsI[1]);
		nbt.setInteger("effLeng", stsI[2]);
		nbt.setInteger("urate",stsI[3]);
		
	}
	
}
