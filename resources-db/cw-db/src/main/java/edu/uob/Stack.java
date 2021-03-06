/*** referenced from Techie Delight (https://www.techiedelight.com/stack-implementation-in-java/) ***/

package edu.uob;

public class Stack
{
    private DBTable arr[];
    private String arr2[];

    private int top;
    private int top2;
    private int capacity;

    // Constructor to initialize the stack
    Stack(int size)
    {
        arr = new DBTable[size];
        arr2 = new String[size];
        capacity = size;
        top = -1;
        top2 = -1;
    }

    // Utility function to add an element `x` to the stack
    public void push(DBTable x)
    {
        if (isFull())
        {
            System.exit(-1);
        }
        arr[++top] = x;
    }

    public void pushOp(String x)
    {
        if (isFull())
        {
            System.exit(-1);
        }

        arr2[++top2] = x;
    }

    // Utility function to pop a top element from the stack
    public DBTable pop()
    {
        // check for stack underflow
        if (isEmpty())
        {
            System.exit(-1);
        }
        // decrease stack size by 1 and (optionally) return the popped element
        return arr[top--];
    }

    public String popOp()
    {
        return arr2[top2--];
    }

//    // Utility function to return the top element of the stack
    public DBTable peek()
    {
        return arr[top];
    }

    public String peekOp()
    {
        return arr2[top2];
    }

    // Utility function to return the size of the stack
    public int size() {
        return top + 1;
    }

    // Utility function to check if the stack is empty or not
    public boolean isEmpty() {
        return top == -1;               // or return size() == 0;
    }

    // Utility function to check if the stack is full or not
    public boolean isFull() {
        return top == capacity - 1;     // or return size() == capacity;
    }
}