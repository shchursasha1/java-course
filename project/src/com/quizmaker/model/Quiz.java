package com.quizmaker.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a quiz session with student answers and scoring information.
 *
 * This class manages quiz instances, student responses, timing information,
 * and score calculation for individual quiz attempts.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class Quiz implements Serializable {

  private static final long serialVersionUID = 1L;

  private String quizId;
  private String studentName;
  private String studentEmail;
  private List<Question> questions;
  private List<Integer> studentAnswers; // Index of answer for each question (-1 if not answered)
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private int totalScore;
  private int maxScore;

  /**
   * Default constructor for Quiz class.
   */
  public Quiz() {
    this.questions = new ArrayList<>();
    this.studentAnswers = new ArrayList<>();
    this.startTime = LocalDateTime.now();
  }

  /**
   * Constructs a Quiz with specified parameters.
   *
   * @param quizId unique identifier for the quiz
   * @param studentName name of the student taking the quiz
   * @param studentEmail email address of the student
   * @param questions list of questions for the quiz
   */
  public Quiz(String quizId, String studentName, String studentEmail, List<Question> questions) {
    this.quizId = quizId;
    this.studentName = studentName;
    this.studentEmail = studentEmail;
    this.questions = new ArrayList<>(questions);
    this.studentAnswers = new ArrayList<>();
    this.startTime = LocalDateTime.now();
    this.maxScore = questions.size() * 100; // Each correct answer = 100 points

    // Initialize all answers as not answered
    for (int i = 0; i < questions.size(); i++) {
      this.studentAnswers.add(-1);
    }
  }

  // Getters and Setters
  public String getQuizId() {
    return quizId;
  }

  public void setQuizId(String quizId) {
    this.quizId = quizId;
  }

  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getStudentEmail() {
    return studentEmail;
  }

  public void setStudentEmail(String studentEmail) {
    this.studentEmail = studentEmail;
  }

  public List<Question> getQuestions() {
    return new ArrayList<>(questions);
  }

  public void setQuestions(List<Question> questions) {
    this.questions = new ArrayList<>(questions);
    this.maxScore = questions.size() * 100;
  }

  public List<Integer> getStudentAnswers() {
    return new ArrayList<>(studentAnswers);
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public int getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }

  public int getMaxScore() {
    return maxScore;
  }

  /**
   * Records a student's answer to a question.
   *
   * @param questionIndex the index of the question
   * @param answerIndex the index of the selected answer
   * @throws IndexOutOfBoundsException if questionIndex is invalid
   */
  public void recordAnswer(int questionIndex, int answerIndex) {
    if (questionIndex < 0 || questionIndex >= studentAnswers.size()) {
      throw new IndexOutOfBoundsException("Invalid question index: " + questionIndex);
    }
    studentAnswers.set(questionIndex, answerIndex);
  }

  /**
   * Gets the answer recorded for a specific question.
   *
   * @param questionIndex the index of the question
   * @return the index of the recorded answer, or -1 if not answered
   */
  public int getRecordedAnswer(int questionIndex) {
    if (questionIndex < 0 || questionIndex >= studentAnswers.size()) {
      return -1;
    }
    return studentAnswers.get(questionIndex);
  }

  /**
   * Checks if a specific question was answered.
   *
   * @param questionIndex the index of the question
   * @return true if the question was answered
   */
  public boolean isQuestionAnswered(int questionIndex) {
    return getRecordedAnswer(questionIndex) != -1;
  }

  /**
   * Gets the number of answered questions.
   *
   * @return the count of answered questions
   */
  public int getAnsweredQuestionCount() {
    int count = 0;
    for (int answer : studentAnswers) {
      if (answer != -1) {
        count++;
      }
    }
    return count;
  }

  /**
   * Finishes the quiz and calculates the score.
   */
  public void finishQuiz() {
    this.endTime = LocalDateTime.now();
    calculateScore();
  }

  /**
   * Calculates the student's score based on correct answers.
   */
  private void calculateScore() {
    this.totalScore = 0;
    for (int i = 0; i < questions.size(); i++) {
      if (i < studentAnswers.size()) {
        int studentAnswer = studentAnswers.get(i);
        Question question = questions.get(i);
        if (studentAnswer == question.getCorrectAnswerIndex()) {
          this.totalScore += 100;
        }
      }
    }
  }

  /**
   * Gets the percentage score.
   *
   * @return the score as a percentage (0-100)
   */
  public double getScorePercentage() {
    if (maxScore == 0) {
      return 0;
    }
    return (double) totalScore / maxScore * 100;
  }

  /**
   * Gets the duration of the quiz in seconds.
   *
   * @return the duration, or -1 if quiz is not finished
   */
  public long getDurationSeconds() {
    if (startTime == null || endTime == null) {
      return -1;
    }
    return java.time.temporal.ChronoUnit.SECONDS.between(startTime, endTime);
  }

  /**
   * Returns a detailed string representation of the quiz.
   *
   * @return formatted quiz information
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("===== QUIZ REPORT =====\n");
    sb.append("Quiz ID: ").append(quizId).append("\n");
    sb.append("Student: ").append(studentName).append("\n");
    sb.append("Email: ").append(studentEmail).append("\n");
    sb.append("Start Time: ").append(startTime).append("\n");
    if (endTime != null) {
      sb.append("End Time: ").append(endTime).append("\n");
      sb.append("Duration: ").append(getDurationSeconds()).append(" seconds\n");
    }
    sb.append("Questions: ").append(questions.size()).append("\n");
    sb.append("Answered: ").append(getAnsweredQuestionCount()).append("\n");
    sb.append("Score: ").append(totalScore).append("/").append(maxScore).append("\n");
    sb.append("Percentage: ").append(String.format("%.2f%%", getScorePercentage()))
        .append("\n");
    return sb.toString();
  }
}

