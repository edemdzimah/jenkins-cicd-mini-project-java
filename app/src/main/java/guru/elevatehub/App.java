package guru.elevatehub;

/**
 * Entry point of the application. This is the class that runs when
 * you start the JAR with `java -jar app.jar` inside the Docker image.
 */
public class App {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println("CI/CD mini project (Java)");
        System.out.println("2 + 3 = " + calculator.add(2, 3));
        System.out.println("5 - 1 = " + calculator.subtract(5, 1));
    }
}
