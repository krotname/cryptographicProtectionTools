/* ===== Публичные «обёртки» ===== */

/** Шифрование */
public static String encrypt(String plaintext, int shift) {
    return transform(plaintext, shift);
}

        /** Расшифрование (достаточно инвертировать сдвиг) */
        public static String decrypt(String ciphertext, int shift) {
            return transform(ciphertext, -shift);
        }

        /* ===== Приватная «кухня» ===== */

        /** Универсальное преобразование */
        private static String transform(String input, int shift) {
            StringBuilder sb = new StringBuilder();

            for (char ch : input.toCharArray()) {
                sb.append(shiftChar(ch, shift));
            }
            return sb.toString();
        }

        /** Сдвиг одного символа вперёд/назад в зависимости от алфавита */
        private static char shiftChar(char ch, int shift) {
            if (Character.isUpperCase(ch)) {
                if (isLatin(ch)) return shiftWithinRange(ch, shift, 'A', 26);
                if (isCyrillic(ch)) return shiftWithinRange(ch, shift, 'А', 32);
            } else if (Character.isLowerCase(ch)) {
                if (isLatin(ch)) return shiftWithinRange(ch, shift, 'a', 26);
                if (isCyrillic(ch)) return shiftWithinRange(ch, shift, 'а', 32);
            }
            // Прочие символы (цифры, знаки, пробелы) оставляем без изменений
            return ch;
        }

        /* ===== Служебные методы ===== */

        private static boolean isLatin(char ch) {
            return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
        }

        private static boolean isCyrillic(char ch) {
            return (ch >= 'А' && ch <= 'Я') || (ch >= 'а' && ch <= 'я');
        }

        private static char shiftWithinRange(char ch, int shift, char base, int alphabetSize) {
            // приводим сдвиг к диапазону [0; alphabetSize-1]
            int offset = ((ch - base) + (shift % alphabetSize) + alphabetSize) % alphabetSize;
            return (char) (base + offset);
        }

        /* ===== Демонстрация работы ===== */

        void main() {
            try (Scanner sc = new Scanner(System.in)) {

                IO.print("Введите сообщение: ");
                String text = sc.nextLine();

                IO.print("Сдвиг (целое число): ");
                int k = sc.nextInt();

                String cipher = encrypt(text, k);
                String plain = decrypt(cipher, k);

                System.out.printf("Зашифровано: %s%n", cipher);
                System.out.printf("Расшифровано: %s%n", plain);
            }
        }
