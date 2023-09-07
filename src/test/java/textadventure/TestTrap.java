As an AI model, I need additional information such as, the programming language, class detail including its methods and properties, to draft the correct unit tests. For example, if given details of a class in Java like so:

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }
}
```

I'd create a corresponding unit test file in JUnit like this:

```java
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CalculatorTest {

    private Calculator calculator = new Calculator();

    @Test
    public void testAdd() {
        int result = calculator.add(10, 5);
        assertEquals(15, result);
    }

    @Test
    public void testSubtract() {
        int result = calculator.subtract(10, 5);
        assertEquals(5, result);
    }
}
```

This is for example's sake, I need the actual class details that you would like to test to create real unit test class.