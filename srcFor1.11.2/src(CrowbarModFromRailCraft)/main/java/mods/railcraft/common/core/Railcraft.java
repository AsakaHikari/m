package mods.railcraft.common.core;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;




import java.io.File;
import java.util.Iterator;

import mods.railcraft.common.carts.LinkageManager;
import net.minecraft.block.Block;
import net.minecraft.command.CommandHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import org.apache.logging.log4j.Level;


public final class Railcraft
{
  public static final String MOD_ID = "Railcraft";
  public static final String VERSION = "9.12.2.0";
  public static final String MC_VERSION = "[1.7.10,1.8)";

  @net.minecraftforge.fml.common.Mod.Instance("Railcraft")
  public static Railcraft instance;

  public static Railcraft getMod()
  {
    return instance;
  }

  public static String getModId() {
    return "Railcraft";
  }

  public static String getVersion() {
    return "9.12.2.0";
  }


  @EventHandler
  public void serverCleanUp(FMLServerStoppingEvent event) {
    LinkageManager.reset();
  }

}