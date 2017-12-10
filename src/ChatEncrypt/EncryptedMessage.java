package ChatEncrypt;

import java.io.UnsupportedEncodingException;

public class EncryptedMessage {
    private byte[] message;
    private byte[] params;

    public EncryptedMessage(EncryptedMessage encryptedMessagem) {
        this.message = encryptedMessagem.getMessage();
        this.params = encryptedMessagem.getParams();
    }
    public EncryptedMessage(byte[] params, byte[] mes){
        this.message = mes;
        this.params = params;
    }

    public byte[] getMessage() {
        return message;
    }
    public byte[] getParams() {
        return params;
    }
    public String getMessageString() throws UnsupportedEncodingException {
        return new String(message,"UTF-8");

    }
    public String getParamsString() throws UnsupportedEncodingException {
        return new String(params,"UTF-8");
     }

}
