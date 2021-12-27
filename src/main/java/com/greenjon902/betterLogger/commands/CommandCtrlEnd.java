package com.greenjon902.betterLogger.commands;

import org.apache.commons.codec.binary.Base64;

public class CommandCtrlEnd implements Command {
    static final String commandType = CommandTypes.CTRL;
    static final String ctrlCode = "END";

    @Override
    public byte[] encode() {
        return CommandUtils.makeBytes(CommandUtils.padType(commandType) + CommandUtils.padMessage(ctrlCode));
    }
}
