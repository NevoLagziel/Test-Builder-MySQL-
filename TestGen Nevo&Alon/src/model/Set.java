package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Set <T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<T> arr;

	@SuppressWarnings("unchecked")
	public Set() {
		arr =(ArrayList<T>) new ArrayList<Object>();
	}
	public void clear() {
		arr.clear();
	}
	public boolean remove(int index){
		if(index >= size())
			return false;
		arr.remove(index);
		return true;
	}
	public boolean add(T obj) {
		if(contains(obj))
			return false;
		arr.add(obj);
		return true;
	}
	public boolean contains(T obj) {
		for(int i=0;i<size();i++) {
			if(arr.get(i).equals(obj))
				return true;
		}
		return false;
	}
	public int size() {
		return arr.size();
	}
	public T get(int index) {
		return arr.get(index);
	}
}
