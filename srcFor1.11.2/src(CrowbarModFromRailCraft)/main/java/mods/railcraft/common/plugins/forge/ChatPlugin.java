package mods.railcraft.common.plugins.forge;

import com.mojang.authlib.GameProfile;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ChatPlugin
{
  public static ITextComponent getMessage(String msg)
  {
    return new TextComponentString(msg);
  }

  public static ITextComponent chatComp(String msg, Object[] args) {
    return new TextComponentTranslation(msg, args);
  }

  public static void sendLocalizedChat(EntityPlayer player, String msg, Object[] args) {
    player.sendMessage(getMessage(String.format(LocalizationPlugin.translate(msg), args)));
  }

  public static void sendLocalizedChatFromClient(EntityPlayer player, String msg, Object[] args) {
    if (player.world.isRemote)
      sendLocalizedChat(player, msg, args);
  }

  public static void sendLocalizedChatFromServer(EntityPlayer player, String msg, Object[] args) {
    if (!player.world.isRemote) {
      for (int i = 0; i < args.length; i++) {
        if ((args[i] instanceof String)) {
          args[i] = chatComp((String)args[i], new Object[0]);
        } else if ((args[i] instanceof GameProfile)) {
          String username = ((GameProfile)args[i]).getName();
          args[i] = (username != null ? username : "[unknown]");
        }
      }
      player.sendMessage(chatComp(msg, args));
    }
  }

  public static void sendLocalizedChatToAllFromServer(World world, String msg, Object[] args)
  {
    Iterator localIterator;
    if ((world instanceof WorldServer)) {
      WorldServer worldServer = (WorldServer)world;
      for (localIterator = worldServer.playerEntities.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
        if ((obj instanceof EntityPlayer))
          sendLocalizedChat((EntityPlayer)obj, msg, args);
      }
    }
  }
}