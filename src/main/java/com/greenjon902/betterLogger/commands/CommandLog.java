package com.greenjon902.betterLogger.commands;

import com.greenjon902.betterLogger.BetterLoggerCommunicator;
import com.greenjon902.betterLogger.LogLevel;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CommandLog implements Command {
    static final String commandType = CommandTypes.LOG;
    LogLevel level;

    public CommandLog(LogLevel level, String message) {
        this.level = level;
        this.message = message;
    }

    String message;

    @Override
    public byte[] encode() {
        String formattedMessage = level.get() + ":" + message;
        ByteBuffer encoded = StandardCharsets.UTF_8.encode(
                CommandUtils.padType(commandType) + CommandUtils.padMessage(formattedMessage)
        );
        return encoded.array();
    }
}
