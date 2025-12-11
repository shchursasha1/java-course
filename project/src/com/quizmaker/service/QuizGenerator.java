package com.quizmaker.service;

import com.quizmaker.model.Question;
import com.quizmaker.model.Quiz;
import com.quizmaker.model.QuestionBank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates quiz instances from the question bank.
 *
 * This class provides various methods to create quizzes with specific
 * compositions based on categories, difficulty levels, or random selection.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class QuizGenerator {

  private QuestionBank questionBank;

  /**
   * Constructs a QuizGenerator with a question bank.
   *
   * @param questionBank the QuestionBank to generate quizzes from
   */
  public QuizGenerator(QuestionBank questionBank) {
    this.questionBank = questionBank;
  }

  /**
   * Generates a random quiz with specified number of questions.
   *
   * @param quizId unique identifier for the quiz
   * @param studentName name of the student
   * @param studentEmail email of the student
   * @param questionCount number of questions to include
   * @return a randomly generated Quiz
   */
  public Quiz generateRandomQuiz(String quizId, String studentName, String studentEmail,
      int questionCount) {
    if (quizId == null || quizId.isEmpty()) {
      throw new IllegalArgumentException("Quiz ID cannot be null or empty");
    }
    if (studentName == null || studentName.isEmpty()) {
      throw new IllegalArgumentException("Student name cannot be null or empty");
    }

    List<Question> randomQuestions = questionBank.getRandomQuestions(questionCount);

    if (randomQuestions.isEmpty()) {
      throw new IllegalStateException("Not enough questions in bank for quiz");
    }

    return new Quiz(quizId, studentName, studentEmail, randomQuestions);
  }

  /**
   * Generates a quiz from a specific category.
   *
   * @param quizId unique identifier for the quiz
   * @param studentName name of the student
   * @param studentEmail email of the student
   * @param category the category to select questions from
   * @param questionCount number of questions to include
   * @return a Quiz with questions from specified category
   */
  public Quiz generateQuizByCategory(String quizId, String studentName, String studentEmail,
      String category, int questionCount) {
    if (category == null || category.isEmpty()) {
      throw new IllegalArgumentException("Category cannot be null or empty");
    }

    List<Question> categoryQuestions = questionBank.getQuestionsByCategory(category);

    if (categoryQuestions.isEmpty()) {
      throw new IllegalStateException("No questions found in category: " + category);
    }

    // Shuffle and take the required number
    List<Question> selectedQuestions = shuffleAndSelect(categoryQuestions, questionCount);

    return new Quiz(quizId, studentName, studentEmail, selectedQuestions);
  }

  /**
   * Generates a quiz with specific difficulty level.
   *
   * @param quizId unique identifier for the quiz
   * @param studentName name of the student
   * @param studentEmail email of the student
   * @param difficulty the difficulty level (1-3)
   * @param questionCount number of questions to include
   * @return a Quiz with questions of specified difficulty
   */
  public Quiz generateQuizByDifficulty(String quizId, String studentName, String studentEmail,
      int difficulty, int questionCount) {
    if (difficulty < 1 || difficulty > 3) {
      throw new IllegalArgumentException("Difficulty must be between 1 and 3");
    }

    List<Question> difficultyQuestions = questionBank.getQuestionsByDifficulty(difficulty);

    if (difficultyQuestions.isEmpty()) {
      throw new IllegalStateException("No questions found with difficulty: " + difficulty);
    }

    List<Question> selectedQuestions = shuffleAndSelect(difficultyQuestions, questionCount);

    return new Quiz(quizId, studentName, studentEmail, selectedQuestions);
  }

  /**
   * Generates a mixed difficulty quiz.
   *
   * Distributes questions evenly across easy, medium, and hard levels.
   *
   * @param quizId unique identifier for the quiz
   * @param studentName name of the student
   * @param studentEmail email of the student
   * @param totalQuestions total number of questions to include
   * @return a Quiz with mixed difficulty questions
   */
  public Quiz generateMixedDifficultyQuiz(String quizId, String studentName,
      String studentEmail, int totalQuestions) {
    List<Question> selectedQuestions = new ArrayList<>();

    int perDifficulty = totalQuestions / 3;
    int remaining = totalQuestions % 3;

    // Add easy questions
    List<Question> easyQuestions = questionBank.getQuestionsByDifficulty(1);
    selectedQuestions.addAll(shuffleAndSelect(easyQuestions, perDifficulty));

    // Add medium questions
    List<Question> mediumQuestions = questionBank.getQuestionsByDifficulty(2);
    selectedQuestions.addAll(shuffleAndSelect(mediumQuestions, perDifficulty));

    // Add hard questions
    List<Question> hardQuestions = questionBank.getQuestionsByDifficulty(3);
    selectedQuestions.addAll(
        shuffleAndSelect(hardQuestions, perDifficulty + remaining));

    if (selectedQuestions.isEmpty()) {
      throw new IllegalStateException("Not enough questions for mixed difficulty quiz");
    }

    return new Quiz(quizId, studentName, studentEmail, selectedQuestions);
  }

  /**
   * Generates a quiz with category and difficulty filters.
   *
   * @param quizId unique identifier for the quiz
   * @param studentName name of the student
   * @param studentEmail email of the student
   * @param category the category filter
   * @param difficulty the difficulty filter
   * @param questionCount number of questions to include
   * @return a Quiz with filtered questions
   */
  public Quiz generateFilteredQuiz(String quizId, String studentName, String studentEmail,
      String category, int difficulty, int questionCount) {
    List<Question> filteredQuestions = questionBank
        .getQuestionsByCategoryAndDifficulty(category, difficulty);

    if (filteredQuestions.isEmpty()) {
      throw new IllegalStateException(
          "No questions found for category: " + category + " and difficulty: " + difficulty);
    }

    List<Question> selectedQuestions = shuffleAndSelect(filteredQuestions, questionCount);

    return new Quiz(quizId, studentName, studentEmail, selectedQuestions);
  }

  /**
   * Generates a comprehensive quiz with balanced content.
   *
   * Creates a quiz with balanced distribution across categories and difficulties.
   *
   * @param quizId unique identifier for the quiz
   * @param studentName name of the student
   * @param studentEmail email of the student
   * @param totalQuestions total number of questions to include
   * @return a balanced Quiz
   */
  public Quiz generateBalancedQuiz(String quizId, String studentName, String studentEmail,
      int totalQuestions) {
    List<Question> selectedQuestions = new ArrayList<>();
    List<String> categories = questionBank.getAllCategories();

    if (categories.isEmpty()) {
      throw new IllegalStateException("No categories available in question bank");
    }

    int perCategory = totalQuestions / categories.size();
    int remaining = totalQuestions % categories.size();

    for (int i = 0; i < categories.size(); i++) {
      String category = categories.get(i);
      int count = perCategory + (i < remaining ? 1 : 0);
      List<Question> categoryQuestions = questionBank.getQuestionsByCategory(category);
      selectedQuestions.addAll(shuffleAndSelect(categoryQuestions, count));
    }

    if (selectedQuestions.size() < totalQuestions) {
      // Fill remaining with random questions if not enough by category
      List<Question> allQuestions = questionBank.getAllQuestions();
      selectedQuestions.addAll(
          shuffleAndSelect(allQuestions, totalQuestions - selectedQuestions.size()));
    }

    return new Quiz(quizId, studentName, studentEmail, selectedQuestions);
  }

  /**
   * Shuffles and selects a subset of questions.
   *
   * Implements Fisher-Yates shuffle algorithm.
   *
   * @param questions the list of questions to select from
   * @param count the number of questions to select
   * @return shuffled list of selected questions
   */
  private List<Question> shuffleAndSelect(List<Question> questions, int count) {
    List<Question> copy = new ArrayList<>(questions);
    int validCount = Math.min(count, copy.size());
    List<Question> result = new ArrayList<>();

    // Fisher-Yates shuffle
    for (int i = copy.size() - 1; i > 0; i--) {
      int j = (int) (Math.random() * (i + 1));
      Question temp = copy.get(i);
      copy.set(i, copy.get(j));
      copy.set(j, temp);
    }

    // Take first validCount questions
    for (int i = 0; i < validCount; i++) {
      result.add(copy.get(i));
    }

    return result;
  }

  /**
   * Sets a new question bank for this generator.
   *
   * @param questionBank the new QuestionBank to use
   */
  public void setQuestionBank(QuestionBank questionBank) {
    this.questionBank = questionBank;
  }

  /**
   * Gets the current question bank.
   *
   * @return the current QuestionBank
   */
  public QuestionBank getQuestionBank() {
    return questionBank;
  }
}

