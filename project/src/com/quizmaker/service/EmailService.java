package com.quizmaker.service;

import com.quizmaker.model.Quiz;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Service for sending quiz results via email.
 *
 * <p>This class manages email notifications for quiz completion and score reports.
 * It uses the JavaMail (javax.mail) library and supports sending through
 * Gmail SMTP using an application-specific password.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class EmailService {

  private String senderEmail;
  private String senderName;
  private String smtpHost;
  private int smtpPort;
  private String smtpUsername;
  private String smtpPassword;
  private boolean useTls;

  /**
   * Constructs an EmailService with sender information only.
   *
   * <p>This constructor keeps backward compatibility and defaults to console
   * logging if SMTP settings are not configured.
   *
   * @param senderEmail the email address of the sender
   * @param senderName the name of the sender
   */
  public EmailService(String senderEmail, String senderName) {
    this.senderEmail = senderEmail;
    this.senderName = senderName;
  }

  /**
   * Constructs a fully configured EmailService with SMTP settings.
   *
   * @param senderEmail the email address of the sender
   * @param senderName the name of the sender
   * @param smtpHost the SMTP server host (for Gmail: "smtp.gmail.com")
   * @param smtpPort the SMTP server port (for Gmail TLS: 587)
   * @param smtpUsername the SMTP username (usually full Gmail address)
   * @param smtpPassword the SMTP password (Gmail app password, not account password)
   * @param useTls true to enable STARTTLS
   */
  public EmailService(
      String senderEmail,
      String senderName,
      String smtpHost,
      int smtpPort,
      String smtpUsername,
      String smtpPassword,
      boolean useTls) {
    this.senderEmail = senderEmail;
    this.senderName = senderName;
    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
    this.smtpUsername = smtpUsername;
    this.smtpPassword = smtpPassword;
    this.useTls = useTls;
  }

  /**
   * Factory method to create a Gmail-configured EmailService.
   *
   * <p>Gmail requires an application-specific password when two-factor
   * authentication is enabled. Do not use your regular account password.
   *
   * @param senderEmail the email address of the sender
   * @param senderName the name of the sender
   * @param gmailUsername the Gmail account used for SMTP authentication
   * @param appPassword the Gmail application-specific password
   * @return configured EmailService instance
   */
  public static EmailService createGmailService(
      String senderEmail,
      String senderName,
      String gmailUsername,
      String appPassword) {
    return new EmailService(
        senderEmail,
        senderName,
        "smtp.gmail.com",
        587,
        gmailUsername,
        appPassword,
        true);
  }

  /**
   * Configures SMTP settings for this service.
   *
   * @param smtpHost the SMTP server host
   * @param smtpPort the SMTP server port
   * @param smtpUsername the SMTP username
   * @param smtpPassword the SMTP password
   * @param useTls true to enable STARTTLS
   */
  public void configureSmtp(
      String smtpHost,
      int smtpPort,
      String smtpUsername,
      String smtpPassword,
      boolean useTls) {
    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
    this.smtpUsername = smtpUsername;
    this.smtpPassword = smtpPassword;
    this.useTls = useTls;
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

    String emailContent = generateEmailContent(quiz);
    return sendEmail(quiz.getStudentEmail(), "Quiz Results - " + quiz.getQuizId(),
        emailContent);
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
   * <p>If SMTP settings are configured, this method sends a real email using
   * the JavaMail (javax.mail) library. If SMTP settings are not configured,
   * the message is logged to the console to preserve existing behaviour and
   * keep tests working without external dependencies.
   *
   * @param recipientEmail the recipient email address
   * @param subject the email subject
   * @param content the email content
   * @return true if the email was sent or logged successfully
   */
  private boolean sendEmail(String recipientEmail, String subject, String content) {
    if (!isValidEmail(recipientEmail)) {
      System.err.println("Error: Invalid email address: " + recipientEmail);
      return false;
    }

    if (smtpHost == null || smtpHost.isEmpty()) {
      // Fallback: log to console when SMTP is not configured.
      System.out.println("\n========== EMAIL NOTIFICATION ==========");
      System.out.println("From: " + senderName + " <" + senderEmail + ">");
      System.out.println("To: " + recipientEmail);
      System.out.println("Subject: " + subject);
      System.out.println("----------------------------------------");
      System.out.println(content);
      System.out.println("=========================================\n");
      return true;
    }

    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", useTls ? "true" : "false");
    properties.put("mail.smtp.host", smtpHost);
    properties.put("mail.smtp.port", String.valueOf(smtpPort));
    properties.put("mail.smtp.ssl.trust", smtpHost);
    properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

    Session session;
    if (smtpUsername != null && !smtpUsername.isEmpty()) {
      session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(smtpUsername, smtpPassword);
        }
      });
    } else {
      session = Session.getInstance(properties);
    }

    try {
      MimeMessage mimeMessage = new MimeMessage(session);
      mimeMessage.setFrom(new InternetAddress(senderEmail, senderName));
      mimeMessage.setRecipients(Message.RecipientType.TO,
          InternetAddress.parse(recipientEmail, false));
      mimeMessage.setSubject(subject, "UTF-8");
      mimeMessage.setText(content, "UTF-8");

      Transport.send(mimeMessage);
      return true;
    } catch (MessagingException e) {
      System.err.println("Error sending email via SMTP: " + e.getMessage());
      return false;
    } catch (UnsupportedEncodingException e) {
      System.err.println("Error with sender name encoding: " + e.getMessage());
      return false;
    }
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

