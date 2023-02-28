package util;
import java.util.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.ArrayList;

public class Edge {
    private Vertex start, end;
    private Object weight;
    private int strokeWidth, arrowTipWidth = 15;
    private Color strokeColor, highlightColor;
    private Path2D.Float collisionArea;


    public Edge(Vertex start, Vertex end, Object weight, int strokeWidth, Color strokeColor, Color highlightColor) {
        this.start = start;
        this.end = end;
        this.weight = weight;
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        this.highlightColor = highlightColor;
        collisionArea = refreshCollisionArea();
    }

    public Path2D.Float refreshCollisionArea(){ 

        Path2D.Float path = new Path2D.Float();

        ArrayList<Point> edgePoint = getEdgePoints(this);

        int startX = (int)edgePoint.get(0).getX(),
            startY = (int)edgePoint.get(0).getY(), 
            endX = (int)edgePoint.get(1).getX(), 
            endY = (int)edgePoint.get(1).getY(),
            width = getStrokeWidth() * 2;

        path.moveTo(startX-width, startY-width); //Starting point
        path.lineTo(startX+width, startY+width);
        path.lineTo(endX+width, endY+width); 
        path.lineTo(endX-width, endY-width); //End point
        path.closePath();

        return path;
    }

    public void paint(Graphics graphics, Boolean isOriented, Boolean isWeighted, Object collision){
        graphics.setColor(strokeColor);
        if (collision == this) {
            ((Graphics2D)graphics).setStroke(new BasicStroke(strokeWidth*2)); //Change stroke
        }else{
            ((Graphics2D)graphics).setStroke(new BasicStroke(strokeWidth)); //Change stroke
        }

        ArrayList<Point> edgePoint = getEdgePoints(this), 
            arrowTip = getArrow(this, arrowTipWidth);

        graphics.drawLine((int)(edgePoint.get(0).getX()), (int)(edgePoint.get(0).getY()), (int)(edgePoint.get(1).getX()), (int)(edgePoint.get(1).getY()));
        if(isOriented) //If the graph is oriented, we also have to draw an arrow displaying the arrival vertex
        {

            graphics.drawLine((int)(arrowTip.get(0).getX()),(int)(arrowTip.get(0).getY()), (int)(arrowTip.get(1).getX()), (int)(arrowTip.get(1).getY()));
            graphics.drawLine((int)(arrowTip.get(0).getX()),(int)(arrowTip.get(0).getY()), (int)(arrowTip.get(2).getX()), (int)(arrowTip.get(2).getY()));
        }
        //If the graph is weighted, we then draw every weight (after we have drawn every edge)
        if(isWeighted && weight != null)
        {
            graphics.drawString(weight.toString(), (int)((start.getCoordX()+end.getCoordX())/2), (int)((start.getCoordY()+end.getCoordY())/2));
        }
    }

    //Allow to get the precise starting and ending coordinates of the edge
    public ArrayList<Point> getEdgePoints(Edge edge)
    {
        //Step 1 : retreiving the center of the starting point and ending point
        int startingVertexCenterX = start.getCoordX() + start.getDiameter()/2,
            startingVertexCenterY = start.getCoordY() + start.getDiameter()/2,
            endingVertexCenterX = end.getCoordX() + end.getDiameter()/2,
            endingVertexCenterY = end.getCoordY() + end.getDiameter()/2;
        
        //Step 2 : We must determine the edge direction
        int vectorDirX = endingVertexCenterX - startingVertexCenterX,
            vectorDirY = endingVertexCenterY - startingVertexCenterY;

        //Step 3 : We now normalize the vector
        Double normalizedVectorX = (vectorDirX/Math.sqrt((vectorDirX*vectorDirX+vectorDirY*vectorDirY)));
        Double normalizedVectorY = (vectorDirY/Math.sqrt((vectorDirX*vectorDirX+vectorDirY*vectorDirY)));

        //Step 4 : getting the starting and ending coordinates thanks to the normalized vector
        ArrayList<Point> result = new ArrayList<Point>(); 

        result.add(new Point((int)(startingVertexCenterX + ((start.getDiameter()/2)*normalizedVectorX)),//Starting point first (index 0)
                            (int)(startingVertexCenterY + ((start.getDiameter()/2)*normalizedVectorY))));
        result.add(new Point((int)(endingVertexCenterX - ((end.getDiameter()/2)*normalizedVectorX)),//Ending point (index 1)
                             (int)(endingVertexCenterY - ((end.getDiameter()/2)*normalizedVectorY))));

        //We can now return the result
        return result;
    }

    //Allow to get points to draw the arrow
    public ArrayList<Point> getArrow(Edge edge, int size)
    {
        //Tip of the arrow
        ArrayList<Point> edgePoint = getEdgePoints(edge);
        int arrowTipX = (int)edgePoint.get(1).getX(),
            arrowTipY = (int)edgePoint.get(1).getY();
        

        //Now we need 2 more points to draw the arrow
        //To do so, we first have to retreive the angle of the edge
        Double edgeAngle = Math.atan2(end.getCoordY() - start.getCoordY(),end.getCoordX() - start.getCoordX());

        //Now that we have the angle, we can get the two sub line coord (We picked + and - Pi/1.2 because it seems to work best with this value)
        Point sub45plus = new Point((int)(arrowTipX+(size*Math.cos(edgeAngle + Math.PI/1.2))),(int)(arrowTipY+(size*Math.sin(edgeAngle + Math.PI/1.2))));
        Point sub45minus = new Point((int)(arrowTipX+(size*Math.cos(edgeAngle - Math.PI/1.2))),(int)(arrowTipY+(size*Math.sin(edgeAngle - Math.PI/1.2))));

        //Now we have the tip point and both subline point
        ArrayList<Point> result = new ArrayList<Point>();
        result.add(new Point(arrowTipX,arrowTipY));
        result.add(sub45plus);
        result.add(sub45minus);
        return result;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Path2D.Float getCollisionArea() {
        return collisionArea;
    }

    public void setCollisionArea(Path2D.Float collisionArea) {
        this.collisionArea = collisionArea;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }
    
}
