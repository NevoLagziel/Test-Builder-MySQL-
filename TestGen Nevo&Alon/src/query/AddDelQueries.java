package query;

import java.sql.*;

import model.Answer;
import model.MultipleChoiceQuestion;
import model.OpenQuestion;
import model.Question;
import model.Test;

public class AddDelQueries extends Queries{

	private static final String QUERY_ADD_OPEN_QUESTION = "insert into OpenQuestion (QID,theAnswer) values (?,?)";
	private static final String QUERY_ADD_QUESTION = "insert into Question (questionID,questionTxt,questionType) values (?,?,?)";
	private static final String QUERY_ADD_MULTIPLE_CHOICE_QUESTION = "insert into MultipleChoiceQuestion (QID,numOfAnswers) value (?,?)";
	private static final String QUERY_ADD_ANSWER = "insert into Answer (QID,answerTxt,isItTrue) values (?,?,?)";
	private static final String QUERY_DELETE_ANSWER = "DELETE FROM Answer WHERE QID = ? AND answerTxt = ?";
	private static final String QUERY_ADD_TEST = "insert into Test (testName,testID,numOfQuestions) values (?,?,?)";
	private static final String QUERY_ADD_QUESTIONS_IN_TEST = "insert into QuestionsInTest (TID,QID) values (?,?)";
	private static final String QUERY_ADD_ANSWERS_IN_TEST = "insert into AnswerForMCQuestionInTest (TID,QID,answerTxt,isItTrue) values (?,?,?,?)";
	private static final String QUERY_CHANGE_QUESTION_WORDING = "UPDATE Question SET questionTxt = ? WHERE questionID = ?";
	private static final String QUERY_CHANGE_OPEN_ANSWER_WORDING = "UPDATE OpenQuestion SET theAnswer = ? WHERE QID = ?";
	private static final String QUERY_CHANGE_CHOICE_ANSWER_WORDING = "UPDATE Answer SET answerTxt = ? WHERE QID = ? AND answerTxt = ?";
	private static final String QUERY_UPDATE_NUM_OF_ANSWERS = "UPDATE MultipleChoiceQuestion SET numOfAnswers = ? WHERE QID = ?";

	public void addQuestion(Connection con, Question q) throws SQLException            
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_ADD_QUESTION)) {
			stmt.setInt(1, q.getSerialNum());
			stmt.setString(2, q.getText());
			if(q instanceof OpenQuestion)
			{
				stmt.setString(3,"OpenQuestion");
				System.err.println("Create question: "+stmt.executeUpdate());
				OpenQuestion op =(OpenQuestion)q;
				try(PreparedStatement stmt2 = con.prepareStatement(QUERY_ADD_OPEN_QUESTION)){
					stmt2.setInt(1, op.getSerialNum());
					stmt2.setString(2, op.getAnswer());
					System.err.println("Create open question: "+stmt2.executeUpdate());
				}
			}
			else if(q instanceof MultipleChoiceQuestion)
			{
				stmt.setString(3,"MultipleChoiceQuestion");
				System.err.println("Create question: "+stmt.executeUpdate());
				MultipleChoiceQuestion mp =(MultipleChoiceQuestion)q;
				try(PreparedStatement stmt2 = con.prepareStatement(QUERY_ADD_MULTIPLE_CHOICE_QUESTION)){
					stmt2.setInt(1, mp.getSerialNum());
					stmt2.setInt(2, mp.getNumOfAnswers());
					System.err.println("Create multiple choice question: "+stmt2.executeUpdate());
					for(int i=0;i<mp.getNumOfAnswers();i++)
					{
						addAnswer(con, mp.getAnswers().get(i),mp.getSerialNum());
					}
				}
			}
		}
	}

	public void addAnswer(Connection con, Answer ans, int id) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_ADD_ANSWER)) {
			stmt.setInt(1, id);
			stmt.setString(2, ans.getText());
			stmt.setBoolean(3, ans.isRight());
			System.err.println("Create answer : "+stmt.executeUpdate());
		}
	}
	
	public void updateNumOfAnswers(Connection con, int id, int numOfAnswers) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_UPDATE_NUM_OF_ANSWERS)){
			stmt.setInt(1, numOfAnswers);
			stmt.setInt(2, id);
			System.err.println("Updated num of questions : "+stmt.executeUpdate());
		}
	}

	public void addTest(Connection con, Test t) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_ADD_TEST)) {
			stmt.setString(1, t.getName());
			stmt.setInt(2, t.getTestNum());
			stmt.setInt(3, t.getNumOfQuestions());
			stmt.executeUpdate();
			for(int i = 0;i < t.getNumOfQuestions();i++)
			{
				addQuestionToTest(con, t.getQuestions().get(i), t.getTestNum());
			}
		}
	}

	public void addQuestionToTest(Connection con, Question q, int testID) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_ADD_QUESTIONS_IN_TEST)) {
			stmt.setInt(1, testID);
			stmt.setInt(2, q.getSerialNum());
			stmt.executeUpdate();
			if(q instanceof MultipleChoiceQuestion) {
				MultipleChoiceQuestion mcq = (MultipleChoiceQuestion)q;
				for(int i=0;i<mcq.getNumOfAnswers();i++)
				{
					try(PreparedStatement ansStmt = con.prepareStatement(QUERY_ADD_ANSWERS_IN_TEST)){	
						ansStmt.setInt(1, testID);
						ansStmt.setInt(2, mcq.getSerialNum());
						ansStmt.setString(3, mcq.getAnswers().get(i).getText());
						ansStmt.setBoolean(4, mcq.getAnswers().get(i).isRight());
						ansStmt.executeUpdate();
					}
				}
			}
		}
	}

	public void deleteAnswer(Connection con, String answerTxt, int id) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_DELETE_ANSWER)){
			stmt.setInt(1, id);
			stmt.setString(2, answerTxt);
			System.err.println("Deleted a answer: "+stmt.executeUpdate());
		}
	}
	
	public void changeQuestionTxt(Connection con, String txt, int questionID) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_CHANGE_QUESTION_WORDING)){
			stmt.setString(1, txt);
			stmt.setInt(2, questionID);
			System.err.println("Changed question wording: "+ stmt.executeUpdate());
		}
	}
	
	public void changeOpenQuestionAnswer(Connection con, String txt, int questionID) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_CHANGE_OPEN_ANSWER_WORDING)){
			stmt.setString(1, txt);
			stmt.setInt(2, questionID);
			System.err.println("Changed answer wording: "+ stmt.executeUpdate());
		}
	}
	
	public void changeMultipleChoiceAnswer(Connection con, String newTxt, String oldTxt, int questionID) throws SQLException
	{
		try(PreparedStatement stmt = con.prepareStatement(QUERY_CHANGE_CHOICE_ANSWER_WORDING)){
			stmt.setString(1, newTxt);
			stmt.setInt(2, questionID);
			stmt.setString(3, oldTxt);
			System.err.println("Changed answer wording: "+ stmt.executeUpdate());
		}
	}

}
