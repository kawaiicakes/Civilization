package io.github.kawaiicakes.civilization.api;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.data.CivCity;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.kawaiicakes.civilization.api.ClaimManager.setTileOwner;
import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider.CIV_LEVEL_CAP;

public class CityManager {
    /**
     * Returns all the cities in the passed <code>ServerLevel</code>. This method has the possibility of returning
     * an empty <code>HashSet</code>. This implies either the capability does not exist in the level, or if it exists,
     * that no cities exist in it.
     */
    @NotNull
    public static Set<CivCity> getCities(ServerLevel level) {
        AtomicReference<Set<CivCity>> cities = new AtomicReference<>(new HashSet<>());
        level.getCapability(CIV_LEVEL_CAP).ifPresent(cap -> cities.set(cap.getCities()));
        return cities.get();
    }

    public static boolean foundCity(ServerLevel level, HexTilePos tile, CivCity civCity) {
        if (!validatePassedUUID(level, civCity)) return false;
        if (!isValidClaimSpot(tile)) return false;

        tile.getAdjacent().forEach(hexTilePos -> setTileOwner(level, hexTilePos, civCity.id()));
        level.getCapability(CIV_LEVEL_CAP).ifPresent(cap -> cap.addCity(civCity));

        return true;
    }

    public static boolean validatePassedUUID(ServerLevel level, CivCity city) {
        for (CivCity existingCity : getCities(level)) {
            if (Objects.equals(existingCity.id(), city.id())) return false;
        }

        return true;
    }

    public static boolean isValidClaimSpot(HexTilePos tile) {
        // TODO: check for presence of city core within 3 tiles
        for (HexTilePos pos : tile.getAdjacent()) {
            if (ClaimManager.tileIsOwned(pos).isPresent()) return false;
        }
        return true;
    }
}
