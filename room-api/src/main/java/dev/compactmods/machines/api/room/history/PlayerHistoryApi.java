package dev.compactmods.machines.api.room.history;

import org.jetbrains.annotations.ApiStatus;

public class PlayerHistoryApi {

   /**
	* Set up when the server or single-player instance changes.
	* NOT for API consumers to use! Use the methods provided here for safety.
	*
	* @since 3.0.0
	*/
   @ApiStatus.Internal
   public static IPlayerHistoryApi INSTANCE;

   public static IPlayerEntryPointHistoryManager historyManager() {
	  return INSTANCE.entryPoints();
   }
}
