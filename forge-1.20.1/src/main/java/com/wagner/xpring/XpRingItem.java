package com.wagner.xpring;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public final class XpRingItem extends Item {
    private static final String STORED_LEVELS_TAG = "StoredLevels";

    public XpRingItem(Properties properties) {
        super(properties);
    }

    public static int getStoredLevels(ItemStack stack) {
        if (!stack.hasTag()) {
            return 0;
        }
        return Math.max(0, stack.getTag().getInt(STORED_LEVELS_TAG));
    }

    public static void setStoredLevels(ItemStack stack, int levels) {
        stack.getOrCreateTag().putInt(STORED_LEVELS_TAG, Math.max(0, levels));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(
                    serverPlayer,
                    new SimpleMenuProvider(
                            (containerId, inventory, ignored) ->
                                    new XpRingMenu(containerId, inventory, hand),
                            Component.translatable("menu.xp_ring.title")
                    ),
                    buffer -> buffer.writeBoolean(hand == InteractionHand.MAIN_HAND)
            );
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return getStoredLevels(stack) > 0;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(Component.translatable(
                "tooltip.xp_ring.stored",
                getStoredLevels(stack)
        ));
    }
}
