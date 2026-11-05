package model.world.components;

import java.util.Arrays;

public enum Theme {
    DEFAULT,PEACETOWN,GREENVILLAGE;
    public static Theme fromString(String name) {
        return Arrays.stream(Theme.values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No THEME constant with name: " + name));
    }
}
