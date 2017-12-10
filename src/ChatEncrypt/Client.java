package ChatEncrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;


public class Client {
    private KeyPair keyPair;
    private KeyAgreement keyAgree;
    private KeyFactory keyFac;
    private X509EncodedKeySpec x509KeySpec;
    private DHParameterSpec dhParamFromPubKey;
    private PublicKey pubKey;
    private byte[] sharedSecret;
    private  SecretKeySpec aesKey;

    public void generateKeyPair() throws Exception {
        System.out.println("Generating DH keypair ...");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(2048);
        keyPair = keyPairGen.generateKeyPair();
    }
    public void init() throws Exception {
        System.out.println("Initialization ...");
        keyAgree = KeyAgreement.getInstance("DH");
        keyAgree.init(keyPair.getPrivate());
    }
    public byte[] getMyPublicKey(){
        if(keyPair != null) {
            return keyPair.getPublic().getEncoded();
        }else {
            return null;
        }
    }

    public void setPublicKey(byte[] pubKeyEnc) throws Exception{
        System.out.println("Setting public key...");
        keyFac = KeyFactory.getInstance("DH");
        x509KeySpec = new X509EncodedKeySpec(pubKeyEnc);
        pubKey = keyFac.generatePublic(x509KeySpec);
    }
    public void generateKeyPairFromParam() throws Exception {
        System.out.println("Generating key pair from parametres...");
        dhParamFromPubKey = ((DHPublicKey)pubKey).getParams();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        keyPairGen.initialize(dhParamFromPubKey);
        keyPair = keyPairGen.generateKeyPair();
    }
    public void doPhase1() throws Exception{
        System.out.println("Doing phase 1...");
        keyAgree.doPhase(pubKey, true);
    }
    public void initSharedSecret(){
        sharedSecret = keyAgree.generateSecret();
        System.out.println("Shared secret: " + sharedSecret);
    }
    public EncryptedMessage encrypt(String message) throws Exception{
        aesKey = new SecretKeySpec(sharedSecret, 0, 16, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] cleartext = message.getBytes();
        byte[] encodedParams = cipher.getParameters().getEncoded();
        return new EncryptedMessage(cipher.doFinal(cleartext),encodedParams);
    }
    public String decrypt(EncryptedMessage encryptedMessage) throws Exception{
        AlgorithmParameters aesParams = AlgorithmParameters.getInstance("AES");
        aesParams.init(encryptedMessage.getParams());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesKey = new SecretKeySpec(sharedSecret, 0, 16, "AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, aesParams);
        byte[] recovered = cipher.doFinal(encryptedMessage.getMessage());
        return new String(recovered);
    }

}
