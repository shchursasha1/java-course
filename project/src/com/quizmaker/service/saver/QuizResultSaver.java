package com.quizmaker.service.saver;

import com.quizmaker.model.Question;
import com.quizmaker.model.Quiz;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Saves quiz results to text files.
 *
 * This class has a single responsibility: saving quiz results.
 * Separated from question loading/saving to follow SRP.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class QuizResultSaver {

  /**
   * Saves quiz results to a text file.
   *
   * @param filePath path where to save the results file
   * @param quiz the Quiz object to save
   * @param append whether to append to existing file (true) or overwrite (false)
   * @throws IOException if the file cannot be written
   */
  public void saveResults(String filePath, Quiz quiz, boolean append) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
      writer.write(quiz.toString());
      writer.write("\n");
      writer.write("Answers:\n");

      List<Question> questions = quiz.getQuestions();
      List<Integer> answers = quiz.getStudentAnswers();

      for (int i = 0; i < questions.size(); i++) {
        Question question = questions.get(i);
        writer.write("Q" + (i + 1) + ": " + question.getQuestionText() + "\n");

        if (i < answers.size()) {
          int studentAnswerIndex = answers.get(i);
          if (studentAnswerIndex >= 0 && studentAnswerIndex < question.getAnswers().size()) {
            String studentAnswer = question.getAnswers().get(studentAnswerIndex);
            writer.write("  Student Answer: " + studentAnswer + "\n");
          } else {
            writer.write("  Student Answer: NOT ANSWERED\n");
          }
        }

        writer.write("  Correct Answer: " + question.getCorrectAnswer() + "\n\n");
      }

      writer.write("=====================================\n\n");
    } catch (IOException e) {
      throw new IOException("Failed to save quiz results: " + filePath, e);
    }
  }

  /**
   * Saves quiz results (with append by default).
   *
   * @param filePath path where to save the results file
   * @param quiz the Quiz object to save
   * @throws IOException if the file cannot be written
   */
  public void saveResults(String filePath, Quiz quiz) throws IOException {
    saveResults(filePath, quiz, true);
  }
}

