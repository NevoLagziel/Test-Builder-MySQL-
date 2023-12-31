package model;

import java.io.Serializable;

public abstract class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	protected int serialNum;
	protected static int serialNumGenerator = 1;
	
	
	public static void setSerialNumGenerator(int serialNumGenerator) {
		Question.serialNumGenerator = serialNumGenerator;
	}

	protected String text;
	
	public Question()                          //new
	{
		text = null;
		serialNum = 0;
	}

	public Question (String text)
	{
		this.text=text;
		serialNum=serialNumGenerator++;
	}
	public void setSerialNum(int num) {
		this.serialNum=num;
	}
	 
	public void setText(String text) {
		this.text=text;
	}
	public int getSerialNum() {
		return serialNum;
	}
	public String getText() {
		return text;
	}

	@Override
	public String toString() {             
		return "question number("+serialNum+"): "+"\n"+text+ "\n";
	}
}


