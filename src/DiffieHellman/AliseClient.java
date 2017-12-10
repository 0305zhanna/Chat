package DiffieHellman;

import javax.crypto.KeyAgreement;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class AliseClient {
    private KeyPair aliceKpair;
    private KeyAgreement aliceKeyAgree;
    private byte[] alicePubKeyEnc;
    private byte[] bobPubKeyEnc;
    private X509EncodedKeySpec x509KeySpec;
    public void generateKeyPair() throws Exception{
        System.out.println("ALICE: Generate DH keypair ...");
        KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
        aliceKpairGen.initialize(2048);
        aliceKpair = aliceKpairGen.generateKeyPair();
    }
    public void generateKeyAgreement() throws Exception{
        System.out.println("ALICE: Initialization ...");
        aliceKeyAgree = KeyAgreement.getInstance("DH");
        aliceKeyAgree.init(aliceKpair.getPrivate());
    }

    public void ecodePublicKey(){
        alicePubKeyEnc = aliceKpair.getPublic().getEncoded();
        x509KeySpec = new X509EncodedKeySpec(alicePubKeyEnc);
    }
    public void dpPhase1() throws Exception{
        KeyFactory aliceKeyFac = KeyFactory.getInstance("DH");
        x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
        PublicKey bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);
        System.out.println("ALICE: Execute PHASE1 ...");
        aliceKeyAgree.doPhase(bobPubKey, true);
    }

}
