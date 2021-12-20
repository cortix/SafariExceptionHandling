package many;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ManyIncludesNone {
  public static List<String> getFromMap(Map<String, String> map, String key) {
    List<String> res = new ArrayList<>();
    String value = map.get(key);
    if (value != null) {
      res.add(value);
    }
    return res;
  }

  public static Optional<String> getOptFromMap(Map<String, String> map, String key) {
    String value = map.get(key);
    if (value != null) {
      return Optional.of(value);
    } else {
      return Optional.empty();
    }
  }

  public static void main(String[] args) {
    Map<String, String> names = Map.of("Fred", "Jones");
    String first = "Fred";

    String last = names.get(first);
    if (last != null) {
      String message = "Dear " + last.toUpperCase();

      System.out.println(message);
    }

    System.out.println("---------------");
    List<String> lastNames = getFromMap(names, first);
    lastNames.stream()
        .map(n -> "Dear " + n.toUpperCase())
        .forEach(System.out::println);

    System.out.println("---------------");
    getOptFromMap(names, first)
        .map(n -> "Dear " + n.toUpperCase())
        .ifPresent(System.out::println);

    System.out.println("---------------");
    Optional.of(names)
        // this breaks the rules--if the operation
        // returns null, the resulting Optional is empty!
        .map(m -> m.get(first))
        .map(n -> "Dear " + n.toUpperCase())
        .ifPresent(System.out::println);
  }
}
