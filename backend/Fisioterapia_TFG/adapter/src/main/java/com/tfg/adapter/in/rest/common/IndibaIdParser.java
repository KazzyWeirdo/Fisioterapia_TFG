package com.tfg.adapter.in.rest.common;

import com.tfg.indiba.IndibaSessionId;

public class IndibaIdParser {
    private IndibaIdParser() {}

    public static IndibaSessionId parseIndibaId(String string) {
        try {
            return new IndibaSessionId(Integer.parseInt(string));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
