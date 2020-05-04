package com.tildehacker.armycontainer.components;

import com.tildehacker.armycontainer.services.LoggerService;

public class LoggerComponent implements LoggerService {
    @Override
    public void log(String line) {
        System.out.println(line);
    }
}
