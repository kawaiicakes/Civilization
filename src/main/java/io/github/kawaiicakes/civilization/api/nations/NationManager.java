package io.github.kawaiicakes.civilization.api.nations;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

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
    public static void createNation(ServerLevel level, Nation nation) {
        level.getCapability(CIV_LEVEL_CAP).ifPresent(civ -> {
            civ.addNation(nation);
        });
    }

    /*
     * This method exists to check, from the server, whether a nation is valid to
     * create. Done for security reasons.
     * @param level the <code>ServerLevel</code> to check validity against.
     * @param nation    the <code>Nation</code> to check.
     * @return  <code>true</code> if the given nation may be created. <code>false</code>
     *          otherwise.
     *
    public static boolean nationCreationValid(ServerLevel level, Nation nation) {
        /*
        if (level.isClientSide()) return false;

        AtomicReference<List<Nation>> listAtomicReference = new AtomicReference<>();

        level.getCapability(CIV_LEVEL_CAP).ifPresent(levelCap -> listAtomicReference.set(levelCap.getNations()));

        List<Nation> nationList = listAtomicReference.get();

        nationList.forEach(nat -> {
            if (nat == nation) return false;
        });
    } */
}
