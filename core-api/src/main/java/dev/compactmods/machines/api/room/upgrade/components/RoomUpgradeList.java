package dev.compactmods.machines.api.room.upgrade.components;

import com.mojang.serialization.Codec;
import dev.compactmods.machines.api.room.upgrade.RoomUpgrade;
import dev.compactmods.machines.api.room.upgrade.RoomUpgradeCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a set of room upgrades as stored on an item.
 *
 * @param upgrades
 */
public record RoomUpgradeList(List<RoomUpgrade> upgrades) implements TooltipProvider {

  public static final Codec<RoomUpgradeList> CODEC = RoomUpgradeCodecs.DISPATCH_CODEC.listOf()
      .xmap(RoomUpgradeList::new, RoomUpgradeList::upgrades);

  public static final StreamCodec<RegistryFriendlyByteBuf, RoomUpgradeList> STREAM_CODEC = StreamCodec.composite(
      RoomUpgradeCodecs.STREAM_CODEC.apply(ByteBufCodecs.list()), RoomUpgradeList::upgrades,
      RoomUpgradeList::new
  );

  @Override
  public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltips, TooltipFlag flags) {
    tooltips.accept(Component.empty());

    tooltips.accept(Component.translatableWithFallback("cm.upgrades.list_header", "Room Upgrades:")
        .withColor(CommonColors.GRAY)
        .withStyle(s -> s.withUnderlined(true)));

    Consumer<Component> tooltipsWInsert = (comp) -> {
      var reformatted = Component.literal(" - ")
          .withColor(CommonColors.LIGHT_GRAY)
          .append(comp.copy())
          .withStyle(s -> s.withItalic(true));

      tooltips.accept(reformatted);
    };

    for(var upgrade : upgrades)
      upgrade.addToTooltip(ctx, tooltipsWInsert, flags);
  }
}
