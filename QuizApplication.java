import java.util.Scanner;

public class QuizApplication {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String[] questions = {
                "1. What is the capital of India?",
                "2. Which language is used to develop Android Apps?",
                "3. Which keyword is used to create an object in Java?",
                "4. Which company developed Java?",
                "5. What is the size of int in Java?"
        };

        String[][] options = {
                {"A. Mumbai", "B. Delhi", "C. Pune", "D. Chennai"},
                {"A. Python", "B. Java", "C. C", "D. HTML"},
                {"A. this", "B. class", "C. new", "D. public"},
                {"A. Microsoft", "B. Apple", "C. Sun Microsystems", "D. Google"},
                {"A. 2 Bytes", "B. 4 Bytes", "C. 8 Bytes", "D. 16 Bytes"}
        };

        char[] answers = {'B', 'B', 'C', 'C', 'B'};

        int score = 0;

        System.out.println("===== JAVA QUIZ =====");

        for (int i = 0; i < questions.length; i++) {

            System.out.println("\n" + questions[i]);

            for (String option : options[i]) {
                System.out.println(option);
            }

            System.out.print("Enter Your Answer (A/B/C/D): ");
            char userAnswer = Character.toUpperCase(sc.next().charAt(0));

            if (userAnswer == answers[i]) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong!");
                System.out.println("Correct Answer: " + answers[i]);
            }
        }

        System.out.println("\n========================");
        System.out.println("Quiz Completed");
        System.out.println("Your Score: " + score + "/" +
