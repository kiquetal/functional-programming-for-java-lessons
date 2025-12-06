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
