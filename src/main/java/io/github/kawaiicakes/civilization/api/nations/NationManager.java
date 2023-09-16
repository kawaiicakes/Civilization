package io.github.kawaiicakes.civilization.api.nations;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapabilityProvider.CIV_LEVEL_CAP;

/**
 * Central hub for easily accessing nation info. Also exists as a security checkpoint for packets received from clients.
 */
public class NationManager {
    public static LevelNation NULLARIA = new LevelNation(
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

    public static List<LevelNation> getNations(ServerLevel level) {
        if (!level.isClientSide()) {
            AtomicReference<List<LevelNation>> listAtomicReference = new AtomicReference<>();
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
     * @param levelNation    the <code>LevelNation</code> instance to query for creation. Since this argument may be passed from
     *                  the client, a security check is done.
     * @return      the result of attempting to create a levelNation. <code>true</code> indicates success,
     */
    public static boolean createNation(ServerLevel level, LevelNation levelNation) {
        if (!level.isClientSide()) {
            if (nationCreationValid(level, levelNation)) {
                level.getCapability(CIV_LEVEL_CAP).ifPresent(civ -> {
                    level.players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                            Component.literal("Successfully passed validity test!")));
                    civ.addNation(levelNation);
                });
                return true;
            } else {
                level.players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                        Component.literal("Invalid levelNation!")));
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This method exists to check, from the server, whether a levelNation is valid to
     * create. Done for security reasons as a malicious client would otherwise be able
     * to submit any LevelNation instance to the server for creation.
     * @param level the <code>ServerLevel</code> to check validity against.
     * @param levelNation    the <code>LevelNation</code> to check.
     * @return  <code>true</code> if the given levelNation may be created. <code>false</code>
     *          otherwise.
     */
    public static boolean nationCreationValid(ServerLevel level, LevelNation levelNation) {
        if (level.isClientSide()) return false;

        List<LevelNation> levelNationList = getNations(level);
        if (levelNationList == null) return false;
        final Stream<LevelNation> nationStream = levelNationList.stream();

        if (levelNationList.contains(levelNation)) return false;

        return (
                nationStream.noneMatch(nat -> nat.nationUUID() == levelNation.nationUUID())
                && levelNation.territory().isEmpty()
                && levelNation.players().isEmpty()
                && levelNation.cities().isEmpty()
                && levelNation.diplomacy() == 0
        );
    }
}
