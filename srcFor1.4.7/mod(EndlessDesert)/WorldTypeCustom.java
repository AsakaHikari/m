package mod;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.WorldType;

public class WorldTypeCustom extends WorldType
{       
    public WorldTypeCustom(int par1, String par2Str)
    {
        super(par1, par2Str, 0);
        /*
        this.removeBiome(BiomeGenBase.ocean);
        this.removeBiome(BiomeGenBase.plains);
        this.removeBiome(BiomeGenBase.extremeHills);
        this.removeBiome(BiomeGenBase.forest);
        this.removeBiome(BiomeGenBase.taiga);
        this.removeBiome(BiomeGenBase.swampland);
        this.removeBiome(BiomeGenBase.hell);
        this.removeBiome(BiomeGenBase.sky);
        this.removeBiome(BiomeGenBase.frozenOcean);
        this.removeBiome(BiomeGenBase.frozenRiver);
        this.removeBiome(BiomeGenBase.icePlains);
        this.removeBiome(BiomeGenBase.iceMountains);
        this.removeBiome(BiomeGenBase.mushroomIsland);
        this.removeBiome(BiomeGenBase.mushroomIslandShore);
        this.removeBiome(BiomeGenBase.forestHills);
        this.removeBiome(BiomeGenBase.taigaHills);
        this.removeBiome(BiomeGenBase.extremeHillsEdge);
        this.removeBiome(BiomeGenBase.jungle);
        this.removeBiome(BiomeGenBase.jungleHills); 
        */
        biomesForWorldType=new BiomeGenBase[] {BiomeGenBase.ocean,BiomeGenBase.river,BiomeGenBase.desert,BiomeGenBase.desertHills};
        
        //You can either add or remove biomes here. This does not work well because biomes such as oceans and beaches are added in the genlayer classes and will need to be replaced through your chunk provider which is hard work.
    }
    
    @Override
    public WorldChunkManager getChunkManager(World world){
    	
    	return new WorldChunkManagerDesert(world.getSeed(),this);
    }

    
    
}