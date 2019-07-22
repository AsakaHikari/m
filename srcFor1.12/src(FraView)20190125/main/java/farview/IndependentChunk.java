package farview;

import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class IndependentChunk extends Chunk implements IBlockAccess {

	public IndependentChunk(World worldIn, int x, int z) {
		super(worldIn, x, z);
	}

	public IndependentChunk copyFromChunk(Chunk c) {
		for(int i=0;i<this.getBlockStorageArray().length;i++) {
			this.getBlockStorageArray()[i]=c.getBlockStorageArray()[i];
		}
		for(int i=0;i<this.getBiomeArray().length;i++) {
			this.getBiomeArray()[i]=c.getBiomeArray()[i];
		}
		for(int i=0;i<this.getHeightMap().length;i++) {
			this.getHeightMap()[i]=c.getHeightMap()[i];
		}
		for(Map.Entry<BlockPos, TileEntity> e:c.getTileEntityMap().entrySet()) {
			this.getTileEntityMap().put(e.getKey(), e.getValue());
		}
		return this;
	}

	public IBlockState getBlockState(BlockPos pos) {
		return super.getBlockState(pos);
	}

	public static IndependentChunk makeFromChunk(Chunk c) {
		return new IndependentChunk(c.getWorld(),c.x,c.z).copyFromChunk(c);
	}
	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		pos=new BlockPos(pos.getX()&15,pos.getY(),pos.getZ()&15);
		return this.getTileEntity(pos,EnumCreateEntityType.IMMEDIATE);
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		pos=new BlockPos(pos.getX()&15,pos.getY(),pos.getZ()&15);
		int i = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
        int j = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);

        if (j < lightValue)
        {
            j = lightValue;
        }

        return i << 20 | j << 4;
	}
	public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos)
    {
		pos=new BlockPos(pos.getX()&15,pos.getY(),pos.getZ()&15);
        if (!this.getWorld().provider.hasSkyLight() && type == EnumSkyBlock.SKY)
        {
            return 0;
        }
        else
        {
            if (pos.getY() < 0)
            {
                pos = new BlockPos(pos.getX(), 0, pos.getZ());
            }

            if (this.getBlockState(pos).useNeighborBrightness())
            {
                int i1 = this.getLightFor(type, pos.up());
                int i = this.getLightFor(type, pos.east());
                int j = this.getLightFor(type, pos.west());
                int k = this.getLightFor(type, pos.south());
                int l = this.getLightFor(type, pos.north());

                if (i > i1)
                {
                    i1 = i;
                }

                if (j > i1)
                {
                    i1 = j;
                }

                if (k > i1)
                {
                    i1 = k;
                }

                if (l > i1)
                {
                    i1 = l;
                }

                return i1;
            }
            else
            {
                return this.getLightFor(type, pos);
            }
        }
    }
	@Override
	public boolean isAirBlock(BlockPos pos) {
		IBlockState state=this.getBlockState(pos.getX()&15,pos.getY(),pos.getZ()&15);
		state.getBlock().isAir(state, this, pos);
		return false;
	}

	@Override
	public Biome getBiome(BlockPos pos) {
		pos=new BlockPos(pos.getX()&15,pos.getY(),pos.getZ()&15);
		return this.getBiome(pos, this.getWorld().getBiomeProvider());
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		pos=new BlockPos(pos.getX()&15,pos.getY(),pos.getZ()&15);
		return this.getBlockState(pos).getStrongPower(this, pos, direction);
	}

	@Override
	public WorldType getWorldType() {
		return this.getWorldType();
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		pos=new BlockPos(pos.getX()&15,pos.getY(),pos.getZ()&15);
		return getBlockState(pos).isSideSolid(this, pos, side);
	}

}
