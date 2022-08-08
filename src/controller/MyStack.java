package controller;

import shapes.TShape;

public class MyStack {

    private class Node {
        private TShape shape;
        private Node nextNode;

        Node(TShape shape) {
            this.shape = shape;
            this.nextNode = null;
        }

    }


    private Node node;

    public MyStack() {
        this.node = null;
    }

    public boolean isEmpty() {
        return (this.node == null);
    }

    public void push(TShape shape) {
        Node newNode = new Node(shape);
        newNode.nextNode = node;
        this.node = newNode;
    }

    public TShape peek() {
        if (isEmpty()) throw new ArrayIndexOutOfBoundsException();
        return this.node.shape;
    }

    public TShape pop() {
        TShape shape = peek();
        node = node.nextNode;
        return shape;
    }
}
