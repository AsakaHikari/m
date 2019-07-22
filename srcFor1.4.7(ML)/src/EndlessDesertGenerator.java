package net.minecraft.src;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.WorldType;

public class EndlessDesertGenerator extends WorldType
{

	public int getSeaLevel(World par1) {
		return 64;
	}
    public EndlessDesertGenerator(int par1, String par2Str)
    {
        super(par1, par2Str, 0);
        this.addNewBiome(BiomeGenBase.desert);
        this.addNewBiome(BiomeGenBase.desertHills);
        this.removeBiome(BiomeGenBase.ocean);
        this.removeBiome(BiomeGenBase.beach);
        this.removeBiome(BiomeGenBase.forest);
        this.removeBiome(BiomeGenBase.jungle);
        this.removeBiome(BiomeGenBase.mushroomIsland);
        this.removeBiome(BiomeGenBase.mushroomIslandShore);
        this.removeBiome(BiomeGenBase.taiga);
        this.removeBiome(BiomeGenBase.plains);
        this.removeBiome(BiomeGenBase.swampland);
        this.removeBiome(BiomeGenBase.extremeHills);
        this.removeBiome(BiomeGenBase.icePlains);
        this.removeBiome(BiomeGenBase.iceMountains);
        this.removeBiome(BiomeGenBase.frozenOcean);
        this.removeBiome(BiomeGenBase.frozenRiver);
        this.removeBiome(BiomeGenBase.forestHills);
        this.removeBiome(BiomeGenBase.taigaHills);
        this.removeBiome(BiomeGenBase.extremeHillsEdge);
        this.removeBiome(BiomeGenBase.jungleHills);
        //You can either add or remove biomes here.
    }



    //Gets the spawn fuzz for players who join the world.
    @Override
    public int getSpawnFuzz()
    {
        return 100;
    }
}