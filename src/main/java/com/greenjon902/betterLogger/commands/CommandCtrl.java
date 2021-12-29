package com.greenjon902.betterLogger.commands;

public abstract class CommandCtrl implements Command {
    static final String commandType = CommandTypes.CTRL;

    @Override
    public byte[] encode() {
        return CommandUtils.makeBytes(CommandUtils.padType(commandType) + CommandUtils.padMessage(getCtrlCode()));
    }

    public abstract String getCtrlCode();
}
