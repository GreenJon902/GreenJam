package com.greenjon902.betterLogger.commands;

import com.greenjon902.betterLogger.LogLevel;

public class CommandLog implements Command {
    static final String commandType = CommandTypes.LOG;

    public Integer loggerId;
    public LogLevel level;
    public String message;

    public CommandLog(Integer loggerId, LogLevel logLevel, String message) {
        this.loggerId = loggerId;
        this.level = logLevel;
        this.message = message;
    }

    @Override
    public byte[] encode() {
        String formattedMessage = loggerId + ":" + level.get() + ":" + message;
        return CommandUtils.makeBytes(CommandUtils.padType(commandType) + CommandUtils.padMessage(formattedMessage));
    }
}
