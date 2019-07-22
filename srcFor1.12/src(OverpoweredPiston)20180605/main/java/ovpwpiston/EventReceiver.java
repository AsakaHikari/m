package ovpwpiston;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;













import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventReceiver {
	public static int blocksToCheckBranch=15;
	
	
	/*
	@SubscribeEvent
	public void minecartJoinWorldEvent(EntityJoinWorldEvent e){
		if(e.getEntity() instanceof EntityMinecart){
			NBTTagCompound nbt=e.getEntity().getEntityData();
			float maxspeed=Float.MAX_VALUE;
			if(nbt!=null && nbt.hasKey("maxSpeed")){
				maxspeed=nbt.getFloat("maxSpeed");
			}
			e.getEntity().getDataManager().register(Core.MAX_SPEED,maxspeed);
		}
	}
	*/
	@SubscribeEvent
	public void minecartUpdateEvent(MinecartUpdateEvent e){
		EntityMinecart entity=e.getMinecart();
		NBTTagCompound nbt=entity.getEntityData();
		if(nbt.hasKey("section")){
			float f=nbt.getFloat("section");
			double mX = entity.motionX;
	        double mZ = entity.motionZ;

	        if (entity.isBeingRidden())
	        {
	            mX *= 0.75D;
	            mZ *= 0.75D;
	        }
	        IBlockState state = entity.world.getBlockState(entity.getPosition());
	        if (BlockRailBase.isRailBlock(state)) {
	        	float railMaxSpeed = ((BlockRailBase)state.getBlock()).getRailMaxSpeed(entity.world, entity, entity.getPosition());
	        	double max = Math.min(railMaxSpeed, entity.getCurrentCartSpeedCapOnRail());
	        	mX = MathHelper.clamp(mX, -max, max);
	        	mZ = MathHelper.clamp(mZ, -max, max);
	        	f-=MathHelper.sqrt(mX*mX+mZ*mZ);
	        	//System.out.println(f);
	        }
			nbt.setFloat("section", f);
			if(f<0){
				nbt.removeTag("section");
				nbt.setFloat("maxSpeed", Float.MAX_VALUE);
			}
		}
		//System.out.println(entity.motionX+","+entity.motionZ);
		BlockPos entityPos=entity.getPosition();
		for(int x=entityPos.getX()-2;x<=entityPos.getX()+2;x++){
			for(int z=entityPos.getZ()-2;z<=entityPos.getZ()+2;z++){
				blockcheck:for(int y=entityPos.getY()-1;y<=entityPos.getY()+3;y++){
					BlockPos pos=new BlockPos(x,y,z);
					TileEntity t=entity.world.getTileEntity(pos);
					if(t instanceof TileEntitySign){
						IBlockState state=entity.world.getBlockState(pos);

						ITextComponent[] texts=((TileEntitySign)t).signText;
						String text0=texts[0].getUnformattedText().toLowerCase();
						
						boolean goRight = false,goLeft = false,goNowhere,goStraight;
						if(text0.indexOf('>')!=-1){
							goRight=true;
							text0=StringUtils.remove(text0, '>');
						}
						if(text0.indexOf('<')!=-1){
							goLeft=true;
							text0=StringUtils.remove(text0, '<');
						}
						
						if(!text0.equals("*") && !text0.equals("x") && !text0.equals("max") && Float.isNaN(parseFloat(text0))){
							break blockcheck;
						}
						
						if(state.getBlock()==Blocks.STANDING_SIGN &&
								!this.isDirectionSuitable(true,Blocks.STANDING_SIGN.getMetaFromState(state) , entity.motionX, entity.motionZ)){
							break blockcheck;
						}else if(state.getBlock()==Blocks.WALL_SIGN &&
								!this.isDirectionSuitable(false,Blocks.WALL_SIGN.getMetaFromState(state) , entity.motionX, entity.motionZ)){
							break blockcheck;
						}
						
						String text1=texts[1].getUnformattedText().toLowerCase();
						if(text1!=null){
							//System.out.println(text1);
							String[] strs1=text1.split("\\|");
							boolean flag=false;
							for(int i=0;i<strs1.length;i++){
								String s1=strs1[i];
								String[] strs2=s1.split("\\&");
								boolean flag1=true;
								for(int j=0;j<strs2.length;j++){
									//System.out.println(strs2[j]);
									if(strs2[j].equals("e")){
										if(entity.getPassengers().size()==0){
											continue;
										}else{
											flag1=false;
											break;
										}
									}else if(strs2[j].equals("f")){
										if(entity.getPassengers().size()==0){
											flag1=false;
											break;
											
										}else{
											continue;
										}
									}
									
									
									if(!isEntityMatches(entity,strs2[j]))flag1=false;
								}
								if(flag1){
									flag=true;
									break;
								}
							}
							if(!flag){
								break blockcheck;
							}
						}
						int dirx=entity.motionX<-0.0001?-1:(entity.motionX>0.0001?1:0);
						int dirz=entity.motionZ<-0.0001?-1:(entity.motionZ>0.0001?1:0);
						if(goRight){
							int metadata=-1;
							int metaup = -1,metadown =-1;
							int maxx=entityPos.getX(),minx=entityPos.getX(),maxz=entityPos.getZ(),minz=entityPos.getZ();
							if(dirx==0 && dirz==-1){

								//S
								
								metadata=6;
								metaup=5;
								metadown=4;
								minz=maxz-blocksToCheckBranch;
							}else if(dirx==0 && dirz==1){
								//N
								metadata=8;

								metaup=4;
								metadown=5;
								maxz=minz+blocksToCheckBranch;
								
							}else if(dirx==-1 && dirz==0){
								//W
								metadata=9;
								
								metaup=3;
								metadown=2;
								minx=maxx-blocksToCheckBranch;
							}else if(dirx==1 && dirz==0){
								//E
								
								metadata=7;
								metaup=2;
								metadown=3;
								maxx=minx+blocksToCheckBranch;
							}
							//System.out.println(metaup);
							if(metadata!=-1){
								boolean flag=false;
								int ty=entityPos.getY();
								for(int tx=minx;tx<=maxx;tx++)a:{
									for(int tz=minz;tz<=maxz;tz++){
										//System.out.println(tx+","+ty+","+tz);
										IBlockState tstate=entity.world.getBlockState(new BlockPos(tx,ty,tz));
										if(BlockRailBase.isRailBlock(tstate)){
											BlockRailBase block=(BlockRailBase)tstate.getBlock();
											int m=((BlockRailBase.EnumRailDirection)tstate.getValue(block.getShapeProperty())).getMetadata();
											if(m==metadata){
												flag=true;
												break a;
											}
											/*else if(m==metaup){
												ty++;

											}else if(m==metadown){
												ty--;
											}*/
										}
									}
								}
								if(!flag)break blockcheck;
							}
						}
						if(goLeft){
							int metadata=-1;
							int metaup = -1,metadown =-1;
							int maxx=entityPos.getX(),minx=entityPos.getX(),maxz=entityPos.getZ(),minz=entityPos.getZ();
							if(dirx==0 && dirz==-1){

								//S
								
								metadata=7;
								metaup=5;
								metadown=4;
								minz=maxz-blocksToCheckBranch;
								
							}else if(dirx==0 && dirz==1){
								//N
								metadata=9;

								metaup=4;
								metadown=5;
								maxz=minz+blocksToCheckBranch;
							}else if(dirx==-1 && dirz==0){
								//W
								metadata=6;
								
								metaup=3;
								metadown=2;
								minx=maxx-blocksToCheckBranch;
							}else if(dirx==1 && dirz==0){
								//E
								
								metadata=8;
								metaup=2;
								metadown=3;
								maxx=minx+blocksToCheckBranch;
							}
							//System.out.println(metadata);
							if(metadata!=-1){
								boolean flag=false;
								int ty=entityPos.getY();
								for(int tx=minx;tx<=maxx;tx++)a:{
									for(int tz=minz;tz<=maxz;tz++){
										//System.out.println(tx+","+ty+","+tz);
										IBlockState tstate=entity.world.getBlockState(new BlockPos(tx,ty,tz));
										if(BlockRailBase.isRailBlock(tstate)){
											BlockRailBase block=(BlockRailBase)tstate.getBlock();
											int m=((BlockRailBase.EnumRailDirection)tstate.getValue(block.getShapeProperty())).getMetadata();
											if(m==metadata){
												flag=true;
												break a;
											}
											/*else if(m==metaup){
												ty++;

											}else if(m==metadown){
												ty--;
											}*/
										}
									}
								}
								if(!flag)break blockcheck;
							}
						}
						if(text0.equals("*") || text0.equals("x") || text0.equals("max") ){
							nbt.setFloat("maxSpeed", Float.MAX_VALUE);
						}else if(!Float.isNaN(parseFloat(text0))){
							nbt.setFloat("maxSpeed", Float.parseFloat(text0)/72f);
							
						}else{break blockcheck;}
						
						String text2=texts[2].getUnformattedText().toLowerCase();
						float text2value=parseFloat(text2);
						if(!Float.isNaN(text2value)){
							nbt.setFloat("section",text2value);
						}
						
					}else if(t instanceof TileEntityBanner)banner:{
						TileEntityBanner banner=((TileEntityBanner)t);
						List patternList = Lists.<BannerPattern>newArrayList();
		                patternList.add(BannerPattern.BASE);
		                NBTTagList patterns=null;
						try {
							patterns = (NBTTagList) Core.Fpatterns.get(banner);
						} catch (IllegalArgumentException e1) {
							e1.printStackTrace();
							//break banner;
						} catch (IllegalAccessException e1) {
							e1.printStackTrace();
							//break banner;
						}
		                if (patterns != null)
		                {
		                    for (int i = 0; i < patterns.tagCount(); ++i)
		                    {
		                        NBTTagCompound nbttagcompound = patterns.getCompoundTagAt(i);
		                        BannerPattern bannerpattern = byHash(nbttagcompound.getString("Pattern"));

		                        if (bannerpattern != null)
		                        {
		                            patternList.add(bannerpattern);
		                        }
		                    }
		                }
						
						List pattern=new ArrayList<BannerPattern>();
						pattern.add(BannerPattern.TRIANGLE_BOTTOM);
						pattern.add(BannerPattern.TRIANGLE_TOP);
						if(patternList.containsAll(pattern)){
							IBlockState state=entity.world.getBlockState(pos);
							if(state.getBlock()==Blocks.STANDING_BANNER &&
									!this.isDirectionSuitable(true,Blocks.STANDING_BANNER.getMetaFromState(state) , entity.motionX, entity.motionZ)){
								break blockcheck;
							}else if(state.getBlock()==Blocks.WALL_BANNER &&
									!this.isDirectionSuitable(false,Blocks.WALL_BANNER.getMetaFromState(state) , entity.motionX, entity.motionZ)){
								break blockcheck;
							}
							nbt.setFloat("maxSpeed", Float.MAX_VALUE);
						}
					}
				}
			}
		}
	}
	
	public static boolean isEntityMatches(Entity e,String s){
		if(e.getName().toLowerCase().contains(s)){
        	return true;
        }
		if(e.getClass().getSimpleName().toLowerCase().contains(s)){
			return true;
		}
		if (e.hasCustomName())
        {
            if(e.getCustomNameTag().toLowerCase().contains(s)){
            	return true;
            }
        }
		String str = EntityList.getEntityString(e);
		if (str == null)
		{
			str = "generic";
		}
		if(I18n.translateToLocal("entity." + str + ".name").toLowerCase().contains(s)){
			return true;
		}
		for(Entity rider:e.getPassengers()){
			if(isEntityMatches(rider, s)){
				return true;
			}
		}
		return false;

	}
	
	public static float parseFloat(String s) {
	    try {
	    	return Float.parseFloat(s);
	        
	    } catch (Exception e) {
	        return Float.NaN;
	    }
	}
	
	public static boolean isDirectionSuitable(boolean isStanding,int metadata,double motionX,double motionZ){
		int dirx=motionX<-0.0001?-1:(motionX>0.0001?1:0);
		int dirz=motionZ<-0.0001?-1:(motionZ>0.0001?1:0);
		if(isStanding){
			if(dirx==0 && dirz==-1){
				if(metadata==15 || metadata==0 || metadata==1){
					return true;
				}
				return false;
			}else if(dirx==1 && dirz==-1){
				if(metadata==1 || metadata==2 || metadata==3){
					return true;
				}
				return false;
			}else if(dirx==1 && dirz==0){
				if(metadata==3 || metadata==4 || metadata==5){
					return true;
				}
				return false;
			}else if(dirx==1 && dirz==1){
				if(metadata==5 || metadata==6 || metadata==7){
					return true;
				}
				return false;
			}else if(dirx==0 && dirz==1){
				if(metadata==7 || metadata==8 || metadata==9){
					return true;
				}
				return false;
			}else if(dirx==-1 && dirz==1){
				if(metadata==9 || metadata==10 || metadata==11){
					return true;
				}
				return false;
			}else if(dirx==-1 && dirz==0){
				if(metadata==11 || metadata==12 || metadata==13){
					return true;
				}
				return false;
			}else if(dirx==-1 && dirz==-1){
				if(metadata==13 || metadata==14 || metadata==15){
					return true;
				}
				return false;
			}
		}else{
			int dir=metadata&0x7;
			if(dir==2 && dirz==1 && dirx==0){
				return true;
			}else if(dir==3 && dirz==-1 && dirx==0){
				return true;
			}else if(dir==4 && dirz==0 && dirx==1){
				return true;
			}else if(dir==5 && dirz==0 && dirx==-1){
				return true;
			}
		}
		return false;
	}
	
	public static BannerPattern byHash(String hash)
    {
        for (BannerPattern bannerpattern : BannerPattern.values())
        {
            if (bannerpattern.getHashname().equals(hash))
            {
                return bannerpattern;
            }
        }

        return null;
    }
}
