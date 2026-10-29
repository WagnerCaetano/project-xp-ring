package com.wagner.xpring;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class XpRingItem extends BlockItem {
    public XpRingItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static int getStoredLevels(ItemStack stack) {
        if (!stack.hasTag()) {
            return 0;
        }
        return XpStorage.sanitize(stack.getTag().getInt(XpStorage.STORED_LEVELS));
    }

    public static void setStoredLevels(ItemStack stack, int levels) {
        int stored = XpStorage.sanitize(levels);
        stack.getOrCreateTag().putInt(XpStorage.STORED_LEVELS, stored);
        stack.getOrCreateTagElement("BlockEntityTag").putInt(XpStorage.STORED_LEVELS, stored);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && player.isSecondaryUseActive()) {
            return super.useOn(context);
        }
        if (player == null) {
            return InteractionResult.PASS;
        }
        return use(context.getLevel(), player, context.getHand()).getResult();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            XpRingMenu.open(player, hand);
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
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.xp_ring.stored", getStoredLevels(stack)));
        tooltip.add(Component.translatable("tooltip.xp_ring.place"));
    }
}
