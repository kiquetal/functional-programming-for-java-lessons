#### Streams


##### Coding Principles

###### Terminal vs. Intermediate Operations

Understanding the distinction between intermediate (partial) and terminal operations is crucial for working effectively with Streams.

**Intermediate Operations:**
These operations are "lazy." When you invoke an intermediate operation, it does not immediately process the data. Instead, it returns a new Stream describing the transformation or filter to be applied. You can chain multiple intermediate operations together to form a processing pipeline. Think of this as writing a recipe or setting up a series of stations on a factory conveyor belt; nothing actually moves or gets cooked until the final command is given. Common conceptual examples include filtering items based on a condition or transforming an object from one type to another.

**Terminal Operations:**
These operations are "eager" and trigger the actual processing of the Stream. When a terminal operation is called, the data flows through the pipeline defined by the intermediate operations. A terminal operation produces a result (like a list, a single value, or a boolean) or performs a side effect (like printing to a console). Once a terminal operation is performed, the Stream is considered consumed and cannot be reused.

**Key Takeaway:**
If you define a complex chain of intermediate operations but forget to attach a terminal operation, absolutely no processing will occur. The Stream pipeline is merely a definition of *what* should happen, not the execution itself.

###### Conceptual Technical Example

Imagine you have a large list of **Customer Orders** and you want to calculate the total revenue from all orders shipped to "Paris" that were over $100.

1.  **The Source:** You start with your collection of all orders.
2.  **Intermediate Operation (Filter):** You specify a rule: "Only look at orders where the destination is Paris." *At this point, no orders have been checked yet.*
3.  **Intermediate Operation (Filter):** You add another rule: "Only look at orders with a value greater than $100." *Still, no data has been processed.*
4.  **Intermediate Operation (Map):** You specify: "I only care about the price field of these filtered orders." *The pipeline is now defined.*
5.  **Terminal Operation (Sum):** You finally say: "Add these prices together."

**The Execution Flow:**
Only when you call the **Sum** (Terminal) operation does the computer start looking at the orders. It pulls the first order, checks if it's for Paris and over $100. If so, it takes the price. Then it moves to the next order. It doesn't create a temporary list of Paris orders, then another list of expensive orders; it processes each item through the entire pipeline one by one, efficiently and only when requested by the final step.


###### Getting a result

Remember to wrap with Optional to be able to use map, and so on methods.


##### Notes about concurrency

Multiples threads:


###### More flexible ways to a result.
