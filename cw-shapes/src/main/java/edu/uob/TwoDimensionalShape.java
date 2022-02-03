package edu.uob;

abstract class TwoDimensionalShape {
    private Colour shapeColour;

    public Colour getShapeColour(){ //getter
        return shapeColour;
    }

    public void setShapeColour(Colour newShapeColour){ //setter
        this.shapeColour = newShapeColour;
    }

    public TwoDimensionalShape() {}

    abstract double calculateArea();

    abstract int calculatePerimeterLength();

}
