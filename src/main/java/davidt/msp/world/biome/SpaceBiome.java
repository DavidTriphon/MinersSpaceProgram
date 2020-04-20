package davidt.msp.world.biome;

import net.minecraft.world.biome.*;


public class SpaceBiome extends Biome
{
   private static final Biome.Builder BIOME_BUILDER = new Biome.Builder();
   
   public SpaceBiome()
   {
      super(BIOME_BUILDER);
   }
}
