/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Event's type
 *
 * @author User
 */
public enum EventType {
    I(1), C(2), U(3);

    private int value;

    private static Map map = new HashMap<>();

    private EventType(int value) {
        this.value = value;
    }

    static {
        for (EventType event : EventType.values()) {
            map.put(event.value, event);
        }
    }

    public static EventType valueOf(int user) {
        return (EventType) map.get(user);
    }

    public int getValue() {
        return value;
    }

}
