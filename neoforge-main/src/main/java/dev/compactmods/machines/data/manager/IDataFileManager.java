package dev.compactmods.machines.data.manager;

import dev.compactmods.machines.data.CMDataFile;
import dev.compactmods.machines.data.CodecHolder;

public interface IDataFileManager<T extends CMDataFile & CodecHolder<T>> {
   T data();

   void save();
}
