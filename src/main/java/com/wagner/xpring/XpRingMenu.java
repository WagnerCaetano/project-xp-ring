package com.wagner.xpring;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;

public final class XpRingMenu extends AbstractContainerMenu {
    private static final int[] AMOUNTS = {1, 5, 10};

    private final Player player;
    private final InteractionHand hand;
    private int syncedStoredLevels;

    public XpRingMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        this(containerId, inventory,
                buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
    }

    public XpRingMenu(int containerId, Inventory inventory, InteractionHand hand) {
        super(XpRingMod.XP_RING_MENU.get(), containerId);
        this.player = inventory.player;
        this.hand = hand;
        this.syncedStoredLevels = XpRingItem.getStoredLevels(getRing());

        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return XpRingItem.getStoredLevels(getRing());
            }

            @Override
            public void set(int value) {
                syncedStoredLevels = value;
            }
        });
    }

    public int getStoredLevels() {
        return player.level().isClientSide
                ? syncedStoredLevels
                : XpRingItem.getStoredLevels(getRing());
    }

    @Override
    public boolean clickMenuButton(Player clickingPlayer, int id) {
        if (id < 0 || id >= AMOUNTS.length * 2 || !stillValid(clickingPlayer)) {
            return false;
        }

        int amount = AMOUNTS[id % AMOUNTS.length];
        ItemStack ring = getRing();
        int stored = XpRingItem.getStoredLevels(ring);

        if (id < AMOUNTS.length) {
            if (clickingPlayer.experienceLevel < amount
                    || stored > Integer.MAX_VALUE - amount) {
                return false;
            }
            clickingPlayer.giveExperienceLevels(-amount);
            XpRingItem.setStoredLevels(ring, stored + amount);
        } else {
            if (stored < amount) {
                return false;
            }
            clickingPlayer.giveExperienceLevels(amount);
            XpRingItem.setStoredLevels(ring, stored - amount);
        }

        broadcastChanges();
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player == this.player && getRing().is(XpRingMod.XP_RING.get());
    }

    private ItemStack getRing() {
        return player.getItemInHand(hand);
    }
}
