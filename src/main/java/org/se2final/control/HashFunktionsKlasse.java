package org.se2final.control;


import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashFunktionsKlasse {

    private HashFunktionsKlasse(){

    }

    // Algorithms: MD2, MD5, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512

    public static String getHash(byte[] inputBytes, String algorithm){

        String hashValue = "";

        try{
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(inputBytes);
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();

        }
        catch (Exception e){
            Logger.getLogger(HashFunktionsKlasse.class.getName()).log(Level.SEVERE, null, e);
        }
        return hashValue;
    }
}
