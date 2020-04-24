package davidt.msp.item;

import davidt.msp.dimension.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.registries.*;

import static davidt.msp.MinersSpaceProgram.*;


public class MSPItems
{
   public static final DeferredRegister <Item> ITEMS =
     new DeferredRegister <>(ForgeRegistries.ITEMS, MOD_ID);
   
   public static final RegistryObject <Item> TELEPORTER_ITEM =
     ITEMS.register("space_teleporter",
       () -> new SpecialActionItem(DimensionHoppingHandler::teleport));
   
   public static final RegistryObject <Item> TRANSDIMENSIONAL_ITEM =
     ITEMS.register("transdimensional",
       () -> new SpecialActionItem(DimensionHoppingHandler::transdimensionalHop));
   
   public static final RegistryObject <Item> MIRROR_PLAYER_ITEM =
     ITEMS.register("mirror_player",
       () -> new SpecialActionItem(DimensionHoppingHandler::createMirrorSelf));
}
