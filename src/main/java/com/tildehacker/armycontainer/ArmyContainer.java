package com.tildehacker.armycontainer;

import com.tildehacker.armycontainer.annotations.Autowired;
import com.tildehacker.armycontainer.annotations.Component;
import com.tildehacker.armycontainer.interfaces.Runnable;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ArmyContainer {
    private Set<Class> managedServices;
    private Map<Class, Object> managedComponents;

    private ArmyContainer() {
        managedServices = new HashSet<>();
        managedComponents = new HashMap<>();
    }

    public void start() {
        for (Class service : managedServices) {
            if (Runnable.class.isAssignableFrom(service)) {
                ((Runnable) managedComponents.get(service)).run();
            }
        }
    }

    public static class ContainerBuilder {
        private ArmyContainer container;

        private ContainerBuilder() {
            container = new ArmyContainer();
        }

        public static ContainerBuilder initialize() {
            return new ContainerBuilder();
        }

        private ContainerBuilder findServices() {
            Reflections reflections = new Reflections("com.tildehacker.armycontainer");
            Set<Class<?>> services = reflections.getTypesAnnotatedWith(Component.class);

            for (Class service : services) {
                if (service.isInterface()) container.managedServices.add(service);
            }

            return this;
        }

        private ContainerBuilder constructComponents() throws Exception {
            for (Class service : container.managedServices) {
                Component annotation = (Component) service.getAnnotation(Component.class);
                Class componentClass = annotation.defaultImplementation();
                Constructor<?> componentConstructor = componentClass.getConstructor();
                container.managedComponents.put(service, componentConstructor.newInstance());
            }

            return this;
        }

        private ContainerBuilder wireComponents() throws Exception {
            Reflections reflections = new Reflections("com.tildehacker.armycontainer", new FieldAnnotationsScanner());
            Set<Field> fields = reflections.getFieldsAnnotatedWith(Autowired.class);

            for (Field field : fields) {
                Class injectInService = null;
                for (Class implemented : field.getDeclaringClass().getInterfaces()) {
                    if (container.managedServices.contains(implemented)) {
                        injectInService = implemented;
                        break;
                    }
                }

                if (injectInService == null) {
                    continue;
                }

                Class toInjectService = field.getType();
                if (!container.managedServices.contains(toInjectService)) {
                    continue;
                }

                Object injectInComponent = container.managedComponents.get(injectInService);
                Object toInjectComponent = container.managedComponents.get(toInjectService);
                field.setAccessible(true);
                field.set(injectInComponent, toInjectComponent);
            }

            return this;
        }

        public ArmyContainer build() throws Exception {
            findServices()
                    .constructComponents()
                    .wireComponents();

            return container;
        }
    }
}
