package com.quizmaker.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages a collection of questions with filtering and retrieval capabilities.
 *
 * This class provides functionality to store, filter, and retrieve questions
 * by various criteria such as category, difficulty level, and random selection.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class QuestionBank {

  private List<Question> questions;
  private Map<String, Integer> categoryCount;

  /**
   * Default constructor initializing an empty question bank.
   */
  public QuestionBank() {
    this.questions = new ArrayList<>();
    this.categoryCount = new HashMap<>();
  }

  /**
   * Adds a question to the bank.
   *
   * @param question the question to add
   */
  public void addQuestion(Question question) {
    if (question != null) {
      questions.add(question);
      updateCategoryCount(question.getCategory(), 1);
    }
  }

  /**
   * Removes a question from the bank by its ID.
   *
   * @param questionId the ID of the question to remove
   * @return true if the question was found and removed
   */
  public boolean removeQuestion(String questionId) {
    Question question = findQuestionById(questionId);
    if (question != null) {
      questions.remove(question);
      updateCategoryCount(question.getCategory(), -1);
      return true;
    }
    return false;
  }

  /**
   * Finds a question by its ID.
   *
   * @param questionId the ID to search for
   * @return the Question object or null if not found
   */
  public Question findQuestionById(String questionId) {
    return questions.stream()
        .filter(q -> q.getId().equals(questionId))
        .findFirst()
        .orElse(null);
  }

  /**
   * Gets all questions in the bank.
   *
   * @return list of all questions
   */
  public List<Question> getAllQuestions() {
    return new ArrayList<>(questions);
  }

  /**
   * Gets questions filtered by category.
   *
   * @param category the category to filter by
   * @return list of questions in the specified category
   */
  public List<Question> getQuestionsByCategory(String category) {
    return questions.stream()
        .filter(q -> q.getCategory().equalsIgnoreCase(category))
        .collect(Collectors.toList());
  }

  /**
   * Gets questions filtered by difficulty level.
   *
   * @param difficulty the difficulty level (1-3)
   * @return list of questions with the specified difficulty
   */
  public List<Question> getQuestionsByDifficulty(int difficulty) {
    return questions.stream()
        .filter(q -> q.getDifficulty() == difficulty)
        .collect(Collectors.toList());
  }

  /**
   * Gets questions filtered by both category and difficulty.
   *
   * @param category the category filter
   * @param difficulty the difficulty filter
   * @return list of filtered questions
   */
  public List<Question> getQuestionsByCategoryAndDifficulty(String category, int difficulty) {
    return questions.stream()
        .filter(q -> q.getCategory().equalsIgnoreCase(category) && q.getDifficulty() == difficulty)
        .collect(Collectors.toList());
  }

  /**
   * Gets a random question from the bank.
   *
   * @return a random Question object or null if bank is empty
   */
  public Question getRandomQuestion() {
    if (questions.isEmpty()) {
      return null;
    }
    int randomIndex = (int) (Math.random() * questions.size());
    return questions.get(randomIndex);
  }

  /**
   * Gets a specified number of random questions from the bank.
   *
   * @param count the number of questions to retrieve
   * @return list of random questions
   */
  public List<Question> getRandomQuestions(int count) {
    List<Question> randomQuestions = new ArrayList<>();
    int validCount = Math.min(count, questions.size());

    List<Integer> indices = new ArrayList<>();
    for (int i = 0; i < questions.size(); i++) {
      indices.add(i);
    }

    // Fisher-Yates shuffle
    for (int i = indices.size() - 1; i > 0; i--) {
      int j = (int) (Math.random() * (i + 1));
      int temp = indices.get(i);
      indices.set(i, indices.get(j));
      indices.set(j, temp);
    }

    for (int i = 0; i < validCount; i++) {
      randomQuestions.add(questions.get(indices.get(i)));
    }

    return randomQuestions;
  }

  /**
   * Gets the total number of questions in the bank.
   *
   * @return the number of questions
   */
  public int getQuestionCount() {
    return questions.size();
  }

  /**
   * Gets the number of categories in the bank.
   *
   * @return the number of unique categories
   */
  public int getCategoryCount() {
    return categoryCount.size();
  }

  /**
   * Gets all unique categories.
   *
   * @return list of category names
   */
  public List<String> getAllCategories() {
    return new ArrayList<>(categoryCount.keySet());
  }

  /**
   * Gets the count of questions in a specific category.
   *
   * @param category the category name
   * @return the number of questions in that category
   */
  public int getQuestionCountByCategory(String category) {
    return categoryCount.getOrDefault(category, 0);
  }

  /**
   * Updates the category count when questions are added or removed.
   *
   * @param category the category to update
   * @param delta the change to apply (usually +1 or -1)
   */
  private void updateCategoryCount(String category, int delta) {
    int count = categoryCount.getOrDefault(category, 0) + delta;
    if (count <= 0) {
      categoryCount.remove(category);
    } else {
      categoryCount.put(category, count);
    }
  }

  /**
   * Clears all questions from the bank.
   */
  public void clear() {
    questions.clear();
    categoryCount.clear();
  }

  /**
   * Returns a string representation of the question bank.
   *
   * @return formatted bank information
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Question Bank Summary:\n");
    sb.append("Total Questions: ").append(questions.size()).append("\n");
    sb.append("Categories: ").append(categoryCount.size()).append("\n");
    for (String category : categoryCount.keySet()) {
      sb.append("  - ").append(category).append(": ").append(categoryCount.get(category))
          .append(" questions\n");
    }
    return sb.toString();
  }
}

