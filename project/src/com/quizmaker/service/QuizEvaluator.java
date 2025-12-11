package com.quizmaker.service;

import com.quizmaker.model.Question;
import com.quizmaker.model.Quiz;
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates quiz results and generates detailed feedback.
 *
 * This class calculates scores, provides answer analysis, and generates
 * performance reports for completed quizzes.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class QuizEvaluator {

  /**
   * Evaluates a quiz and calculates the score.
   *
   * @param quiz the Quiz to evaluate
   * @return the Quiz object with updated score information
   */
  public static Quiz evaluateQuiz(Quiz quiz) {
    if (quiz == null) {
      throw new IllegalArgumentException("Quiz cannot be null");
    }

    int score = 0;
    List<Question> questions = quiz.getQuestions();
    List<Integer> answers = quiz.getStudentAnswers();

    for (int i = 0; i < questions.size(); i++) {
      if (i < answers.size()) {
        int studentAnswer = answers.get(i);
        Question question = questions.get(i);
        if (question.isCorrectAnswer(studentAnswer)) {
          score += 100;
        }
      }
    }

    quiz.setTotalScore(score);
    return quiz;
  }

  /**
   * Generates a detailed answer analysis report.
   *
   * @param quiz the Quiz to analyze
   * @return a formatted analysis report string
   */
  public static String generateAnalysisReport(Quiz quiz) {
    if (quiz == null) {
      throw new IllegalArgumentException("Quiz cannot be null");
    }

    StringBuilder report = new StringBuilder();
    report.append("===== DETAILED ANALYSIS REPORT =====\n\n");

    List<Question> questions = quiz.getQuestions();
    List<Integer> answers = quiz.getStudentAnswers();

    int correctCount = 0;
    int wrongCount = 0;
    int skippedCount = 0;

    for (int i = 0; i < questions.size(); i++) {
      report.append("Question ").append(i + 1).append(":\n");
      report.append("Text: ").append(questions.get(i).getQuestionText()).append("\n");
      report.append("Category: ").append(questions.get(i).getCategory()).append("\n");
      report.append("Difficulty: ").append(getDifficultyString(questions.get(i).getDifficulty()))
          .append("\n");

      if (i < answers.size()) {
        int studentAnswerIndex = answers.get(i);
        if (studentAnswerIndex == -1) {
          report.append("Your Answer: NOT ANSWERED\n");
          report.append("Result: SKIPPED\n");
          skippedCount++;
        } else {
          String studentAnswer = questions.get(i).getAnswers().get(studentAnswerIndex);
          boolean isCorrect = questions.get(i).isCorrectAnswer(studentAnswerIndex);

          report.append("Your Answer: ").append(studentAnswer).append("\n");
          report.append("Result: ").append(isCorrect ? "CORRECT" : "WRONG").append("\n");

          if (isCorrect) {
            correctCount++;
          } else {
            wrongCount++;
          }
        }
      }

      report.append("Correct Answer: ").append(questions.get(i).getCorrectAnswer()).append("\n");
      report.append("\n");
    }

    report.append("===== SUMMARY =====\n");
    report.append("Correct Answers: ").append(correctCount).append("\n");
    report.append("Wrong Answers: ").append(wrongCount).append("\n");
    report.append("Skipped Questions: ").append(skippedCount).append("\n");
    report.append("Total Score: ").append(quiz.getTotalScore()).append("/")
        .append(quiz.getMaxScore()).append("\n");
    report.append("Percentage: ").append(String.format("%.2f%%", quiz.getScorePercentage()))
        .append("\n");

    return report.toString();
  }

  /**
   * Generates a performance rating based on score percentage.
   *
   * @param quiz the Quiz to rate
   * @return a performance rating string
   */
  public static String getPerformanceRating(Quiz quiz) {
    if (quiz == null) {
      throw new IllegalArgumentException("Quiz cannot be null");
    }

    double percentage = quiz.getScorePercentage();

    if (percentage >= 90) {
      return "EXCELLENT";
    } else if (percentage >= 80) {
      return "VERY GOOD";
    } else if (percentage >= 70) {
      return "GOOD";
    } else if (percentage >= 60) {
      return "SATISFACTORY";
    } else if (percentage >= 50) {
      return "PASSING";
    } else {
      return "FAILING";
    }
  }

  /**
   * Identifies weak areas where student performed poorly.
   *
   * @param quiz the Quiz to analyze
   * @return a list of categories where performance was weak
   */
  public static List<String> identifyWeakAreas(Quiz quiz) {
    if (quiz == null) {
      throw new IllegalArgumentException("Quiz cannot be null");
    }

    List<String> weakAreas = new ArrayList<>();
    java.util.Map<String, int[]> categoryStats = new java.util.HashMap<>();

    List<Question> questions = quiz.getQuestions();
    List<Integer> answers = quiz.getStudentAnswers();

    // Calculate stats per category
    for (int i = 0; i < questions.size(); i++) {
      Question question = questions.get(i);
      String category = question.getCategory();

      categoryStats.putIfAbsent(category, new int[2]); // {correct, total}
      categoryStats.get(category)[1]++;

      if (i < answers.size()) {
        int studentAnswer = answers.get(i);
        if (question.isCorrectAnswer(studentAnswer)) {
          categoryStats.get(category)[0]++;
        }
      }
    }

    // Identify weak areas (less than 70% correct)
    for (String category : categoryStats.keySet()) {
      int[] stats = categoryStats.get(category);
      double categoryPercentage = (double) stats[0] / stats[1] * 100;
      if (categoryPercentage < 70) {
        weakAreas.add(category + " (" + String.format("%.0f%%", categoryPercentage) + ")");
      }
    }

    return weakAreas;
  }

  /**
   * Gets a difficulty string representation.
   *
   * @param difficulty the difficulty level (1-3)
   * @return a human-readable difficulty string
   */
  private static String getDifficultyString(int difficulty) {
    switch (difficulty) {
      case 1:
        return "Easy";
      case 2:
        return "Medium";
      case 3:
        return "Hard";
      default:
        return "Unknown";
    }
  }

  /**
   * Compares two quiz scores.
   *
   * @param quiz1 the first Quiz
   * @param quiz2 the second Quiz
   * @return positive if quiz1 score is higher, negative if quiz2 is higher, 0 if equal
   */
  public static int compareScores(Quiz quiz1, Quiz quiz2) {
    if (quiz1 == null || quiz2 == null) {
      throw new IllegalArgumentException("Quiz objects cannot be null");
    }

    return Integer.compare(quiz1.getTotalScore(), quiz2.getTotalScore());
  }

  /**
   * Calculates the average score from multiple quizzes.
   *
   * @param quizzes the list of quizzes to analyze
   * @return the average score percentage
   */
  public static double calculateAverageScore(List<Quiz> quizzes) {
    if (quizzes == null || quizzes.isEmpty()) {
      return 0;
    }

    double total = 0;
    for (Quiz quiz : quizzes) {
      total += quiz.getScorePercentage();
    }

    return total / quizzes.size();
  }
}

