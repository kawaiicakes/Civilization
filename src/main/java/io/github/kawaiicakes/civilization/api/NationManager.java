package io.github.kawaiicakes.civilization.api;

import io.github.kawaiicakes.civilization.capabilities.data.CivNation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.kawaiicakes.civilization.Civilization.CHAT_HEADER;
import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider.CIV_LEVEL_CAP;

/**
 * Central hub for easily accessing nation info. Also exists as a security checkpoint for packets received from clients.
 */
public class NationManager {
    private static final Map<ChunkPos, UUID> CHUNK_MAP = new HashMap<>();

    /**
     * Only use this when absolutely necessary and ensure that it is called on the server thread.
     * Do not cache the return as it may cause memory leaks.
     * @return <code>null</code> if not available. Otherwise, the <code>ServerLevel</code>
     * corresponding to the Overworld on the server.
     */
    public static ServerLevel getOverworld() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            return server.overworld();
        }
        return null;
    }

    public static Set<CivNation> getNations(ServerLevel level) {
        if (!level.isClientSide()) {
            AtomicReference<Set<CivNation>> listAtomicReference = new AtomicReference<>();
            level.getCapability(CIV_LEVEL_CAP).ifPresent(levelCap -> listAtomicReference.set(levelCap.getNations()));
            return listAtomicReference.get();
        } else {
            return null;
        }
    }

    /**
     * This should only ever be called from the server's end.
     * @param level the <code>ServerLevel</code> this is occurring on. You must ensure that the argument is passed from
     *              exclusively the server.
     * @param civNation    the <code>CivLevelNation</code> instance to query for creation. Since this argument may be passed from
     *                  the client, a security check is done.
     * @return      the result of attempting to create a civLevelNation. <code>true</code> indicates success,
     */
    public static boolean createNation(ServerLevel level, CivNation civNation) {
        if (!level.isClientSide()) {
            if (nationCreationValid(level, civNation)) {
                level.getCapability(CIV_LEVEL_CAP).ifPresent(civ -> {
                    level.players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                            CHAT_HEADER().append(Component.translatable("chat.civilization.nation_founded", civNation.name()).setStyle(Style.EMPTY))
                    ));
                    civ.addNation(civNation);
                });
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This method exists to check, from the server, whether a CivLevelNation is valid to
     * create. Done for security reasons as a malicious client would otherwise be able
     * to submit any CivLevelNation instance to the server for creation.
     * @param level the <code>ServerLevel</code> to check validity against.
     * @param civNation    the <code>CivLevelNation</code> to check.
     * @return  <code>true</code> if the given civLevelNation may be created. <code>false</code>
     *          otherwise.
     */
    public static boolean nationCreationValid(ServerLevel level, CivNation civNation) {
        if (level.isClientSide()) return false;

        Set<CivNation> civNationList = getNations(level);

        if (civNationList == null) return false;

        if (civNationList.contains(civNation)) return false;

        /*return (
                civNationList.stream().noneMatch(nat -> Objects.equals(nat.nationUUID, civNation.nationUUID))
                && civNation.tiles.isEmpty()
                && civNation.players.isEmpty()
                && civNation.cities.isEmpty()
                && civNation.diplomacy == 0
        );*/
        return true;
    }
}
