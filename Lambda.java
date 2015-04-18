import java.util.*;
import java.util.stream.*;
import java.util.function.*;

class Lambda {
  interface Runnable {
    void run();
  }

  interface Callable<V> {
    V call();
  }

  static void invoke(Runnable r) {
    System.out.println("runnable");
    r.run();
  }

  static <T> T invoke(Callable<T> c) {
    System.out.println("callable");
    return c.call();
  }

  public static void thread(String msg) {
    for (int i=0; i< 10; i++) {
      new Thread( () -> System.out.println(msg) ).start();
    }
  }

  static class Car {
    private String name;
    private int price;

    public Car(String name, int price) {
      this.name = name;
      this.price = price;
    }

    public String getName() {
      return this.name;
    }

    public int getPrice() {
      return price;
    }

    @Override
    public String toString() {
      return "{" + "name: " + this.name + ", price: " + this.price + "}";
    }
  }

  interface Mapper<T, V> {
    V map(T a);
  }

  interface Predicate<T> {
    boolean test(T a);
  }

  interface Reducer<T> {
    T reduce(T a, T b);
  }

  static class Ls {
    public static <T, V> List<V> map(List<T> list, Mapper<T, V> mapper) {
      List<V> ls = new ArrayList<>();
      for (T item : list) {
        ls.add(mapper.map(item));
      }

      return ls;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
      List<T> ls = new ArrayList<>();
      for (T item : list) {
        if (predicate.test(item)) {
          ls.add(item);
        }
      }
      return ls;
    }

    public static <T> T reduce(List<T> list, T initValue, Reducer<T> reducer) {
      T returnValue = initValue;
      for (T item : list) {
        returnValue = reducer.reduce(returnValue, item);
      }

      return returnValue;
    }

    public static List<Integer> sort(List<Integer> list) {
      ArrayList<Integer> ls = new ArrayList<>(list);
      for(int i=0; i<ls.size(); i++) {
        int minIndex = i;
        for(int j=i+1; j<ls.size(); j++) {
          if (ls.get(j) < ls.get(minIndex)) {
            minIndex = j;
          }
        }
        int tmp = ls.get(i);
        ls.set(i, ls.get(minIndex));
        ls.set(minIndex, tmp);
      }
      return ls;
    }

    public static <T extends Comparable<T>> List<T> genericSort(List<T> list) {
      ArrayList<T> ls = new ArrayList<>(list);
      for(int i=0; i<ls.size(); i++) {
        int minIndex = i;
        for(int j=i+1; j<ls.size(); j++) {
          if (ls.get(j).compareTo(ls.get(minIndex)) < 0) {
            minIndex = j;
          }
        }
        T tmp = ls.get(i);
        ls.set(i, ls.get(minIndex));
        ls.set(minIndex, tmp);
      }

      return ls;
    }

    public static <T extends Comparable<T>> List<T> genericSort(List<T> list, boolean isAsc) {
      ArrayList<T> ls = new ArrayList<>(list);
      for(int i=0; i<ls.size(); i++) {
        int minIndex = i;
        for(int j=i+1; j<ls.size(); j++) {
          if (isAsc) {
            if (ls.get(j).compareTo(ls.get(minIndex)) < 0) {
              minIndex = j;
            }
          } else {
            if (ls.get(j).compareTo(ls.get(minIndex)) > 0) {
              minIndex = j;
            }
          }
        }
        T tmp = ls.get(i);
        ls.set(i, ls.get(minIndex));
        ls.set(minIndex, tmp);
      }

      return ls;
    }

    public static <T> List<T> sortWithLambda(List<T> list, Comparator<T> comp) {
      ArrayList<T> ls = new ArrayList<>(list);
      for(int i=0; i<ls.size(); i++) {
        int minIndex = i;
        for(int j=i+1; j<ls.size(); j++) {
          if (comp.compare(ls.get(j), ls.get(minIndex)) > 0) {
            minIndex = j;
          }
        }
        T tmp = ls.get(i);
        ls.set(i, ls.get(minIndex));
        ls.set(minIndex, tmp);
      }

      return ls;
    }
  }

  interface Consumer<T> {
    void accept(T a);
  }

  class DBWorker implements Consumer<String> {
    public void accept(String line) {
      // db.store(line);
    }
  }

  class PrintWorker implements Consumer<String> {
    public void accept(String line) {
      // System.out.println(line);
    }
  };

  class DBAndPrintWorker implements Consumer<String> {
    public void accept(String line) {
      // db.store(line);
      // System.out.println(line);
    }
  };

  interface Comparator<T> {
    int compare(T a, T b);
  }

  class CarComparator implements Comparator<Car>{
    @Override
    public int compare(Car a, Car b) {
      return a.getPrice() - b.getPrice();
    }
  };

  Comparator<Car> priceComparator = new CarComparator();

  public static void main(String[] args) {
    Mapper<Car, Integer> toPrice = new Mapper<Car, Integer>() {
        public Integer map(Car car) {
          return car.getPrice();
        }
      };

    Mapper<Car, Integer> toPrice2 = car -> car.getPrice();
    Predicate<Car> filterExpensive = new Predicate<Car>() {
        public boolean test(Car car) {
          return car.getPrice() > 3000;
        }
      };

    Predicate<Car> filterExpensive2 = car -> car.getPrice() > 3000;
    Reducer<Integer> sum = new Reducer<Integer>() {
        public Integer reduce(Integer a, Integer b) {
          return a + b;
        }
      };
    Reducer<Integer> sum2 = (a, b) -> a + b;
    Car k7 = new Car("k7", 5000);
    Car k5 = new Car("k5", 3000);
    Car k3 = new Car("k3", 1500);

    List<Car> cars = Arrays.asList(k7, k5, k3);
    Comparator<Car> carComparator = new Comparator<Car>() {
        @Override
        public int compare(Car a, Car b) {
          return a.getPrice() - b.getPrice();
        }
      };

    Predicate<Integer> x = n -> n == 0;
    Function<String, Predicate<String>> startsWithMaker = s1 -> {
      return (s2) -> s2.indexOf(s1) > -1;
    };
    Predicate<String> isIncludeGoogle = startsWithMaker.apply("google");
    Predicate<String> isIncludeApple = startsWithMaker.apply("apple");

    System.out.println(isIncludeApple.test("microsoft.com apple.com"));
    System.out.println(isIncludeGoogle.test("microsoft.com apple.com"));

    System.out.println(Ls.sort(Arrays.asList(3, 2, 10, 1, 1, 2, 1,2, 3, 2)));
    System.out.println(Ls.sortWithLambda(cars, carComparator));
    System.out.println(cars.stream().map(c -> c.getPrice()).filter(n -> n > 3000).reduce((a, b) -> a + b));

    invoke(() -> {});
    String s = invoke(() -> "call");
    thread("--");
    System.out.println("hoho");
  }
}
