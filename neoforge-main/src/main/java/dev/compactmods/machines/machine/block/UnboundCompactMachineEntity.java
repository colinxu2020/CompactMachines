package dev.compactmods.machines.machine.block;

import dev.compactmods.machines.api.machine.block.IUnboundCompactMachineBlockEntity;
import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.machine.Machines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UnboundCompactMachineEntity extends BlockEntity implements IUnboundCompactMachineBlockEntity {

    private @Nullable ResourceLocation templateId;

    public UnboundCompactMachineEntity(BlockPos pos, BlockState state) {
        super(Machines.BlockEntities.UNBOUND_MACHINE.get(), pos, state);
        this.templateId = null;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider holders) {
        super.loadAdditional(nbt, holders);
        if (nbt.contains(NBT_TEMPLATE_ID))
            this.templateId = new ResourceLocation(nbt.getString(NBT_TEMPLATE_ID));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider holders) {
        super.saveAdditional(nbt, holders);
        if (templateId != null)
            nbt.putString(NBT_TEMPLATE_ID, templateId.toString());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider holders) {
        CompoundTag data = super.getUpdateTag(holders);
        saveAdditional(data, holders);

        if (templateId != null)
            data.putString(NBT_TEMPLATE_ID, templateId.toString());

        return data;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);

        if (tag.contains(NBT_TEMPLATE_ID))
            templateId = new ResourceLocation(tag.getString(NBT_TEMPLATE_ID));
    }

    public void setTemplate(ResourceLocation template) {
        this.templateId = template;
        this.setChanged();
    }

    @Nullable
    public ResourceLocation templateId() {
        return templateId;
    }

    public Optional<RoomTemplate> template() {
        assert level != null;
        var t = this.components().getOrDefault(Machines.DataComponents.ROOM_TEMPLATE.get(), RoomTemplate.INVALID_TEMPLATE);
        return Optional.ofNullable(t);
    }
}
