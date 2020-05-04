package com.tildehacker.armycontainer.components;

import com.tildehacker.armycontainer.annotations.Autowired;
import com.tildehacker.armycontainer.services.HelloWorldService;
import com.tildehacker.armycontainer.services.LoggerService;

public class HelloWorldComponent implements HelloWorldService {
    @Autowired
    private LoggerService logger;

    @Override
    public void run() {
        logger.log("Hello world!");
    }
}
