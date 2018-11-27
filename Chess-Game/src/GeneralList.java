/**
 * The GeneralList interface specifies the general
 * operations that should be supported by a list
 * 
*/

public interface GeneralList<E>
{
   /** The add method adds an element to the list.
       @param element The element to add. 
   */
   public void add(E element);

   /** The add method adds a element at a specific index.
       @param index The added element's position.
       @param element The element to add.
       @exception IndexOutOtBoundsException When index
                  is out of bounds.
   */   
   public void add(int index, E element);

   /** The clear method clears the list. */
   public void clear();
   
   /** The contains method searches the list
       for a specified element.
       @param element The element to search for.
       @return true if the list contains the element;
               false otherwise.
   */
   public boolean contains(E element);
   
   /** The get method gets an element at a specific position.
       @param index The specified index.
       @return The element at index.
       @exception IndexOutOtBoundsException When index
                  is out of bounds.
   */
   public E get(int index);
   
   /** The indexOf method gets the index of the first
       occurrence of the specified element.
       @param element The element to search for.
       @return The index of the first occurrence of element
               if it exists; -1 if element is not in the list.
   */
   public int indexOf(E element);
   
   /** The isEmpty method determines whether the list is empty.
       @return true if the list is empty; false otherwise.
   */
   public boolean isEmpty();
   
   /** The remove method remove a specific element from the list.
       @param element The element to remove.
       @return true if the element was found; false otherwise.
   */
   public boolean remove(E element);
   
   /** The remove method removes a element at a specific index.
       @param index The index of the element to remove.
       @return The element that was removed.
       @exception IndexOutOtBoundsException When index
                  is out of bounds.
   */ 
   public E remove(int index);
   
   /** The set method replaces the element at a specified
       index with another element.
       @param index The index of the element to replace.
       @param element The element to replace it with.
       @return The element previously stored at the index.
       @exception IndexOutOtBoundsException When index
                  is out of bounds.     
   */
   public E set(int index, E element);

   /** The size method gets the number of elements in the list.
       @return The number of elements in the list.
   */
   public int size();
}