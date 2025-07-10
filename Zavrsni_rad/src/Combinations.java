import java.util.ArrayList;
import java.util.List;

public class Combinations {
    public static List<List<String>> combine(List<String> nums, int k) {
        List<List<String>> result = new ArrayList<>();
        List<String> current = new ArrayList<>();
        combine(nums, k, 0, current, result);
        return result;
    }

    private static void combine(List<String> nums, int k, int start, List<String> current, List<List<String>> result) {
        if (k == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < nums.size(); i++) {
            current.add(nums.get(i));
            combine(nums, k - 1, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
