/*
 * Princeton Algorithms 1 on Coursera (2013 Aug offering)
 * 
 * Programming assignment 2 - Randomized Queues and DeQues
 * 
 * Fan Xiao
 * 
 * 2013 Oct
 * 
 */


import java.util.Arrays;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] que;
	private int first = 0;
	//position of the next available item but not the last item 
	private int last = 0;  
	private int size = 0;
	
	// construct an empty randomized queue
	public RandomizedQueue(){
		que = (Item []) new Object[2];
	}
	
	private void resize(int newSize){
		Item[] temp = (Item[]) new Object[newSize];
		
		for(int i = 0; i < size; i++){
			temp[i] = que[(first + i) % que.length];
		}
		que = temp;
		first = 0;
		last = size;
	}

	// is the queue empty?
	public boolean isEmpty(){
		return (size == 0);
	}
	
	// return the number of items on the queue
	public int size(){
		return size;
	}
	
	// add the item
	public void enqueue(Item item){
		if(item == null){
			throw new java.lang.NullPointerException();			
		}
		// double size of array if necessary and recopy to front of array
        if (size == que.length){
        	resize(2 * que.length);   
        }
		que[last] = item;
		size++;
		last = (last + 1) % que.length;
	}
	
	// delete and return a random item
	public Item dequeue(){
		if(isEmpty()){
			throw new java.util.NoSuchElementException();
		}
		
		int randomInt = StdRandom.uniform(size);
		int randomIndex = (first + randomInt) % que.length;
		Item randomItem = que[randomIndex];

		//Swap the item at first to the random index
		que[randomIndex] = que[first];
		que[first] = null;
		size--;
		first = (first + 1) % que.length;
		
		// shrink size of array if necessary
        if (size > 0 && size == que.length/4){
        	resize(que.length/2); 
        }
		
		return randomItem;
	}
	
	// return (but do not delete) a random item
	public Item sample(){
		if(isEmpty()){
			throw new java.util.NoSuchElementException();
		}
		
		int randomIndex = StdRandom.uniform(size);

		return que[(first + randomIndex) % que.length];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator(){
		return new RandomizedQueueIterator();
	}
	
	private class RandomizedQueueIterator implements Iterator<Item>{
		private Item[] iteratorQue;
		int cursor = 0;
		
		private RandomizedQueueIterator(){
			myArrayCopy();
			StdRandom.shuffle(iteratorQue);
		}
		
		private void myArrayCopy(){
			Item[] temp = (Item[]) new Object[size];
			
			for(int i = 0; i < size; i++){
				temp[i] = que[(first + i) % que.length];
			}
			iteratorQue = temp;
		}
		
		@Override
		public boolean hasNext() {
			return cursor < size;
		}

		@Override
		public Item next() {
			if(!hasNext()){
				throw new java.util.NoSuchElementException();
			}
			
			Item nextItem = iteratorQue[cursor++];
			
			return nextItem;
		}

		@Override
		public void remove() {
			throw new java.lang.UnsupportedOperationException();	
		}
	}
}