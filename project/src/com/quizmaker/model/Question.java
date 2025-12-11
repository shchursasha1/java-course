package com.quizmaker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single quiz question with multiple choice answers.
 *
 * This class stores question data including the question text, possible answers,
 * the correct answer index, and difficulty level. It supports serialization for
 * storage in files.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class Question implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private String questionText;
  private List<String> answers;
  private int correctAnswerIndex;
  private String category;
  private int difficulty; // 1 - easy, 2 - medium, 3 - hard

  /**
   * Default constructor for Question class.
   */
  public Question() {
    this.answers = new ArrayList<>();
    this.difficulty = 1;
  }

  /**
   * Constructs a Question with specified parameters.
   *
   * @param id unique identifier for the question
   * @param questionText the text of the question
   * @param answers list of possible answers
   * @param correctAnswerIndex index of the correct answer (0-based)
   * @param category category of the question
   * @param difficulty difficulty level (1-3)
   */
  public Question(
      String id,
      String questionText,
      List<String> answers,
      int correctAnswerIndex,
      String category,
      int difficulty) {
    this.id = id;
    this.questionText = questionText;
    this.answers = new ArrayList<>(answers);
    this.correctAnswerIndex = correctAnswerIndex;
    this.category = category;
    this.difficulty = difficulty;
  }

  // Getters and Setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public List<String> getAnswers() {
    return new ArrayList<>(answers);
  }

  public void setAnswers(List<String> answers) {
    this.answers = new ArrayList<>(answers);
  }

  public int getCorrectAnswerIndex() {
    return correctAnswerIndex;
  }

  public void setCorrectAnswerIndex(int correctAnswerIndex) {
    this.correctAnswerIndex = correctAnswerIndex;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(int difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Checks if the provided answer index is correct.
   *
   * @param answerIndex the index of the answer to check
   * @return true if the answer index matches the correct answer index
   */
  public boolean isCorrectAnswer(int answerIndex) {
    return answerIndex == correctAnswerIndex;
  }

  /**
   * Gets the text of the correct answer.
   *
   * @return the correct answer text
   */
  public String getCorrectAnswer() {
    if (correctAnswerIndex >= 0 && correctAnswerIndex < answers.size()) {
      return answers.get(correctAnswerIndex);
    }
    return null;
  }

  /**
   * Returns a string representation of the question.
   *
   * @return formatted question string
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Question ID: ").append(id).append("\n");
    sb.append("Category: ").append(category).append("\n");
    sb.append("Difficulty: ").append(difficulty).append("\n");
    sb.append("Question: ").append(questionText).append("\n");
    sb.append("Answers:\n");
    for (int i = 0; i < answers.size(); i++) {
      sb.append("  ").append(i + 1).append(") ").append(answers.get(i)).append("\n");
    }
    return sb.toString();
  }
}

