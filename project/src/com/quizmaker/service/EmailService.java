package com.quizmaker.service;

import com.quizmaker.model.Quiz;

/**
 * Service for sending quiz results via email.
 *
 * This class manages email notifications for quiz completion and score reports.
 * Note: This is a demonstration class. In production, use a real SMTP library
 * like JavaMail.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class EmailService {

  private String senderEmail;
  private String senderName;

  /**
   * Constructs an EmailService with sender information.
   *
   * @param senderEmail the email address of the sender
   * @param senderName the name of the sender
   */
  public EmailService(String senderEmail, String senderName) {
    this.senderEmail = senderEmail;
    this.senderName = senderName;
  }

  /**
   * Sends a quiz result email to the student.
   *
   * @param quiz the Quiz to send results for
   * @return true if email was sent successfully
   */
  public boolean sendQuizResultsEmail(Quiz quiz) {
    if (quiz == null) {
      System.err.println("Error: Quiz cannot be null");
      return false;
    }

    if (quiz.getStudentEmail() == null || quiz.getStudentEmail().isEmpty()) {
      System.err.println("Error: Student email is not set");
      return false;
    }

    try {
      String emailContent = generateEmailContent(quiz);
      return sendEmail(quiz.getStudentEmail(), "Quiz Results - " + quiz.getQuizId(),
          emailContent);
    } catch (Exception e) {
      System.err.println("Error sending email: " + e.getMessage());
      return false;
    }
  }

  /**
   * Sends a quiz completion notification.
   *
   * @param quiz the completed Quiz
   * @return true if notification was sent successfully
   */
  public boolean sendCompletionNotification(Quiz quiz) {
    if (quiz == null) {
      System.err.println("Error: Quiz cannot be null");
      return false;
    }

    String subject = "Quiz Completed: " + quiz.getStudentName();
    String content = "Student " + quiz.getStudentName() + " (" + quiz.getStudentEmail()
        + ") has completed the quiz " + quiz.getQuizId() + ".\n"
        + "Score: " + quiz.getTotalScore() + "/" + quiz.getMaxScore() + "\n"
        + "Percentage: " + String.format("%.2f%%", quiz.getScorePercentage());

    return sendEmail(senderEmail, subject, content);
  }

  /**
   * Generates the email content for quiz results.
   *
   * @param quiz the Quiz to generate content for
   * @return formatted email content string
   */
  private String generateEmailContent(Quiz quiz) {
    StringBuilder content = new StringBuilder();

    content.append("Dear ").append(quiz.getStudentName()).append(",\n\n");
    content.append("Thank you for completing the quiz!\n\n");
    content.append("===== QUIZ RESULTS =====\n");
    content.append("Quiz ID: ").append(quiz.getQuizId()).append("\n");
    content.append("Total Questions: ").append(quiz.getQuestions().size()).append("\n");
    content.append("Answered: ").append(quiz.getAnsweredQuestionCount()).append("\n");
    content.append("Score: ").append(quiz.getTotalScore()).append("/")
        .append(quiz.getMaxScore()).append("\n");
    content.append("Percentage: ").append(String.format("%.2f%%", quiz.getScorePercentage()))
        .append("\n\n");

    String rating = QuizEvaluator.getPerformanceRating(quiz);
    content.append("Performance Rating: ").append(rating).append("\n\n");

    java.util.List<String> weakAreas = QuizEvaluator.identifyWeakAreas(quiz);
    if (!weakAreas.isEmpty()) {
      content.append("Areas for Improvement:\n");
      for (String area : weakAreas) {
        content.append("  - ").append(area).append("\n");
      }
      content.append("\n");
    }

    if (quiz.getStartTime() != null && quiz.getEndTime() != null) {
      long duration = quiz.getDurationSeconds();
      content.append("Time Spent: ").append(formatDuration(duration)).append("\n\n");
    }

    content.append("Best regards,\n");
    content.append(senderName).append("\n");

    return content.toString();
  }

  /**
   * Sends an email message.
   *
   * In a real implementation, this would use JavaMail or a similar library.
   * This is a simulation that logs the email instead.
   *
   * @param recipientEmail the recipient email address
   * @param subject the email subject
   * @param content the email content
   * @return true if the email was "sent" successfully
   */
  private boolean sendEmail(String recipientEmail, String subject, String content) {
    if (!isValidEmail(recipientEmail)) {
      System.err.println("Error: Invalid email address: " + recipientEmail);
      return false;
    }

    // In production, use JavaMail library here
    System.out.println("\n========== EMAIL NOTIFICATION ==========");
    System.out.println("From: " + senderName + " <" + senderEmail + ">");
    System.out.println("To: " + recipientEmail);
    System.out.println("Subject: " + subject);
    System.out.println("----------------------------------------");
    System.out.println(content);
    System.out.println("=========================================\n");

    return true;
  }

  /**
   * Validates an email address format.
   *
   * @param email the email address to validate
   * @return true if the email format is valid
   */
  private boolean isValidEmail(String email) {
    if (email == null || email.isEmpty()) {
      return false;
    }

    String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    return email.matches(emailRegex);
  }

  /**
   * Formats duration in seconds to a human-readable string.
   *
   * @param seconds the duration in seconds
   * @return formatted duration string
   */
  private String formatDuration(long seconds) {
    long hours = seconds / 3600;
    long minutes = (seconds % 3600) / 60;
    long secs = seconds % 60;

    if (hours > 0) {
      return String.format("%d h %d m %d s", hours, minutes, secs);
    } else if (minutes > 0) {
      return String.format("%d m %d s", minutes, secs);
    } else {
      return String.format("%d s", secs);
    }
  }

  /**
   * Sets the sender email address.
   *
   * @param email the sender email address
   */
  public void setSenderEmail(String email) {
    this.senderEmail = email;
  }

  /**
   * Sets the sender name.
   *
   * @param name the sender name
   */
  public void setSenderName(String name) {
    this.senderName = name;
  }
}

