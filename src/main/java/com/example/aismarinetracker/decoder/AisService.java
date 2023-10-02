package com.example.aismarinetracker.decoder;

import com.example.aismarinetracker.decoder.reports.AisMessage;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AisService {

    public Map<String, String> getMap(AisMessage aisMessage) {
        var methods = aisMessage.getClass().getMethods();
        var map = new TreeMap<String, String>();
        Arrays.stream(methods)
                .filter(e -> e.getName().startsWith("get") || e.getName().startsWith("is"))
                .filter(e -> !e.getName().equals("getClass"))
                .filter(e -> e.getParameterCount() == 0)
                .forEach(e -> {
                    var strippedName = e.getName();
                    if (e.getName().startsWith("get")) {
                        strippedName = strippedName.substring(3);
                    } else if (e.getName().startsWith("is")) {
                        strippedName = strippedName.substring(2);
                    }
                    try {
                        map.put(strippedName, e.invoke(aisMessage).toString());
                    } catch (IllegalAccessException illegalAccessException) {
                        illegalAccessException.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        return map;
    }
}
