/**
 * Простейшая реализация шифра Виженера.
 *  – принимает только буквы алфавита (регистр игнорируется);
 *  – остальные символы (пробелы, знаки препинания) копируются без изменений;
 *  – ключ повторяется по длине открытого текста.
 */

private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
private static final int N = ALPHABET.length();

/** Шифрование */
public static String encrypt(String plaintext, String key) {
    if (key.length() == 0) {
        throw new IllegalArgumentException("Key must not be empty");
    }
    StringBuilder out = new StringBuilder();
    key = key.toUpperCase();
    int keyPos = 0;

    for (char ch : plaintext.toCharArray()) {
        int idx = ALPHABET.indexOf(Character.toUpperCase(ch));
        if (idx != -1) {                         // символ из алфавита
            int shift = ALPHABET.indexOf(key.charAt(keyPos));
            int encIdx = (idx + shift) % N;
            char enc = matchCase(ALPHABET.charAt(encIdx), ch);
            out.append(enc);

            keyPos = (keyPos + 1) % key.length();
        } else {                                // любые другие символы
            out.append(ch);
        }
    }
    return out.toString();
}

/** Расшифрование (обратный сдвиг) */
public static String decrypt(String ciphertext, String key) {
    if (key.length() == 0) {
        throw new IllegalArgumentException("Key must not be empty");
    }
    StringBuilder out = new StringBuilder();
    key = key.toUpperCase();
    int keyPos = 0;

    for (char ch : ciphertext.toCharArray()) {
        int idx = ALPHABET.indexOf(Character.toUpperCase(ch));
        if (idx != -1) {
            int shift = ALPHABET.indexOf(key.charAt(keyPos));
            int decIdx = (idx - shift + N) % N;
            char dec = matchCase(ALPHABET.charAt(decIdx), ch);
            out.append(dec);

            keyPos = (keyPos + 1) % key.length();
        } else {
            out.append(ch);
        }
    }
    return out.toString();
}

/** Сохраняет регистр символа-образца (удобно для смешанного текста) */
private static char matchCase(char template, char sample) {
    return Character.isLowerCase(sample) ? Character.toLowerCase(template)
            : template;
}

// Пример использования
void main() {
    String plain = "Attack at dawn!";
    String key = "LEMON";

    String cipher = encrypt(plain, key);
    IO.println("Ciphertext:  " + cipher);      // Lxfopv ef rnhr!

    String decoded = decrypt(cipher, key);
    IO.println("Decrypted :  " + decoded);     // Attack at dawn!
}
