package model;

import java.io.Serializable;
import java.util.Objects;

public class Answer implements Serializable {

	private static final long serialVersionUID = 1L;
	private String text;
	private boolean isRight;

	public Answer() {
		text = null;
		isRight = false;
	}
	
	public Answer (String text,boolean isRight) {
		this.text=text;
		this.isRight=isRight;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Answer other = (Answer) obj;
		return isRight == other.isRight && Objects.equals(text, other.text);
	}

	@Override
	public String toString() {
		return text +"("+ isRight +")"+"\n";
	}
}
