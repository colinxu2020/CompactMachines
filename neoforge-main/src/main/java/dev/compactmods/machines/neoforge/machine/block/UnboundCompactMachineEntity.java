package dev.compactmods.machines.neoforge.machine.block;

import dev.compactmods.machines.api.machine.block.IUnboundCompactMachineBlockEntity;
import dev.compactmods.machines.api.room.RoomTemplate;
import dev.compactmods.machines.neoforge.machine.Machines;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UnboundCompactMachineEntity extends BlockEntity implements IUnboundCompactMachineBlockEntity {

    private @Nullable ResourceLocation templateId;

    public UnboundCompactMachineEntity(BlockPos pos, BlockState state) {
        super(Machines.UNBOUND_MACHINE_ENTITY.get(), pos, state);
        this.templateId = null;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(NBT_TEMPLATE_ID))
            this.templateId = new ResourceLocation(nbt.getString(NBT_TEMPLATE_ID));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (templateId != null)
            nbt.putString(NBT_TEMPLATE_ID, templateId.toString());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag data = super.getUpdateTag();
        saveAdditional(data);

        if (templateId != null)
            data.putString(NBT_TEMPLATE_ID, templateId.toString());

        return data;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);

        if (tag.contains(NBT_TEMPLATE_ID))
            templateId = new ResourceLocation(tag.getString(NBT_TEMPLATE_ID));
    }

    public void setTemplate(ResourceLocation template) {
        this.templateId = template;
        this.setChanged();
    }

    @Override
    public int getColor() {
        return this.getData(Machines.MACHINE_COLOR);
    }

    @Nullable
    public ResourceLocation templateId() {
        return templateId;
    }

    public Optional<RoomTemplate> template() {
        assert level != null;
        var t = level.registryAccess()
                .registryOrThrow(RoomTemplate.REGISTRY_KEY)
                .get(templateId);

        return Optional.ofNullable(t);
    }
}
