import java.util.HashMap;
import java.util.Map;

public class SubstitutionCipher {

    /** Базовый алфавит (без «Ё») — 33 заглавные русские буквы. */
    private static final String ALPHABET =
            "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    private final Map<Character, Character> enc = new HashMap<>();
    private final Map<Character, Character> dec = new HashMap<>();

    /**
     * @param key Строка-перестановка длиной 33 символа, состоящая
     *            из тех же букв, что и ALPHABET, без повторов.
     */
    public SubstitutionCipher(String key) {
        if (key.length() != ALPHABET.length())
            throw new IllegalArgumentException("Ключ должен содержать 33 символа");

        // строим таблицы: открытая -> шифртекст и обратно
        for (int i = 0; i < ALPHABET.length(); i++) {
            char plain = ALPHABET.charAt(i);
            char cipher = Character.toUpperCase(key.charAt(i));
            if (enc.put(plain, cipher) != null)                // проверка повторов
                throw new IllegalArgumentException("Повтор буквы в ключе: " + cipher);
            dec.put(cipher, plain);
        }
    }

    /** Шифрует строку (регистр и знаки препинания сохраняются). */
    public String encrypt(String text) {
        StringBuilder out = new StringBuilder(text.length());
        for (char ch : text.toCharArray()) {
            boolean upper = Character.isUpperCase(ch);
            char base = Character.toUpperCase(ch);

            char res = enc.getOrDefault(base, base);
            out.append(upper ? res : Character.toLowerCase(res));
        }
        return out.toString();
    }

    /** Дешифрует строку. */
    public String decrypt(String text) {
        StringBuilder out = new StringBuilder(text.length());
        for (char ch : text.toCharArray()) {
            boolean upper = Character.isUpperCase(ch);
            char base = Character.toUpperCase(ch);

            char res = dec.getOrDefault(base, base);
            out.append(upper ? res : Character.toLowerCase(res));
        }
        return out.toString();
    }

    // Мини-демонстрация
    public static void main(String[] args) {
        // Пример ключа — просто циклический сдвиг на 4 позиции («русский Цезарь»)
        String key = "ДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯАБВГ";

        SubstitutionCipher cipher = new SubstitutionCipher(key);

        String plaintext  = "Безопасность важна!";
        String ciphertext = cipher.encrypt(plaintext);
        String decoded    = cipher.decrypt(ciphertext);

        System.out.println("Открытый текст : " + plaintext);
        System.out.println("Шифр-текст     : " + ciphertext);
        System.out.println("Расшифровка    : " + decoded);
    }

    static String vigenere(String text, String key, boolean encrypt) {
        String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder out = new StringBuilder();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int pos = ABC.indexOf(Character.toUpperCase(c));
            if (pos < 0) {                       // не-буква → копируем
                out.append(c); continue;
            }
            int shift = ABC.indexOf(key.charAt(j++ % key.length()));
            int idx = (pos + (encrypt ? shift : -shift) + 26) % 26;
            char nc = ABC.charAt(idx);
            out.append(Character.isLowerCase(c) ? Character.toLowerCase(nc) : nc);
        }
        return out.toString();
    }
}
