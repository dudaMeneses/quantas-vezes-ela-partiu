package com.duda.quantasvezeselapartiu.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

interface JsonReader {

    default JsonNode contentToNode(ObjectMapper mapper, String content){
        try {
            return mapper.readTree(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
