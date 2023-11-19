package Structures;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @author Luke Walters
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V> pairs[];

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   */

  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> newAssocArray = new AssociativeArray<K, V>();
    while (newAssocArray.pairs.length < this.pairs.length) {
      newAssocArray.expand();
    } // while()
    for (int i = 0; i < this.pairs.length; i++) {
      if (!(this.pairs[i] == null)) {
        K keyHolder = this.pairs[i].key;
        V valHolder = this.pairs[i].value;
        KVPair<K, V> newKvPair = new KVPair<>(keyHolder, valHolder);
        newAssocArray.pairs[i] = newKvPair;
      } // if()
      else {
        newAssocArray.pairs[i] = null;
      } // else()
    } // for()
    newAssocArray.size = this.size;
    return newAssocArray;
  } // clone()

  /**
   * Convert the array to a string.
   */
  public String toString() {
    if (this.size == 0) {
      return "{}";
    } // base case
    int sizeTemp = this.size;
    String holder = "{ ";

    for (int i = 0; i < this.pairs.length; i++) {
      if (!(this.pairs[i] == null)) {
        if (sizeTemp != 1) {
          if (this.pairs[i].value == null) {
            holder = holder.concat(this.pairs[i].key.toString() + ": " + "null" + ", ");
            sizeTemp--;
          } else {
            holder = holder.concat(this.pairs[i].key.toString() + ": " + this.pairs[i].value.toString() + ", ");
            sizeTemp--;
          }
        } // if(sizeTemp)
        else {
          if (this.pairs[i].value == null) {
            holder = holder.concat(this.pairs[i].key.toString() + ": " + "null" + ", ");
          } else {
            holder = holder.concat(this.pairs[i].key.toString() + ": " + this.pairs[i].value.toString());
          }
        } // else(sizeTemp)
      } // if (null)
    }
    holder = holder.concat(" }");
    return holder;
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   * Ignores null keys
   */
  public void set(K key, V value) {
    if (key == null) {
      return;
    }

    KVPair<K, V> currentPair = new KVPair<K, V>(key, value);
    try {
      if (hasKey(key)) { // checks it the key is already in the array and overwrites the previous value

        this.pairs[find(key)].value = value;
      }

      // if()
      else {
        for (int i = 0; i < this.pairs.length; i++) {
          if (this.pairs[i] == null) {
            this.pairs[i] = currentPair;
            this.size++;
            return;
          } // if()
          if (this.size == this.pairs.length) {
            this.expand();
            this.pairs[size] = currentPair;
            size++;
            return;
          }
        } // for()
      }

    } catch (KeyNotFoundException e) {
      System.err.println("Key does not exist");
    }
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @throws KeyNotFoundException
   *                              when the key does not appear in the associative
   *                              array.
   *                              Throws an error when given a null key
   */
  public V get(K key) throws KeyNotFoundException {
    if (key == null) {
      throw new KeyNotFoundException("Null Keys are not permissible");
    }
    return this.pairs[find(key)].value;
  } // get(K)

  /**
   * Determine if key appears in the associative array.
   * Ignores null keys
   */
  public boolean hasKey(K key) {
    if (key == null) {
      return false;
    }
    for (int i = 0; i < this.pairs.length; i++) { // Iterates through the Associative Array
      KVPair<K, V> currentPair = this.pairs[i];
      if (!(currentPair == null)) {
        if (currentPair.key.equals(key)) { // checks to see if the key in array is equal to the given key
          return true;
        } // if(key)
      } // if(null)
    } // for(size)
    return false;

  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   */
  public void remove(K key) {

    try {
      this.pairs[find(key)] = null;
      this.size--;
    } catch (KeyNotFoundException e) {

    }
  } // remove(K)

  /**
   * Determine how many values are in the associative array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  public void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   */
  public int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.pairs.length; i++) { // Iterates through the Associative Array
      KVPair<K, V> currentPair = this.pairs[i];
      if (!(currentPair == null)) {
        if (currentPair.key.equals(key)) { // checks to see if the key in array is equal to the given key
          return i;
        } // if(key)
      } // if(null)
    } // for(size)
    throw new KeyNotFoundException(); // STUB
  } // find(K)

} // class AssociativeArray
