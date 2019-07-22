package orgchem.item;

import java.util.ArrayList;
import java.util.List;

import orgchem.core.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemBeaker extends Item{
	Icon emptyIcon;
	public ItemBeaker(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setHasSubtypes(true);
		this.setTextureName("orgchem:beaker");
        this.setMaxDamage(0);
	}
	
	public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return par1ItemStack.getItemDamage() == 1 ? "item.emptyBeaker" : "item.beaker";
    }
	
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
	
	public Icon getIconFromDamage(int par1)
    {
        return par1 == 1 ? this.emptyIcon : super.getIconFromDamage(par1);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        this.emptyIcon = par1IconRegister.registerIcon("orgchem:emptyBeaker");
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5){
    	onBeakerUpdate(par1ItemStack,par2World);
    }
    
    
    public void onBeakerUpdate(ItemStack par1ItemStack,World par2World){
    	if(!par2World.isRemote){
    		if(par2World.getWorldTime()%10==0){
    			NBTTagCompound nbt=par1ItemStack.getTagCompound();
    			if(nbt==null){
    				nbt=new NBTTagCompound();
    				nbt.setIntArray("compounds",new int[]{});
    				nbt.setIntArray("quantity", new int[]{});
    				nbt.setShort("temperature", (short) 20);
    				nbt.setByte("light", (byte)0);
    				par1ItemStack.setTagCompound(nbt);
    			}else{
    				int[] compounds=nbt.getIntArray("compounds");
    				int[] quantity=nbt.getIntArray("quantity");
    				int totalAmount=0;
    				for(int i=0;i<quantity.length;i++){
    					totalAmount+=quantity[i];
    				}
    				List<Integer> compoundsList=new ArrayList();
    				List<Integer> quantityList=new ArrayList();
    				for(int i=0;i<compounds.length;i++){
    					compoundsList.add(compounds[i]);
    					quantityList.add(quantity[i]);
    				}
    				short temperature=nbt.getShort("temperature");
    				byte light=nbt.getByte("light");
    				int nextlight=0;
    				for(int i=0;i<compounds.length;i++){
    					for(int j=0;j<ChemicalReaction.reactionList[i].size();j++){
    						ChemicalReaction cr=ChemicalReaction.reactionList[i].get(j);
    						int[] ids=new int[cr.necessaryCompounds.length];
    						for(int k=0;k<ids.length;k++){
    							ids[k]=-1;
    						}
    						boolean flag=false;
    						for(int k=0;k<cr.necessaryCompounds.length;k++){
    							for(int l=0;l<compounds.length;l++){
    								if(cr.necessaryCompounds[k]==CompoundsData.compoundsMap.get(compounds[l])){
    									ids[k]=l;
    									break;
    								}
    							}
    							if(ids[k]==-1){
    								flag=true;
    								break;
    							}
    						}
    						if(!flag){
    							switch(cr.energySource){
    							case heat:
    								if(temperature>=cr.energyValue){
    									float multiplier=1.0f;
    									for(int k=0;k<ids.length;k++){
    										if(quantity[k]<cr.speed*cr.ratio[k]&&multiplier>quantity[k]/cr.speed*cr.ratio[k]){
    											multiplier=quantity[k]/cr.speed*cr.ratio[k];
    										}
    									}
    									for(int k=0;k<ids.length;k++){
    										quantity[k]-=cr.speed*cr.ratio[k]*multiplier;
    									}
    									for(int k=0;k<cr.resultCompounds.length;k++){
    										if(compoundsList.contains(cr.resultCompounds[k].id)){
    											int id=compoundsList.indexOf(cr.resultCompounds[k].id);
    											quantityList.set(id, (int) (quantityList.get(id)+cr.resultRatio[k]*cr.speed*multiplier));
    										}else{
    											compoundsList.add(cr.resultCompounds[k].id);
    											quantityList.add((int) (cr.resultRatio[k]*cr.speed*multiplier));
    										}
    									}
    									switch(cr.resultEnergySource){
										case electricity:
											break;
										case heat:
											temperature+=cr.resultEnergyValue*(cr.speed*multiplier);
											break;
										case light:
											nextlight+=cr.resultEnergyValue/(cr.speed*multiplier);
											break;
										case shock:
											break;
										
    									}
    								}
    								break;
								case electricity:
									break;
								case light:
									break;
								case shock:
									break;
    							
    							}
    							int[] newCompounds=new int[compoundsList.size()];
    							for(int k=0;k<compoundsList.size();k++){
    								newCompounds[k]=compoundsList.get(k);
    							}
    							int[] newQuantity=new int[quantityList.size()];
    							for(int k=0;k<quantityList.size();k++){
    								newQuantity[k]=quantityList.get(k);
    							}
    							
    							nbt.setIntArray("compounds", newCompounds);
    							nbt.setIntArray("quantity", newQuantity);
    							nbt.setShort("temperature", (short) temperature);
    							nbt.setByte("light", (byte) nextlight);
    						}
    					}
    				}
    			}
    		}
    	}
    }
}
