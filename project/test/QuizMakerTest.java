import com.quizmaker.model.Question;
import com.quizmaker.model.Quiz;
import com.quizmaker.model.QuestionBank;
import com.quizmaker.service.EmailService;
import com.quizmaker.service.FileManager;
import com.quizmaker.service.QuizEvaluator;
import com.quizmaker.service.QuizGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive test suite for the Quiz Maker application.
 *
 * This test class verifies all major functionalities of the Quiz Maker system
 * including question management, quiz generation, evaluation, and file I/O.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class QuizMakerTest {

  private static int totalTests = 0;
  private static int passedTests = 0;

  /**
   * Main method to run all tests.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    System.out.println("====================================");
    System.out.println("  QUIZ MAKER TEST SUITE");
    System.out.println("====================================\n");

    testQuestionClass();
    testQuestionBank();
    testQuizClass();
    testFileManager();
    testQuizGenerator();
    testQuizEvaluator();
    testEmailService();
    testIntegration();

    printSummary();
  }

  /**
   * Tests the Question class functionality.
   */
  private static void testQuestionClass() {
    System.out.println("\n===== TEST: Question Class =====");

    // Test 1: Create a question
    totalTests++;
    try {
      List<String> answers = new ArrayList<>();
      answers.add("Berlin");
      answers.add("Paris");
      answers.add("London");
      answers.add("Rome");

      Question question = new Question("Q001", "What is the capital of France?", answers, 1,
          "Geography", 1);

      assert question.getId().equals("Q001") : "Question ID mismatch";
      assert question.getQuestionText().equals("What is the capital of France?")
          : "Question text mismatch";
      assert question.getCorrectAnswerIndex() == 1 : "Correct answer index mismatch";
      assert question.getCategory().equals("Geography") : "Category mismatch";
      assert question.getDifficulty() == 1 : "Difficulty mismatch";

      System.out.println("✓ Test 1 PASSED: Question creation");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 1 FAILED: " + e.getMessage());
    }

    // Test 2: Check correct answer
    totalTests++;
    try {
      List<String> answers = new ArrayList<>();
      answers.add("A");
      answers.add("B");
      answers.add("C");
      answers.add("D");

      Question question = new Question("Q002", "Sample question", answers, 2, "Test", 2);

      assert question.isCorrectAnswer(2) : "Correct answer check failed";
      assert !question.isCorrectAnswer(1) : "Incorrect answer recognized as correct";

      System.out.println("✓ Test 2 PASSED: Answer validation");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 2 FAILED: " + e.getMessage());
    }

    // Test 3: Get correct answer text
    totalTests++;
    try {
      List<String> answers = new ArrayList<>();
      answers.add("Option 1");
      answers.add("Option 2");
      answers.add("Option 3");
      answers.add("Option 4");

      Question question = new Question("Q003", "Test", answers, 2, "Test", 1);
      String correctAnswer = question.getCorrectAnswer();

      assert correctAnswer.equals("Option 3") : "Correct answer text mismatch";

      System.out.println("✓ Test 3 PASSED: Correct answer retrieval");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 3 FAILED: " + e.getMessage());
    }
  }

  /**
   * Tests the QuestionBank class functionality.
   */
  private static void testQuestionBank() {
    System.out.println("\n===== TEST: QuestionBank Class =====");

    // Test 4: Add and retrieve questions
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();

      Question q1 = createTestQuestion("Q101", "Geography");
      Question q2 = createTestQuestion("Q102", "Science");
      Question q3 = createTestQuestion("Q103", "Geography");

      bank.addQuestion(q1);
      bank.addQuestion(q2);
      bank.addQuestion(q3);

      assert bank.getQuestionCount() == 3 : "Question count mismatch";
      assert bank.findQuestionById("Q101") != null : "Question not found";

      System.out.println("✓ Test 4 PASSED: Add and retrieve questions");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 4 FAILED: " + e.getMessage());
    }

    // Test 5: Filter by category
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();

      for (int i = 1; i <= 5; i++) {
        if (i % 2 == 0) {
          bank.addQuestion(createTestQuestion("Q" + i, "Math"));
        } else {
          bank.addQuestion(createTestQuestion("Q" + i, "Science"));
        }
      }

      List<Question> mathQuestions = bank.getQuestionsByCategory("Math");
      assert mathQuestions.size() == 2 : "Category filter failed";

      System.out.println("✓ Test 5 PASSED: Filter by category");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 5 FAILED: " + e.getMessage());
    }

    // Test 6: Filter by difficulty
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();

      List<String> answers = new ArrayList<>();
      for (int i = 0; i < 4; i++) {
        answers.add("Answer " + (i + 1));
      }

      for (int i = 1; i <= 6; i++) {
        bank.addQuestion(new Question("Q" + i, "Question " + i, answers, 0, "Test", i % 3 + 1));
      }

      List<Question> easyQuestions = bank.getQuestionsByDifficulty(1);
      assert easyQuestions.size() >= 1 : "Difficulty filter failed";

      System.out.println("✓ Test 6 PASSED: Filter by difficulty");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 6 FAILED: " + e.getMessage());
    }

    // Test 7: Random question selection
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();

      for (int i = 1; i <= 10; i++) {
        bank.addQuestion(createTestQuestion("Q" + i, "Test"));
      }

      Question random = bank.getRandomQuestion();
      assert random != null : "Random question is null";

      System.out.println("✓ Test 7 PASSED: Random question selection");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 7 FAILED: " + e.getMessage());
    }

    // Test 8: Get random questions
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();

      for (int i = 1; i <= 20; i++) {
        bank.addQuestion(createTestQuestion("Q" + i, "Test"));
      }

      List<Question> randomQuestions = bank.getRandomQuestions(5);
      assert randomQuestions.size() == 5 : "Random questions selection failed";

      System.out.println("✓ Test 8 PASSED: Get multiple random questions");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 8 FAILED: " + e.getMessage());
    }

    // Test 9: Remove question
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();
      Question question = createTestQuestion("Q201", "Test");
      bank.addQuestion(question);

      assert bank.getQuestionCount() == 1 : "Add question failed";
      bank.removeQuestion("Q201");
      assert bank.getQuestionCount() == 0 : "Remove question failed";

      System.out.println("✓ Test 9 PASSED: Remove question");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 9 FAILED: " + e.getMessage());
    }
  }

  /**
   * Tests the Quiz class functionality.
   */
  private static void testQuizClass() {
    System.out.println("\n===== TEST: Quiz Class =====");

    // Test 10: Create quiz and record answers
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        questions.add(createTestQuestion("Q" + i, "Test"));
      }

      Quiz quiz = new Quiz("QUIZ001", "John Doe", "john@example.com", questions);

      assert quiz.getStudentName().equals("John Doe") : "Student name mismatch";
      assert quiz.getStudentEmail().equals("john@example.com") : "Email mismatch";
      assert quiz.getQuestions().size() == 5 : "Questions count mismatch";

      System.out.println("✓ Test 10 PASSED: Quiz creation");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 10 FAILED: " + e.getMessage());
    }

    // Test 11: Record and retrieve answers
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
        questions.add(createTestQuestion("Q" + i, "Test"));
      }

      Quiz quiz = new Quiz("QUIZ002", "Jane Doe", "jane@example.com", questions);
      quiz.recordAnswer(0, 2);
      quiz.recordAnswer(1, 1);

      assert quiz.getRecordedAnswer(0) == 2 : "Answer 1 not recorded correctly";
      assert quiz.getRecordedAnswer(1) == 1 : "Answer 2 not recorded correctly";

      System.out.println("✓ Test 11 PASSED: Record and retrieve answers");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 11 FAILED: " + e.getMessage());
    }

    // Test 12: Check if question is answered
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      questions.add(createTestQuestion("Q1", "Test"));

      Quiz quiz = new Quiz("QUIZ003", "Test User", "test@example.com", questions);
      quiz.recordAnswer(0, 0);

      assert quiz.isQuestionAnswered(0) : "Question marked as unanswered";

      System.out.println("✓ Test 12 PASSED: Check question answered status");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 12 FAILED: " + e.getMessage());
    }

    // Test 13: Calculate score
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      questions.add(createTestQuestion("Q1", "Test"));
      questions.add(createTestQuestion("Q2", "Test"));
      questions.add(createTestQuestion("Q3", "Test"));

      Quiz quiz = new Quiz("QUIZ004", "Test User", "test@example.com", questions);
      quiz.recordAnswer(0, 0); // All test questions have correct index 0
      quiz.recordAnswer(1, 0);
      quiz.recordAnswer(2, 0);

      quiz.finishQuiz();

      assert quiz.getTotalScore() == 300 : "Score calculation failed";
      assert quiz.getScorePercentage() == 100.0 : "Percentage calculation failed";

      System.out.println("✓ Test 13 PASSED: Score calculation");
      passedTests++;
    } catch (AssertionError e) {
      System.out.println("✗ Test 13 FAILED: " + e.getMessage());
    }
  }

  /**
   * Tests the FileManager class functionality.
   */
  private static void testFileManager() {
    System.out.println("\n===== TEST: FileManager Class =====");

    // Test 14: Load CSV file
    totalTests++;
    try {
      QuestionBank bank = FileManager.loadFromCSV("resources/questions/questions.csv");

      assert bank.getQuestionCount() > 0 : "CSV loading failed";
      assert bank.findQuestionById("Q001") != null : "Specific question not found";

      System.out.println("✓ Test 14 PASSED: Load CSV file");
      passedTests++;
    } catch (IOException e) {
      System.out.println("✗ Test 14 FAILED: " + e.getMessage());
    }

    // Test 15: Load JSON file
    totalTests++;
    try {
      QuestionBank bank = FileManager.loadFromJSON("resources/questions/questions.json");

      assert bank.getQuestionCount() > 0 : "JSON loading failed";

      System.out.println("✓ Test 15 PASSED: Load JSON file");
      passedTests++;
    } catch (IOException e) {
      System.out.println("✗ Test 15 FAILED: " + e.getMessage());
    }

    // Test 16: Save and load CSV
    totalTests++;
    try {
      QuestionBank originalBank = new QuestionBank();
      originalBank.addQuestion(createTestQuestion("TEST1", "Test"));
      originalBank.addQuestion(createTestQuestion("TEST2", "Test"));

      String testFile = "resources/questions/test_save.csv";
      FileManager.saveToCSV(testFile, originalBank);
      QuestionBank loadedBank = FileManager.loadFromCSV(testFile);

      assert loadedBank.getQuestionCount() == 2 : "Save/Load CSV mismatch";

      System.out.println("✓ Test 16 PASSED: Save and load CSV");
      passedTests++;
    } catch (IOException e) {
      System.out.println("✗ Test 16 FAILED: " + e.getMessage());
    }
  }

  /**
   * Tests the QuizGenerator class functionality.
   */
  private static void testQuizGenerator() {
    System.out.println("\n===== TEST: QuizGenerator Class =====");

    // Test 17: Generate random quiz
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();
      for (int i = 1; i <= 20; i++) {
        bank.addQuestion(createTestQuestion("Q" + i, "Test"));
      }

      QuizGenerator generator = new QuizGenerator(bank);
      Quiz quiz = generator.generateRandomQuiz("QUIZ_TEST1", "Student", "student@test.com", 5);

      assert quiz.getQuestions().size() == 5 : "Random quiz size mismatch";

      System.out.println("✓ Test 17 PASSED: Generate random quiz");
      passedTests++;
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("✗ Test 17 FAILED: " + e.getMessage());
    }

    // Test 18: Generate quiz by category
    totalTests++;
    try {
      QuestionBank bank = new QuestionBank();
      for (int i = 1; i <= 10; i++) {
        bank.addQuestion(createTestQuestion("Q" + i, "Math"));
      }

      QuizGenerator generator = new QuizGenerator(bank);
      Quiz quiz = generator.generateQuizByCategory("QUIZ_TEST2", "Student", "student@test.com",
          "Math", 5);

      assert quiz.getQuestions().size() == 5 : "Category quiz size mismatch";

      System.out.println("✓ Test 18 PASSED: Generate quiz by category");
      passedTests++;
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("✗ Test 18 FAILED: " + e.getMessage());
    }

    // Test 19: Generate mixed difficulty quiz
    totalTests++;
    try {
      QuestionBank bank = createMultiDifficultyBank();
      QuizGenerator generator = new QuizGenerator(bank);
      Quiz quiz = generator.generateMixedDifficultyQuiz("QUIZ_TEST3", "Student",
          "student@test.com", 9);

      assert quiz.getQuestions().size() == 9 : "Mixed difficulty quiz size mismatch";

      System.out.println("✓ Test 19 PASSED: Generate mixed difficulty quiz");
      passedTests++;
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("✗ Test 19 FAILED: " + e.getMessage());
    }
  }

  /**
   * Tests the QuizEvaluator class functionality.
   */
  private static void testQuizEvaluator() {
    System.out.println("\n===== TEST: QuizEvaluator Class =====");

    // Test 20: Evaluate quiz
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      for (int i = 0; i < 4; i++) {
        questions.add(createTestQuestion("Q" + i, "Test"));
      }

      Quiz quiz = new Quiz("EVAL_QUIZ1", "Student", "student@test.com", questions);
      quiz.recordAnswer(0, 0);
      quiz.recordAnswer(1, 0);
      quiz.recordAnswer(2, 0);

      Quiz evaluatedQuiz = QuizEvaluator.evaluateQuiz(quiz);

      assert evaluatedQuiz.getTotalScore() >= 0 : "Evaluation failed";

      System.out.println("✓ Test 20 PASSED: Evaluate quiz");
      passedTests++;
    } catch (Exception e) {
      System.out.println("✗ Test 20 FAILED: " + e.getMessage());
    }

    // Test 21: Get performance rating
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        questions.add(createTestQuestion("Q" + i, "Test"));
      }

      Quiz quiz = new Quiz("RATING_QUIZ1", "Student", "student@test.com", questions);
      for (int i = 0; i < 10; i++) {
        quiz.recordAnswer(i, 0);
      }
      quiz.finishQuiz();
      QuizEvaluator.evaluateQuiz(quiz);

      String rating = QuizEvaluator.getPerformanceRating(quiz);
      assert rating != null && !rating.isEmpty() : "Rating generation failed";

      System.out.println("✓ Test 21 PASSED: Get performance rating");
      passedTests++;
    } catch (Exception e) {
      System.out.println("✗ Test 21 FAILED: " + e.getMessage());
    }

    // Test 22: Identify weak areas
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      for (int i = 1; i <= 4; i++) {
        questions.add(new Question("Q" + i, "Question " + i, createAnswerList(), 1, "Category" + i,
            1));
      }

      Quiz quiz = new Quiz("WEAK_QUIZ1", "Student", "student@test.com", questions);
      quiz.recordAnswer(0, 0); // Wrong answer for category1
      quiz.recordAnswer(1, 1); // Correct for category2
      quiz.finishQuiz();
      QuizEvaluator.evaluateQuiz(quiz);

      List<String> weakAreas = QuizEvaluator.identifyWeakAreas(quiz);
      assert weakAreas != null : "Weak areas identification failed";

      System.out.println("✓ Test 22 PASSED: Identify weak areas");
      passedTests++;
    } catch (Exception e) {
      System.out.println("✗ Test 22 FAILED: " + e.getMessage());
    }

    // Test 23: Generate analysis report
    totalTests++;
    try {
      List<Question> questions = new ArrayList<>();
      questions.add(createTestQuestion("Q1", "Test"));

      Quiz quiz = new Quiz("REPORT_QUIZ1", "Student", "student@test.com", questions);
      quiz.recordAnswer(0, 0);
      quiz.finishQuiz();

      String report = QuizEvaluator.generateAnalysisReport(quiz);
      assert report != null && !report.isEmpty() : "Report generation failed";
      assert report.contains("DETAILED ANALYSIS") : "Report format incorrect";

      System.out.println("✓ Test 23 PASSED: Generate analysis report");
      passedTests++;
    } catch (Exception e) {
      System.out.println("✗ Test 23 FAILED: " + e.getMessage());
    }
  }

  /**
   * Tests the EmailService class functionality.
   */
  private static void testEmailService() {
    System.out.println("\n===== TEST: EmailService Class =====");

    // Test 24: Send quiz results email
    totalTests++;
    try {
      EmailService emailService = new EmailService("admin@quiz.com", "Admin");

      List<Question> questions = new ArrayList<>();
      questions.add(createTestQuestion("Q1", "Test"));

      Quiz quiz = new Quiz("EMAIL_QUIZ1", "John Doe", "john@example.com", questions);
      quiz.recordAnswer(0, 0);
      quiz.finishQuiz();

      boolean sent = emailService.sendQuizResultsEmail(quiz);
      assert sent : "Email sending failed";

      System.out.println("✓ Test 24 PASSED: Send quiz results email");
      passedTests++;
    } catch (Exception e) {
      System.out.println("✗ Test 24 FAILED: " + e.getMessage());
    }

    // Test 25: Email validation
    totalTests++;
    try {
      EmailService emailService = new EmailService("admin@quiz.com", "Admin");

      List<Question> questions = new ArrayList<>();
      questions.add(createTestQuestion("Q1", "Test"));

      Quiz quiz = new Quiz("INVALID_EMAIL_QUIZ", "Student", "invalid-email", questions);
      quiz.recordAnswer(0, 0);

      boolean sent = emailService.sendQuizResultsEmail(quiz);
      assert !sent : "Invalid email was accepted";

      System.out.println("✓ Test 25 PASSED: Email validation");
      passedTests++;
    } catch (Exception e) {
      System.out.println("✗ Test 25 FAILED: " + e.getMessage());
    }
  }

  /**
   * Tests integration between components.
   */
  private static void testIntegration() {
    System.out.println("\n===== TEST: Integration Tests =====");

    // Test 26: Full quiz workflow
    totalTests++;
    try {
      QuestionBank bank = FileManager.loadFromCSV("resources/questions/questions.csv");
      QuizGenerator generator = new QuizGenerator(bank);
      Quiz quiz = generator.generateRandomQuiz("INTEGRATION1", "Test User", "test@example.com",
          5);

      // Simulate answers
      for (int i = 0; i < 5; i++) {
        quiz.recordAnswer(i, (i + 1) % 4);
      }

      quiz.finishQuiz();
      QuizEvaluator.evaluateQuiz(quiz);
      String report = QuizEvaluator.generateAnalysisReport(quiz);

      assert quiz.getTotalScore() >= 0 : "Workflow evaluation failed";
      assert !report.isEmpty() : "Report generation failed";

      System.out.println("✓ Test 26 PASSED: Full quiz workflow");
      passedTests++;
    } catch (Exception e) {
      System.out.println("✗ Test 26 FAILED: " + e.getMessage());
    }

    // Test 27: Category-based quiz with evaluation
    totalTests++;
    try {
      QuestionBank bank = FileManager.loadFromCSV("resources/questions/questions.csv");
      List<String> categories = bank.getAllCategories();

      if (!categories.isEmpty()) {
        QuizGenerator generator = new QuizGenerator(bank);
        String category = categories.get(0);

        Quiz quiz = generator.generateQuizByCategory("CATEGORY_QUIZ1", "Student",
            "student@example.com", category, 3);

        assert quiz.getQuestions().size() == 3 : "Category quiz creation failed";

        System.out.println("✓ Test 27 PASSED: Category-based quiz with evaluation");
        passedTests++;
      } else {
        System.out.println("⊘ Test 27 SKIPPED: No categories in test data");
      }
    } catch (Exception e) {
      System.out.println("✗ Test 27 FAILED: " + e.getMessage());
    }
  }

  /**
   * Helper method to create a test question.
   *
   * @param id the question ID
   * @param category the question category
   * @return a test Question object
   */
  private static Question createTestQuestion(String id, String category) {
    List<String> answers = new ArrayList<>();
    answers.add("Answer A");
    answers.add("Answer B");
    answers.add("Answer C");
    answers.add("Answer D");

    return new Question(id, "Test question " + id, answers, 0, category, 1);
  }

  /**
   * Helper method to create a list of answers.
   *
   * @return a list of four answers
   */
  private static List<String> createAnswerList() {
    List<String> answers = new ArrayList<>();
    answers.add("Option 1");
    answers.add("Option 2");
    answers.add("Option 3");
    answers.add("Option 4");
    return answers;
  }

  /**
   * Helper method to create a multi-difficulty question bank.
   *
   * @return a QuestionBank with questions of varying difficulty
   */
  private static QuestionBank createMultiDifficultyBank() {
    QuestionBank bank = new QuestionBank();

    for (int i = 1; i <= 3; i++) {
      List<String> answers = createAnswerList();
      bank.addQuestion(new Question("EASY_" + i, "Easy question " + i, answers, 0, "Test", 1));
    }

    for (int i = 1; i <= 3; i++) {
      List<String> answers = createAnswerList();
      bank.addQuestion(
          new Question("MEDIUM_" + i, "Medium question " + i, answers, 0, "Test", 2));
    }

    for (int i = 1; i <= 3; i++) {
      List<String> answers = createAnswerList();
      bank.addQuestion(new Question("HARD_" + i, "Hard question " + i, answers, 0, "Test", 3));
    }

    return bank;
  }

  /**
   * Prints the test summary.
   */
  private static void printSummary() {
    System.out.println("\n====================================");
    System.out.println("  TEST SUMMARY");
    System.out.println("====================================");
    System.out.println("Total Tests: " + totalTests);
    System.out.println("Passed: " + passedTests);
    System.out.println("Failed: " + (totalTests - passedTests));
    System.out.println("Success Rate: " + String.format("%.2f%%", (double) passedTests / totalTests * 100));
    System.out.println("====================================\n");

    if (passedTests == totalTests) {
      System.out.println("✓ ALL TESTS PASSED!");
    } else {
      System.out.println("✗ SOME TESTS FAILED");
    }
  }
}

