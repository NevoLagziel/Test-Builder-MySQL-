package model;

public interface MainInterface {               //OQ = Open Question || MCQ = Multiple Choice Question             

	public void showAllQuestions();            //all of the questions in our data base and their answers                        

	public void addOQToDataBase();   

	public void addMCQToDataBase(); 

	public void changeQuestionWording();

	public void changeMCQAnswerWording();

	public void changeOQAnswerWording();

	public void addMCQAnswer();
	
	public void deleteMCQAnswer();  

	public void buildTestManualy();

	public void buildTestAutomatically();

	public void showAlreadyBuiltTest();        //prints names of all the tests in the data base

	public void makeCopyOfATest();
}
