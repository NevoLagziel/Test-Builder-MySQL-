package query;
import java.sql.*;
import java.util.ArrayList;

import model.Answer;
import model.MultipleChoiceQuestion;
import model.OpenQuestion;
import model.Question;
import model.Set;
import model.Test;

public class Queries {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//private static final String QUERY_NUMBER_OF_QUESTIONS = "SELECT COUNT(*) FROM question";
	private static final String QUERY_ALL_QUESTIONS = "SELECT questionID,questionType FROM Question";
	private static final String QUERY_OPEN_QUESTION = "SELECT questionID,questionTxt,theAnswer FROM OpenQuestion JOIN Question ON questionID = QID WHERE questionID = ?";
	private static final String QUERY_MULTIPLE_CHOICE_QUESTION = "SELECT questionID,questionTxt,numOfAnswers FROM MultipleChoiceQuestion JOIN Question ON questionID = QID WHERE questionID = ?";
	private static final String QUERY_ANSWERS = "SELECT answerTxt,isItTrue FROM Answer WHERE QID = ?";
	private static final String QUERY_ALL_TESTS_NAMES = "SELECT testName,testID FROM Test";
	private static final String QUERY_NUM_OF_TESTS = "SELECT COUNT(*) FROM Test";
	private static final String QUERY_TEST = "SELECT testName,testID,numOfQuestions FROM Test WHERE testID = ?";
	private static final String QUERY_QUESTIONS_IN_TEST = "SELECT questionID,questionTxt,questionType FROM QuestionsInTest join Question ON QID = questionID WHERE TID = ?";
	private static final String QUERY_ANSWERS_TEST = "SELECT answerTxt,isItTrue FROM AnswerForMCQuestionInTest WHERE TID = ? AND QID = ?";
	private static final String QUERY_EXISTS_TEST_NAME = "SELECT COUNT(*) FROM Test WHERE testName = ?";

	public Connection getConnection()
	{
		try {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/testgenerator", "root", "nevo1998");
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
/*
	public int getNumOfQuestions(Connection con) throws SQLException
	{
		System.err.println(QUERY_NUMBER_OF_QUESTIONS);
		int num = 0;
		try(Statement stmt = con.createStatement()){
			try(ResultSet rs = stmt.executeQuery(QUERY_NUMBER_OF_QUESTIONS)){
				if(rs.next())
					num = rs.getInt("COUNT(*)");
			}
		}
		return num;
	}
*/
	public ArrayList<Question> getAllQuestions(Connection con) throws Exception
	{
		System.err.println(QUERY_ALL_QUESTIONS);
		ArrayList<Question> questions = new ArrayList<Question>();
		try(Statement stmt = con.createStatement()){
			try(ResultSet rs = stmt.executeQuery(QUERY_ALL_QUESTIONS)){
				questions = getQuestionsFromRs(con, rs);
			}
		}
		return questions;
	}

	public ArrayList<Question> getQuestionsFromRs(Connection con,ResultSet rs) throws SQLException
	{
		ArrayList<Question> questions = new ArrayList<Question>();
		while(rs.next())
		{
			if(QuestionType.OpenQuestion.equals(QuestionType.valueOf(rs.getString("questionType"))))
			{
				questions.add(getOpenQuestion(con, rs.getInt("questionID")));

			}
			else if(QuestionType.MultipleChoiceQuestion.equals(QuestionType.valueOf(rs.getString("questionType"))))
			{
				questions.add(getMultipleChoiceQuestion(con, rs.getInt("questionID")));
			}
		}
		return questions;
	}

	public OpenQuestion getOpenQuestion(Connection con, int id) throws SQLException
	{
		System.err.println(QUERY_OPEN_QUESTION);
		OpenQuestion q = new OpenQuestion();
		try(PreparedStatement stmt = con.prepareStatement(QUERY_OPEN_QUESTION)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next())
				{
					q.setSerialNum(rs.getInt("questionID"));
					q.setText(rs.getString("questionTxt"));
					q.setAnswer(rs.getString("theAnswer"));
				}
			}
		}
		return q;
	}

