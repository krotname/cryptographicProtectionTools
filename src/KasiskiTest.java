/**
 * Выполняет тест Касиски для данного текста.
 * @param text          Шифротекст (только буквы, без пробелов).
 * @param seqLen        Длина анализируемой подпоследовательности (обычно 3–5).
 * @param maxKeyLength  Максимальная проверяемая длина ключа.
 * @return Список пар (длина ключа, количество “попаданий”), отсортированных по убыванию.
 */
public static List<Map.Entry<Integer, Integer>> analyze(String text, int seqLen, int maxKeyLength) {
    Map<String, List<Integer>> positions = new HashMap<>();
    // 1) Собираем все подпоследовательности длины seqLen и их позиции
    for (int i = 0; i + seqLen <= text.length(); i++) {
        String seq = text.substring(i, i + seqLen);
        positions.computeIfAbsent(seq, k -> new ArrayList<>()).add(i);
    }

    // 2) Вычисляем все расстояния между повторными вхождениями
    List<Integer> distances = new ArrayList<>();
    for (List<Integer> posList : positions.values()) {
        if (posList.size() > 1) {
            for (int i = 1; i < posList.size(); i++) {
                distances.add(posList.get(i) - posList.get(i - 1));
            }
        }
    }

    // 3) Для каждого возможного ключа d=2..maxKeyLength считаем, сколько дистанций делятся на d
    Map<Integer, Integer> factorCounts = new HashMap<>();
    for (int d = 2; d <= maxKeyLength; d++) {
        int count = 0;
        for (int dist : distances) {
            if (dist % d == 0) count++;
        }
        if (count > 0) {
            factorCounts.put(d, count);
        }
    }

    // 4) Сортируем по убыванию частоты
    List<Map.Entry<Integer, Integer>> result = new ArrayList<>(factorCounts.entrySet());
    result.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));
    return result;
}

void main() {
    String cipher = "XQFQRXQFQRXQFQRYABCXQFQ"; // пример шифротекста
    // Убираем всё, кроме больших букв:
    cipher = cipher.replaceAll("[^A-Z]", "");
    List<Map.Entry<Integer, Integer>> likelyKeyLengths = analyze(cipher, 3, 20);

    IO.println("Возможные длины ключа по тесту Касиски:");
    for (Map.Entry<Integer, Integer> entry : likelyKeyLengths) {
        System.out.printf("Длина = %2d, совпадений = %d%n", entry.getKey(), entry.getValue());
    }
}
