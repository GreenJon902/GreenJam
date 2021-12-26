package com.greenjon902.betterLogger.commands;

import com.greenjon902.betterLogger.BetterLoggerCommunicator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CommandCtrlEnd implements Command {
    static final String commandType = CommandTypes.CTRL;
    static final String ctrlCode = "END";

    @Override
    public byte[] encode() {
        ByteBuffer encoded = (
                StandardCharsets.UTF_8.encode(
                        String.format("%1$" + BetterLoggerCommunicator.pythonConn_typeLength + "s",
                                commandType.length()).replace(" ", "0") + commandType +
                                String.format("%1$" + BetterLoggerCommunicator.pythonConn_messageLength + "s",
                                        ctrlCode.length()).replace(" ", "0") + ctrlCode));
        return encoded.array();
    }
}
