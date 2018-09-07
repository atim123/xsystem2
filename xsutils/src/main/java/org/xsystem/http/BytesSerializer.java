/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xsystem.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Base64;

/**
 *
 * @author atimofeev
 */
public class BytesSerializer implements JsonSerializer<byte[]>{

    @Override
    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
        String b64str= Base64.getEncoder().encodeToString(src);
        return new JsonPrimitive(b64str);
    }
    
}
