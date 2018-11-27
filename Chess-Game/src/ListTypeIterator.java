import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Jonathan De Leon
 * @category CS 241
 * @version 1.00
 * @content:
 * 		Iterator class for the ListType class
 */

public class ListTypeIterator<E> implements Iterator<E> {
	
	private GeneralList<E> list;	//list to iterate over
	private int previous;			//previous element
	boolean canRemove;				//Flag

	/**
	 * Constructor
	 * @param aList List to iterate over
	 */
	public ListTypeIterator(GeneralList<E> aList) {
		list = aList;
		previous = -1;		//the iterator is positioned before first element
		canRemove=false;
	}
	
	/**
	 * The method determines whether another element
	 * can be returned by a call to next
	 * @return true if the iterator has more elements
	 */
	public boolean hasNext(){
		if((previous+1) < list.size())
			return true;
		else
			return false;
	}
	
	/**
	 * The method returns the next element in the list
	 * @return Next element in list
	 */
	public E next(){
		if(!hasNext())
			throw new NoSuchElementException();
		
		//Gives us index of the next element in list
		previous++;
		//this indicates that remove method can be called
		canRemove = true;
		
		return list.get(previous);
	}
	
	/**
	 * Method removes from the list the last element 
	 * that was returned by the next method. 
	 */
	public void remove(){
		if(!canRemove)
			throw new IllegalStateException();
		//remove previous element
		list.remove(previous);
		//adjust previous
		previous--;
		//reset the field
		canRemove = false;
	}
}
