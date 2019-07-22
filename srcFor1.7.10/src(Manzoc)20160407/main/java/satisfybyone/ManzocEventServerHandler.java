package satisfybyone;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;


public class ManzocEventServerHandler {
	public int lastHour;
	public int lastMinute;
	public Calendar calendar = new GregorianCalendar();
	
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		calendar.setTimeInMillis(System.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int thirtyMinutes = hour * 2 + (minute / 30);
		int lastThirtyMinutes = this.lastHour * 2 + (this.lastMinute / 30);
		if (thirtyMinutes != lastThirtyMinutes && minute < 5) {
			if (thirtyMinutes == 34) {
				for (WorldServer w : MinecraftServer.getServer().worldServers) {
					for (Object o : w.playerEntities) {
						EntityPlayer p=(EntityPlayer) o;
						for (int x = MathHelper.floor_double(p.posX - 6); x < MathHelper
								.ceiling_double_int(p.posX + 6); x++) {
							for (int y = MathHelper.floor_double(p.posY - 6); y < MathHelper
									.ceiling_double_int(p.posY + 6); y++) {
								for (int z = MathHelper
										.floor_double(p.posZ - 6); z < MathHelper
										.ceiling_double_int(p.posZ + 6); z++) {
									if (w.getBlock(x, y, z) == ManzocCore.blockBar) {
										System.out.println("manzoc at "+x+","+y+","+z);
										w.playSoundEffect(
												x,
												y,
												z,
												"manzoc:manzoc.CM"+(w.rand.nextInt(5)+1), 
												0.7f,
												w.rand.nextFloat() * 0.05F + 0.95F);
									}
								}
							}
						}
						List<Entity> l = w
								.getEntitiesWithinAABBExcludingEntity(p,
										AxisAlignedBB.getBoundingBox(
												p.posX - 6, p.posY - 6,
												p.posZ - 6, p.posX + 6,
												p.posY + 6, p.posZ + 6));
						for (Entity e : l) {
							if (e instanceof EntityManzocCart) {
								w.playSoundAtEntity(e,
										"manzoc:manzoc.CM"+(w.rand.nextInt(5)+1), 0.7f,
										w.rand.nextFloat() * 0.05F + 0.95F);
							}
						}
					}
					
				}
			}
		}
		this.lastHour = hour;
		this.lastMinute = minute;
	}
}
