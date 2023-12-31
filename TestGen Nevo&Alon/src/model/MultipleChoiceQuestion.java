package model;

import java.sql.Connection;

import query.AddDelQueries;

public class MultipleChoiceQuestion extends Question implements Cloneable {

	private static final long serialVersionUID = 1L;
	private Set<Answer> answers;
	private int numOfAnswers=0;

	public MultipleChoiceQuestion()
	{
		super();
		numOfAnswers = 0;
		answers = null;
	}
	
	public MultipleChoiceQuestion(String text) {
		super(text);
		numOfAnswers=0;
		answers=new Set<Answer>();
	}
	public MultipleChoiceQuestion clone() throws CloneNotSupportedException {
		MultipleChoiceQuestion clone = new MultipleChoiceQuestion();
		clone.serialNum = this.serialNum;
		clone.text = this.text;
		clone.numOfAnswers = this.numOfAnswers;
		clone.answers = this.answers;
		return clone;
	}
	public boolean addAnswer(String text,boolean isRight) {
		Answer ans = new Answer(text, isRight);
		if(answers.add(ans)) {
			numOfAnswers++;
			
			AddDelQueries jdbc = new AddDelQueries();
			try(Connection con = jdbc.getConnection()) {
				jdbc.addAnswer(con, ans, this.getSerialNum());
				jdbc.updateNumOfAnswers(con, this.getSerialNum(), numOfAnswers);
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
			
			return true;
		}
		return false;
	}	
	public int getNumOfAnswers() {
		return numOfAnswers;
	}
	public void setNumOfAnswers(int numOfAnswers) {
		this.numOfAnswers=numOfAnswers;
	}
	public Set<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += "1)"+answers.get(0).toString();
		for(int i=1;i<numOfAnswers;i++){
			s +=(i+1)+")"+answers.get(i).toString();
		}
		s +="\n";
		return s;
	}

}
