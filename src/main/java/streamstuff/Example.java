package streamstuff;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
interface ExFunction<A, R> {
  R apply(A a) throws Throwable;
  static <A, R> Function<A, Optional<R>> wrap(ExFunction<A, R> op) {
    return a -> {
      try {
        return Optional.of(op.apply(a));
      } catch (Throwable t) {
        return Optional.empty();
      }
    };
  }
}

public class Example {
  public static Stream<String> getLines(String fn) {
    try {
      return Files.lines(Path.of(fn));
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static Optional<Stream<String>> getOptLines(String fn) {
    try {
      return Optional.of(Files.lines(Path.of(fn)));
    } catch (Throwable t) {
      return Optional.empty();
    }
  }

  public static void main(String[] args) /*throws Throwable */{
      List.of("a.txt", "b.txt", "c.txt").stream()
          .map(Example::getOptLines)
          .peek(opt -> {
            if (opt.isEmpty()) {
              System.out.println("something broke");
            }
          })
          .filter(Optional::isPresent)
          .flatMap(Optional::get)
          .forEach(System.out::println);


      //    try {
//      List.of("a.txt", "b.txt", "c.txt").stream()
//          .flatMap(Example::getLines)
//          .forEach(System.out::println);
//    } catch (Throwable t) {
//      System.out.println("it broke " + t.getMessage());
//    }
  }
}
