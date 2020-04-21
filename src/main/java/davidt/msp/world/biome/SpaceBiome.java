package davidt.msp.world.biome;

import net.minecraft.block.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.surfacebuilders.*;


public class SpaceBiome extends Biome
{
   private static final Biome.Builder BIOME_BUILDER = new Biome.Builder()
     .surfaceBuilder(new ConfiguredSurfaceBuilder(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(
       Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(),Blocks.AIR.getDefaultState())))
     .precipitation(RainType.NONE)
     .category(Category.NONE)
     .waterColor(0x55ff88)
     .waterFogColor(0x55ff88)
     .depth(0.125F)
     .scale(0.05F)
     .temperature(0.8F)
     .downfall(0.4F);
   
   public SpaceBiome()
   {
      super(BIOME_BUILDER);
   }
}
