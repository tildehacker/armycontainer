package com.tildehacker.armycontainer;

public class Main {
    public static void main(String[] args) throws Exception {
        ArmyContainer container = ArmyContainer.ContainerBuilder
                .initialize()
                .build();

        container.start();
    }
}
