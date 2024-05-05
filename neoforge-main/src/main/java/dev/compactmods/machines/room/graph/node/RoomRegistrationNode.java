package dev.compactmods.machines.room.graph.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.compactmods.machines.api.room.RoomInstance;
import dev.compactmods.machines.api.room.spatial.IRoomBoundaries;
import dev.compactmods.feather.node.Node;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * Hosts core information about a machine room, such as how large it is and its code.
 */
public record RoomRegistrationNode(UUID id, Data data) implements Node<RoomRegistrationNode.Data>, IRoomBoundaries {

    public static final Codec<RoomRegistrationNode> CODEC = RecordCodecBuilder.create(i -> i.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(RoomRegistrationNode::id),
            Data.CODEC.fieldOf("data").forGetter(RoomRegistrationNode::data)
    ).apply(i, RoomRegistrationNode::new));

    public record Data(String code, int defaultMachineColor, AABB boundaries) {
        public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("code").forGetter(Data::code),
                Codec.INT.fieldOf("color").forGetter(Data::defaultMachineColor),
                Vec3.CODEC.fieldOf("dimensions").forGetter(Data::dimensions),
                Vec3.CODEC.fieldOf("center").forGetter(x -> x.boundaries.getCenter())
        ).apply(i, Data::new));

        public Data(RoomInstance inst) {
            this(inst.code(), inst.defaultMachineColor(), inst.boundaries().outerBounds());
        }

        private Vec3 dimensions() {
            return new Vec3(boundaries.getXsize(), boundaries.getYsize(), boundaries.getZsize());
        }

        private Data(String code, int defaultMachineColor, Vec3 dimensions, Vec3 center) {
            this(code, defaultMachineColor, AABB.ofSize(center, dimensions.x(), dimensions.y(), dimensions.z()));
        }
    }



    public String code() {
        return data.code;
    }

    public int defaultMachineColor() {
        return data.defaultMachineColor;
    }

    @Override
    public AABB outerBounds() {
        return data.boundaries;
    }
}
