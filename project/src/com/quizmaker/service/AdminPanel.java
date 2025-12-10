package com.quizmaker.service;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Administrative interface for managing questions in the question bank.
 *
 * This class provides functions to add, edit, delete, and view questions.
 * It offers both programmatic and interactive command-line interfaces.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class AdminPanel {

  private QuestionBank questionBank;
  private BufferedReader reader;

  /**
   * Constructs an AdminPanel with a question bank.
   *
   * @param questionBank the QuestionBank to manage
   */
  public AdminPanel(QuestionBank questionBank) {
    this.questionBank = questionBank;
    this.reader = new BufferedReader(new InputStreamReader(System.in));
  }

  /**
   * Starts the interactive admin menu.
   */
  public void startInteractiveMode() {
    boolean running = true;

    while (running) {
      displayMenu();
      try {
        int choice = getIntInput();
        running = handleMenuChoice(choice);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number.");
      } catch (IOException e) {
        System.out.println("Error reading input: " + e.getMessage());
      }
    }
  }

  /**
   * Displays the admin menu options.
   */
  private void displayMenu() {
    System.out.println("\n========== ADMIN PANEL ==========");
    System.out.println("1. Add New Question");
    System.out.println("2. Edit Question");
    System.out.println("3. Delete Question");
    System.out.println("4. View All Questions");
    System.out.println("5. View Questions by Category");
    System.out.println("6. Save Questions to CSV");
    System.out.println("7. Save Questions to JSON");
    System.out.println("8. Load Questions from CSV");
    System.out.println("9. Load Questions from JSON");
    System.out.println("10. View Bank Statistics");
    System.out.println("0. Exit");
    System.out.print("Enter your choice: ");
  }

  /**
   * Handles menu selection.
   *
   * @param choice the menu choice
   * @return false to exit, true to continue
   */
  private boolean handleMenuChoice(int choice) throws IOException {
    switch (choice) {
      case 1:
        addNewQuestion();
        break;
      case 2:
        editQuestion();
        break;
      case 3:
        deleteQuestion();
        break;
      case 4:
        viewAllQuestions();
        break;
      case 5:
        viewQuestionsByCategory();
        break;
      case 6:
        saveToCSV();
        break;
      case 7:
        saveToJSON();
        break;
      case 8:
        loadFromCSV();
        break;
      case 9:
        loadFromJSON();
        break;
      case 10:
        viewStatistics();
        break;
      case 0:
        System.out.println("Exiting Admin Panel...");
        return false;
      default:
        System.out.println("Invalid choice. Please try again.");
    }
    return true;
  }

  /**
   * Adds a new question interactively.
   */
  private void addNewQuestion() throws IOException {
    System.out.println("\n===== ADD NEW QUESTION =====");

    System.out.print("Question ID: ");
    String id = reader.readLine().trim();

    System.out.print("Question Text: ");
    String text = reader.readLine().trim();

    List<String> answers = new ArrayList<>();
    System.out.println("Enter 4 answer options:");
    for (int i = 1; i <= 4; i++) {
      System.out.print("  Answer " + i + ": ");
      answers.add(reader.readLine().trim());
    }

    System.out.print("Correct Answer Index (0-3): ");
    int correctIndex = getIntInput();

    System.out.print("Category: ");
    String category = reader.readLine().trim();

    System.out.print("Difficulty (1-Easy, 2-Medium, 3-Hard): ");
    int difficulty = getIntInput();

    Question question = new Question(id, text, answers, correctIndex, category, difficulty);
    questionBank.addQuestion(question);
    System.out.println("Question added successfully!");
  }

  /**
   * Edits an existing question.
   */
  private void editQuestion() throws IOException {
    System.out.print("\nEnter Question ID to edit: ");
    String id = reader.readLine().trim();

    Question question = questionBank.findQuestionById(id);
    if (question == null) {
      System.out.println("Question not found!");
      return;
    }

    System.out.println("\nCurrent Question Details:");
    System.out.println(question);

    System.out.print("\nEdit Question Text? (y/n): ");
    if (reader.readLine().trim().equalsIgnoreCase("y")) {
      System.out.print("New Question Text: ");
      question.setQuestionText(reader.readLine().trim());
    }

    System.out.print("Edit Answers? (y/n): ");
    if (reader.readLine().trim().equalsIgnoreCase("y")) {
      List<String> answers = new ArrayList<>();
      System.out.println("Enter new answer options:");
      for (int i = 1; i <= 4; i++) {
        System.out.print("  Answer " + i + ": ");
        answers.add(reader.readLine().trim());
      }
      question.setAnswers(answers);
    }

    System.out.print("Edit Correct Answer Index? (y/n): ");
    if (reader.readLine().trim().equalsIgnoreCase("y")) {
      System.out.print("New Correct Index (0-3): ");
      question.setCorrectAnswerIndex(getIntInput());
    }

    System.out.print("Edit Difficulty? (y/n): ");
    if (reader.readLine().trim().equalsIgnoreCase("y")) {
      System.out.print("New Difficulty (1-3): ");
      question.setDifficulty(getIntInput());
    }

    System.out.println("Question updated successfully!");
  }

  /**
   * Deletes a question by ID.
   */
  private void deleteQuestion() throws IOException {
    System.out.print("\nEnter Question ID to delete: ");
    String id = reader.readLine().trim();

    if (questionBank.removeQuestion(id)) {
      System.out.println("Question deleted successfully!");
    } else {
      System.out.println("Question not found!");
    }
  }

  /**
   * Displays all questions.
   */
  private void viewAllQuestions() {
    System.out.println("\n===== ALL QUESTIONS =====");
    List<Question> questions = questionBank.getAllQuestions();

    if (questions.isEmpty()) {
      System.out.println("No questions in bank.");
      return;
    }

    for (int i = 0; i < questions.size(); i++) {
      System.out.println("\n" + (i + 1) + ". " + questions.get(i));
    }
  }

  /**
   * Displays questions by category.
   */
  private void viewQuestionsByCategory() throws IOException {
    System.out.print("\nEnter Category: ");
    String category = reader.readLine().trim();

    List<Question> questions = questionBank.getQuestionsByCategory(category);

    if (questions.isEmpty()) {
      System.out.println("No questions found in category: " + category);
      return;
    }

    System.out.println("\n===== QUESTIONS IN CATEGORY: " + category.toUpperCase() + " =====");
    for (int i = 0; i < questions.size(); i++) {
      System.out.println("\n" + (i + 1) + ". " + questions.get(i));
    }
  }

  /**
   * Saves questions to a CSV file.
   */
  private void saveToCSV() throws IOException {
    System.out.print("\nEnter file path (e.g., resources/questions/questions.csv): ");
    String filePath = reader.readLine().trim();

    try {
      FileManager.saveToCSV(filePath, questionBank);
      System.out.println("Questions saved to CSV successfully!");
    } catch (IOException e) {
      System.err.println("Error saving to CSV: " + e.getMessage());
    }
  }

  /**
   * Saves questions to a JSON file.
   */
  private void saveToJSON() throws IOException {
    System.out.print("\nEnter file path (e.g., resources/questions/questions.json): ");
    String filePath = reader.readLine().trim();

    try {
      FileManager.saveToJSON(filePath, questionBank);
      System.out.println("Questions saved to JSON successfully!");
    } catch (IOException e) {
      System.err.println("Error saving to JSON: " + e.getMessage());
    }
  }

  /**
   * Loads questions from a CSV file.
   */
  private void loadFromCSV() throws IOException {
    System.out.print("\nEnter absolute path to CSV file: ");
    String filePath = reader.readLine().trim();

    try {
      QuestionBank loadedBank = FileManager.loadFromCSV(filePath);
      System.out.print("Append to existing questions? (y/n): ");
      if (!reader.readLine().trim().equalsIgnoreCase("y")) {
        questionBank.clear();
      }

      for (Question q : loadedBank.getAllQuestions()) {
        questionBank.addQuestion(q);
      }

      System.out.println("Questions loaded from CSV successfully!");
      System.out.println("Total questions in bank: " + questionBank.getQuestionCount());
    } catch (IOException e) {
      System.err.println("Error loading from CSV: " + e.getMessage());
    }
  }

  /**
   * Loads questions from a JSON file.
   */
  private void loadFromJSON() throws IOException {
    System.out.print("\nEnter absolute path to JSON file: ");
    String filePath = reader.readLine().trim();

    try {
      QuestionBank loadedBank = FileManager.loadFromJSON(filePath);
      System.out.print("Append to existing questions? (y/n): ");
      if (!reader.readLine().trim().equalsIgnoreCase("y")) {
        questionBank.clear();
      }

      for (Question q : loadedBank.getAllQuestions()) {
        questionBank.addQuestion(q);
      }

      System.out.println("Questions loaded from JSON successfully!");
      System.out.println("Total questions in bank: " + questionBank.getQuestionCount());
    } catch (IOException e) {
      System.err.println("Error loading from JSON: " + e.getMessage());
    }
  }

  /**
   * Displays statistics about the question bank.
   */
  private void viewStatistics() {
    System.out.println("\n===== BANK STATISTICS =====");
    System.out.println(questionBank.toString());

    // Additional statistics
    for (String category : questionBank.getAllCategories()) {
      int easyCount = questionBank.getQuestionsByCategory(category).stream()
          .filter(q -> q.getDifficulty() == 1).toArray().length;
      int mediumCount = questionBank.getQuestionsByCategory(category).stream()
          .filter(q -> q.getDifficulty() == 2).toArray().length;
      int hardCount = questionBank.getQuestionsByCategory(category).stream()
          .filter(q -> q.getDifficulty() == 3).toArray().length;

      System.out.println("  " + category + " Details:");
      System.out.println("    Easy: " + easyCount + ", Medium: " + mediumCount + ", Hard: "
          + hardCount);
    }
  }

  /**
   * Gets integer input from the user.
   *
   * @return the integer value entered by the user
   * @throws IOException if input cannot be read
   * @throws NumberFormatException if input is not a valid integer
   */
  private int getIntInput() throws IOException, NumberFormatException {
    return Integer.parseInt(reader.readLine().trim());
  }
}

