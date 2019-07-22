package farview;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.Project;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Renderer implements IWorldEventListener{
	public static int blocksToCheckBranch=15;
	//public  Map<Integer,BlockPos> idToChunk=new HashMap();
	public  Map<BlockPos,Integer> chunkToId=new HashMap();
	public Map<BlockPos,Integer> unloadEntry=new HashMap();
	public  boolean flag=true;
	public  int timer;
	public  BlockModelShapes bms;
	public  int brightnessTypes=3;
	//public  int frequent=4;
	public  Deque<BlockPos> chunkStack=new ArrayDeque();
	public  int cooldown=0;
	public  int cooldownmax=1;
	public int loadChallenges=10;
	public int chunkLoadPerFrame=1;
	public int maxRenderChunkDistance=20;
	public long maxLoadTime=10*1000*1000;
	public int loadPartialThreshold=20;
	public int fogStartChunk=10;
	public int fogEndChunk=20;
	public  boolean fastload=true;

    public boolean enablePreload;
	//public boolean textured=true;

	public  Deque<BlockPos> updateStack=new ArrayDeque();

	//public  boolean madeSkyUp=false;

	public  boolean assigned=false;
	public Minecraft mc;

	public int chunkToLoadx=0;
	public int chunkToLoady=0;
	public int chunkToLoadStage=0;
	public int chunkLoadPartial=4;

	public Map<ChunkPos,IndependentChunk> receivedChunkMap=new HashMap();
	public List<ChunkPos> sentChunks=new ArrayList();

	public Renderer() {
		this.mc=Minecraft.getMinecraft();
	}

	public void fogSettings(RenderFogEvent e){
		 GlStateManager.setFogStart(e.getFarPlaneDistance()*1.5f);
		 GlStateManager.setFogEnd(e.getFarPlaneDistance()*2f);
		 //Make Sky Higher
		 /*
		 if(e.getFogMode()==-1){
			 //GlStateManager.setFogStart(0);
			 //GlStateManager.setFogEnd(1);
			 GL11.glTranslatef(0, 70f, 0);
			 GL11.glScalef(1, 1.5f, 1);
			 //
			 madeSkyUp=true;
		 }else if(madeSkyUp){
			 //
			 GL11.glScalef(1, 1/1.5f, 1);
			 GL11.glTranslatef(0, -70f, 0);
			 madeSkyUp=false;
		 }*/

	}

	public void render(RenderWorldLastEvent e){

		if(mc.isSingleplayer()) {
			this.enablePreload=true;
		}

		int chunkLoadSpeed=Core.Cfg.chunkLoadingSpeed;
		this.cooldownmax=chunkLoadSpeed<4?2*(4-chunkLoadSpeed):1;
		this.chunkLoadPerFrame=chunkLoadSpeed<4?1:(chunkLoadSpeed*2-4);
		this.loadChallenges=chunkLoadSpeed*2;
		this.loadPartialThreshold=chunkLoadSpeed;
		if(this.maxRenderChunkDistance!=Core.Cfg.maxRenderChunkDistance) {
			this.chunkToLoadStage=0;
			this.chunkToLoadx=0;
			this.chunkToLoady=0;
			this.chunkStack.clear();
		}
		this.maxRenderChunkDistance=Core.Cfg.maxRenderChunkDistance;
		this.maxLoadTime=(long)10l*1000l*1000l;
		this.fogStartChunk=Core.Cfg.fogStartChunk;
		this.fogEndChunk=this.maxRenderChunkDistance;
		//System.out.println(""+this.loadChallenges+" "+this.maxRenderChunkDistance);
		/*
		List<RenderChunk> renderChunks = null;
		ChunkRenderContainer crc = null;
		List renderInfos=null;
		ViewFrustum frustum=null;
		try {
			crc=(ChunkRenderContainer) Core.FrenderContainer.get(mc.renderGlobal);
			renderChunks=(List<RenderChunk>) Core.FrenderChunks.get(crc);
			renderInfos=(List) Core.FrenderInfos.get(mc.renderGlobal);
			frustum=(ViewFrustum) Core.FviewFrustum.get(mc.renderGlobal);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		if(frustum.renderChunks.length>0){
			RenderChunk renderchunk=frustum.renderChunks[0];
			if(renderchunk!=null){
				//System.out.println((renderchunk.compiledChunk.isEmpty())+" "+renderchunk.boundingBox.toString());
				//System.out.println(crc.getClass().getSimpleName());

				VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(BlockRenderLayer.SOLID.ordinal());
				GlStateManager.pushMatrix();
				GL11.glTranslatef(0, 30, 0);
				crc.preRenderChunk(renderchunk);
				renderchunk.multModelviewMatrix();
				vertexbuffer.bindBuffer();
				try {
					Core.MsetupArrayPointers.invoke(crc, new Object[]{});
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					e1.printStackTrace();
				}
				//((VboRenderList) crc).setupArrayPointers();
				vertexbuffer.drawArrays(7);
				GlStateManager.popMatrix();


				OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
				GlStateManager.resetColor();
				/*
				ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
				GlStateManager.pushMatrix();
				crc.preRenderChunk(renderchunk);
				GlStateManager.callList(listedrenderchunk.getDisplayList(BlockRenderLayer.SOLID, listedrenderchunk.getCompiledChunk()));
				GlStateManager.popMatrix();
				GlStateManager.resetColor();

			}
		}*/


		Entity entity=mc.getRenderViewEntity();
		if(entity==null || entity.world==null)return;
		bms=mc.getBlockRendererDispatcher().getBlockModelShapes();
		int renderDistance=mc.gameSettings.renderDistanceChunks;
		float gamma=mc.gameSettings.gammaSetting;
		/*
		if(flag && player.world.getChunkFromChunkCoords(0, 0).isLoaded()){
			createList(player.world.getChunkFromChunkCoords(0, 0));
			flag=false;
		}
		 */
		if(!assigned){
			entity.world.addEventListener(this);
			assigned=true;
		}
/*
		if(timer%100==0){
			for(int x=0;x<3;x++){
				for(int z=0;z<3;z++){
					Chunk c=entity.world.getChunkFromChunkCoords(x, z);
					for(int y=0;y<entity.world.getHeight()/16;y++){

//					if(c.isLoaded()){
//						boolean flag=false;
//						BlockPos pos=new BlockPos(x,y,z);
//						if(!idToChunk.values().contains(pos)) {
//							createList(c,pos.getY(),0, GlStateManager.glGenLists(brightnessTypes));
//						}
//					}
						BlockPos pos=new BlockPos(x,y,z);
						if(!chunkStack.contains(pos) && !idToChunk.containsValue(pos)){
							chunkStack.addFirst(pos);
						}
					}

				}
			}
		}


/*
		if(timer%frequent==0){
			Chunk chunkToCreateRenderList=player.world.getChunkFromChunkCoords((((int)player.posX)>>4)-renderDistance/2+((timer/frequent)%renderDistance), (((int)player.posZ)>>4)-renderDistance/2+(((timer/frequent)/renderDistance)%renderDistance));
			if(chunkToCreateRenderList.isLoaded()){
				boolean flag=false;
				for(Chunk c1:idToChunk.values()){
					if(c1.x==chunkToCreateRenderList.x && c1.z==chunkToCreateRenderList.z){
						flag=true;
						break;
					}
				}
				if(!flag){
					createList(chunkToCreateRenderList);
				}
			}
		}
*/
		long nanotime;
/*
		int dist=this.maxRenderChunkDistance;
		int chunkx=(((int)entity.posX)>>4)-dist+((timer)%(dist*2));
		int chunkz=(((int)entity.posZ)>>4)-dist+(((timer)/(dist*2))%(dist*2));
		//Chunk chunkToCreateRenderList=entity.world.getChunkFromChunkCoords(, );
		//if(chunkToCreateRenderList.isLoaded()){
			for(int y=0;y<entity.world.getHeight()/16;y++){

				BlockPos pos=new BlockPos(chunkx,y,chunkz);
				//System.out.println(pos);
				if(!chunkStack.contains(pos) && !idToChunk.containsValue(pos)){
					chunkStack.addFirst(pos);
				}
			}
		//}

*/
		nanotime=System.nanoTime();
		this.putChunk(entity, this.chunkStack, entity.world.getActualHeight()/16,this.maxRenderChunkDistance);
		//System.out.println("putchunk:"+(System.nanoTime()-nanotime));

		timer++;
		if((((int)(entity.posX))/16) != (((int)(entity.prevPosX))/16)
				||(((int)(entity.posZ))/16) != (((int)(entity.prevPosZ))/16)){
			cooldown=cooldownmax*10;
			this.chunkToLoadStage--;
			if(this.chunkToLoadx>0) {
				this.chunkToLoadx--;
			}
			//System.out.println("moved");
		}
		if((((int)(entity.posY))/16) != (((int)(entity.prevPosY))/16)){
			this.chunkToLoadStage--;
			if(this.chunkToLoadx>0) {
				this.chunkToLoadx--;
			}
			//System.out.println("moved");
		}
		cooldown--;

		if(cooldown<0){
			if(!chunkStack.isEmpty()){
				nanotime=System.nanoTime();
				for(int j=0;j<this.chunkLoadPerFrame;j++) {
					for(int i=0;i<loadChallenges;i++){
						BlockPos pos=chunkStack.pollFirst();
						if(pos!=null) {


							Chunk c=entity.world.getChunkFromChunkCoords(pos.getX(), pos.getZ());
							if(!c.isLoaded()) {
								Integer id=this.unloadEntry.get(pos);
								if(id!=null) {
									this.chunkToId.put(pos, id);
									continue;

								}

								//System.out.println("not loaded");
								c=null;
								c=this.receivedChunkMap.get(new ChunkPos(pos.getX(),pos.getZ()));
							}

							if(c!=null) {
								if(i<loadChallenges-1 && !isChunkForRender(pos.getX(),pos.getY(),pos.getZ(), entity, renderDistance*14,this.maxRenderChunkDistance*16)){
									chunkStack.addLast(pos);
									//System.out.println("mlater:"+pos);
								}else{
									createList(c,pos.getY(),0, GlStateManager.glGenLists(brightnessTypes));
									//System.out.println("make:"+pos);
									cooldown=cooldownmax;
									break;
								}
							}else if(this.enablePreload && (!this.sentChunks.contains(new ChunkPos(pos.getX(),pos.getZ())))){

								Core.INSTANCE.sendToServer(new MessageHandlerServer.Message(pos.getX(),pos.getZ(),entity.world.provider.getDimension()));
								this.sentChunks.add(new ChunkPos(pos.getX(),pos.getZ()));

								chunkStack.addLast(pos);
								break;
							}else {
								chunkStack.addLast(pos);
							}
						}
					}
					if(System.nanoTime()-nanotime>this.maxLoadTime)break;
				}
				//System.out.println("newMadeList:"+(System.nanoTime()-nanotime));
			}
			if(!updateStack.isEmpty()){
				for(int j=0;j<this.chunkLoadPerFrame;j++) {
					for(int i=0;i<loadChallenges;i++){
						BlockPos pos=updateStack.pollFirst();
						if(pos!=null) {
							if(entity.world.getChunkFromChunkCoords(pos.getX(), pos.getZ()).isLoaded()) {
								if(i<loadChallenges-1 && !isChunkForRender(pos.getX(),pos.getY(),pos.getZ(), entity, renderDistance*14,this.maxRenderChunkDistance*16)){
									updateStack.addLast(pos);
									//System.out.println("ulater:"+pos);
								}else{
									createList(entity.world.getChunkFromChunkCoords(pos.getX(), pos.getZ()), pos.getY(), 0, chunkToId.get(pos));
									//System.out.println("update:"+pos);
									cooldown=cooldownmax;
									break;
								}
							}
						}
					}
				}
			}
		}
		//System.out.println("called");

		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.loadIdentity();

		try {
			Project.gluPerspective((Float) Core.MgetFOVModifier.invoke(mc.entityRenderer,(float)e.getPartialTicks(), true), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, this.maxRenderChunkDistance*128);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.loadIdentity();
		float yaw = entity.rotationYaw+180;
        float pitch = entity.rotationPitch;
		GL11.glPushMatrix();
		int rendererUpdateCount=0;
		try {
			Core.MhurtCameraEffect.invoke(mc.entityRenderer, (float) e.getPartialTicks());
			if(mc.gameSettings.viewBobbing) {
				Core.MapplyBobbing.invoke(mc.entityRenderer, (float) e.getPartialTicks());
			}
			rendererUpdateCount=Core.FrendererUpdateCount.getInt(mc.entityRenderer);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}

		float f1 = mc.player.prevTimeInPortal + (mc.player.timeInPortal - mc.player.prevTimeInPortal) * e.getPartialTicks();


		if (f1 > 0.0F)
		{
			int i = 20;

			if (mc.player.isPotionActive(MobEffects.NAUSEA))
			{
				i = 7;
			}

			float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
			f2 = f2 * f2;
			GlStateManager.rotate(((float)rendererUpdateCount + e.getPartialTicks()) * (float)i, 0.0F, 1.0F, 1.0F);
			GlStateManager.scale(1.0F / f2, 1.0F, 1.0F);
			GlStateManager.rotate(-((float)rendererUpdateCount + e.getPartialTicks()) * (float)i, 0.0F, 1.0F, 1.0F);
		}
		try {
			Core.MorientCamera.invoke(mc.entityRenderer, (float) e.getPartialTicks());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}

		//GL11.glRotatef(pitch,1, 0, 0);
		//GL11.glRotatef(yaw,0, 1, 0);
		//GL11.glRotatef(roll,0, 0, 1);

		float scale=1+0.1f*renderDistance;
		scale=8f;
		GL11.glScalef(scale,scale,scale);
		double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)e.getPartialTicks();
        double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)e.getPartialTicks();
        double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)e.getPartialTicks();
        GL11.glTranslated(-d0,-d1,-d2);
		//GL11.glTranslated(-entity.posX, -entity.posY, -entity.posZ);
		if (this.mc.gameSettings.thirdPersonView == 2) {
			yaw+=180;
			yaw=MathHelper.wrapDegrees(yaw);
			pitch*=-1;
			//pitch=MathHelper.wrapDegrees(pitch);
		}
		//System.out.println(player.posX+" "+player.posY+" "+player.posZ+"");

		GlStateManager.disableTexture2D();
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glEnable(GL11.GL_LIGHTING);

		//GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT,ByteBuffer.allocateDirect(32).asFloatBuffer().put(1f).put(0f).put(0f).put(1f));
		//GL11.glEnable(GL11.GL_LIGHT0);
		/*
		ResourceLocation lightmap=mc.getTextureManager().getDynamicTextureLocation("lightMap", new DynamicTexture(16,16));

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.loadIdentity();
        float f = 0.00390625F;
        GlStateManager.scale(0.00390625F, 0.00390625F, 0.00390625F);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        mc.getTextureManager().bindTexture(lightmap);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        */
		//mc.entityRenderer.enableLightmap();
		//float brightness=gamma*player.world.calculateSkylightSubtracted(1f)/25f;
		int wtime=(int) (entity.world.getWorldTime()%24000L);

		//System.out.println("b:"+brightness+" gamma"+gamma+" light:"+player.world.calculateSkylightSubtracted(1f));
		//GL14.glBlendColor((timer%30)/60f,(timer%30)/60f,(timer%30)/60f,(timer%30)/60f);
		/*
		GL11.glColor4f(1, 1, 1, 1f);
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex3d(1000, -1, -1000);
		GL11.glVertex3d(-1000, -1, -1000);
		GL11.glVertex3d(-1000, -1, 1000);
		GL11.glVertex3d(1000, -1, -1000);
		GL11.glVertex3d(-1000, -1, 1000);
		GL11.glVertex3d(1000, -1, 1000);
		GL11.glEnd();*/
