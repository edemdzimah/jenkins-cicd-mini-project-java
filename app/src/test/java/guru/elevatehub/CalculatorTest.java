package guru.elevatehub;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for Calculator. These run in the pipeline's Test stage
 * via `mvn test`, the same way they run on your own machine.
 */
class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void addsTwoNumbers() {
        assertEquals(58, calculator.add(2, 3));
        assertEquals(0, calculator.add(-1, 1));
    }

    @Test
    void subtractsTwoNumbers() {
        assertEquals(4, calculator.subtract(5, 1));
        assertEquals(-2, calculator.subtract(1, 3));
    }

    @Test
    void multipliesTwoNumbers() {
    assertEquals(15, calculator.multiply(3, 5));
    assertEquals(0, calculator.multiply(0, 9));
    }
}
