package dev.compactmods.machines.api.room;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.Constants;
import dev.compactmods.machines.api.machine.MachineTranslations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Template structure for creating a new Compact Machine room. These can be added and removed from the registry
 * at any point, so persistent data must be stored outside these instances.
 *
 * @param internalDimensions    The internal dimensions of the room when it is created.
 * @param color                 The color of the machine blocks created for this template.
 * @param structures            Information used to fill a newly-created room with structures.
 */
public record RoomTemplate(RoomDimensions internalDimensions, int color, List<RoomStructureInfo> structures)
    implements TooltipProvider {

    public static final ResourceKey<Registry<RoomTemplate>> REGISTRY_KEY = ResourceKey.createRegistryKey(Constants.modRL("room_templates"));

    public static final ResourceLocation NO_TEMPLATE = Constants.modRL("empty");

    public static final RoomTemplate INVALID_TEMPLATE = new RoomTemplate(0, 0);

    public static Codec<RoomTemplate> CODEC = RecordCodecBuilder.create(i -> i.group(
            RoomDimensions.CODEC.fieldOf("dimensions").forGetter(RoomTemplate::internalDimensions),
            Codec.INT.fieldOf("color").forGetter(RoomTemplate::color),
            RoomStructureInfo.CODEC.listOf().optionalFieldOf("structures", Collections.emptyList())
                    .forGetter(RoomTemplate::structures)
    ).apply(i, RoomTemplate::new));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, RoomTemplate> STREAM_CODEC = StreamCodec.composite(
            RoomDimensions.STREAM_CODEC, RoomTemplate::internalDimensions,
            ByteBufCodecs.INT, RoomTemplate::color,
            RoomStructureInfo.STREAM_CODEC.apply(ByteBufCodecs.list()), RoomTemplate::structures,
            RoomTemplate::new
    );

    public RoomTemplate(int cubicSizeInternal, int color) {
        this(RoomDimensions.cubic(cubicSizeInternal), color, Collections.emptyList());
    }

    public AABB getZeroBoundaries() {
        return AABB.ofSize(Vec3.ZERO, internalDimensions.width(), internalDimensions.height(), internalDimensions.depth())
                .inflate(1)
                .move(0, internalDimensions.height() / 2f, 0);
    }

    public AABB getBoundariesCenteredAt(Vec3 center) {
        return AABB.ofSize(center, internalDimensions.width(), internalDimensions.height(), internalDimensions.depth())
                .inflate(1);
    }

    @Override
    public void addToTooltip(Item.TooltipContext ctx, Consumer<Component> tooltips, TooltipFlag flags) {
        final var roomDimensions = internalDimensions();
        tooltips.accept(Component.translatableWithFallback(MachineTranslations.IDs.SIZE, "Size: %s", roomDimensions).withStyle(ChatFormatting.YELLOW));

        if (!structures().isEmpty()) {
            tooltips.accept(Component.literal("Generates " + structures().size() + " structures after creation.").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
