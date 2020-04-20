package davidt.msp.dimension;

import com.mojang.datafixers.*;
import davidt.msp.world.biome.*;

import net.minecraft.nbt.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.provider.*;
import net.minecraft.world.dimension.*;
import net.minecraft.world.gen.*;

import javax.annotation.*;


public class SpaceDimension extends Dimension
{
   public static final float DIMENSION_BRIGHTNESS = 0.5f;
   
   
   public SpaceDimension(World world, DimensionType type)
   {
      super(world, type, DIMENSION_BRIGHTNESS);
   }
   
   
   @Override
   public ChunkGenerator <?> createChunkGenerator()
   {
      // create a biome provider that only provides one biome
      SingleBiomeProviderSettings biomeProviderSettings =
        BiomeProviderType.FIXED.createSettings(this.world.getWorldInfo()).setBiome(MSPBiomes.SPACE.get());
      SingleBiomeProvider biomeProvider = BiomeProviderType.FIXED.create(biomeProviderSettings);
      
      // generation settings
      CompoundNBT nbt = new CompoundNBT();
      nbt.put("layers", new ListNBT()); // empty list, we don't want any blocks
      // nbt.putString("biome"); // supposedly biome string defaults to ""?
      nbt.put("structures", new CompoundNBT()); // empty structures
      FlatGenerationSettings flatGenerationSettings =
        FlatGenerationSettings.createFlatGenerator(new Dynamic <>(NBTDynamicOps.INSTANCE, nbt));
      
      // flat chunk
      return new FlatChunkGenerator(world, biomeProvider, flatGenerationSettings);
   }
   
   
   @Nullable
   @Override
   public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid)
   {
      return findSpawn(chunkPosIn.x * 16 + 8, chunkPosIn.z * 16 + 8, checkValid);
   }
   
   
   @Nullable
   @Override
   public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
   {
      return new BlockPos(posX, 65, posZ);
   }
   
   
   /**
    * Calculates the angle of sun and moon in the sky relative to a specified time (usually
    * worldTime)
    *
    * @param worldTime
    * @param partialTicks
    */
   @Override
   public float calculateCelestialAngle(long worldTime, float partialTicks)
   {
      return (float) (worldTime / 24000.0 - 0.25);
   }
   
   
   /**
    * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End
    * dimensions.
    */
   @Override
   public boolean isSurfaceWorld()
   {
      return false;
   }
   
   
   /**
    * Return Vec3D with biome specific fog color
    *
    * @param celestialAngle
    * @param partialTicks
    */
   @Override
   public Vec3d getFogColor(float celestialAngle, float partialTicks)
   {
      // gray
      return new Vec3d(0.5, 0.5, 0.5);
   }
   
   
   /**
    * True if the player can respawn in this dimension (true = overworld, false = nether).
    */
   @Override
   public boolean canRespawnHere()
   {
      return false;
   }
   
   
   /**
    * Returns true if the given X,Z coordinate should show environmental fog.
    *
    * @param x
    * @param z
    */
   @Override
   public boolean doesXZShowFog(int x, int z)
   {
      return false;
   }
}
