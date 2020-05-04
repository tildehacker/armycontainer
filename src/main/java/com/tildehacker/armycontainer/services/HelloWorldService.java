package com.tildehacker.armycontainer.services;

import com.tildehacker.armycontainer.annotations.Component;
import com.tildehacker.armycontainer.components.HelloWorldComponent;
import com.tildehacker.armycontainer.interfaces.Runnable;

@Component(defaultImplementation = HelloWorldComponent.class)
public interface HelloWorldService extends Runnable {
}
