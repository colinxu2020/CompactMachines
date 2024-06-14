package dev.compactmods.machines;

import dev.compactmods.machines.api.CompactMachines;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingUtil {

    public static Logger modLog() {
        return LogManager.getLogger(CompactMachines.MOD_ID);
    }
}
