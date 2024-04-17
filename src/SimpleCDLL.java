import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Simple circular doubly-linked lists.
 *
 * @author Sam Rebelsky
 * @author Amelia Vrieze
 */
public class SimpleCDLL<T> implements SimpleList<T> {
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The dummy node
   */
  Node2<T> dummy = new Node2<T>(null);

  /**
   * The number of values in the list.
   */
  int size;

  /**
   * The number of changes made to the list so far
   */

   int changes;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty list.
   */
  public SimpleCDLL() {
    this.dummy.prev = dummy;
    this.dummy.next = dummy;
    this.size = 0;
    this.changes = 0;
  } // SimpleDLL

  // +-----------+---------------------------------------------------------
  // | Iterators |
  // +-----------+

  public Iterator<T> iterator() {
    return listIterator();
  } // iterator()

  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      // +--------+--------------------------------------------------------
      // | Fields |
      // +--------+

      /**
       * The position in the list of the next value to be returned.
       * Included because ListIterators must provide nextIndex and
       * prevIndex.
       */
      int pos = 0;

      /**
       * The cursor is between neighboring values, so we start links
       * to the previous and next value..
       */
      Node2<T> prev = SimpleCDLL.this.dummy;
      Node2<T> next = SimpleCDLL.this.dummy.next;

      /**
       * The node to be updated by remove or set.  Has a value of
       * null when there is no such value.
       */
      Node2<T> update;

      /**
       * Number of changes at time of creation of the iterator
       * Only changes made by this iterator instance update the value
       */
       int changes = SimpleCDLL.this.changes;

      // +---------+-------------------------------------------------------
      // | Methods |
      // +---------+

      public void add(T val) throws UnsupportedOperationException {
        failFast();

        this.prev = this.prev.insertAfter(val);

        // Note that we cannot update
        this.update = null;

        // Increase the size
        ++SimpleCDLL.this.size;

        // Update the position.  (See SimpleArrayList.java for more of
        // an explanation.)
        ++this.pos;
        updateChanges();
      } // add(T)

      public boolean hasNext() {
        failFast();
        return this.next != SimpleCDLL.this.dummy;
      } // hasNext()

      public boolean hasPrevious() {
        failFast();
        return this.prev != SimpleCDLL.this.dummy;
      } // hasPrevious()

      public T next() {
        failFast();
        if (!this.hasNext()) {
         throw new NoSuchElementException();
        } // if
        // Identify the node to update
        this.update = this.next;
        // Advance the cursor
        this.prev = this.next;
        this.next = this.next.next;
        // Note the movement
        ++this.pos;
        // And return the value
        return this.update.value;
      } // next()

      public int nextIndex() {
        failFast();
        return this.pos;
      } // nextIndex()

      public int previousIndex() {
        failFast();
        return this.pos - 1;
      } // prevIndex

      public T previous() throws NoSuchElementException {
        failFast();
        if (!this.hasPrevious())
          throw new NoSuchElementException();

        this.update = this.prev;
        this.next = this.prev;
        this.prev = this.prev.prev;
        
        --this.pos;
        return this.update.value;
      } // previous()

      public void remove() {
        failFast();
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if

        // Update the cursor
        if (this.next == this.update) {
          this.next = this.update.next;
        } // if
        if (this.prev == this.update) {
          this.prev = this.update.prev;
          --this.pos;
        } // if

        // Do the real work
        this.update.remove();
        --SimpleCDLL.this.size;

        // Note that no more updates are possible
        this.update = null;
        updateChanges();
      } // remove()

      public void set(T val) {
        failFast();
        // Sanity check
        if (this.update == null) {
          throw new IllegalStateException();
        } // if
        // Do the real work
        this.update.value = val;
      } // set(T)

      void failFast() {
        if (SimpleCDLL.this.changes != this.changes) {
          throw new ConcurrentModificationException();
        }
      }

      void updateChanges() {
        ++SimpleCDLL.this.changes;
        ++this.changes;
      }

    };
  } // listIterator()

} // class SimpleDLL<T>
