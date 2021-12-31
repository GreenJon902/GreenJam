package com.greenjon902.betterLogger.commands;

public class CommandCtrlNewLogger extends CommandCtrlNameBased {
    public CommandCtrlNewLogger(Integer loggerId, String name) {
        super(loggerId, name);
    }

    @Override
    public String getCtrlCode() {
        return "NEW_LOGGER";
    }
}
