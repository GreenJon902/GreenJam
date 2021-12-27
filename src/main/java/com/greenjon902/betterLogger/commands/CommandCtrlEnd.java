package com.greenjon902.betterLogger.commands;

import org.apache.commons.codec.binary.Base64;

public class CommandCtrlEnd implements Command {
    static final String commandType = CommandTypes.CTRL;
    static final String ctrlCode = "END";

    @Override
    public byte[] encode() {
        return Base64.encodeBase64(
                (CommandUtils.padType(commandType) + CommandUtils.padMessage(ctrlCode)).getBytes()
        );
    }
}
