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

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
	private Node<Item> first;
	private Node<Item> last;
	private int size = 0;

	// construct an empty deque
	public Deque(){

	}

	// is the deque empty?
	public boolean isEmpty(){
		return (size == 0);
	}

	// return the number of items on the deque
	public int size(){
		return size;
	}

	// insert the item at the front
	public void addFirst(Item item){
		if(item == null){
			throw new java.lang.NullPointerException();			
		}
		
		if(isEmpty()){
			first = new Node<Item>();
			first.setItem(item);
			last = first;
		} else {
			Node<Item> newNode = new Node<Item>();
			newNode.setItem(item);
			newNode.setNext(first);
			first.setPrev(newNode);
			first = newNode;
		}
		
		size++;
	}

	// insert the item at the end
	public void addLast(Item item) {
		if(item == null){
			throw new java.lang.NullPointerException();			
		}
		
		if(isEmpty()){
			last = new Node<Item>();
			last.setItem(item);
			first = last;
		} else {
			Node<Item> newNode = new Node<Item>();
			newNode.setItem(item);
			last.setNext(newNode);
			newNode.setPrev(last);
			last = newNode;
		}
		
		size++;
	}

	// delete and return the item at the front
	public Item removeFirst() {
		Item item = null;
		
		if(first == null){
			throw new java.util.NoSuchElementException();
		} else {
			item = first.getItem();
			first = first.getNext();
			size--;
			if(isEmpty()){
				last = null;
			} else {
				first.setPrev(null);
			}
		}
		
		return item;
	}

	// delete and return the item at the end
	public Item removeLast() {
		Item item = null;
		
		if(last == null){
			throw new java.util.NoSuchElementException();
		} else {
			item = last.getItem();
			last = last.getPrev();
			size--;
			if(isEmpty()){
				first = null;
			} else {
				last.setNext(null);
			}
		}
		
		return item;
	}

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}
	
	private class DequeIterator implements Iterator<Item>{
		private Node<Item> current = first;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			if(!hasNext()){
				throw new java.util.NoSuchElementException();
			}
			
			Item item = current.getItem();
			current = current.getNext();
			
			return item;
		}

		@Override
		public void remove() {
			throw new java.lang.UnsupportedOperationException();	
		}
	}
}

//Node of double linked list
class Node<Item> {
	private Item item;
	private Node<Item> next;
	private Node<Item> prev;
		
	public Item getItem(){
		return item;		
	}
	
	public void setItem(Item item){
		this.item = item;		
	}
	
	public Node<Item> getNext(){
		return next;		
	}
	
	public void setNext(Node<Item> node){
		this.next = node;		
	}
	
	public Node<Item> getPrev(){
		return prev;		
	}
	
	public void setPrev(Node<Item> node){
		this.prev = node;		
	}
}
