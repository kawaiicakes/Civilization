package io.github.kawaiicakes.civilization.registry;

import io.github.kawaiicakes.civilization.client.menu.TownHallMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

import static io.github.kawaiicakes.civilization.registry.Registry.MENU_REGISTRY;

public class Menus {
    public static final RegistryObject<MenuType<TownHallMenu>> TOWN_HALL =
            MENU_REGISTRY.register("town_hall", () -> IForgeMenuType.create(TownHallMenu::new));
}
