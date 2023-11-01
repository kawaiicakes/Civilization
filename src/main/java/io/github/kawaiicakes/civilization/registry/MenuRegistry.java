package io.github.kawaiicakes.civilization.registry;

import io.github.kawaiicakes.civilization.client.menu.TownHallMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;
import static net.minecraftforge.registries.ForgeRegistries.MENU_TYPES;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(MENU_TYPES, MOD_ID);

    public static final RegistryObject<MenuType<TownHallMenu>> TOWN_HALL =
            REGISTRY.register("town_hall", () -> IForgeMenuType.create(TownHallMenu::new));

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
