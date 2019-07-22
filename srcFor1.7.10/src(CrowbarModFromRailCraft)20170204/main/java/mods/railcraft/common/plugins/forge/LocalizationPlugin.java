package mods.railcraft.common.plugins.forge;

import java.util.IllegalFormatException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.StatCollector;

public class LocalizationPlugin
{
  public static final String ENGLISH = "en_US";

  public static String translate(String tag)
  {
    return StatCollector.translateToLocal(tag).replace("\\n", "\n").replace("@", "%").replace("\\%", "@");
  }

  public static String translate(String tag, Object[] args) {
    String text = translate(tag);
    try
    {
      return String.format(text, args); } catch (IllegalFormatException ex) {
    }
    return "Format error: " + text;
  }

  public static boolean hasTag(String tag)
  {
    return StatCollector.canTranslate(tag);
  }

  public static String getEntityLocalizationTag(Entity entity) {
    String s = EntityList.getEntityString(entity);

    if (s == null) {
      s = "generic";
    }

    return "entity." + s + ".name";
  }
}