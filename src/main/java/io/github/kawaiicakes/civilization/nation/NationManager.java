package io.github.kawaiicakes.civilization.nation;

import io.github.kawaiicakes.civilization.api.utils.NamedUUID;
import io.github.kawaiicakes.civilization.api.utils.json.JsonIO;

import java.util.UUID;

public class NationManager {

    // TODO: implement. also consider alternate ways to return a named UUID only from a UUID. A name must be stored w/ its UUID somewhere...
    // this roundabout way of doing it is so that I *know* that this is something intended to be handled by the NationManager
    public static NamedUUID fromUUID(UUID uuid) {
        return JsonIO.getNamedUUID(uuid);
    }
}
