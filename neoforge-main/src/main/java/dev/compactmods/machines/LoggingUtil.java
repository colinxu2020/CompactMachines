package dev.compactmods.machines;

import dev.compactmods.machines.api.CompactMachinesApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingUtil {

    public static Logger modLog() {
        return LogManager.getLogger(CompactMachinesApi.MOD_ID);
    }
}
