package edu.uob;

import java.util.concurrent.TransferQueue;

public class Shapes {
    public static void main(String[] args) {
//        Triangle testTriangle = new Triangle(5, 7, 9);
//        Rectangle testRectangle = new Rectangle(3, 4);
//        Circle testCircle = new Circle(4);
//        TwoDimensionalShape triangle = new Triangle(5, 5, 5);
//        TwoDimensionalShape rectangle = new Rectangle(3, 4);
//        TwoDimensionalShape circle = new Circle(4);
//
//        triangle.setShapeColour(Colour.PURPLE);
//        rectangle.setShapeColour(Colour.WHITE);
//        circle.setShapeColour(Colour.GREEN);

        //Task 4: interface
//        Triangle triangle = new Triangle(3, 3, 3);
//        if(triangle instanceof MultiVariantShape){
//            System.out.println(triangle.getVariant());
//        }
//        else{
//            System.out.println("This shape has only one variant");
//        }

        //Task 8: array
        TwoDimensionalShape[] array = new TwoDimensionalShape[100];
        int count = 0;
        for(int i=0; i<100; i++){
//            System.out.println(Math.random());
            if(Math.random()<0.33){
                array[i] = new Rectangle(3, 4);
            }
            else if (Math.random()<0.66){
                array[i] = new Triangle(3, 4, 5);
            }
            else{
                array[i] = new Circle(3);
            }
//            if(array[i] instanceof Triangle){
//                count++;
//            }
        }
        System.out.println(Triangle.count);
    }

}
