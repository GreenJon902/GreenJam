package com.greenjon902.betterLogger.commands;

public class CommandCtrlPopLoggerName extends CommandCtrl {
    private final Integer loggerId;

    public CommandCtrlPopLoggerName(Integer loggerId) {
        this.loggerId = loggerId;
    }

    @Override
    public byte[] encode() {
        return CommandUtils.makeBytes(CommandUtils.padType(commandType) +
                CommandUtils.padMessage(getCtrlCode() + ":" + loggerId));
    }

    @Override
    public String getCtrlCode() {
        return "POP_NAME";
    }
}
