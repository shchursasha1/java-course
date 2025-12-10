package com.quizmaker.ui;

import com.quizmaker.model.Question;
import com.quizmaker.model.Quiz;
import com.quizmaker.model.QuestionBank;
import com.quizmaker.service.AdminPanel;
import com.quizmaker.service.EmailService;
import com.quizmaker.service.FileManager;
import com.quizmaker.service.QuizEvaluator;
import com.quizmaker.service.QuizGenerator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for the Quiz Maker system.
 *
 * This class provides the main user interface and orchestrates all components
 * of the quiz application including question management, quiz generation,
 * quiz taking, and result evaluation.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class QuizApplication {

  private QuestionBank questionBank;
  private QuizGenerator quizGenerator;
  private EmailService emailService;
  private AdminPanel adminPanel;
  private BufferedReader reader;
  private List<Quiz> completedQuizzes;

  /**
   * Constructs the main Quiz Application.
   */
  public QuizApplication() {
    this.questionBank = new QuestionBank();
    this.quizGenerator = new QuizGenerator(questionBank);
    this.emailService = new EmailService("admin@quizmaker.com", "Quiz Administrator");
    this.adminPanel = new AdminPanel(questionBank);
    this.reader = new BufferedReader(new InputStreamReader(System.in));
    this.completedQuizzes = new ArrayList<>();
  }

  /**
   * Starts the main application loop.
   */
  public void start() {
    System.out.println("\n========================================");
    System.out.println("  Welcome to Quiz Maker Application!");
    System.out.println("========================================\n");

    boolean running = true;
    while (running) {
      displayMainMenu();
      try {
        int choice = getIntInput();
        running = handleMainMenuChoice(choice);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number.");
      } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
      }
    }

    System.out.println("\nThank you for using Quiz Maker!");
  }

  /**
   * Displays the main menu.
   */
  private void displayMainMenu() {
    System.out.println("\n========== MAIN MENU ==========");
    System.out.println("1. Take Quiz");
    System.out.println("2. Admin Panel");
    System.out.println("3. View Completed Quizzes");
    System.out.println("4. Test Mode (Load from File)");
    System.out.println("0. Exit");
    System.out.print("Enter your choice: ");
  }

  /**
   * Handles main menu selection.
   *
   * @param choice the menu choice
   * @return false to exit, true to continue
   */
  private boolean handleMainMenuChoice(int choice) throws IOException {
    switch (choice) {
      case 1:
        takeQuiz();
        break;
      case 2:
        adminPanel.startInteractiveMode();
        break;
      case 3:
        viewCompletedQuizzes();
        break;
      case 4:
        testMode();
        break;
      case 0:
        return false;
      default:
        System.out.println("Invalid choice. Please try again.");
    }
    return true;
  }

  /**
   * Handles quiz taking process.
   */
  private void takeQuiz() throws IOException {
    System.out.println("\n===== TAKE A QUIZ =====");

    if (questionBank.getQuestionCount() == 0) {
      System.out.println("No questions available. Please load questions from Admin Panel.");
      return;
    }

    System.out.print("Enter your name: ");
    String studentName = reader.readLine().trim();

    System.out.print("Enter your email: ");
    String studentEmail = reader.readLine().trim();

    System.out.println("\nQuiz Generation Options:");
    System.out.println("1. Random Quiz");
    System.out.println("2. Quiz by Category");
    System.out.println("3. Quiz by Difficulty");
    System.out.println("4. Mixed Difficulty Quiz");
    System.out.println("5. Balanced Quiz");
    System.out.print("Select option: ");

    int quizType = getIntInput();
    Quiz quiz = null;

    try {
      switch (quizType) {
        case 1:
          System.out.print("Number of questions: ");
          int count = getIntInput();
          quiz = quizGenerator.generateRandomQuiz("QUIZ_" + System.currentTimeMillis(),
              studentName, studentEmail, count);
          break;
        case 2:
          System.out.println("Available categories: " + questionBank.getAllCategories());
          System.out.print("Enter category: ");
          String category = reader.readLine().trim();
          System.out.print("Number of questions: ");
          count = getIntInput();
          quiz = quizGenerator.generateQuizByCategory("QUIZ_" + System.currentTimeMillis(),
              studentName, studentEmail, category, count);
          break;
        case 3:
          System.out.print("Difficulty (1=Easy, 2=Medium, 3=Hard): ");
          int difficulty = getIntInput();
          System.out.print("Number of questions: ");
          count = getIntInput();
          quiz = quizGenerator.generateQuizByDifficulty("QUIZ_" + System.currentTimeMillis(),
              studentName, studentEmail, difficulty, count);
          break;
        case 4:
          System.out.print("Total questions: ");
          count = getIntInput();
          quiz = quizGenerator.generateMixedDifficultyQuiz("QUIZ_" + System.currentTimeMillis(),
              studentName, studentEmail, count);
          break;
        case 5:
          System.out.print("Total questions: ");
          count = getIntInput();
          quiz = quizGenerator.generateBalancedQuiz("QUIZ_" + System.currentTimeMillis(),
              studentName, studentEmail, count);
          break;
        default:
          System.out.println("Invalid option.");
          return;
      }

      if (quiz != null) {
        administrateQuiz(quiz);
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Administrates the quiz process (asking questions and recording answers).
   *
   * @param quiz the Quiz to administrate
   */
  private void administrateQuiz(Quiz quiz) throws IOException {
    System.out.println("\n===== QUIZ IN PROGRESS =====");
    System.out.println("Quiz ID: " + quiz.getQuizId());
    System.out.println("Total Questions: " + quiz.getQuestions().size());
    System.out.println("================\n");

    List<Question> questions = quiz.getQuestions();

    for (int i = 0; i < questions.size(); i++) {
      Question question = questions.get(i);

      System.out.println("\nQuestion " + (i + 1) + " of " + questions.size());
      System.out.println(question.getQuestionText());

      List<String> answers = question.getAnswers();
      for (int j = 0; j < answers.size(); j++) {
        System.out.println("  " + (j + 1) + ") " + answers.get(j));
      }

      System.out.print("Your answer (1-4, or 0 to skip): ");
      int answerChoice = getIntInput();

      if (answerChoice >= 1 && answerChoice <= 4) {
        quiz.recordAnswer(i, answerChoice - 1);
      }
    }

    // Finish and evaluate quiz
    quiz.finishQuiz();
    Quiz evaluatedQuiz = QuizEvaluator.evaluateQuiz(quiz);
    completedQuizzes.add(evaluatedQuiz);

    // Display results
    System.out.println("\n===== QUIZ COMPLETED =====");
    System.out.println(evaluatedQuiz.toString());

    // Offer to send results via email
    System.out.print("\nSend results to email? (y/n): ");
    if (reader.readLine().trim().equalsIgnoreCase("y")) {
      emailService.sendQuizResultsEmail(evaluatedQuiz);
      System.out.println("Results sent!");
    }

    // Save results to file
    try {
      FileManager.saveQuizResults("resources/quiz_results.txt", evaluatedQuiz);
      System.out.println("Results saved to file.");
    } catch (IOException e) {
      System.err.println("Could not save results: " + e.getMessage());
    }
  }

  /**
   * Views all completed quizzes with their results.
   */
  private void viewCompletedQuizzes() {
    System.out.println("\n===== COMPLETED QUIZZES =====");

    if (completedQuizzes.isEmpty()) {
      System.out.println("No completed quizzes.");
      return;
    }

    for (int i = 0; i < completedQuizzes.size(); i++) {
      Quiz quiz = completedQuizzes.get(i);
      System.out.println("\n" + (i + 1) + ". Quiz ID: " + quiz.getQuizId());
      System.out.println("   Student: " + quiz.getStudentName());
      System.out.println("   Score: " + quiz.getTotalScore() + "/" + quiz.getMaxScore());
      System.out.println("   Percentage: " + String.format("%.2f%%", quiz.getScorePercentage()));
      System.out.println("   Rating: " + QuizEvaluator.getPerformanceRating(quiz));
    }

    System.out.print("\nView detailed analysis for a quiz? (y/n): ");
    try {
      if (reader.readLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter quiz number: ");
        int quizNum = getIntInput() - 1;
        if (quizNum >= 0 && quizNum < completedQuizzes.size()) {
          System.out.println(QuizEvaluator.generateAnalysisReport(completedQuizzes.get(quizNum)));
        }
      }
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Test mode for loading questions and running tests from files.
   */
  private void testMode() throws IOException {
    System.out.println("\n===== TEST MODE =====");
    System.out.println("1. Load CSV File");
    System.out.println("2. Load JSON File");
    System.out.print("Select option: ");

    int choice = getIntInput();
    System.out.print("Enter file path: ");
    String filePath = reader.readLine().trim();

    try {
      QuestionBank loadedBank;
      if (choice == 1) {
        loadedBank = FileManager.loadFromCSV(filePath);
      } else if (choice == 2) {
        loadedBank = FileManager.loadFromJSON(filePath);
      } else {
        System.out.println("Invalid choice.");
        return;
      }

      System.out.println("Loaded " + loadedBank.getQuestionCount() + " questions.");
      System.out.println(loadedBank.toString());

      // Ask to use loaded questions
      System.out.print("Use these questions for quiz? (y/n): ");
      if (reader.readLine().trim().equalsIgnoreCase("y")) {
        questionBank.clear();
        for (Question q : loadedBank.getAllQuestions()) {
          questionBank.addQuestion(q);
        }
        System.out.println("Questions loaded successfully!");
      }
    } catch (IOException e) {
      System.err.println("Error loading file: " + e.getMessage());
    }
  }

  /**
   * Gets integer input from the user.
   *
   * @return the integer value
   * @throws IOException if input cannot be read
   * @throws NumberFormatException if input is not a valid integer
   */
  private int getIntInput() throws IOException, NumberFormatException {
    return Integer.parseInt(reader.readLine().trim());
  }

  /**
   * Main entry point for the application.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    QuizApplication app = new QuizApplication();
    app.start();
  }
}

