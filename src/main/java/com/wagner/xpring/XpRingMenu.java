package com.wagner.xpring;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;

public final class XpRingMenu extends AbstractContainerMenu {
    private static final int[] AMOUNTS = {1, 5, 10};

    private final Player player;
    private final InteractionHand hand;
    private final BlockPos pos;
    private int syncedStoredLevels;

    public XpRingMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buffer) {
        this(containerId, inventory, Source.read(buffer));
    }

    public XpRingMenu(int containerId, Inventory inventory, InteractionHand hand) {
        this(containerId, inventory, new Source(hand, null));
    }

    public XpRingMenu(int containerId, Inventory inventory, BlockPos pos) {
        this(containerId, inventory, new Source(null, pos));
    }

    private XpRingMenu(int containerId, Inventory inventory, Source source) {
        super(XpRingMod.XP_RING_MENU.get(), containerId);
        this.player = inventory.player;
        this.hand = source.hand;
        this.pos = source.pos;
        this.syncedStoredLevels = readStoredLevels();
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getStoredLevels() & 0xFFFF;
            }

            @Override
            public void set(int value) {
                syncedStoredLevels = (syncedStoredLevels & 0xFFFF0000) | (value & 0xFFFF);
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return (getStoredLevels() >>> 16) & 0xFFFF;
            }

            @Override
            public void set(int value) {
                syncedStoredLevels = (syncedStoredLevels & 0xFFFF) | ((value & 0xFFFF) << 16);
            }
        });
    }

    public static void open(Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(
                    new SimpleMenuProvider(
                            (containerId, inventory, ignored) -> new XpRingMenu(containerId, inventory, hand),
                            Component.translatable("menu.xp_ring.title")
                    ),
                    buffer -> {
                        buffer.writeBoolean(false);
                        buffer.writeBoolean(hand == InteractionHand.MAIN_HAND);
                    }
            );
        }
    }

    public static void open(Player player, BlockPos pos) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(
                    new SimpleMenuProvider(
                            (containerId, inventory, ignored) -> new XpRingMenu(containerId, inventory, pos),
                            Component.translatable("menu.xp_ring.title")
                    ),
                    buffer -> {
                        buffer.writeBoolean(true);
                        buffer.writeBlockPos(pos);
                    }
            );
        }
    }

    public int getStoredLevels() {
        return player.level().isClientSide ? syncedStoredLevels : readStoredLevels();
    }

    @Override
    public boolean clickMenuButton(Player clickingPlayer, int id) {
        if (id < 0 || id >= AMOUNTS.length * 2 || !stillValid(clickingPlayer)) {
            return false;
        }

        int amount = AMOUNTS[id % AMOUNTS.length];
        int stored = readStoredLevels();
        if (id < AMOUNTS.length) {
            if (clickingPlayer.experienceLevel < amount || stored > Integer.MAX_VALUE - amount) {
                return false;
            }
            clickingPlayer.giveExperienceLevels(-amount);
            writeStoredLevels(stored + amount);
        } else {
            if (stored < amount) {
                return false;
            }
            clickingPlayer.giveExperienceLevels(amount);
            writeStoredLevels(stored - amount);
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
        if (player != this.player) {
            return false;
        }
        if (pos != null) {
            return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0
                    && player.level().getBlockState(pos).is(XpRingMod.XP_RING_BLOCK.get());
        }
        return getRing().is(XpRingMod.XP_RING.get());
    }

    private int readStoredLevels() {
        if (pos != null && player.level().getBlockEntity(pos) instanceof XpRingBlockEntity ring) {
            return ring.getStoredLevels();
        }
        return hand == null ? 0 : XpRingItem.getStoredLevels(getRing());
    }

    private void writeStoredLevels(int levels) {
        if (pos != null && player.level().getBlockEntity(pos) instanceof XpRingBlockEntity ring) {
            ring.setStoredLevels(levels);
            return;
        }
        if (hand != null) {
            XpRingItem.setStoredLevels(getRing(), levels);
        }
    }

    private ItemStack getRing() {
        return player.getItemInHand(hand);
    }

    private record Source(InteractionHand hand, BlockPos pos) {
        private static Source read(RegistryFriendlyByteBuf buffer) {
            if (buffer.readBoolean()) {
                return new Source(null, buffer.readBlockPos());
            }
            return new Source(
                    buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND,
                    null
            );
        }
    }
}
