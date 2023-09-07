package io.github.kawaiicakes.civilization.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kawaiicakes.civilization.api.screen.AbstractGUI;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static io.github.kawaiicakes.civilization.Civilization.MOD_ID;

public class MainPlayerScreen extends AbstractGUI {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/menu/main.png");
    private static final Component MAIN_SCREEN = Component.translatable("menu.civilization.main_menu");

    /**
     * The area on the png to draw pixels from when blitting the background
     */
    private static final short bgBlitWidth = 277;
    private static final short bgBlitHeight = 218;
    private static final byte bgBlitXYPos = 0;

    /**
     * Characteristics of tabs in this screen
     */
    private static final short tabWidth = 155;
    private static final byte tabHeight = 28;
    private static final short tabBlitUOffset = 277;
    private static final short tabSelectedBlitVOffset = 104;
    private static final short tabSelectedBottomBlitVOffset = 132; // Renders 'flush' with the background
    private static final short tabUnselectedBlitVOffset = 76;

    public MainPlayerScreen() {
        super(MAIN_SCREEN, 800, 256);
    }

    @Override
    public void init() {
        this.leftPos = (this.width - this.bgWidth) / 2;
        this.topPos = (this.height - this.bgHeight) / 2;

        this.addWidget()
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);

        // Stuff that is rendered before widgets goes here

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        // Tooltips go here
    }
}
