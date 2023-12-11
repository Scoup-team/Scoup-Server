package com.scoup.server.config.hashconvert;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Converter
public class PasswordConverter implements AttributeConverter<String, String> {

    public String convertToDatabaseColumn(String raw) {
        return encode(raw);
    }

    public String convertToEntityAttribute(String encoded) {
        return encoded;
    }

    public static String encode(String password){
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            return String.format("%064x", new BigInteger(1, md.digest()));
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
