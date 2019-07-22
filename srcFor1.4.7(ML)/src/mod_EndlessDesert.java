package net.minecraft.src;
import net.minecraft.world.WorldType;
public class mod_EndlessDesert extends BaseMod
{
	public static WorldType Desert = new EndlessDesertGenerator(15, "Desert");
    public String getVersion() {
        return "1.0.0";
    }

    public void load() {
        //addLocalization("generator." + Generator名,ワールド名(ゲーム中に表示されます));
        ModLoader.addLocalization("generator.EndlessDesert", "EndlessDesert");
        //WorldID, Generator名


    }
}