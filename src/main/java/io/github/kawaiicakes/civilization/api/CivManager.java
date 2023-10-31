package io.github.kawaiicakes.civilization.api;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.capabilities.CivGlobalDataCapability;
import io.github.kawaiicakes.civilization.capabilities.CivLevelCapability;
import io.github.kawaiicakes.civilization.data.CivCity;
import io.github.kawaiicakes.civilization.data.CivNation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.kawaiicakes.civilization.Civilization.CHAT_HEADER;
import static io.github.kawaiicakes.civilization.capabilities.CivGlobalDataCapability.Provider.CIV_GLOBAL_CAP;
import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider.CIV_LEVEL_CAP;

/**
 * Central hub for easily accessing the mod's data. Also exists as a "security checkpoint" for packets received from
 * clients. This class is intended to work only on the serverside and should not be easily accessible from a client.
 */
public class CivManager {
    /**
     * Theoretically this will only ever return null or throw an error if called from a client.
     * Do NOT cache the return as it may cause memory leaks.
     * @return the <code>ServerLevel</code> corresponding to the Overworld on the server.
     */
    private static ServerLevel getOverworld() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server.overworld();
    }

    /**
     * This method returns a set containing all nations on the server. A nation, regardless of where it was founded or
     * owns territory, exists through all levels on the server.
     * @return  a set of all currently existing nations across all levels.
     */
    public static Set<CivNation> getNations() {
        AtomicReference<Set<CivNation>> listAtomicReference = new AtomicReference<>();
        getOverworld().getCapability(CIV_GLOBAL_CAP).ifPresent(levelCap -> listAtomicReference.set(CivGlobalDataCapability.getNations()));
        return listAtomicReference.get();
    }

    @Nullable
    public static CivNation getNationById(UUID id) {
        AtomicReference<CivNation> nation = new AtomicReference<>();
        getOverworld().getCapability(CIV_GLOBAL_CAP).ifPresent(cap -> nation.set(CivGlobalDataCapability.getNationById(id)));
        return nation.get();
    }

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

    public static void setTileOwner(ServerLevel level, HexTilePos hex, UUID owner) {
        LazyOptional<CivLevelCapability> levelCap = level.getCapability(CIV_LEVEL_CAP);

        hex.toChunkPos().forEach(chunk -> levelCap.ifPresent(cap -> cap.setOwner(chunk, owner)));
    }

    public static boolean foundCity(ServerLevel level, HexTilePos tile, CivCity civCity) {
        if (!cityCreationValid(level, civCity)) return false;

        // TODO: add check for territory claiming
        tile.getAdjacent().forEach(hexTilePos -> setTileOwner(level, hexTilePos, civCity.id()));
        level.getCapability(CIV_LEVEL_CAP).ifPresent(cap -> cap.addCity(civCity));

        return true;
    }

    /**
     * This should only ever be called from the server's end.
     * @param civNation    the <code>CivNation</code> instance to query for creation. Since this argument may be passed from
     *                  the client, a security check is done.
     * @return      the result of attempting to create a <code>CivNation</code>. <code>true</code> indicates success,
     */
    public static boolean foundNation(CivNation civNation) {
        if (!nationCreationValid(civNation)) return false;

        getOverworld().getCapability(CIV_GLOBAL_CAP).ifPresent(civ -> {
            getOverworld().players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.nation_founded", civNation.name).setStyle(Style.EMPTY))
            ));
            CivGlobalDataCapability.addNation(civNation);
        });

        return true;
    }

    public static boolean cityCreationValid(ServerLevel level, CivCity city) {
        for (CivCity existingCity : getCities(level)) {
            if (Objects.equals(existingCity.id(), city.id())) return false;
        }

        return true;
    }

    /**
     * This method exists to check, from the server, whether a CivLevelNation is valid to
     * create. Done for security reasons as a malicious client would otherwise be able
     * to submit any CivLevelNation instance to the server for creation.
     * @param civNation    the <code>CivLevelNation</code> to check.
     * @return  <code>true</code> if the given civLevelNation may be created. <code>false</code>
     *          otherwise.
     */
    public static boolean nationCreationValid(CivNation civNation) {
        Set<CivNation> civNationList = getNations();

        if (civNationList == null) return false;

        return !civNationList.contains(civNation);
    }
}
