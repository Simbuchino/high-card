package it.sara.demo.service.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
