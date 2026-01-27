export class Node<T> {
  public next: Node<T> | null = null;
  public prev: Node<T> | null = null;

  constructor(public data: T) {}
}

export class DoublyLinkedList<T> {
  /** Head node */
  private head: Node<T> | null = null;
  /** Tail node */
  private tail: Node<T> | null = null;
  /** List size */
  private size: number = 0;

  public length(): number {
    return this.size;
  }

  public isEmpty(): boolean {
    return this.size === 0;
  }

  /** insert value at the front */
  unshift(data: T): void {
    const newNode = new Node(data);

    if (this.isEmpty()) {
      this.head = newNode;
      this.tail = newNode;
    } else {
      // Link the current head back to the new node
      this.head!.prev = newNode;
      // Link the new node to the current head
      newNode.next = this.head;
      // Update the list's head to the new node
      this.head = newNode;
    }

    this.size++;
  }

  /** insert value at the back */
  push(data: T): void {
    const newNode = new Node(data);

    if (this.isEmpty()) {
      this.head = newNode;
      this.tail = newNode;
    } else {
      // Link the current tail to the new node
      this.tail!.next = newNode;
      // Link the new node back to the current tail
      newNode.prev = this.tail;
      // Update the list's tail to the new node
      this.tail = newNode;
    }

    this.size++;
  }

  /** remove value at front and return */
  shift(): T | null {
    if (!this.head) return null;

    let removedItem = this.head;

    if (this.size === 1) {
      this.head = null;
      this.tail = null;
    } else {
      this.head = removedItem.next;
      this.head!.prev = null;
      removedItem.next = null;
    }

    this.size--;

    return removedItem.data;
  }

  /** remove value at back and return */
  pop(): T | null {
    if (!this.tail) return null;

    let removedItem = this.tail;

    if (this.size === 1) {
      this.head = null;
      this.tail = null;
    } else {
      this.tail = this.tail.prev;
      this.tail!.next = null;
      removedItem.prev = null;
    }

    this.size--;

    return removedItem.data;
  }
}