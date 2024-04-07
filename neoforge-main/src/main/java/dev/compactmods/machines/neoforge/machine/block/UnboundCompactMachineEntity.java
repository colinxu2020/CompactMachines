package dev.compactmods.machines.neoforge.machine.block;

import dev.compactmods.machines.api.machine.block.ICompactMachineBlockEntity;
import dev.compactmods.machines.api.machine.block.IUnboundCompactMachineBlockEntity;
import dev.compactmods.machines.api.room.RoomApi;
import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.api.machine.IColoredMachine;
import dev.compactmods.machines.neoforge.machine.Machines;
import dev.compactmods.machines.neoforge.network.machine.UnboundMachineTemplateSyncPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UnboundCompactMachineEntity extends BlockEntity implements IUnboundCompactMachineBlockEntity, IColoredMachine {

    private RoomTemplate template;
    private @Nullable ResourceLocation templateId;

    public UnboundCompactMachineEntity(BlockPos pos, BlockState state) {
        super(Machines.UNBOUND_MACHINE_ENTITY.get(), pos, state);
        this.template = RoomTemplate.INVALID_TEMPLATE;
        this.templateId = null;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        if(nbt.contains(NBT_TEMPLATE_ID)) {
            this.templateId = new ResourceLocation(nbt.getString(NBT_TEMPLATE_ID));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if(this.level != null && this.templateId != null) {
            this.template = level.registryAccess()
                    .registryOrThrow(RoomTemplate.REGISTRY_KEY)
                    .get(this.templateId);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        if (templateId != null)
            nbt.putString(NBT_TEMPLATE_ID, templateId.toString());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag data = super.getUpdateTag();

        if(templateId != null) {
            var encodedTemplate = RoomTemplate.CODEC.encodeStart(NbtOps.INSTANCE, this.template)
                    .getOrThrow(false, err -> {
                    });

            data.putString(NBT_TEMPLATE_ID, templateId.toString());
            data.put("template", encodedTemplate);
        }

        return data;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);

        if (tag.contains(NBT_TEMPLATE_ID)) {
            templateId = new ResourceLocation(tag.getString(NBT_TEMPLATE_ID));

            this.template = RoomTemplate.CODEC
                    .parse(NbtOps.INSTANCE, tag.get("template"))
                    .getOrThrow(false, err -> {});
        }
    }

    public void setTemplate(ResourceLocation template) {
        this.templateId = template;
        this.template = level.registryAccess()
                .registryOrThrow(RoomTemplate.REGISTRY_KEY)
                .get(template);

        if(!level.isClientSide) {
            PacketDistributor.TRACKING_CHUNK.with(level.getChunkAt(worldPosition))
                    .send(new UnboundMachineTemplateSyncPacket(GlobalPos.of(level.dimension(), worldPosition), this.templateId));
        }

        this.setChanged();
    }

    public Optional<RoomTemplate> template() {
        return Optional.ofNullable(template);
    }

    @Override
    public int getColor() {
        return this.template().map(RoomTemplate::color).orElse(0xFFFF0000);
    }

    @Nullable
    public ResourceLocation templateId() {
        return templateId;
    }
}
