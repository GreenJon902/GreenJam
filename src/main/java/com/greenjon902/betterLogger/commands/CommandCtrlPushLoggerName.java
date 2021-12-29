package com.greenjon902.betterLogger.commands;

public class CommandCtrlPushLoggerName extends CommandCtrlNameBased {
    public CommandCtrlPushLoggerName(Integer loggerId, String name) {
        super(loggerId, name);
    }

    @Override
    public String getCtrlCode() {
        return "PUSH_NAME";
    }
}
