/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum Incidence's urgency
 * 
 * @author Jonathan Cacay
 */
public enum Urgency {
    NORMAL(1), URGENT(2);

    private int value;

    private static Map map = new HashMap<>();

    private Urgency(int value) {
	this.value = value;
    }

    static {
	for (Urgency user : Urgency.values()) {
	    map.put(user.value, user);
	}
    }

    public static Urgency valueOf(int user) {
	return (Urgency) map.get(user);
    }

    public int getValue() {
	return value;
    }

}
