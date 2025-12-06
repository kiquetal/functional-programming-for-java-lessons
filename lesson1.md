### Think in state not in behaviour


The interface is the decision marker.

The class in the implementation of the marker.

```java

interface CarCriterion {
 boolean test(Car c);
}

class RedCarCriterion implements CarCriterion{

 public boolen test(Car c) {
  return c.getColor().equals("Red");

}

}

```java

public static List<Car> getCarsByCriterion(Iterable<Car> in, CarCriterion crit) {

List<Car> output = new ArrayList<>()

for (Car c: in) {
    if cri.test(c) {
     output.add(c);
}

}

}
```java
