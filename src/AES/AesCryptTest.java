package AES;
import org.junit.Assert;
import org.junit.Test;

public class AesCryptTest {
    @Test
    public void test() throws Exception {
        AesCrypt aes = new AesCrypt("password");

        String crypted = aes.encrypt("Test string");
        Assert.assertEquals("Test string", aes.decrypt(crypted));

    }
}