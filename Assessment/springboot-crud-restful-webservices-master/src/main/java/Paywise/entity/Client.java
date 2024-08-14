package Paywise.entity;


import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Base64;

@Service
public class Client
{
    private static final String RSA = "RSA";

    public PrivateKey privateKey ;
    public PublicKey publicKey ;

    public void init() throws NoSuchAlgorithmException {
        KeyPair  keyPair = generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public String encrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //Generate Key Pair
        //Initialize the private key and public
        init();
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptedValue  = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);

    }

    public String decrypt(String encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] decryptedValue  = cipher.doFinal(Base64.getMimeDecoder().decode(encryptedData));
        return new String(decryptedValue);

    }



}