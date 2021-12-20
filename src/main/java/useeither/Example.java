package useeither;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
interface ExFunction<A, R> {
  R apply(A a) throws Throwable;
}

// Either usually calls these left and right
class Either<F, S> {
  private F failure;
  private S success;

  private Either(F left, S right) {
    this.failure = left;
    this.success = right;
  }

  public static <F, S> Either<F, S> success(S s) {
    return new Either<>(null, s);
  }

  public static <F, S> Either<F, S> failure(F f) {
    return new Either<>(f, null);
  }

  public void ifFailure(Consumer<F> op) {
    if (failure != null) {
      op.accept(failure);
    }
  }

  public S getSuccess() {
    if (isSuccess()) {
      return success;
    } else {
      throw new IllegalStateException("attempt to get success from a failure");
    }
  }

  public boolean isSuccess() {
    return failure == null;
  }

  public boolean isFailure() {
    return failure != null;
  }

  public Either<F, S> retry(Function<F, Either<F, S>> op) {
    if (isSuccess()) return this;
    return op.apply(failure);
  }

  // decision to explicitly wrap and return the Throwable in the Either
  // is perhaps a bit hacky...
  static <A, R> Function<A, Either<Throwable, R>> wrap(ExFunction<A, R> op) {
    return a -> {
      try {
        return Either.success(op.apply(a));
        // Should we in fact catch all throwable?
        // generally we want to recover from checked exceptions
        // but usage is a mess!
      } catch (Throwable t) { // Cannot catch a generic type (type erasure)
        // Could add a Consumer<Throwable> as an argument to wrap
        // and execute it here, for a form of semi-centralized
        // observability
        return Either.failure(t);
      }
    };
  }
}

public class Example {
  public static void main(String[] args) {
//    Function<Throwable, Either<Throwable, Stream<String>>> recovery =
//        Either.wrap(t -> Files.lines(Path.of("d.txt")));

    List.of("a1.txt", "b.txt", "c.txt").stream()
        .map(Either.wrap(fn -> Files.lines(Path.of(fn))))
        .peek(e -> e.ifFailure(t -> System.out.println("ERROR " + t.getMessage())))
//        .map(e -> e.retry(recovery))
        .map(e -> e.retry(Either.wrap(t -> Files.lines(Path.of("d.txt")))))
        .peek(e -> e.ifFailure(t -> System.out.println("ERROR " + t.getMessage())))
        .filter(Either::isSuccess)
        .flatMap(Either::getSuccess)
        .forEach(System.out::println);
  }
}
