import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

public class StreamCipher {
    // Генерация гаммы (псевдослучайный поток байт)
    private static byte[] generateKeystream(int length, long seed) {
        Random rnd = new Random(seed);
        byte[] gamma = new byte[length];
        rnd.nextBytes(gamma);
        return gamma;
    }

    // Шифрование: XOR текста с гаммой, результат кодируется в Base64
    public static String encrypt(String plaintext, long seed) {
        byte[] data  = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] gamma = generateKeystream(data.length, seed);
        byte[] out   = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = (byte) (data[i] ^ gamma[i]);
        }
        return Base64.getEncoder().encodeToString(out);
    }

    // Расшифрование: обратная операция
    public static String decrypt(String ciphertext, long seed) {
        byte[] data  = Base64.getDecoder().decode(ciphertext);
        byte[] gamma = generateKeystream(data.length, seed);
        byte[] out   = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = (byte) (data[i] ^ gamma[i]);
        }
        return new String(out, StandardCharsets.UTF_8);
    }

    // Пример использования
    public static void main(String[] args) {
        long key = 20250721L;  // любой числовой ключ-сид
        String text = "Пример шифра гаммирования";

        String cipher = encrypt(text, key);
        String plain  = decrypt(cipher, key);

        System.out.println("Зашифровано:   " + cipher);
        System.out.println("Расшифровано:  " + plain);
    }
}
