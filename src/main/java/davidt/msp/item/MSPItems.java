package davidt.msp.item;

import net.minecraft.item.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.registries.*;

import static davidt.msp.MinersSpaceProgram.*;


public class MSPItems
{
   public static final DeferredRegister <Item> ITEMS =
     new DeferredRegister <>(ForgeRegistries.ITEMS, MOD_ID);
   
   public static final RegistryObject <Item> TELEPORTER_ITEM =
     ITEMS.register("space_teleporter", TeleporterItem::new);
}
