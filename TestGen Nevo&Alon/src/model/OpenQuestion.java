package model;

public class OpenQuestion extends Question implements Cloneable{

	private static final long serialVersionUID = 1L;
	private String answer;

	
	public OpenQuestion() {                //new
		super();
		answer = null;
	}
	
	public OpenQuestion(String text,String answer) {
		super(text);
		this.answer=answer;
	}
	
	public String getAnswer() {
		return answer;
	}
	public OpenQuestion clone() throws CloneNotSupportedException {
		OpenQuestion clone = new OpenQuestion();
		clone.serialNum = this.serialNum;
		clone.text = this.text;
		clone.answer = this.answer;
		return clone;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return super.toString()+ "answer: " +answer+"\n"+"\n";
	}
}