/*
		float brightness=0.15f+gamma/3;
        GL14.glBlendColor(brightness,brightness,brightness,1f);
        GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA,GL11.GL_CONSTANT_COLOR, GL11.GL_CONSTANT_ALPHA,GL11.GL_CONSTANT_ALPHA);
        GL14.glBlendEquation(GL14.GL_FUNC_ADD);
        */
		//System.out.println(pitch);
		float psizeModifier=Math.max((float)mc.displayWidth/854 , (float)mc.displayHeight/480);

		if(this.fogStartChunk<this.fogEndChunk && this.fogStartChunk>=0) {
			GlStateManager.setFog(FogMode.LINEAR);
			//GlStateManager.glFogi(GL11.GL_FOG_COLOR, 0xff000000);
			GlStateManager.setFogStart(this.fogStartChunk*16*scale);
			GlStateManager.setFogEnd(this.fogEndChunk*16*scale);

			GlStateManager.enableFog();
		}
		nanotime=System.nanoTime();

		Map<BlockPos,Integer> unloadEntryCache=new HashMap();
		GL11.glDisable(GL11.GL_BLEND);
		for(Entry<BlockPos, Integer> entry:chunkToId.entrySet()){
			int id=entry.getValue();
			BlockPos c=entry.getKey();
			double dx=entity.posX-(c.getX()*16+8);
			double dz=entity.posZ-(c.getZ()*16+8);
			double dy=entity.posY-(c.getY()*16+8);
			if(dx>this.maxRenderChunkDistance*16 || dy>this.maxRenderChunkDistance*16 || dz>this.maxRenderChunkDistance*16) {

				unloadEntryCache.put(c, id);
				continue;
			}
			int var1=MathHelper.absFloor(dx)+MathHelper.absFloor(dy)+MathHelper.absFloor(dz);

			GL11.glPushMatrix();
			//GL11.glTranslated(c.getX()*16,-0.0001*(dx*dx+dz*dz),c.getZ()*16);
			GL11.glTranslated(c.getX()*16,0,c.getZ()*16);
			GL11.glEnable(GL11.GL_POINT_SMOOTH);


			var1+=100;
			//if(!c.isLoaded()){
			if(this.isChunkForRender(c.getX(),c.getY(),c.getZ(),entity, renderDistance*16,this.maxRenderChunkDistance*16)){

				double dyaw=this.getYawDifference(c.getX(), c.getY(),c.getZ(), entity, yaw, pitch);
				double dpitch=this.getPitchDifference(c.getX(), c.getY(),c.getZ(), entity, yaw, pitch);
				double angle=Math.abs(dyaw<-90?180+dyaw:dyaw>90?180-dyaw:dyaw)+Math.abs(dpitch);
				if(!((pitch>50 && dy<0) ||(pitch<-50 && dy>0))) {
					if((pitch>50 && dy>0) ||(pitch<-50 && dy<0) || (dyaw<90 && dyaw>-90)) {
						float psize=psizeModifier*MathHelper.clamp((1200+20*Math.abs((float)angle))/var1, 1f, 40f);
						GL11.glPointSize(psize);
						int n=0;
						if(fastload){

						}else{
							n=getNFromOffset(dx,dy,dz);
						}
						GL11.glPushMatrix();
						//System.out.println(c);
						//long nanotime=System.nanoTime();
						if(wtime<12000){
							GlStateManager.callList(id+(n)*brightnessTypes);
						}else if(wtime>13000 && wtime<23000){
							GlStateManager.callList(id+(n)*brightnessTypes+1);
						}else {
							GlStateManager.callList(id+(n)*brightnessTypes+2);
						}
						//System.out.println(System.nanoTime()-nanotime);
						GL11.glPopMatrix();

					}
				}

			}
			//GL11.glDisable(GL11.GL_POINT_SMOOTH);
			GL11.glPopMatrix();
		}
		for(Entry<BlockPos, Integer> entry:unloadEntryCache.entrySet()){
			this.unloadEntry.put(entry.getKey(), entry.getValue());
			this.chunkToId.remove(entry.getKey());
		}
		//System.out.println("Rendering:"+(System.nanoTime()-nanotime));
		//mc.entityRenderer.disableLightmap();
		//GL11.glDisable(GL11.GL_LIGHT0);
		//GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.disableFog();
		GlStateManager.enableTexture2D();

		GL11.glPopMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}

	public void createList(Chunk c,int chunkY,int n,int id){
		IBlockAccess iba=c.getWorld();
		if(c instanceof IBlockAccess) {
			iba=(IBlockAccess)c;
		}
		int i=n;
		int j=chunkY;
		//idToChunk.put(id, new BlockPos(c.x,chunkY,c.z));
		chunkToId.put(new BlockPos(c.x,chunkY,c.z),id);
		int[][][] bitmapCache=new int[16][16][16];
		GlStateManager.glNewList(id, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_POINTS);
		for(int x=0;x<16;x++){
			for(int z=0;z<16;z++){
				for(int y=j*16;y<j*16+16;y++){
					if(j==0 && y==0)continue;
					//System.out.println("called-1");
					BlockPos pos=new BlockPos(x,y,z);
					bitmapCache[x][y-j*16][z]=0;


					//MapColor color=c.getBlockState(x, y, z).getMapColor(c.getWorld(), pos);
					if(c.getBlockState(pos).getBlock()!=Blocks.AIR){
						IBlockState state=c.getBlockState(pos).getActualState(iba, pos);
						BlockPos globalPos=new BlockPos(c.x*16+x,y,c.z*16+z);
						int renderSide=0;
						//System.out.println("called0");
						//System.out.println(state.getBlock().toString());
						if(fastload){
							renderSide=shouldBeRendered(iba,globalPos,c.getWorld().getActualHeight());
						}else{
							renderSide=shouldBeRendered(c,globalPos,i);
						}
						//System.out.println("renderside:"+renderSide);
						if(renderSide>0){
							//GL11.glColor4ub((byte)((color.colorValue&0xff0000)>>16), (byte)((color.colorValue&0x00ff00)>>8), (byte)(color.colorValue&0x0000ff), (byte)255);

							BlockColors bc=mc.getBlockColors();
							IBakedModel bm=bms.getModelForState(state);
							BakedQuad bq=null;
							TextureAtlasSprite tas=null;
							EnumFacing facing=EnumFacing.UP;
							int side=0;
							if(fastload){
								int num=(y+z-x+100)%(renderSide>>8);
								int var=renderSide&0xff;
								for(int k=0;k<6;k++){
									if((var&0x1)!=0){
										if(num==0){
											facing=EnumFacing.getFront(k);
										}
										num--;
									}
									var=var>>1;
								}
							}else{
								if((renderSide&0x1)!=0){
									switch((renderSide&0x6)){
									case 0:
										side=1;
										break;
									case 2:
									case 4:
										if((y+z-x+100)%2==0){
											side=1;
										}
										break;
									case 6:
										if((y+z-x+100)%3==0){
											side=1;
										}
										break;
									}
								}
								if(side==0&&(renderSide&0x2)!=0){
									switch((renderSide&0x4)){
									case 0:
										side=2;
										break;
									case 4:
										if((y+z-x+100)%2==0){
											side=2;

										}
										break;
									}
								}
								if(side==0&&(renderSide&0x4)!=0){
									side=3;
								}
								switch(side){
								case 1:

									switch(i){
									case 0:
									case 1:
									case 2:
									case 3:
										facing=EnumFacing.EAST;
										break;
									case 4:
									case 5:
									case 6:
									case 7:
										facing=EnumFacing.WEST;
										break;
									}
									break;

								case 2:
									switch(i){
									case 0:
									case 1:
									case 4:
									case 5:
										facing=EnumFacing.UP;
										break;
									case 2:
									case 3:
									case 6:
									case 7:
										facing=EnumFacing.DOWN;
										break;

									}
									break;
								case 3:
									switch(i){
									case 0:
									case 2:
									case 4:
									case 6:
										facing=EnumFacing.SOUTH;
										break;
									case 1:
									case 3:
									case 5:
									case 7:
										facing=EnumFacing.NORTH;
										break;
									}
									break;
								}
							}

							if (bm == null || bm == bms.getModelManager().getMissingModel()){
								//System.out.println("called1");
								tas=bms.getTexture(state);
							}else{
								//System.out.println("called2");
								//System.out.println(bm.getClass().getName());
								List<BakedQuad> bqlist=bm.getQuads(state, facing, 0L);
								//System.out.println(bqlist.size());
								if(bqlist!=null && bqlist.size()>0){
									bq=bqlist.get(0);
									tas=bq.getSprite();
								}else {
									tas=bm.getParticleTexture();
								}
							}
							if(tas!=null){
								//System.out.println("called3");
								int[][] aint=tas.getFrameTextureData(0);
								if(aint!=null){
									//System.out.println("called4");
									int k=0x00ffffff;
									if(bq!=null){
										k=bc.colorMultiplier(state, c.getWorld(), globalPos,bq.getTintIndex());
									}else{
										k=bc.colorMultiplier(state, c.getWorld(), globalPos,0);
									}
									//System.out.println(c.getLightFor(EnumSkyBlock.SKY, globalPos)+" "+c.getLightFor(EnumSkyBlock.BLOCK, globalPos));
									//c.getWorld().getLight(globalPos);
									int blockLight;
									int skyLight;
									if(iba instanceof World) {
										blockLight=((World) iba).getLightFor(EnumSkyBlock.BLOCK,globalPos.offset(facing));
										skyLight=((World) iba).getLightFor(EnumSkyBlock.SKY,globalPos.offset(facing));
										blockLight=Math.max(state.getLightValue(iba,globalPos),blockLight);
									}else if(iba instanceof Chunk) {
										blockLight=((Chunk) iba).getLightFor(EnumSkyBlock.BLOCK,pos.offset(facing));
										skyLight=((Chunk) iba).getLightFor(EnumSkyBlock.SKY,pos.offset(facing));
										blockLight=Math.max(state.getLightValue(iba,globalPos),blockLight);
									}else {
										int combLight=iba.getCombinedLight(globalPos.offset(facing), state.getLightValue(iba,globalPos));
										blockLight=(combLight>>4)&0xf;
										skyLight=(combLight>>20)&0xf;
									}
									int brightness=MathHelper.clamp(Math.max((int)(skyLight),blockLight),1,16);
									//if(brightness==0)continue;
									//System.out.println("called5");
									int indu=((x+y+z+100)%aint.length);
									int indv=((x+y-z+100)%aint[indu].length);
									int r=(((aint[indu][indv]>>16)&0xff) * ((k>>16)&0xff))>>8;
									int g=(((aint[indu][indv]>>8)&0xff) * ((k>>8)&0xff))>>8;
									int b=(((aint[indu][indv]&0x0000ff)) * ((k&0x0000ff)))>>8;
									int alpha=((((aint[indu][indv]>>24)&0xff)));
									int clr=aint[indu][indv]&k;
									//System.out.println(Integer.toHexString(alpha));

									if(state.getMaterial().isLiquid() && brightness<16) {
										brightness+=1;
									}
									if(state.getMaterial().isLiquid() || alpha>200) {
										MathHelper.clamp(r, 0, 255);
										MathHelper.clamp(g, 0, 255);
										MathHelper.clamp(b, 0, 255);
										GL11.glColor4ub((byte)((r*brightness)>>4), (byte)((g*brightness)>>4), (byte)((b*brightness)>>4), (byte)(255));
										GL11.glVertex3f(x+0.5f, y,z+0.5f);

										alpha=alpha>>4;
										bitmapCache[x][y-j*16][z]=(alpha<<28)+(facing.getIndex()<<24)+(r<<16)+(g<<8)+b;
									}
									//System.out.println(renderSide);
									//System.out.println(x+","+y+","+z);
									//System.out.println(r+","+g+","+b);

								}
							}
							//System.out.println((color.colorValue&0xff0000)+","+(color.colorValue&0x00ff00)+","+(color.colorValue&0x0000ff));
							//GL11.glVertex3f(x+0.5f, y,z+0.5f);
						}
					}
				}
			}
		}
		GL11.glEnd();
		GlStateManager.glEndList();
		this.createListFromBitmap(bitmapCache, c, j,id+1,0.3f);
		this.createListFromBitmap(bitmapCache, c, j,id+2,0.6f);
	}
	public void createListFromBitmap(int[][][] bitmapCache,Chunk c,int yOffset,int id,float skylight) {
		IBlockAccess iba=c.getWorld();
		if(c instanceof IBlockAccess) {
			iba=(IBlockAccess)c;
		}
		int j=yOffset;
		GlStateManager.glNewList(id, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_POINTS);
		for(int x=0;x<16;x++){
			for(int z=0;z<16;z++){
				for(int y=j*16;y<j*16+16;y++){
					int cache=bitmapCache[x][y-j*16][z];
					if(cache!=0){
						int r=(cache>>16)&0xff;
						int g=(cache>>8)&0xff;
						int b=cache&0xff;
						int alpha=(cache>>28)&0xf;
						EnumFacing facing=EnumFacing.getFront((cache>>24)&0xf);
						BlockPos globalPos=new BlockPos(c.x*16+x,y,c.z*16+z);

						BlockPos pos=new BlockPos(x,y,z);
						IBlockState state=c.getBlockState(pos).getActualState(iba, globalPos);
						int blockLight;
						int skyLight;
						if(iba instanceof World) {
							blockLight=((World) iba).getLightFor(EnumSkyBlock.BLOCK,globalPos.offset(facing));
							skyLight=((World) iba).getLightFor(EnumSkyBlock.SKY,globalPos.offset(facing));
							blockLight=Math.max(state.getLightValue(iba,globalPos),blockLight);
						}else if(iba instanceof Chunk) {
							blockLight=((Chunk) iba).getLightFor(EnumSkyBlock.BLOCK,pos.offset(facing));
							skyLight=((Chunk) iba).getLightFor(EnumSkyBlock.SKY,pos.offset(facing));
							blockLight=Math.max(state.getLightValue(iba,globalPos),blockLight);
						}else {
							int combLight=iba.getCombinedLight(globalPos.offset(facing), state.getLightValue(iba,globalPos));
							blockLight=(combLight>>4)&0xf;
							skyLight=(combLight>>20)&0xf;
						}
						int brightness=MathHelper.clamp(Math.max((int)(skyLight*skylight),blockLight),1,16);
						//System.out.println(Integer.toHexString(combLight));
						//System.out.println(globalPos.toString()+" bl"+blockLight+" sl"+skyLight);
						if(state.getMaterial().isLiquid() && brightness<16) {
							brightness+=1;
						}
						//System.out.println(x+" "+y+" "+z);
						//System.out.println(r+" "+g+" "+b);
						GL11.glColor4ub((byte)((r*brightness)>>4), (byte)((g*brightness)>>4), (byte)((b*brightness)>>4), (byte)(255));
						GL11.glVertex3f(x+0.5f, y,z+0.5f);
					}
				}
			}
		}
		GL11.glEnd();
		GlStateManager.glEndList();
	}

	public void createList(Chunk c){
		int id=this.getNewId(c);

		GL11.glDisable(GL11.GL_BLEND);
		for(int i=0;i<(fastload?1:8);i++){

			for(int j=0;j<c.getWorld().getActualHeight()/16;j++){
				createList(c,j,i,id+(i+(fastload?1:8)*j)*brightnessTypes);
			}
		}
		//GL11.glDisable(GL11.GL_BLEND);
	}

	public int getNewId(Chunk c){
		return GlStateManager.glGenLists(brightnessTypes*(fastload?1:8)*(c.getWorld().getActualHeight()/16));
	}

	//player-chunk
	public int getNFromOffset(double x,double y,double z){
		if(x>0){
			if(y>0){
				if(z>0){
					return 0;
				}else{
					return 1;
				}
			}else{
				if(z>0){
					return 2;
				}else{
					return 3;
				}
			}
		}else{
			if(y>0){
				if(z>0){
					return 4;
				}else{
					return 5;
				}
			}else{
				if(z>0){
					return 6;
				}else{
					return 7;
				}
			}
		}
	}/*
	//bit1:D 2:U 3:N 4:S 5:W 6:E 9-12:amount of facings
	public int shouldBeRendered(Chunk c,BlockPos pos){
		IBlockState state=c.getBlockState(pos).getActualState(c.getWorld(), pos);
		int i=0;
		int j=0;
		if(pos.getY()>0 && state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.DOWN)){
			i+=1;
			j++;
		}
		if(pos.getY()<c.getWorld().getHeight() && state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.UP)){
			i+=2;
			j++;
		}
		if((pos.getZ()&0xf)>0 && state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.NORTH)){
			i+=4;
			j++;
		}
		if((pos.getZ()&0xf)<15 && state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.SOUTH)){
			i+=8;
			j++;
		}
		if((pos.getX()&0xf)>0 && state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.WEST)){
			i+=16;
			j++;
		}
		if((pos.getX()&0xf)<15 && state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.EAST)){
			i+=32;
			j++;
		}
		return i+(j<<8);
	}
	*/
	//bit1:D 2:U 3:N 4:S 5:W 6:E 9-12:amount of facings
		public int shouldBeRendered(IBlockAccess c,BlockPos pos,int maxHeight){
			IBlockState state=c.getBlockState(pos).getActualState(c, pos);
			int i=0;
			int j=0;
			Material md=c.getBlockState(pos.offset(EnumFacing.DOWN)).getMaterial();
			Material mu=c.getBlockState(pos.offset(EnumFacing.UP)).getMaterial();
			Material mn=c.getBlockState(pos.offset(EnumFacing.NORTH)).getMaterial();
			Material ms=c.getBlockState(pos.offset(EnumFacing.SOUTH)).getMaterial();
			Material mw=c.getBlockState(pos.offset(EnumFacing.WEST)).getMaterial();
			Material me=c.getBlockState(pos.offset(EnumFacing.EAST)).getMaterial();
			if(pos.getY()>0 && !md.isOpaque() && !md.isSolid() && !md.isLiquid()&& state.shouldSideBeRendered(c, pos, EnumFacing.DOWN)){
				i+=1;
				j++;
			}
			if(pos.getY()<maxHeight && !mu.isOpaque() && !mu.isSolid() && !mu.isLiquid()&& state.shouldSideBeRendered(c, pos, EnumFacing.UP)){
				i+=2;
				j++;
			}
			if((pos.getZ()&0xf)>0  && !mn.isOpaque() && !mn.isSolid() && !mn.isLiquid()&& state.shouldSideBeRendered(c, pos, EnumFacing.NORTH)){
				i+=4;
				j++;
			}
			if((pos.getZ()&0xf)<15 && !ms.isOpaque() && !ms.isSolid()&& !ms.isLiquid()&& state.shouldSideBeRendered(c, pos, EnumFacing.SOUTH)){
				i+=8;
				j++;
			}
			if((pos.getX()&0xf)>0 && !mw.isOpaque() && !mw.isSolid() && !mw.isLiquid()&& state.shouldSideBeRendered(c, pos, EnumFacing.WEST)){
				i+=16;
				j++;
			}
			if((pos.getX()&0xf)<15 && !me.isOpaque() && !me.isSolid() && !me.isLiquid()&& state.shouldSideBeRendered(c, pos, EnumFacing.EAST)){
				i+=32;
				j++;
			}
			return i+(j<<8);
		}
	//bit1:xside bit2:yside bit3:zside
	public int shouldBeRendered(Chunk c,BlockPos pos,int n){
		IBlockState state=c.getBlockState(pos).getActualState(c.getWorld(), pos);
		int i=0;

			switch(n){
			case 0:
			case 1:
			case 2:
			case 3:
				i+=state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.EAST)?1:0;
				break;
			case 4:
			case 5:
			case 6:
			case 7:
				i+=state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.WEST)?1:0;
				break;
			}
			switch(n){
			case 0:
			case 1:
			case 4:
			case 5:
				i+=state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.UP)?2:0;
				break;
			case 2:
			case 3:
			case 6:
			case 7:
				i+=state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.DOWN)?2:0;
				break;

			}
			switch(n){
			case 0:
			case 2:
			case 4:
			case 6:
				i+=state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.SOUTH)?4:0;
				break;
			case 1:
			case 3:
			case 5:
			case 7:
				i+=state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.NORTH)?4:0;
				break;

		}
			return i;
	}

	public boolean isChunkForRender(int x,int y,int z,Entity e,float minDistance,float maxDistance){
		if(!e.world.getChunkFromChunkCoords(x, z).isLoaded())return true;
		//if(true)return true;
		double dx= (x*16)-e.posX;
		double dz=(z*16)-e.posZ;
		double dy=(y*16)-e.posY;
		//System.out.println(" dx:"+dx+" dy:"+dy+" dz:"+dz+" dist:"+MathHelper.sqrt(dx*dx+dy*dy+dz*dz)+" max:"+maxDistance);
		return isChunkEnoughFar(dx,dy,dz,minDistance);
	}

	public boolean isChunkEnoughFar(double dx,double dy,double dz,float minDistance) {
		if(dx>minDistance ||-dx>minDistance|| dz>minDistance || -dz>minDistance|| dy>minDistance|| -dy>minDistance)return true;
		float ms=minDistance/1.732f;
		if((dx<ms&&-dx<ms) && (dz<ms&&-dz<ms) && (dy<ms&&-dy<ms))return false;
		return MathHelper.sqrt(dx*dx+dy*dy+dz*dz)>minDistance;
	}

	public boolean isChunkEnoughNear(double dx,double dy,double dz,float maxDistance) {
		if(dx>maxDistance ||-dx>maxDistance|| dz>maxDistance || -dz>maxDistance|| dy>maxDistance|| -dy>maxDistance)return false;
		float ms=maxDistance/1.732f;
		if((dx<ms&&-dx<ms) && (dz<ms&&-dz<ms) && (dy<ms&&-dy<ms))return true;
		return MathHelper.sqrt(dx*dx+dy*dy+dz*dz)<maxDistance;
	}

	public boolean isChunkForRender(Chunk c,Entity e,float yaw,float pitch,float maxDistance){
		double dx= (c.x<<4)-e.posX;
		double dz=(c.z<<4)-e.posZ;
		double chunkyaw=MathHelper.atan2(dz,dx)/Math.PI*180-90;
		double dt=chunkyaw-yaw;
		dt=MathHelper.wrapDegrees(dt);
		/*
		if(dt<180){
			dt+=180;
		}else if(dt>180){
			dt-=180;
		}*/
		double d=Math.abs(dt)/180;
		if(d<0.5)return false;
		d=d*d;
		double dist=MathHelper.sqrt(dx*dx+dz*dz);
		//System.out.println(" d:"+d+" dist:"+dist+" max:"+maxDistance);
		return dist>d*maxDistance;
	}
	public double getYawDifference(int chunkX,int chunkY,int chunkZ,Entity e,float yaw,float pitch){
		double dx= (chunkX<<4)-e.posX;
		double dy=(chunkY<<4)-e.posY;
		double dz=(chunkZ<<4)-e.posZ;
		double dist=MathHelper.sqrt(dx*dx+dz*dz);
		double chunkyaw=MathHelper.atan2(dz,dx)/Math.PI*180+90;
		double dt=chunkyaw-yaw;
		dt=Math.abs(MathHelper.wrapDegrees(dt));
		/*
		if(dt<180){
			dt+=180;
		}else if(dt>180){
			dt-=180;
		}*/
		//System.out.println("cy:"+chunkyaw+" cp"+chunkpitch+" y:"+yaw+" p:"+pitch+" dt"+dt+" dp"+dp);
		return dt;
	}
	public double getPitchDifference(int chunkX,int chunkY,int chunkZ,Entity e,float yaw,float pitch){
		double dx= (chunkX<<4)-e.posX;
		double dy=(chunkY<<4)-e.posY;
		double dz=(chunkZ<<4)-e.posZ;
		double dist=MathHelper.sqrt(dx*dx+dz*dz);
		double chunkpitch=MathHelper.atan2(dist,dy)/Math.PI*180-90;
		double dp=chunkpitch-pitch;
		dp=Math.abs(MathHelper.wrapDegrees(dp));
		/*
		if(dt<180){
			dt+=180;
		}else if(dt>180){
			dt-=180;
		}*/
		//System.out.println("cy:"+chunkyaw+" cp"+chunkpitch+" y:"+yaw+" p:"+pitch+" dt"+dt+" dp"+dp);
		return dp;
	}
	public void putChunk(Entity entity,Deque<BlockPos> q,int heightMax,int rangeMax){
		if(chunkToLoadStage>rangeMax) {
			return;
		}
		int echunkX=(int)entity.posX/16;
		int echunkY=(int)entity.posY/16;
		int echunkZ=(int)entity.posZ/16;
		if(chunkToLoadStage<=this.loadPartialThreshold || this.chunkLoadPartial==4){
			if(chunkToLoady<=0){
				if(chunkToLoadx==0 || (echunkY+chunkToLoady)<0){
					chunkToLoadStage++;
					chunkToLoadx=chunkToLoadStage;
					chunkToLoady=0;

				}else{
					chunkToLoadx--;
					if(echunkY-chunkToLoady<=heightMax){
						chunkToLoady=-chunkToLoady+1;
					}else{
						chunkToLoady--;
					}
				}
			}else{
				chunkToLoady=-chunkToLoady;
			}
			this.chunkLoadPartial=0;
		}
		if(chunkToLoadx==0) {
			BlockPos pos=new BlockPos(echunkX,echunkY+chunkToLoady,echunkZ);
			if(!this.chunkToId.containsKey(pos) && !q.contains(pos)) {
				q.addLast(pos);
			}
		}else {
			if(chunkToLoadStage<=this.loadPartialThreshold || this.chunkLoadPartial==3){
				//System.out.println((echunkX+chunkToLoadx)+","+(echunkY+chunkToLoady)+","+(echunkZ+chunkToLoadx)+" -> "+(echunkX-chunkToLoadx)+","+(echunkY+chunkToLoady)+","+(echunkZ-chunkToLoadx));
				for(int z=chunkToLoadx;z>-chunkToLoadx;z--){
					BlockPos pos=new BlockPos(echunkX+chunkToLoadx,echunkY+chunkToLoady,echunkZ+z);
					if(!this.chunkToId.containsKey(pos) && !q.contains(pos)) {

						q.addLast(pos);
					}
				}
				this.chunkLoadPartial++;
			}
			if(chunkToLoadStage<=this.loadPartialThreshold || this.chunkLoadPartial==2){
				for(int x=chunkToLoadx;x>-chunkToLoadx;x--){
					BlockPos pos=(new BlockPos(echunkX+x,echunkY+chunkToLoady,echunkZ-chunkToLoadx));
					if(!this.chunkToId.containsKey(pos) && !q.contains(pos)) {
						q.addLast(pos);
					}
				}
				this.chunkLoadPartial++;
			}
			if(chunkToLoadStage<=this.loadPartialThreshold || this.chunkLoadPartial==1){
				for(int z=-chunkToLoadx;z<chunkToLoadx;z++){
					BlockPos pos=(new BlockPos(echunkX-chunkToLoadx,echunkY+chunkToLoady,echunkZ+z));
					if(!this.chunkToId.containsKey(pos) && !q.contains(pos)) {
						q.addLast(pos);
					}
				}
				this.chunkLoadPartial++;
			}
			if(chunkToLoadStage<=this.loadPartialThreshold || this.chunkLoadPartial==0){
				for(int x=-chunkToLoadx;x<chunkToLoadx;x++){
					BlockPos pos=(new BlockPos(echunkX+x,echunkY+chunkToLoady,echunkZ+chunkToLoadx));
					if(!this.chunkToId.containsKey(pos) && !q.contains(pos)) {
						q.addLast(pos);
					}
				}
				this.chunkLoadPartial++;
			}
		}
	}
	/*
	public boolean shouldBeRendered(Chunk c,BlockPos pos,int n){
		IBlockState state=c.getBlockState(pos).getActualState(c.getWorld(), pos);
		int i;
		switch(n){
		case 0:
			//(+,+,+)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.EAST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.SOUTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.UP));
		case 1:
			//(+,+,-)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.EAST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.NORTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.UP));
		case 2:
			//(+,-,+)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.EAST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.SOUTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.DOWN));
		case 3:
			//(+,-,-)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.EAST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.NORTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.DOWN));
		case 4:
			//(-,+,+)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.WEST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.SOUTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.UP));
		case 5:
			//(-,+,-)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.WEST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.NORTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.UP));
		case 6:
			//(-,-,+)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.WEST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.SOUTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.DOWN));
		case 7:
			//(-,-,-)
			return (state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.WEST)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.NORTH)
					||state.shouldSideBeRendered(c.getWorld(), pos, EnumFacing.DOWN));
		default:
			return false;
		}
	}*/
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
	/*
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
	 */

	public void notifyBlockUpdate(World worldIn, BlockPos pos,
			IBlockState oldState, IBlockState newState, int flags) {
		this.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());

	}

	public void notifyLightSet(BlockPos pos) {

	}

	public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2,
			int y2, int z2) {
		//System.out.println(x1+","+y1+","+z1+","+x2+","+y2+","+z2+",");
		for(int x=(x1>>4);x<=(x2>>4);x++){
			for(int z=(z1>>4);z<=(z2>>4);z++){
				if(mc.player.world.getChunkFromChunkCoords(x, z).isLoaded()) {
					for(int y=(y1>>4);y<=(y2>>4);y++){
						BlockPos pos=new BlockPos(x,y,z);
						if(chunkToId.containsKey(pos)){
							if(!updateStack.contains(pos)){
								updateStack.addFirst(pos);
								//System.out.println("add:"+x+","+y+","+z);
								break;
							}
						}
					}	}
			}
		}

	}

	public void playSoundToAllNearExcept(EntityPlayer player,
			SoundEvent soundIn, SoundCategory category, double x, double y,
			double z, float volume, float pitch) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void playRecord(SoundEvent soundIn, BlockPos pos) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void spawnParticle(int particleID, boolean ignoreRange,
			double xCoord, double yCoord, double zCoord, double xSpeed,
			double ySpeed, double zSpeed, int... parameters) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void spawnParticle(int id, boolean ignoreRange, boolean p_190570_3_,
			double x, double y, double z, double xSpeed, double ySpeed,
			double zSpeed, int... parameters) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void onEntityAdded(Entity entityIn) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void onEntityRemoved(Entity entityIn) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void broadcastSound(int soundID, BlockPos pos, int data) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn,
			int data) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}

	public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
		// TODO �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ黷ｽ�ｽ�ｽ�ｽ\�ｽb�ｽh�ｽE�ｽX�ｽ^�ｽu

	}
}
