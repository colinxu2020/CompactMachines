package dev.compactmods.machines.data.manager;

import dev.compactmods.machines.data.CMDataFile;
import dev.compactmods.machines.data.CodecHolder;

import java.util.Optional;

public interface IKeyedDataFileManager<Key, T extends CMDataFile & CodecHolder<T>> {
   T data(Key key);

   Optional<T> optionalData(Key key);

   void save();

}

