package org.example.recapprojectjanuary.UTIL;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdService {

    public String randomId() {
        return UUID.randomUUID().toString();
    }

}
