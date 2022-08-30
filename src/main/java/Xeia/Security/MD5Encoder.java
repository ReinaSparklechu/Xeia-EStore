package Xeia.Security;

import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        System.out.println("MD5 Enconder encoding " + rawPassword.toString());
        try {
            System.out.println(convertToHex(MessageDigest.getInstance("MD5").digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8))));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        try {
            return convertToHex(MessageDigest.getInstance("MD5").digest(rawPassword.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword) == encodedPassword;
    }
    private String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }

}
