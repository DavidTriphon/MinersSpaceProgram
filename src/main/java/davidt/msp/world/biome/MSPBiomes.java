package davidt.msp.world.biome;

import davidt.msp.*;
import net.minecraft.world.biome.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.registries.*;


public final class MSPBiomes
{
   public static final DeferredRegister <Biome> BIOMES =
     new DeferredRegister <>(ForgeRegistries.BIOMES, MinersSpaceProgram.MOD_ID);
   
   public static final RegistryObject <Biome> SPACE = BIOMES.register("msp_space", SpaceBiome::new);
}
