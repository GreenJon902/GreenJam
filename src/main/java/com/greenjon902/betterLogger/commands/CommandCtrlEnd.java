package com.greenjon902.betterLogger.commands;

import com.greenjon902.betterLogger.BetterLoggerCommunicator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CommandCtrlEnd implements Command {
    static final String commandType = CommandTypes.CTRL;
    static final String ctrlCode = "END";

    @Override
    public byte[] encode() {
        ByteBuffer encoded = StandardCharsets.UTF_8.encode(
                CommandUtils.padType(commandType) + CommandUtils.padMessage(ctrlCode)
        );
        return encoded.array();
    }
}
