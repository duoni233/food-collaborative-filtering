import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class RecommendationDemo {
    private enum Action {
        FAVORITE(1), CART(2), COMPLETED_ORDER(3), COMMENT(2);

        private final int weight;

        Action(int weight) {
            this.weight = weight;
        }
    }

    private static int preference(Action first, Action... others) {
        return 1 + EnumSet.of(first, others).stream().mapToInt(action -> action.weight).sum();
    }

    private static Map<String, Integer> ratings(int porridge, int noodles, int rice) {
        Map<String, Integer> ratings = new LinkedHashMap<>();
        ratings.put("南瓜粥", porridge);
        ratings.put("鸡丝面", noodles);
        ratings.put("番茄饭", rice);
        return ratings;
    }

    private static double pearson(Map<String, Integer> left, Map<String, Integer> right) {
        List<String> common = left.keySet().stream().filter(right::containsKey).collect(Collectors.toList());
        if (common.size() < 2) {
            return 0;
        }

        double leftMean = common.stream().mapToInt(left::get).average().getAsDouble();
        double rightMean = common.stream().mapToInt(right::get).average().getAsDouble();
        double covariance = 0;
        double leftVariance = 0;
        double rightVariance = 0;

        for (String dish : common) {
            double leftDelta = left.get(dish) - leftMean;
            double rightDelta = right.get(dish) - rightMean;
            covariance += leftDelta * rightDelta;
            leftVariance += leftDelta * leftDelta;
            rightVariance += rightDelta * rightDelta;
        }

        double denominator = Math.sqrt(leftVariance * rightVariance);
        return denominator == 0 ? 0 : covariance / denominator;
    }

    private static List<String> recommend(
            Map<String, Integer> current, Map<String, Map<String, Integer>> neighbors) {
        if (current.isEmpty() || neighbors.isEmpty()) {
            return Collections.emptyList();
        }

        String nearest = null;
        double bestSimilarity = -1;
        for (Map.Entry<String, Map<String, Integer>> neighbor : neighbors.entrySet()) {
            double correlation = pearson(current, neighbor.getValue());
            System.out.printf(Locale.ROOT, "%s Pearson: %.2f%n", neighbor.getKey(), correlation);

            double similarity = Math.abs(correlation);
            if (similarity > bestSimilarity) {
                nearest = neighbor.getKey();
                bestSimilarity = similarity;
            }
        }

        System.out.println("近邻: " + nearest);
        return neighbors.get(nearest).keySet().stream()
                .filter(dish -> !current.containsKey(dish))
                .limit(10)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Map<String, Integer> current = ratings(
                preference(Action.FAVORITE, Action.COMPLETED_ORDER, Action.COMMENT),
                preference(Action.CART),
                preference(Action.FAVORITE));

        Map<String, Integer> neighborA = ratings(
                preference(Action.CART, Action.COMPLETED_ORDER, Action.COMMENT),
                preference(Action.COMPLETED_ORDER),
                preference(Action.FAVORITE));
        neighborA.put("菌菇粥", preference(Action.CART));

        Map<String, Integer> neighborB = ratings(
                preference(Action.COMPLETED_ORDER),
                preference(Action.CART, Action.COMPLETED_ORDER, Action.COMMENT),
                preference(Action.FAVORITE));
        neighborB.put("莲藕汤", preference(Action.FAVORITE));

        Map<String, Map<String, Integer>> neighbors = new LinkedHashMap<>();
        neighbors.put("用户 A", neighborA);
        neighbors.put("用户 B", neighborB);

        System.out.println("当前用户偏好分: " + current);
        System.out.println("推荐候选: " + recommend(current, neighbors));
    }
}
