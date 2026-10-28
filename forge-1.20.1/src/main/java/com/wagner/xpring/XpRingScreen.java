package com.wagner.xpring;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class XpRingScreen extends AbstractContainerScreen<XpRingMenu> {
    private static final int[] AMOUNTS = {1, 5, 10};
    private final Button[] saveButtons = new Button[AMOUNTS.length];
    private final Button[] extractButtons = new Button[AMOUNTS.length];

    public XpRingScreen(XpRingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 230;
        imageHeight = 146;
        titleLabelX = 10;
        titleLabelY = 9;
        inventoryLabelY = 10_000;
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < AMOUNTS.length; i++) {
            int x = leftPos + 10 + i * 72;
            int amount = AMOUNTS[i];
            int saveId = i;
            int extractId = i + AMOUNTS.length;

            saveButtons[i] = addRenderableWidget(Button.builder(
                    Component.translatable("button.xp_ring.save", amount),
                    button -> pressButton(saveId)
            ).bounds(x, topPos + 58, 66, 20).build());

            extractButtons[i] = addRenderableWidget(Button.builder(
                    Component.translatable("button.xp_ring.extract", amount),
                    button -> pressButton(extractId)
            ).bounds(x, topPos + 99, 66, 20).build());
        }
        updateButtons();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons();
    }

    private void updateButtons() {
        if (minecraft == null || minecraft.player == null) {
            return;
        }
        int stored = menu.getStoredLevels();
        for (int i = 0; i < AMOUNTS.length; i++) {
            saveButtons[i].active = minecraft.player.experienceLevel >= AMOUNTS[i]
                    && stored <= Integer.MAX_VALUE - AMOUNTS[i];
            extractButtons[i].active = stored >= AMOUNTS[i];
        }
    }

    private void pressButton(int id) {
        if (minecraft != null && minecraft.gameMode != null) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, id);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF171B20);
        graphics.fill(leftPos + 2, topPos + 2, leftPos + imageWidth - 2, topPos + imageHeight - 2, 0xFF303740);
        graphics.fill(leftPos + 8, topPos + 27, leftPos + imageWidth - 8, topPos + 51, 0xFF20262D);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        graphics.drawCenteredString(
                font,
                Component.translatable("menu.xp_ring.stored", menu.getStoredLevels()),
                imageWidth / 2,
                35,
                0x80FF80
        );
        graphics.drawString(font, Component.translatable("menu.xp_ring.save"), 10, 47, 0xFFFFFF, false);
        graphics.drawString(font, Component.translatable("menu.xp_ring.extract"), 10, 88, 0xFFFFFF, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
