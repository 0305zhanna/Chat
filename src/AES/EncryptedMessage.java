package AES;

import java.io.UnsupportedEncodingException;

public class EncryptedMessage {
    private byte[] message;
    private byte[] params;

    public EncryptedMessage(EncryptedMessage encryptedMessagem) {
        this.message = encryptedMessagem.getMessage().clone();
        this.params = encryptedMessagem.getParams().clone();
    }
    public EncryptedMessage(byte[] params, byte[] mes){
        //this.params = new byte[1];
        //System.arraycopy (params, 0, this.params, 0, params.length);
        this.params = params.clone();
//        this.message = new byte[1];
//        System.arraycopy (mes, 0, this.message, 0, mes.length);
        this.message = mes.clone();
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
