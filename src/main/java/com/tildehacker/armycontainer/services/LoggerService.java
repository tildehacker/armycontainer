package com.tildehacker.armycontainer.services;

import com.tildehacker.armycontainer.annotations.Component;
import com.tildehacker.armycontainer.components.LoggerComponent;

@Component(defaultImplementation = LoggerComponent.class)
public interface LoggerService {
    void log(String line);
}
