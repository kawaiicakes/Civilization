package io.github.kawaiicakes.civilization.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final String KEY_CATEGORY_CIV = "key.category.civilization.keys";
    public static final String KEY_CIV_MENU = "key.category.civilization.keys.civ_menu";

    public static final KeyMapping CIV_MENU = new KeyMapping(KEY_CIV_MENU, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, KEY_CATEGORY_CIV);
}
