package controller;


//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import model.Answer;
import model.MultipleChoiceQuestion;
import model.OpenQuestion;
import model.Question;
import model.Set;
import model.Test;
import query.AddDelQueries;

public class TestBuilder {

	private ArrayList<Question> allQuestions;
	private int numOfQuestions; 
	private ArrayList<Test> allTests;
	private int numOfTests; 

	public TestBuilder() {
		numOfQuestions=0;
		allQuestions=new ArrayList<Question>();
		allTests=new ArrayList<Test>();
		numOfTests=0;
	}	
	public int getNumOfTests() {
		return numOfTests;
	}
	public void setNumOfTests(int numOfTests) {
		this.numOfTests = numOfTests;
	}
	public String cloneTest (int testNum) throws IOException {
		if((testNum > numOfTests) || (testNum <= 0))
		{
			System.out.println("there is no test with that number");
			return "there is no test with that number";
		}
		AddDelQueries jdbc = new AddDelQueries();
		try(Connection con = jdbc.getConnection()){
			Test t = jdbc.getTest(con, testNum);

			Test clone=new Test(t);
			jdbc.addTest(con, clone);
			allTests.add(clone);
			numOfTests++;

			/*
			File questionsT=new File("exam_"+clone.getName()+".txt");
			FileWriter fwQ=new FileWriter(questionsT);
			PrintWriter pwQ=new PrintWriter(fwQ);
			pwQ.println(onlyQuestions(clone.getQuestions()));
			pwQ.close();

			File answersT=new File("solution_"+clone.getName()+".txt");
			FileWriter fwA=new FileWriter(answersT);
			PrintWriter pwA=new PrintWriter(fwA);
			pwA.println(onlyAnswers(clone.getQuestions()));
			pwA.close();
			*/
			System.out.println("the test copied seccesfully");
			printQuestions(clone.getQuestions());
			return "the test copied seccesfully";
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Test findTestByNum(int testNum) {
		if((testNum>0)&&(testNum<=numOfTests)){
			for(int i=0;i<allTests.size();i++) {
				if(allTests.get(i).getTestNum()==testNum)
					return allTests.get(i);
			}
		}
		return null;
	}
	public void printTestsNames() {
		if(numOfTests>0) {
			AddDelQueries jdbc = new AddDelQueries();
			try(Connection con = jdbc.getConnection()){
				setAllTests(jdbc.getAllTestsNames(con));
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
			String s="";
			for(int i=0;i<allTests.size();i++) {
				s += allTests.get(i).getTestNum()+") "+allTests.get(i).getName() +"\n";
			}
			System.out.println(s);
		}
	}
	public void setAllTests(ArrayList<Test> allTests) {
		this.allTests=allTests;
		this.numOfTests=allTests.size();
		Test.setTestNumGenerator(numOfTests+1);
	}
	
	public ArrayList<Question> getAllQuestions(){
		return allQuestions;
	}
	public void setAllQuestions(ArrayList<Question> allQuestions) {
		this.allQuestions=allQuestions;
		this.numOfQuestions=allQuestions.size();
		Question.setSerialNumGenerator(numOfQuestions+1);
	}
	
	public ArrayList<Question> orgByAnsLength (ArrayList<Question> test){
		ArrayList<Question> orgTest=new ArrayList<Question>();
		int counter=1;
		while(test.size()>0) {
			int index=0;
			for(int i=1;i<test.size();i++) {
				if(ansLength(test.get(index))>ansLength(test.get(i))) {
					index=i;
				}
			}
			test.get(index).setSerialNum(counter++);
			orgTest.add(test.get(index));
			test.remove(index);
		}
		return orgTest;
	}
	public int ansLength(Question q) {
		int length=0;
		if (q instanceof OpenQuestion){
			length=((OpenQuestion)q).getAnswer().length();
		}else if(q instanceof MultipleChoiceQuestion) {
			Set<Answer> answers = ((MultipleChoiceQuestion)q).getAnswers();
			for(int i=0;i<answers.size();i++) {
				length += answers.get(i).getText().length();
			}
		}
		return length;
	}
	/*
	public void saveTestAsTxt(ArrayList<Question> questions) throws IOException {       //just in case we want to save question and solution in text file
		DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy_MM_dd");
		LocalDate now = LocalDate.now();
		dft.format(now);
		String qName = "exam_"+now;
		String aName = "solution_"+now;
		String testName =""+now;
		File questionsT=new File(qName+".txt");
		boolean qExists=questionsT.exists();
		int qFileCounter=1;
		while(qExists) {
			String qReplaceName= qName+"__"+qFileCounter;
			questionsT=new File(qReplaceName+".txt");
			testName =""+now+"__"+qFileCounter;
			qFileCounter++;
			qExists=questionsT.exists();
		}
		FileWriter fwQ=new FileWriter(questionsT);
		PrintWriter pwQ=new PrintWriter(fwQ);
		pwQ.println(onlyQuestions(questions));
		pwQ.close();

		File answersT=new File(aName+".txt");
		boolean aExists=answersT.exists();
		int aFileCounter=1;
		while(aExists) {
			String aReplaceName=aName+"__"+aFileCounter;
			answersT=new File(aReplaceName+".txt");
			aFileCounter++;
			aExists=answersT.exists();
		}
		FileWriter fwA=new FileWriter(answersT);
		PrintWriter pwA=new PrintWriter(fwA);
		pwA.println(onlyAnswers(questions));
		pwA.close();
		Test newTest = new Test(questions,questions.size(),testName);

		AddDelQueries jdbc = new AddDelQueries();
		try(Connection con = jdbc.getConnection()) {
			jdbc.addTest(con, newTest);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}

		allTests.add(newTest);
		numOfTests++;
	}
	 */

	public void buildTest(ArrayList<Question> questions) {
		DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy_MM_dd");
		LocalDate now = LocalDate.now();
		dft.format(now);
		String testName =""+now;
		AddDelQueries jdbc = new AddDelQueries();
		try(Connection con = jdbc.getConnection()){
			boolean qExists = jdbc.checkTestName(con, testName);
			int qCounter=1;
			while(qExists) {
				testName =""+now+"__"+qCounter;
				qCounter++;
				qExists = jdbc.checkTestName(con, testName); 
			}

			Test newTest = new Test(questions,questions.size(),testName);
			jdbc.addTest(con, newTest);
			allTests.add(newTest);
			numOfTests++;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String onlyQuestions(ArrayList<Question> questions) {
		String str="";
		for(int i=0;i<questions.size();i++) {
			str += +(i+1)+") "+ questions.get(i).getText()+"\n";
			if(questions.get(i) instanceof MultipleChoiceQuestion) {
				for(int j=0;j<((MultipleChoiceQuestion)questions.get(i)).getNumOfAnswers();j++) {
					str +="  " +(j+1)+") "+ ((MultipleChoiceQuestion)questions.get(i)).getAnswers().get(j).getText()+"\n";
				}
			}
			str += "\n";
		}
		return str;
	}
	public String onlyAnswers(ArrayList<Question> questions) {
		String str="";
		for(int i=0;i<questions.size();i++) {
			if(questions.get(i) instanceof MultipleChoiceQuestion) {
				str += +(i+1)+") "+"\n";
				for(int j=0;j<((MultipleChoiceQuestion)questions.get(i)).getNumOfAnswers();j++) {
					str +="  " +(j+1)+") "+ ((MultipleChoiceQuestion)questions.get(i)).getAnswers().get(j).isRight()+"\n";
				}
			}
			else {
				str += +(i+1)+") "+((OpenQuestion)questions.get(i)).getAnswer()+"\n";
			}
			str += "\n";
		}
		return str;

	}
	public int getNumOfQuestions() {
		return numOfQuestions;
	}
	public boolean addAnswer(int serialNum,String answer,boolean isRight) {
		if((findBySerialNum(serialNum)==null)||(findBySerialNum(serialNum) instanceof OpenQuestion)){
			System.out.println("there is no multiple choice question with that number");
			return false;
		}
		if(answer.isEmpty()) {
			System.out.println("the answer you entered is not valid");
			return false;
		}
		if(((MultipleChoiceQuestion)findBySerialNum(serialNum)).addAnswer(answer, isRight)) 
			return true;

		System.out.println("the answer already exists for that question");
		return false;
	}
	public boolean addMultipleChoiceQ(String text,String answer,boolean isRight) {
		if(text.isEmpty()) {
			System.out.println("the question you entered is not valid");
			return false;
		}
		if(answer.isEmpty()) {
			System.out.println("the answer you entered is not valid");
			return false;
		}
		if (multipleQuestionExists(text)) {
			System.out.println("question already exist");
			return false;
		}
		MultipleChoiceQuestion mQuestion = new MultipleChoiceQuestion(text);

		AddDelQueries jdbc = new AddDelQueries();
		try(Connection con = jdbc.getConnection()) {
			jdbc.addQuestion(con, mQuestion);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}

		mQuestion.addAnswer(answer,isRight);
		allQuestions.add(mQuestion);
		numOfQuestions++;

		return true;
	}
	public boolean addOpenQ(String text,String answer) {
		if(text.isEmpty()) {
			System.out.println("the question you entered is not valid");
			return false;
		}	
		if(answer.isEmpty()) {
			System.out.println("the answer you entered is not valid");
			return false;
		}
		if (openQuestionExists(text)) {
			System.out.println("question already exist");
			return false;
		}	
		OpenQuestion q = new OpenQuestion(text,answer);
		AddDelQueries jdbc = new AddDelQueries();
		try(Connection con = jdbc.getConnection()) {
			jdbc.addQuestion(con, q);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}

		allQuestions.add(q);
		numOfQuestions++;

		return true;
	}
	public Question findBySerialNum (int serialNum) {
		if(serialNum<1)
			return null;
		for(int i=0;i<numOfQuestions;i++) {
			if(serialNum==allQuestions.get(i).getSerialNum()) {
				return allQuestions.get(i);
			}
		}
		return null; 
	}
	public boolean changeQText(String text,int serialNum){
		if(findBySerialNum(serialNum)==null) {
			System.out.println("the question number you entered does not exist");
			return false;
		}
		if(text.isEmpty()) {
			System.out.println("the question you entered is not valid");
			return false;
		}
		if (findBySerialNum(serialNum) instanceof OpenQuestion) {
			if(openQuestionExists(text)) {
				System.out.println("question already exist");
				return false;
			}
			else {
				findBySerialNum(serialNum).setText(text);
				return true;
			}
		}
		else {
			if(multipleQuestionExists(text)) {
				System.out.println("question already exist");
				return false;
			}
			else {
				findBySerialNum(serialNum).setText(text);
				return true;
			}
		}
	}
	public boolean changeOpenQAnswer(String answerText,int serialNum){
		if(findBySerialNum(serialNum) instanceof OpenQuestion) {
			if(answerText.isEmpty()) {
				System.out.println("the answer you entered is not valid");
				return false;
			}
			((OpenQuestion)findBySerialNum(serialNum)).setAnswer(answerText);
			return true;
		}
		else {
			System.out.println("there is no open question with that number");
			return false;
		}
	}
	public boolean changeMultipleChoiceQAnswer(String answerText,int serialNum,int answerNum) {
		if(findBySerialNum(serialNum) instanceof MultipleChoiceQuestion) {
			if((answerNum>((MultipleChoiceQuestion)findBySerialNum(serialNum)).getNumOfAnswers())||(answerNum<1)) {
				System.out.println("the answer number you entered does not exist in the chosen question");
				return false;
			}
			if(answerText.isEmpty()) {
				System.out.println("the answer you entered is not valid");
				return false;
			}
			AddDelQueries jdbc = new AddDelQueries(); 
			try(Connection con = jdbc.getConnection()) {
				jdbc.changeMultipleChoiceAnswer(con, answerText, ((MultipleChoiceQuestion)findBySerialNum(serialNum)).getAnswers().get(answerNum-1).getText(), serialNum);
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
			((MultipleChoiceQuestion)findBySerialNum(serialNum)).getAnswers().get(answerNum-1).setText(answerText);
			return true;
		}
		else {
			System.out.println("there is no multiple choice question with that number");
			return false;
		}
	}
	public boolean removeAnswerFromQ(int serialNum,int answerNum) {
		Question mQuestion=findBySerialNum(serialNum);
		if((mQuestion instanceof MultipleChoiceQuestion)) {
			if((answerNum<1)||(answerNum>((MultipleChoiceQuestion)mQuestion).getNumOfAnswers())) {
				System.out.println("the answer number you entered does not exist in the chosen question");
				return false;
			}
			if(((MultipleChoiceQuestion)mQuestion).getNumOfAnswers()==1) {
				System.out.println("could not remove the answer, question must have at least one answer");
				return false;
			}
			Set<Answer> answers=((MultipleChoiceQuestion)mQuestion).getAnswers();          
			int numOfAnswers=(((MultipleChoiceQuestion)mQuestion).getNumOfAnswers()-1);                                                                                      
			String deletedAnsTxt = answers.get(answerNum-1).getText();

			AddDelQueries jdbc = new AddDelQueries();
			try(Connection con = jdbc.getConnection()) {
				jdbc.deleteAnswer(con, deletedAnsTxt, serialNum);
				jdbc.updateNumOfAnswers(con, serialNum, numOfAnswers);
			}catch(Exception e) {
				throw new RuntimeException(e);
			}

			answers.remove(answerNum-1);
			((MultipleChoiceQuestion)mQuestion).setAnswers(answers);
			((MultipleChoiceQuestion)mQuestion).setNumOfAnswers(numOfAnswers);
			return true;
		}
		System.out.println("there is no multiple choice question with that number");
		return false;
	}
	public boolean openQuestionExists(String text) {
		for(int i=0;i<numOfQuestions;i++) {
			if(allQuestions.get(i).getText().equalsIgnoreCase(text)) {
				if(allQuestions.get(i) instanceof OpenQuestion) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean multipleQuestionExists(String text) {
		for(int i=0;i<numOfQuestions;i++) {
			if(allQuestions.get(i).getText().equalsIgnoreCase(text)) {
				if(allQuestions.get(i) instanceof MultipleChoiceQuestion) {
					return true;
				}
			}
		}
		return false;
	}
	public MultipleChoiceQuestion pickAnswers(int serialNum,ArrayList<Integer> answersNum) throws CloneNotSupportedException {
		MultipleChoiceQuestion mQuestion=(MultipleChoiceQuestion)((MultipleChoiceQuestion)findBySerialNum(serialNum)).clone();
		Set<Answer> pickedAnswers= new Set<Answer>();
		int rightCounter=0;
		for(int i=0;i<answersNum.size();i++) {
			pickedAnswers.add(mQuestion.getAnswers().get(answersNum.get(i)-1));
			if(pickedAnswers.get(i).isRight()==true)
				rightCounter++;
		}
		if(rightCounter>1) {
			pickedAnswers.add(new Answer("There are more than one right answer",true));
			pickedAnswers.add(new Answer("There are no right answers",false));
		}else if(rightCounter==1) {
			pickedAnswers.add(new Answer("There are more than one right answer",false));
			pickedAnswers.add(new Answer("There are no right answers",false));
		}else if(rightCounter<1) {
			pickedAnswers.add(new Answer("There are more than one right answer",false));
			pickedAnswers.add(new Answer("There are no right answers",true));
		}
		mQuestion.setAnswers(pickedAnswers);
		mQuestion.setNumOfAnswers(pickedAnswers.size());
		return mQuestion;
	}
	public boolean answerExists(int serialNum,int answerNum) {
		if((findBySerialNum(serialNum) instanceof MultipleChoiceQuestion)) {
			if((answerNum<1)||(answerNum>((MultipleChoiceQuestion)findBySerialNum(serialNum)).getNumOfAnswers())) {
				System.out.println("the answer number you entered does not exist in the chosen question");
				return false;
			}
			return true;
		}
		return false;
	}
	public ArrayList<Question> autoTestBuild(int manyQuestions) throws CloneNotSupportedException{
		ArrayList<Question> testBuild=new ArrayList<Question>();
		ArrayList <Integer> usedQuestions=new ArrayList<Integer>(Collections.nCopies(numOfQuestions, 0));
		for(int i=0;i<manyQuestions;i++) {
			int index = 0;
			int pickQuestion=0;
			while(index==0) {
				pickQuestion=((int)(Math.random()*numOfQuestions)+1);
				if(findBySerialNum(pickQuestion) instanceof MultipleChoiceQuestion) {
					if(!validMCQforAutoTest((MultipleChoiceQuestion)findBySerialNum(pickQuestion)))
						usedQuestions.set(pickQuestion-1,1);
				}
				if(usedQuestions.get(pickQuestion-1)==0) {
					usedQuestions.set(pickQuestion-1,1);
					index++;
				}
			}
			if(findBySerialNum(pickQuestion) instanceof MultipleChoiceQuestion) {
				int n=((MultipleChoiceQuestion)findBySerialNum(pickQuestion)).getNumOfAnswers();
				ArrayList<Integer> pickedAnswers=new ArrayList <Integer>(Collections.nCopies(n,0));
				ArrayList<Integer> addAnswers=new ArrayList <Integer>(); 
				Set<Answer> answers =((MultipleChoiceQuestion)findBySerialNum(pickQuestion)).getAnswers();
				int rightAnswers=0;
				for(int j=0;j<4;j++) {
					int pickAnswer=0;
					int check=0;
					while(check==0) {
						pickAnswer=((int)(Math.random()*n)+1);
						if(pickedAnswers.get(pickAnswer-1)==0) {
							pickedAnswers.set(pickAnswer-1,1);
							check++;
							if(answers.get(pickAnswer-1).isRight()==true) {
								rightAnswers++;
							}
							if(rightAnswers>1) {
								rightAnswers--;
								check--;
							}
						}	
					}
					addAnswers.add(pickAnswer);
				}
				testBuild.add(pickAnswers(pickQuestion,addAnswers));
			}
			else {
				testBuild.add((OpenQuestion)((OpenQuestion)findBySerialNum(pickQuestion)).clone());
			}
		}
		return testBuild;
	}
	public int ValidQuestionsAutoTest() {
		int validQuestions=0;
		for(int i=0;i<numOfQuestions;i++) {
			if(allQuestions.get(i) instanceof OpenQuestion) {
				validQuestions++;
			}
			else {
				if(validMCQforAutoTest((MultipleChoiceQuestion)allQuestions.get(i))) {
					validQuestions++;
				}
			}
		}
		return validQuestions;
	}
	public boolean validMCQforAutoTest(MultipleChoiceQuestion mQuestion) {
		if(mQuestion.getNumOfAnswers()<4)
			return false;
		Set<Answer> ans=mQuestion.getAnswers();
		int falseAns=0;
		for(int i=0;i<mQuestion.getNumOfAnswers();i++) {
			if(ans.get(i).isRight()==false)
				falseAns++;
		}
		if(falseAns < 3)
			return false;
		return true;
	}
	public void printQuestions(ArrayList<Question> questions) {
		questions = orgByAnsLength(questions);
		System.out.println();
		System.out.println("The Test:");
		System.out.println();
		for(int i=0;i<questions.size();i++) {
			if(questions.get(i) instanceof OpenQuestion) {
				System.out.println(((OpenQuestion)questions.get(i)));
			}
			else {
				System.out.println(((MultipleChoiceQuestion)questions.get(i)));
			}
		}
	}
	public boolean checkIfUsed(int pickQuestion,int[]usedQuestions,int questionCount) {
		for(int i=0;i<questionCount;i++) {
			if(pickQuestion==usedQuestions[i])
				return true;
		}
		return false;
	}
	public void copyTest(String fileName) {

	}
	@Override	public String toString() {
		String s = "there are "+numOfQuestions+" questions:"+"\n"+"\n";
		for(int i=0;i<numOfQuestions;i++) {
			if(allQuestions.get(i) instanceof OpenQuestion) {
				s += ((OpenQuestion)allQuestions.get(i)).toString();
			}
			else {
				s += ((MultipleChoiceQuestion)allQuestions.get(i)).toString();
			}
		}
		return s;
	}

	public ArrayList<Test> getAllTests() {
		return allTests;
	}
}

