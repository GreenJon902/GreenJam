package com.greenjon902.betterLogger.commands;

public class CommandCtrlPopLoggerName extends CommandCtrlNameBased {
    public CommandCtrlPopLoggerName(Integer loggerId, String name) {
        super(loggerId, name);
    }

    @Override
    public String getCtrlCode() {
        return "POP_NAME";
    }
}
