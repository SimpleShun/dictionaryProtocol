import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Test {
	public static void main(String[] args) {
		String secretKey = "dictserverprotoc";
		String message = "123";
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			// Encrypt the message
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));
			String encryptedMessage = Base64.getEncoder().encodeToString(encryptedBytes);
			System.out.println("Encrypted Message: " + encryptedMessage);

			// Decrypt the message
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
			String decryptedMessage = new String(decryptedBytes, "UTF-8");
			System.out.println("Decrypted Message: " + decryptedMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
