package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Question> questions;
	private int numOfQuestions;
	private String name;
	private int testNum;
	private static int testNumGenerator = 1;
	
	public Test() {
		questions = null;
		numOfQuestions = 0;
		name = null;
		testNum = 0;
	}
	
	public Test (ArrayList<Question> questions,int numOfQuestions,String name) {
		this.questions=questions;
		this.name=name;
		this.numOfQuestions=numOfQuestions;
		testNum=testNumGenerator++;
	}
	public Test(Test other) {
		this.name=other.name+"copy";
		this.numOfQuestions=other.numOfQuestions;
		this.questions=other.questions;
		testNum=testNumGenerator++;
	}
	public int getTestNum() {
		return testNum;
	}
	public void setTestNum(int testNum) {
		this.testNum = testNum;
	}
	public static void setTestNumGenerator(int testNumGenerator) {
		Test.testNumGenerator = testNumGenerator;
	}
	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	public int getNumOfQuestions() {
		return numOfQuestions;
	}

	public void setNumOfQuestions(int numOfQuestions) {
		this.numOfQuestions = numOfQuestions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
