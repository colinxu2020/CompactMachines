package dev.compactmods.machines.data.datagen.compat.curios;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class CurioSlotGenerator extends JsonCodecProvider<CurioSlotGenerator.CurioSlot> {

    public record CurioSlot(int size, String operation, ResourceLocation icon) {
        public static Codec<CurioSlot> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.fieldOf("size").forGetter(CurioSlot::size),
                Codec.STRING.fieldOf("operation").forGetter(CurioSlot::operation),
                ResourceLocation.CODEC.fieldOf("icon").forGetter(CurioSlot::icon)
        ).apply(i, CurioSlot::new));

    }

    public CurioSlotGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, PackOutput.Target.DATA_PACK, "curios/slots", PackType.SERVER_DATA, CurioSlot.CODEC, lookupProvider, CompactMachines.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gather() {
        var cmPSDSlot = new CurioSlot(1, "SET", CompactMachines.modRL("slot/empty_psd"));
        unconditional(CompactMachines.modRL("psd"), cmPSDSlot);
    }
}
