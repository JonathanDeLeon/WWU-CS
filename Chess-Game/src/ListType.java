import java.util.Iterator;

/** 
 * 
 * @author Jonathan De Leon
 * @category CS 241
 * @version 1.00
 * @content:
 * The ListType class is a concrete generic class
 * for storing a list of E objects.
*/

public class ListType<E> implements GeneralList<E>, Iterable<E>{
	
	//constants
	private final int DEFAULT_CAPACITY = 10;
	private final int RESIZE_FACTOR = 2;
	
	//private fields
	private E[] list;	//list
	private int elements;	//number of elements
	private int listCapacity; 	//capacity of the list

	/**
	 * Default constructor
	 */
	public ListType(){
		list = (E[])(new Object[DEFAULT_CAPACITY]);
		elements = 0;
		listCapacity = DEFAULT_CAPACITY;
	}
	
	/**
	 * Constructor with param
	 * @param capacity
	 * @exception IllegalArgumentException if the capacity
	 * is less than one
	 */
	public ListType(int capacity){
		if(capacity < 1)
			throw new IllegalArgumentException();
		list = (E[])(new Object[capacity]);
		elements = 0;
		listCapacity = capacity;
	}
	
	/**
	 * Add element to the end of the list
	 * @param element The element to add
	 */
	public void add(E element) {
		if(elements == list.length)
			resize();
		list[elements] = element;
		elements++;
	}

	/**
	 * Add element to specified index.
	 * @param index Added element's position
	 * @param element The element to add
	 * @exception IndexOutOfBoundsException When index is out of bounds
	 */
	public void add(int index, E element) {
		if(index > elements || index < 0)
			throw new IndexOutOfBoundsException();
		if(elements == list.length)
			resize();
		for(int i = elements; i > index; i--)
			list[i]	= list[i-1];
		list[index] = element;
		elements++;
	}

	/**
	 * Clears the list
	 */
	public void clear() {
		for(int i = 0; i < list.length; i++)
			list[i] = null;
		elements = 0;
	}
	
	/**
	 * Search the list for a specified element
	 * @param element The element searched for
	 * @return true if the list contains the element
	 */
	public boolean contains(E element) {
		int index = 0;
		boolean found = false;
		
		while(!found && index < elements){
			if(list[index].equals(element))
				found = true;
			index++;
		}
		return found;
	}

	/**
	 * Get an element at a specific position.
	 * @param index The specified index
	 * @return The element at index
	 * @exception IndexOutOfBoundsException When index is out of bounds
	 */
	public E get(int index) {
		if(index >= elements || index < 0)
			throw new IndexOutOfBoundsException();
		return list[index];
	}

	/**
	 * Gets the index of the first occurence of the specified element
	 * @param element Element searched for
	 * @return The index of the element if it exists; -1 if it does not exist
	 */
	public int indexOf(E element) {
		int index = 0;
		boolean found = false;
		
		while(!found && index < elements){
			if(list[index].equals(element))
				found = true;
			else
				index++;
		}
		if(!found)
			index = -1;
		return index;
	}

	/**
	 * Determines whether the list is empty
	 * @return true if the list is empty
	 */
	public boolean isEmpty() {
		return (elements == 0);
	}

	/**
	 * Remove a specific element from the list
	 * @param element Element to be removed
	 * @return true if element was found
	 */
	public boolean remove(E element) {
		int index = 0;
		boolean found = false;
		
		while(!found && index < elements){
			if(list[index].equals(element)){
				found = true;
				list[index] = null;
			}
			index++;
		}
		if(found){
			while(index < elements){
				list[index -1] = list[index];
				index++;
			}
		}
		elements--;
		return found;
	}

	/**
	 * Remove an element at a specific index.
	 * @param index The index of the element to remove
	 * @return The element that was removed
	 * @exception IndexOutOfBoundsException When index is out of bounds
	 */
	public E remove(int index) {
		if(index >= elements || index < 0)
			throw new IndexOutOfBoundsException();
		E temp = list[index];
		list[index] = null;
		index++;

		while(index < elements){
			list[index -1] = list[index];
			index++;
		}
		
		elements--;
		return temp;
	}
	
	/**
	 * Resizes the list to twice its current length
	 */
	public void resize(){
		int newLength = list.length * RESIZE_FACTOR;
		E[] tempList = (E[])(new Object[newLength]);
		listCapacity = newLength;
		for(int i = 0; i < elements; i++)
			tempList[i] = list[i];
		list = tempList;
	}
	
	/**
	 * Replace the element at a specified index with another element
	 * @param index The index of the element to replace
	 * @param element The element to replace it with.
	 * @return The element previously stored at the index
	 * @exception IndexOutOfBoundsException When index is out of bounds
	 */

	public E set(int index, E element) {
		if(index >= elements || index < 0)
			throw new IndexOutOfBoundsException();
		E temp = list[index];
		list[index] = element;
		return temp;
	}
	
	/**
	 * Get the number of elements in the list.
	 * @return The number of elements in the list
	 */
	public int size() {
		return elements;
	}
	
	/**
	 * @return capacity of list 
	 */
	public int getCapacity(){
		return listCapacity;
	}
	
	/**
	 * Increases capacity if capacity is less than minCapacity
	 * @param minCapacity Desired capacity
	 */
	public void ensureCapacity(int minCapacity){
		if(listCapacity < minCapacity){
			listCapacity = minCapacity;
			E[] tempList = (E[])(new Object[listCapacity]);
			for(int i = 0; i < elements; i++)
				tempList[i] = list[i];
			list = tempList;
		}
	}
	
	/**
	 * Trims the list's capacity to be equal to list's current size
	 */
	public void trimToSize(){
		if(elements < listCapacity && elements > 0){
			listCapacity = elements;
			E[] tempList = (E[])(new Object[listCapacity]);
			for(int i = 0; i < elements; i++)
				tempList[i] = list[i];
			list = tempList;
		}
	}
	
	/**
	 * Convert the list to a String array.
	 * @return A String array representing the list.
	 */
	public String[] toStringArray(){
		String[] str = new String[elements];
		for(int i = 0; i < elements; i++)
			str[i] = list[i].toString();
		return str;
	}
	
	/**
	 * Return an iterator for this list.
	 * @return an iterator
	 */
	public Iterator<E> iterator(){
		return new ListTypeIterator<E>(this);
	}
}