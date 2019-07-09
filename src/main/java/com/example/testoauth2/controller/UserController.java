package com.example.testoauth2.controller;

import com.alibaba.fastjson.JSON;
import com.example.testoauth2.dto.UserDto;
import com.example.testoauth2.po.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

@RestController
@RequestMapping("/user")
public class UserController {
    private final SecretKey key;

    public UserController() throws Exception {
        key = getSecretEncryptionKey();
    }

    @GetMapping("/login")
    public String login(UserDto userDto) throws Exception {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if(username==null && !username.equals("xxx")){
            throw  new Exception("the username doesn't exit");
        }
        if(password==null && !password.equals("123456")){
            throw  new Exception("password is wrong");
        }
        User user = new User();
        user.setUsername("xxx");
        user.setPassword("123456");
        user.setEmail("1493427524@qq.com");
        user.setMobile("18235889953");
        user.setRole("admin");
        String s = JSON.toJSONString(user);
        byte[] bytes = encryptText(s, key);
        String s1 = Base64.getEncoder().encodeToString(bytes);
        return s1;
    }
    public SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }

    public byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    public  String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }

    @GetMapping("/sayhello")
    public String sayhello(String token,@RequestAttribute String username) throws Exception {
        String token1=token.replaceAll(" +","+");
        byte[] decode = Base64.getDecoder().decode(token1);

        String s = decryptText(decode, key);
        return username+"xxx";
    }
}



