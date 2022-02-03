package edu.uob;

class Circle extends TwoDimensionalShape {

//    private Colour cirColour;
//
//    public Colour getCirColour(){ //getter
//        return cirColour;
//    }
//
//    public void setCirColour(Colour newCirColour){ //setter
//        this.cirColour = newCirColour;
//    }

    int radius;

    public Circle(int r) {
        radius = r;
    }

    double calculateArea() {
        return (int) Math.round(Math.PI * radius * radius);
    }

    int calculatePerimeterLength() {
        return (int) Math.round(Math.PI * radius * 2.0);
    }

    public String toString() {
        return "Circle with radius " + radius;
    }

}
