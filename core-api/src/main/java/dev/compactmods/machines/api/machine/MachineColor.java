package dev.compactmods.machines.api.machine;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;

import java.util.Locale;

public record MachineColor(int red, int green, int blue) {

    public static MachineColor fromARGB(int argb) {
        var red = FastColor.ARGB32.red(argb);
        var green = FastColor.ARGB32.green(argb);
        var blue = FastColor.ARGB32.blue(argb);
        return new MachineColor(red, green, blue);
    }

    public static final Codec<MachineColor> CODEC = Codec.STRING.comapFlatMap(str -> {
        if (!str.startsWith("#")) {
            return DataResult.error(() -> "Not a color code: " + str);
        } else {
            final var stripped = str.substring(1);
            if(stripped.length() != 6)
                return DataResult.error(() -> "Not a color code: " + stripped);

            try {
                int i = (int)Long.parseLong(stripped, 16);
                return DataResult.success(MachineColor.fromARGB(i));
            } catch (NumberFormatException nfe) {
                return DataResult.error(() -> "Exception parsing color code: " + nfe.getMessage());
            }
        }
    }, MachineColor::formatValue);

    public static final StreamCodec<ByteBuf, MachineColor> STREAM_CODEC = ByteBufCodecs.INT.map(MachineColor::fromARGB, MachineColor::rgb);

    public int rgb() {
        return FastColor.ARGB32.color(red, green, blue);
    }

    private String formatValue() {
        return "#" + String.format(Locale.ROOT, "%08X", this.rgb()).substring(2);
    }

}
