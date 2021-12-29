package com.greenjon902.betterLogger.commands;

public class CommandCtrlSetLoggerName extends CommandCtrlNameBased {
    public CommandCtrlSetLoggerName(Integer loggerId, String name) {
        super(loggerId, name);
    }

    @Override
    public String getCtrlCode() {
        return "SET_NAME";
    }
}
