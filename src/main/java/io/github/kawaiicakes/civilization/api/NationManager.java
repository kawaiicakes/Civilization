package io.github.kawaiicakes.civilization.api;

import io.github.kawaiicakes.civilization.api.level.HexTilePos;
import io.github.kawaiicakes.civilization.capabilities.CivGlobalDataCapability;
import io.github.kawaiicakes.civilization.capabilities.CivLevelChunkCapability;
import io.github.kawaiicakes.civilization.capabilities.data.CivNation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.kawaiicakes.civilization.Civilization.CHAT_HEADER;
import static io.github.kawaiicakes.civilization.capabilities.CivGlobalDataCapability.Provider.CIV_GLOBAL_CAP;
import static io.github.kawaiicakes.civilization.capabilities.CivLevelChunkCapability.Provider.CIV_LEVEL_CHUNK_CAP;

/**
 * Central hub for easily accessing nation info. Also exists as a security checkpoint for packets received from clients.
 * This class is intended to work only on the serverside and should not be easily accessible from a client.
 */
public class NationManager {
    /**
     * Theoretically this will only ever return null or throw an error if called from a client.
     * Do NOT cache the return as it may cause memory leaks.
     * @return the <code>ServerLevel</code> corresponding to the Overworld on the server.
     */
    public static ServerLevel getOverworld() {
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
     * Be incredibly careful with this. It can easily cause world corruption.
     * // TODO: find out when this and <code>#getChunkCap</code> are safe to use.
     */
    @Deprecated
    public static void setTileOwner(ServerLevel level, HexTilePos hex, UUID owner) {
        hex.toChunkPos().forEach(chunk -> getChunkCap(level, chunk).ifPresent(cap -> cap.claim(owner)));
    }

    /**
     * Queries the passed chunk in the passed level for its CivLevelChunkCapability. BE VERY CAREFUL WITH THIS.
     * Haphazardly writing to the level or attempting to operate on chunks that have yet to be loaded may cause
     * horrific bugs or world corruption.
     */
    @Deprecated
    private static LazyOptional<CivLevelChunkCapability> getChunkCap(ServerLevel level, ChunkPos pos) {
        return level.getChunk(pos.x, pos.z).getCapability(CIV_LEVEL_CHUNK_CAP);
    }

    /**
     * This should only ever be called from the server's end.
     * @param civNation    the <code>CivNation</code> instance to query for creation. Since this argument may be passed from
     *                  the client, a security check is done.
     * @return      the result of attempting to create a <code>CivNation</code>. <code>true</code> indicates success,
     */
    public static boolean createNation(CivNation civNation) {
        if (!nationCreationValid(civNation)) return false;

        getOverworld().getCapability(CIV_GLOBAL_CAP).ifPresent(civ -> {
            getOverworld().players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                    CHAT_HEADER().append(Component.translatable("chat.civilization.nation_founded", civNation.name()).setStyle(Style.EMPTY))
            ));
            CivGlobalDataCapability.addNation(civNation);
        });

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
