import { describe, it, expect } from 'vitest';
import { DoublyLinkedList } from '../src/doubly-linked-list.js';

describe('DoublyLinkedList basic behavior', () => {
  it('starts empty', () => {
    const list = new DoublyLinkedList<number>();
    expect(list.isEmpty()).toBe(true);
    expect(list.length()).toBe(0);
  });

  it('push adds items to the back', () => {
    const list = new DoublyLinkedList<number>();
    list.push(1);
    list.push(2);
    expect(list.isEmpty()).toBe(false);
    expect(list.length()).toBe(2);

    expect(list.pop()).toBe(2);
    expect(list.pop()).toBe(1);
  });

  it('unshift adds items to the front', () => {
    const list = new DoublyLinkedList<number>();
    list.unshift(1);
    list.unshift(2);
    expect(list.length()).toBe(2);

    expect(list.shift()).toBe(2);
    expect(list.shift()).toBe(1);
  });

  it('mixed push and unshift keeps correct order', () => {
    const list = new DoublyLinkedList<number>();
    list.push(1);      // list: 1
    list.unshift(2);   // list: 2,1
    list.push(3);      // list: 2,1,3

    expect(list.shift()).toBe(2);
    expect(list.pop()).toBe(3);
    expect(list.shift()).toBe(1);
  });
  
});
