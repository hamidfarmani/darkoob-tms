package security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class symmetricCipher {

    private static byte[] iv
            = {0x0a, 0x0a, 0x02, 0x04, 0x0b, 0x0c, 0x0d};

    private static byte[] encrypt(byte[] inpByte, SecretKey key, String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ips);
        return cipher.doFinal(inpByte);
    }

    private static byte[] decrypt(byte[] inpByte, SecretKey key, String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ips);
        return cipher.doFinal(inpByte);
    }
}
