package com.gdrc.microservice_arq.store.shopping.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
public class ErrorMessage {
    private String code;
    private List<Map<String, String>> messages;
}