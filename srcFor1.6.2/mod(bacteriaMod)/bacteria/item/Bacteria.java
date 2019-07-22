package mod.bacteria.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.FakePlayerFactory;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.*;
import net.minecraft.world.World;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class Bacteria extends Item{
	public byte[] stsB=new byte[16];
	public int[] stsI=new int[16];
	private long lastUpdate;
	public int[] make=new int[4];
	public int[] mrate=new int[4];
	public int[] mmin=new int[4];
	public int[] mmax=new int[4];
	
	public Bacteria(int par1)
		{
			super(par1);
			this.setCreativeTab(CreativeTabs.tabMaterials);	//クリエイティブのタブ
			this.setUnlocalizedName("bacteria");	//システム名の登録
			this.func_111206_d("bacteriamod:Schale");	//テクスチャの指定
			this.setMaxStackSize(64);	//スタックできる量
		}
		
/*		@Override
		public void onCreated
		(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
			this.readFromNBT(par1ItemStack);
		}
		*/
		@Override
		public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
		{
			this.readFromNBT(item);
			stsI[0]+=1000;
			this.writeToNBT(item);
			return item;
		}
		
		@Override
		public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
			System.out.println("called");
		this.readFromNBT(par1ItemStack);
			if(lastUpdate==0){
				lastUpdate=par2World.getTotalWorldTime();
			}
			if(par2World.getTotalWorldTime()-lastUpdate>=100){
				if(stsI[0]>0){
					if(par2World.getTotalWorldTime()-100==lastUpdate){
						stsI[0]-=Math.round(Math.pow(1.3,stsB[1]));
						
					}else{
						long n=par2World.getTotalWorldTime()-lastUpdate;
						long loop=(n-n%100)/100;
						stsI[0]-=Math.round(Math.pow(1.3, stsB[1])*loop);
				
						}
						if(stsI[0]<0){
							stsI[0]=0;
						}
					}
				lastUpdate=par2World.getTotalWorldTime();
				
			}
			
			this.writeToNBT(par1ItemStack);
		}
		
		public void writeToNBT(ItemStack itemstack){
			NBTTagCompound nbt = itemstack.getTagCompound();
			if(nbt == null)
			{
				nbt = new NBTTagCompound();
				itemstack.setTagCompound(nbt);
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
			nbt.setLong("lastUpdate",lastUpdate);
			for(int i=0;i<4;i++){
				nbt.setInteger("make"+i,make[i]);
				nbt.setInteger("mrate"+i,mrate[i]);
				nbt.setInteger("mmin"+i, mmin[i]);
				nbt.setInteger("mmax"+i,mmax[i]);
			}
			itemstack.setTagCompound(nbt);
		}
		
		public void readFromNBT(ItemStack itemstack){
			NBTTagCompound nbt = itemstack.getTagCompound();
			if(nbt != null){
			stsB[0]=nbt.getByte("str");
			stsB[1]=nbt.getByte("spd");
			stsB[2]=nbt.getByte("inc");
			stsB[3]=nbt.getByte("therm");
			stsB[4]=nbt.getByte("envUp");
			stsB[5]=nbt.getByte("envDown");
			stsB[6]=nbt.getByte("air");
			stsB[7]=nbt.getByte("effID");
			stsB[8]=nbt.getByte("effLevel");
			stsB[9]=nbt.getByte("biohazard");
			
			stsI[0]=nbt.getInteger("nut");
			stsI[1]=nbt.getInteger("use");
			stsI[2]=nbt.getInteger("effLeng");
			stsI[3]=nbt.getInteger("urate");
			lastUpdate=nbt.getLong("lastUpdate");
			for(int i=0;i<4;i++){
				make[i]=nbt.getInteger("make"+i);
				mrate[i]=nbt.getInteger("mrate"+i);
				mmin[i]=nbt.getInteger("mmin"+i);
				mmax[i]=nbt.getInteger("mmax"+i);
			}
			
			}else{
				for(int i=0;i<16;i++){
					stsB[i]=0;
					stsI[i]=0;
				}
				lastUpdate=0;
				this.writeToNBT(itemstack);
			}
		}
		@Override
		public void addInformation
		(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
			this.readFromNBT(par1ItemStack);
			par3List.add("str="+stsB[0]+" spd="+stsB[1]+" lastUpdate"+lastUpdate+" nut="+stsI[0]);
		}
		
		
		


}
