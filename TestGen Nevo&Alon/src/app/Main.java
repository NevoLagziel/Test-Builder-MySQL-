package app;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

import controller.TestBuilder;
import model.MultipleChoiceQuestion;
import model.OpenQuestion;
import model.Question;
import model.Test;
import query.AddDelQueries;

public class Main {

	public static Scanner scan = new Scanner(System.in);   

	public static void main(String[] args) throws IOException, CloneNotSupportedException {

		TestBuilder test= new TestBuilder();
		AddDelQueries jdbc = new AddDelQueries();
		try(Connection con = jdbc.getConnection()) {
			Test.setTestNumGenerator(jdbc.getNumOfTests(con)+1);
			test.setAllQuestions(jdbc.getAllQuestions(con));
		}catch(Exception e) {
			throw new RuntimeException(e);
		}

		boolean continueMenu1 = true;
		int choice1;
		do{
			try {
				do {
					System.out.println();
					System.out.println("MAIN MENU");
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					System.out.println("1 - show all the questions");
					System.out.println("2 - add new question");
					System.out.println("3 - change question wording");
					System.out.println("4 - change answer");
					System.out.println("5 - build a test manually");
					System.out.println("6 - build a test automatically");
					System.out.println("7 - show a built test");
					System.out.println("8 - make a copy of a test");
					System.out.println("0 - EXIT");
					choice1=scan.nextInt();

					int manyAnswers=0;
					int questionNum=0;
					int answerNum=0;
					int manyQuestions=0;
					int testNum=0;
					boolean isRight;

					switch (choice1) {
					case 0:
						
						System.out.println("bye bye :)");
						break;
					case 1:
						System.out.println(test);
						break;
					case 2:
						int choice2;
						boolean continueMenu2 = true;
						do {
							try {
								do {
									System.out.println("add new question");
									System.out.println(" V   V   V   V");
									System.out.println("   what type of question would you like to add ?");
									System.out.println("   1 - open question");
									System.out.println("   2 - multiple choice question");
									System.out.println("   0 - go back to the Main menu");
									System.out.println();
									choice2=scan.nextInt();
									switch(choice2) {
									case 0:
										break;
									case 1: 
										scan.nextLine();

										System.out.println("enter the question you would like to add:");
										String openQ = scan.nextLine();

										System.out.println("enter the answer for the question:");
										String openA = scan.nextLine();

										if(test.addOpenQ(openQ, openA)) {
											System.out.println("the question has been added");
										}
										break;
									case 2:
										scan.nextLine();

										System.out.println("enter the question you would like to add:");
										String multipleQ = scan.nextLine();

										System.out.println("enter the answer:");
										String multipleA = scan.nextLine();

										boolean continueBool=true;
										do {
											try {
												System.out.println("is the answer true/false");
												isRight = scan.nextBoolean();

												continueBool=false;

												if(!test.addMultipleChoiceQ(multipleQ, multipleA, isRight)) {          
													break;                                                             
												}
											}catch(InputMismatchException e) {
												System.out.println("wrong input, try again");
												scan.nextLine();
											}
										}while(continueBool);

										scan.nextLine();
										System.out.println("would you like to add another answer?(yes/no)");
										String stopper=scan.nextLine();

										while(stopper.equalsIgnoreCase("yes")){

											System.out.println("enter the answer:");
											String anotherAnswer=scan.nextLine();

											continueBool=true;
											do {
												try {
													System.out.println("fill : (ture/false)");
													isRight=scan.nextBoolean();

													continueBool=false;

													if(test.addAnswer(test.getNumOfQuestions(),anotherAnswer,isRight)) {
														System.out.println("answer has been added");
													}
												}catch(InputMismatchException e) {
													System.out.println("wrong input, try again");
													scan.nextLine();
												}
											}while(continueBool);

											scan.nextLine();
											System.out.println("would you like to add another answer?(yes/no)");
											stopper=scan.nextLine();
										}
										System.out.println("the question has been added");
										break;
									default:
										System.out.println("Wrong Option!");
									}
								}while(choice2 != 0);
								continueMenu2 = false;
							}catch(InputMismatchException e) {
								System.out.println("wrong input, try again");
								scan.nextLine();
							}
						}while(continueMenu2);
						break;
					case 3:
						boolean continueNum = true;
						do {
							try {
								System.out.println("What is the number of the question you would like to change ?");
								questionNum = scan.nextInt();

								continueNum=false;

								scan.nextLine();
								System.out.println("write the new question:");
								String newText=scan.nextLine();

								if(test.changeQText(newText, questionNum)) {
									try(Connection con = jdbc.getConnection()) {
										jdbc.changeQuestionTxt(con, newText, questionNum);
									}catch(Exception e) {
										throw new RuntimeException(e);
									}
									System.out.println("question has been changed");
								}
							}catch(InputMismatchException e) {
								System.out.println("wrong input, try again");
								scan.nextLine();
							}
						}while(continueNum);
						break;
					case 4:
						int choice3;
						boolean continueMenu3=true;
						do {
							try {
								do {
									System.out.println("change answer");
									System.out.println(" V   V   V   V");
									System.out.println("   1 - change wording of a open question's answer");
									System.out.println("   2 - change wording of a multiple choice question's answer");
									System.out.println("   3 - delete a multiple choice question's answer");
									System.out.println("   4 - add answer to a multiple choice question");
									System.out.println("   0 - go back to the menu");
									System.out.println();
									choice3=scan.nextInt();
									continueMenu3 = false;
									switch(choice3) {
									case 0:
										break;
									case 1:
										continueNum=true;
										do {
											try {
												System.out.println("What is the answer's question number ?");
												questionNum=scan.nextInt();

												continueNum=false;

												scan.nextLine();
												System.out.println("write the new answer:");
												String newAns = scan.nextLine();

												if(test.changeOpenQAnswer(newAns,questionNum)) {
													try(Connection con = jdbc.getConnection()) {
														jdbc.changeOpenQuestionAnswer(con, newAns, questionNum);
													}catch(Exception e) {
														throw new RuntimeException(e);
													}
													System.out.println("answer has been changed");
													choice3=0;
												}
											}catch(InputMismatchException e) {
												System.out.println("wrong input, try again");
												scan.nextLine();
											}
										}while(continueNum);
										break;
									case 2:
										continueNum=true;
										do {
											try {
												System.out.println("What is the answer's question number ?");
												questionNum=scan.nextInt();

												System.out.println("What is the answer number ?");
												answerNum=scan.nextInt();

												continueNum=false;

												scan.nextLine();
												System.out.println("write the new answer:");
												String newMAns=scan.nextLine();

												if(test.changeMultipleChoiceQAnswer(newMAns, questionNum, answerNum)) {
													System.out.println("answer has been changed");
													choice3=0;
												}
											}catch(InputMismatchException e) {
												System.out.println("wrong input, try again");
												scan.nextLine();
											}
										}while(continueNum);
										break;
									case 3:
										continueNum=true;
										do {
											try {
												System.out.println("What is the answer's question number ?");
												questionNum=scan.nextInt();

												System.out.println("What is the answer number ?");
												answerNum=scan.nextInt();

												continueNum=false;

												if(test.removeAnswerFromQ(questionNum, answerNum)) {
													System.out.println("answer has been removed");
													choice3=0;
												}
											}catch(InputMismatchException e) {
												System.out.println("wrong input, try again");
												scan.nextLine();
											}
										}while(continueNum);
										break;
									case 4:
										continueNum=true;
										do {
											try {
												System.out.println("What is the question number ?");
												questionNum=scan.nextInt();

												scan.nextLine();
												System.out.println("enter the answer text:");
												String anotherAnswer=scan.nextLine();

												System.out.println("fill : (ture/false)");
												isRight=scan.nextBoolean();

												continueNum=false;

												if(test.addAnswer(questionNum,anotherAnswer,isRight)) {
													System.out.println("answer has been added");
													choice3=0;
												}

											}catch(InputMismatchException e) {
												System.out.println("wrong input, try again");
												scan.nextLine();
											}
										}while(continueNum);

										break;
									}
								}while(choice3 != 0);

							}catch(InputMismatchException e) {
								System.out.println("wrong input, try again");
								scan.nextLine();
							}
						}while(continueMenu3);
						break;
					case 5:
						continueNum=true;
						do {
							try {
								System.out.println("how many questions would you like to add ?");
								manyQuestions = scan.nextInt();
								continueNum=false;
							}catch(InputMismatchException e) {
								System.out.println("wrong input, try again");
								scan.nextLine();
							}
						}while(continueNum);

						if(manyQuestions>test.getNumOfQuestions()) {
							System.out.println("there are not enough questions in the data base");
							break;
						}
						ArrayList<Integer> questionsPicked=new ArrayList<Integer>(Collections.nCopies(test.getNumOfQuestions(),0));
						ArrayList<Question> testBuild =new ArrayList<Question>();

						int questionCounter=0;
						while(questionCounter<manyQuestions) {

							boolean questionPickedAlready = true;
							while(questionPickedAlready) {

								continueNum=true;
								do {
									try {
										System.out.println("what is the number of question you would like to add ?");
										questionNum = scan.nextInt();

										continueNum=false;

									}catch(InputMismatchException e) {
										System.out.println("wrong input, try again");
										scan.nextLine();
									}
								}while(continueNum);

								if(questionsPicked.get(questionNum-1)==1){
									System.out.println("already picked that question,pick a diffrent question");
								}else {
									questionPickedAlready = false;
								}
							}
							if(test.findBySerialNum(questionNum) instanceof MultipleChoiceQuestion) {
								int numOfAnswers=((MultipleChoiceQuestion)test.findBySerialNum(questionNum)).getNumOfAnswers();

								boolean notEnoughAnswers = true; 
								while(notEnoughAnswers) {

									continueNum=true;
									do {
										try {
											System.out.println("how many answers would you like to add?(between 1 to "+numOfAnswers+" answers)");
											manyAnswers = scan.nextInt();

											continueNum=false;

										}catch(InputMismatchException e) {
											System.out.println("wrong input, try again");
											scan.nextLine();
										}
									}while(continueNum);

									if((manyAnswers>numOfAnswers)||(manyAnswers<1)) {
										System.out.println("not a valid amount of answers,try to pick again");	
									}else {
										notEnoughAnswers = false;
									}
								}

								ArrayList<Integer> answers=new ArrayList<Integer>();
								int answerCounter=0;
								while(answerCounter<manyAnswers) {

									boolean alreadyPickedAns = true;
									while(alreadyPickedAns) {

										continueNum=true;
										do {
											try {
												System.out.println("what number of answer would you like to add ?");
												answerNum = scan.nextInt();
												continueNum=false;
											}catch(InputMismatchException e) {
												System.out.println("wrong input, try again");
												scan.nextLine();
											}
										}while(continueNum);

										if(answers.contains(answerNum)) {
											System.out.println("you already picked that answer,pick a diffrent answer");
										}else {
											alreadyPickedAns=false;
										}
									}
									if(test.answerExists(questionNum, answerNum)) {
										answers.add(answerNum);
										answerCounter++;
									}
								}
								testBuild.add(test.pickAnswers(questionNum, answers));
								questionCounter++;
								questionsPicked.set(questionNum-1,1);
								System.out.println("added question to the test");
							}else if(test.findBySerialNum(questionNum) instanceof OpenQuestion) {
								testBuild.add((OpenQuestion)((OpenQuestion)test.findBySerialNum(questionNum)).clone());
								questionCounter++;
								questionsPicked.set(questionNum-1,1);
								System.out.println("added question to the test");
							}else{
								System.out.println("question number entered does not exists");
							}
						}
						//test.saveTestAsTxt(testBuild);
						test.buildTest(testBuild);
						test.printQuestions(testBuild);
						break;
					case 6:
						scan.nextLine();
						continueNum=true;
						do {
							try {
								System.out.println("how many questions would you like ?");
								manyQuestions=scan.nextInt();
								continueNum=false;
							}catch(InputMismatchException e) {
								System.out.println("wrong input, try again");
								scan.nextLine();
							}
						}while(continueNum);
						if((manyQuestions>test.getNumOfQuestions())||(test.ValidQuestionsAutoTest()<manyQuestions)){
							System.out.println("there is not enough questions in the database");
							break;
						}
						ArrayList<Question> newRandTest = test.autoTestBuild(manyQuestions);
						//test.saveTestAsTxt(newRandTest);
						test.buildTest(newRandTest);
						test.printQuestions(newRandTest);
						break;
					case 7:
						try(Connection con = jdbc.getConnection()){
							test.setNumOfTests(jdbc.getNumOfTests(con));
						}catch(Exception e) {
							throw new RuntimeException(e);
						}

						System.out.println("there are "+test.getNumOfTests()+" tests:");
						test.printTestsNames();
						continueNum=true;
						do {
							try {
								System.out.println("what number of test would you like to see?(0 - back to menu)");
								testNum = scan.nextInt();
								continueNum=false;
							}catch(InputMismatchException e) {
								System.out.println("wrong input, try again");
								scan.nextLine();
							}
						}while(continueNum);

						if(testNum == 0)
							break;
						if((testNum <= test.getNumOfTests())&&(testNum > 0)) {
							try(Connection con = jdbc.getConnection()){
								Test ts = jdbc.getTest(con, testNum);
								test.printQuestions(ts.getQuestions());
							}catch(Exception e) {
								throw new RuntimeException(e);
							}
						}
						else {
							System.out.println("the test number you entered doesnt exists");
						}
						break;
					case 8:
						continueNum=true;
						do {
							try {
								System.out.println("what number of test would you like clone?(0 - back to menu)");
								testNum = scan.nextInt();
								continueNum=false;
							}catch(InputMismatchException e) {
								System.out.println("wrong input, try again");
								scan.nextLine();
							}
						}while(continueNum);

						if(testNum==0)
							break;
						test.cloneTest(testNum);
						break;
					default:
						System.out.println("Wrong Option!");
					}
				}while(choice1 != 0);
				continueMenu1=false;
			}catch(InputMismatchException e) {
				System.out.println("wrong input, try again");
				scan.nextLine();
			}
		}while(continueMenu1);
	}
}