	public MultipleChoiceQuestion getMultipleChoiceQuestion(Connection con, int id) throws SQLException
	{
		System.err.println(QUERY_MULTIPLE_CHOICE_QUESTION);
		MultipleChoiceQuestion q = new MultipleChoiceQuestion();
		try(PreparedStatement stmt = con.prepareStatement(QUERY_MULTIPLE_CHOICE_QUESTION)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next())
				{
					q.setSerialNum(rs.getInt("questionID"));
					q.setText(rs.getString("questionTxt"));
					q.setNumOfAnswers(rs.getInt("numOfAnswers"));
					q.setAnswers(getAnswers(con, id));
				}
			}
		}
		return q;
	}

	public Set<Answer> getAnswers(Connection con, int questionID) throws SQLException
	{
		System.err.println(QUERY_ANSWERS);
		Set<Answer> answers = new Set<Answer>();
		try(PreparedStatement stmt = con.prepareStatement(QUERY_ANSWERS)){
			stmt.setInt(1, questionID);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next()){
					Answer ans = new Answer();
					ans.setRight(rs.getBoolean("isItTrue"));
					ans.setText(rs.getString("answerTxt"));
					answers.add(ans);
				}
			}
		}
		return answers;
	}

	public int getNumOfTests(Connection con) throws SQLException
	{
		System.err.println(QUERY_NUM_OF_TESTS);
		int num = 0;
		try(Statement stmt = con.createStatement()){
			try(ResultSet rs = stmt.executeQuery(QUERY_NUM_OF_TESTS)){
				if(rs.next())
					num = rs.getInt("COUNT(*)");
			}
		}
		return num;
	}

	public ArrayList<Test> getAllTestsNames(Connection con) throws SQLException
	{
		System.err.println(QUERY_ALL_TESTS_NAMES);
		ArrayList<Test> allTests = new  ArrayList<Test>();	
		try(Statement stmt = con.createStatement()){
			try(ResultSet rs = stmt.executeQuery(QUERY_ALL_TESTS_NAMES)){
				Test t;
				while(rs.next()) {
					t=new Test();
					t.setName(rs.getString("testName"));
					t.setTestNum(rs.getInt("testID"));
					allTests.add(t);
				}
			}
		}
		return allTests;
	}

	public Test getTest(Connection con, int testID) throws SQLException
	{
		System.err.println(QUERY_TEST);
		Test t = new Test();
		try(PreparedStatement stmt = con.prepareStatement(QUERY_TEST)){
			stmt.setInt(1, testID);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next())
				{
					t.setName(rs.getString("testName"));
					t.setTestNum(testID);
					t.setNumOfQuestions(rs.getInt("numOfQuestions"));
					t.setQuestions(getTestQuestions(con, testID));
				}
			}	
		}
		return t;
	}

	public ArrayList<Question> getTestQuestions(Connection con,int testID) throws SQLException
	{
		System.err.println(QUERY_QUESTIONS_IN_TEST);
		ArrayList<Question> questions = new ArrayList<Question>();
		try(PreparedStatement stmt = con.prepareStatement(QUERY_QUESTIONS_IN_TEST)){
			stmt.setInt(1, testID);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next())
				{
					if(QuestionType.OpenQuestion.equals(QuestionType.valueOf(rs.getString("questionType"))))
					{
						questions.add(getOpenQuestion(con, rs.getInt("questionID")));

					}
					else if(QuestionType.MultipleChoiceQuestion.equals(QuestionType.valueOf(rs.getString("questionType"))))
					{
						questions.add(getMultipleChoiceQuestionInTest(con, rs.getInt("questionID"), testID));
					}
				}
			}
		}
		return questions;
	}

	public MultipleChoiceQuestion getMultipleChoiceQuestionInTest(Connection con, int questionID, int testID) throws SQLException
	{
		System.err.println(QUERY_MULTIPLE_CHOICE_QUESTION);
		MultipleChoiceQuestion q = new MultipleChoiceQuestion();
		try(PreparedStatement stmt = con.prepareStatement(QUERY_MULTIPLE_CHOICE_QUESTION)){
			stmt.setInt(1, questionID);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next())
				{
					q.setSerialNum(rs.getInt("questionID"));
					q.setText(rs.getString("questionTxt"));
					q.setAnswers(getTestAnswers(con, testID, questionID));
					q.setNumOfAnswers(q.getAnswers().size());
				}
			}
		}
		return q;
	}

	public Set<Answer> getTestAnswers(Connection con, int testID, int questionID) throws SQLException
	{
		System.err.println(QUERY_ANSWERS_TEST);
		Set<Answer> answers = new Set<Answer>();
		try(PreparedStatement stmt = con.prepareStatement(QUERY_ANSWERS_TEST)){
			stmt.setInt(1, testID);
			stmt.setInt(2, questionID);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next()){
					Answer ans = new Answer();
					ans.setText(rs.getString("answerTxt"));
					ans.setRight(rs.getBoolean("isItTrue"));
					answers.add(ans);
				}
			}
		}
		return answers;
	}

	public boolean checkTestName(Connection con, String testName) throws SQLException
	{
		System.err.println(QUERY_EXISTS_TEST_NAME);
		int exists = 0;
		try(PreparedStatement stmt = con.prepareStatement(QUERY_EXISTS_TEST_NAME)){
			stmt.setString(1, testName);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next())
					exists = rs.getInt("COUNT(*)");
			}
		}
		if(exists == 0)
			return false;
		return true;
	}

	private enum QuestionType{
		OpenQuestion,MultipleChoiceQuestion;
	}

}