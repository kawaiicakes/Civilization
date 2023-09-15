package io.github.kawaiicakes.civilization.api.nations;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static io.github.kawaiicakes.civilization.capabilities.CivLevelCapabilityProvider.CIV_LEVEL_CAP;

/**
 * Central hub for easily accessing nation info.
 */
public class NationManager {
    public static Nation NULLARIA = new Nation(
            UUID.randomUUID(),
            "Nullaria",
            NonNullList.create(),
            NonNullList.create(),
            NonNullList.create(),
            0
    );

    /**
     * This should only ever be called from the server's end.
     * @param level the <code>ServerLevel</code> this is occurring on. You must ensure that the argument is passed from
     *              exclusively the server.
     * @param nation    the <code>Nation</code> instance to query for creation. Since this argument may be passed from
     *                  the client, a security check is done.
     * @return      the result of attempting to create a nation. <code>true</code> indicates success,
     */
    public static boolean createNation(ServerLevel level, Nation nation) {
        if (!level.isClientSide()) {
            if (nationCreationValid(level, nation)) {
                level.getCapability(CIV_LEVEL_CAP).ifPresent(civ -> {
                    level.players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                            Component.literal("Successfully passed validity test!")));
                    civ.addNation(nation);
                });
                return true;
            } else {
                level.players().forEach(serverPlayer -> serverPlayer.sendSystemMessage(
                        Component.literal("Invalid nation!")));
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This method exists to check, from the server, whether a nation is valid to
     * create. Done for security reasons as a malicious client would otherwise be able
     * to submit any Nation instance to the server for creation.
     * @param level the <code>ServerLevel</code> to check validity against.
     * @param nation    the <code>Nation</code> to check.
     * @return  <code>true</code> if the given nation may be created. <code>false</code>
     *          otherwise.
     */
    public static boolean nationCreationValid(ServerLevel level, Nation nation) {
        if (level.isClientSide()) return false;

        AtomicReference<List<Nation>> listAtomicReference = new AtomicReference<>();
        level.getCapability(CIV_LEVEL_CAP).ifPresent(levelCap -> listAtomicReference.set(levelCap.getNations()));
        final List<Nation> nationList = listAtomicReference.get();
        final Stream<Nation> nationStream = nationList.stream();

        if (nationList.contains(nation)) return false;

        return (
                nationStream.noneMatch(nat -> nat.nationUUID() == nation.nationUUID())
                && nation.territory().isEmpty()
                && nation.players().isEmpty()
                && nation.cities().isEmpty()
                && nation.diplomacy() == 0
        );
    }
}
