package sixth.sem.dictionary;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {
    private Encrypt() {
    }

    public static String decrypt(String key) {
        String SecretKey = "dictserverprotoc";
        try {
            var secretKeySpec = new SecretKeySpec(SecretKey.getBytes("utf-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            var decryptedText = new String(cipher.doFinal(Base64.getDecoder().decode(key)), "UTF-8");
            return decryptedText;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "wrong";
        }
    }

}
