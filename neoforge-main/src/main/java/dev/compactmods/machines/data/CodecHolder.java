package dev.compactmods.machines.data;

import com.mojang.serialization.Codec;

public interface CodecHolder<T> {

   Codec<T> codec();
}
