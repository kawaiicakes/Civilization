package io.github.kawaiicakes.civilization.api.nbt;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.kawaiicakes.civilization.Civilization.CHAT_HEADER;
import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapability.Provider.CIV_LEVEL_CAP;

/**
 * Central hub for easily accessing nation info. Also exists as a security checkpoint for packets received from clients.
 */
public class NationManager {
    public static CivLevelNation NULLARIA = new CivLevelNation(
            UUID.randomUUID(),
            "Nullaria",
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            0
    );

    /**
     * Only use this when absolutely necessary and ensure that it is called on the server thread.
     * Do not cache the return as it may cause memory leaks.
     * @return <code>null</code> if not available. Otherwise, the <code>ServerLevel</code>
     * corresponding to the Overworld on the server.
     */
    public static ServerLevel getOverworld() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            if (server.getLevel(ServerLevel.OVERWORLD) != null) {
                return server.getLevel(ServerLevel.OVERWORLD);
            }
        }
        return null;
    }

    public static Set<CivLevelNation> getNations(ServerLevel level) {
        if (!level.isClientSide()) {
            AtomicReference<Set<CivLevelNation>> listAtomicReference = new AtomicReference<>();
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
     * @param civLevelNation    the <code>CivLevelNation</code> instance to query for creation. Since this argument may be passed from
     *                  the client, a security check is done.
     * @return      the result of attempting to create a civLevelNation. <code>true</code> indicates success,
     */
    public static boolean createNation(ServerLevel level, CivLevelNation civLevelNation) {
        if (!level.isClientSide()) {
            if (nationCreationValid(level, civLevelNation)) {
                level.getCapability(CIV_LEVEL_CAP).ifPresent(civ -> {
                    level.players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                            CHAT_HEADER().append(Component.translatable("chat.civilization.nation_founded", civLevelNation.nationName).setStyle(Style.EMPTY))
                    ));
                    civ.addNation(civLevelNation);
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
     * @param civLevelNation    the <code>CivLevelNation</code> to check.
     * @return  <code>true</code> if the given civLevelNation may be created. <code>false</code>
     *          otherwise.
     */
    public static boolean nationCreationValid(ServerLevel level, CivLevelNation civLevelNation) {
        if (level.isClientSide()) return false;

        Set<CivLevelNation> civLevelNationList = getNations(level);

        if (civLevelNationList == null) return false;

        if (civLevelNationList.contains(civLevelNation)) return false;

        return (
                civLevelNationList.stream().noneMatch(nat -> Objects.equals(nat.nationUUID, civLevelNation.nationUUID))
                && civLevelNation.tiles.isEmpty()
                && civLevelNation.players.isEmpty()
                && civLevelNation.cities.isEmpty()
                && civLevelNation.diplomacy == 0
        );
    }
}