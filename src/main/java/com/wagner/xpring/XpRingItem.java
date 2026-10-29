package com.wagner.xpring;

import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public final class XpRingItem extends BlockItem {
    public XpRingItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static int getStoredLevels(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return XpStorage.sanitize(data.copyTag().getInt(XpStorage.STORED_LEVELS));
    }

    public static void setStoredLevels(ItemStack stack, int levels) {
        int stored = XpStorage.sanitize(levels);
        CustomData.update(DataComponents.CUSTOM_DATA, stack,
                tag -> tag.putInt(XpStorage.STORED_LEVELS, stored));
        CompoundTag blockEntityData = new CompoundTag();
        blockEntityData.putInt(XpStorage.STORED_LEVELS, stored);
        setBlockEntityData(stack, XpRingMod.XP_RING_BLOCK_ENTITY.get(), blockEntityData);
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
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.xp_ring.stored", getStoredLevels(stack)));
        tooltip.add(Component.translatable("tooltip.xp_ring.place"));
    }
}
