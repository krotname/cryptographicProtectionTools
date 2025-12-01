// Генерация гаммы (псевдослучайный поток байт, равный длине входящего текста)
private static byte[] generateKeystream(int length, long key) {
    Random rnd = new Random(key);
    byte[] gamma = new byte[length];
    rnd.nextBytes(gamma);
    return gamma;
}

// Шифрование: XOR текста с гаммой, результат кодируется в Base64
public static String encrypt(String plaintext, long key) {
    byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
    byte[] gamma = generateKeystream(data.length, key);
    byte[] out = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
        out[i] = (byte) (data[i] ^ gamma[i]);
    }
    return Base64.getEncoder().encodeToString(out);
}

// Расшифрование: обратная операция
public static String decrypt(String ciphertext, long key) {
    byte[] data = Base64.getDecoder().decode(ciphertext);
    byte[] gamma = generateKeystream(data.length, key);
    byte[] out = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
        out[i] = (byte) (data[i] ^ gamma[i]);
    }
    return new String(out, StandardCharsets.UTF_8);
}

// Пример использования
void main() {
    long key = 20250721L;  // любой числовой ключ-сид
    String text = "зример шифра гаммирования СО СПЕЦИ СИМВОЛАМИ ‰ … ½";

    String cipher = encrypt(text, key);
    String plain = decrypt(cipher, key);

    IO.println("Исходный:   " + text);
    IO.println("Зашифровано:   " + cipher);
    IO.println("Расшифровано:  " + plain);
}
