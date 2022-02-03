package edu.uob;

public class Triangle extends TwoDimensionalShape implements MultiVariantShape {

    long sideA; //instance
    long sideB;
    long sideC;
    static int count=0; //class var

    public Triangle(long A, long B, long C) { //constructor
        sideA = A; //object
        sideB = B;
        sideC = C;
        if(sideA<=0 || sideB<=0 || sideC<=0){
            this.triangleVariant = TriangleVariant.ILLEGAL;
        }
        else if(sideA==sideB && sideB==sideC && sideC==sideA){
            this.triangleVariant = TriangleVariant.EQUILATERAL;
        }
        else if(sideA==sideB+sideC || sideB==sideA+sideC || sideC==sideA+sideB){
            this.triangleVariant = TriangleVariant.FLAT;
        }
        else if(sideA==sideB || sideB==sideC || sideC==sideA) {
            this.triangleVariant = TriangleVariant.ISOSCELES;
        }
        else if(sideA*sideA+sideB*sideB==sideC*sideC || sideA*sideA+sideC*sideC==sideB*sideB || sideC*sideC+sideB*sideB==sideA*sideA){
            this.triangleVariant = TriangleVariant.RIGHT;
        }
        else if(sideA>sideB+sideC || sideB>sideA+sideC || sideC>sideA+sideB){
            this.triangleVariant = TriangleVariant.IMPOSSIBLE;
        }
        else{
            this.triangleVariant = TriangleVariant.SCALENE;
        }
        Triangle.count++; //class
    }

    public double calculateArea() {
        double p = calculatePerimeterLength()/2.0;
        double area = Math.sqrt(p*(p-sideA)*(p-sideB)*(p-sideC));
        return area;
    }

    public int calculatePerimeterLength() {
        return (int)(sideA + sideB + sideC);
    }

    public String toString() {
        return "This is a Triangle with sides of length" +
                " " + sideA +
                " " + sideB +
                " " + sideC ;
    }

    private TriangleVariant triangleVariant;

    public TriangleVariant getVariant(){ //getter
        return triangleVariant;
    }
}

