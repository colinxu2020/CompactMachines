package dev.compactmods.machines.neoforge.room.capability;

import dev.compactmods.machines.api.room.RoomInstance;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.CapabilityRegistry;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class RoomCapability<Cap, MachCtx, Ctx> extends BaseCapability<Cap, Ctx> {

    private static final CapabilityRegistry<RoomCapability<?,?>> registry = new CapabilityRegistry<>(RoomCapability::new);
    final Map<String, List<ICapabilityProvider<RoomInstance, MachCtx, Cap>>> providers = new IdentityHashMap<>();

    protected RoomCapability(ResourceLocation name, Class<Cap> typeClass, Class<MachCtx> contextClass) {
        super(name, typeClass, contextClass);
    }

    private static <T, C> RoomCapability<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return (RoomCapability<T, C>) registry.create(name, typeClass, contextClass);
    }

    public static <T> RoomCapability<T, Void> createVoid(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, void.class);
    }

    public static <T> RoomCapability<T, GlobalPos> createMachineUnsided(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, GlobalPos.class);
    }

    public static <T> RoomCapability<T, GlobalPos> createMachineSided(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, GlobalPos.class);
    }

    @ApiStatus.Internal
    @Nullable
    public Cap getCapability(RoomInstance room, MachCtx context) {
        for (var provider : providers.getOrDefault(room.code(), List.of())) {
            var ret = provider.getCapability(room, context);
            if (ret != null)
                return ret;
        }
        return null;
    }
}
