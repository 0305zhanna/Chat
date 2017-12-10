package AES;

import javax.crypto.*;
import java.security.NoSuchAlgorithmException;

public class ExampleBPE {
    private static SecretKey aesKey;
    static {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            aesKey = keygen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        try {

            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // Initialize the cipher for encryption
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

            // Our cleartext
            byte[] cleartext = "This is just an example".getBytes();

            // Encrypt the cleartext
            byte[] ciphertext = aesCipher.doFinal(cleartext);

            // Initialize the same cipher for decryption
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey);

            // Decrypt the ciphertext
            byte[] cleartext1 = aesCipher.doFinal(ciphertext);
            System.out.println(new String(cleartext1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
