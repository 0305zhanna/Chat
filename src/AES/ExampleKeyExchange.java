package AES;

public class ExampleKeyExchange {
    public static void main(String[] args) {
        Client client1 = new Client();
        Client client2 = new Client();
        try {
            client1.generateKeyPair();
            client1.init();

            client2.setPublicKey(client1.getMyPublicKey());
            client2.generateKeyPairFromParam();
            client2.init();
            client2.doPhase1();
            client2.initSharedSecret();

            client1.setPublicKey(client2.getMyPublicKey());
            client1.doPhase1();
            client1.initSharedSecret();

            String mes = "Hello";
            String encryptedMessage = client1.encrypt(mes);
            System.out.println(client2.decrypt(client1.getEcodedParams(),encryptedMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
