package com.example.tripai_backend.helpers;

import org.springframework.stereotype.Component;

@Component
public class TextNormalizer {
    public String normalize(String text){
        return text.trim();
    }
}
