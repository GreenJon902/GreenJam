package com.greenjon902.betterLogger.commands;

public abstract class CommandCtrlNameBased extends CommandCtrl {
    public Integer loggerId;
    public String name;

    public CommandCtrlNameBased(Integer loggerId, String name) {
        this.loggerId = loggerId;
        this.name = name;
    }
}
