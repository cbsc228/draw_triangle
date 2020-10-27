import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.stream.IntStream;

public class drawTriangle extends JPanel {

    public static Polygon[] smallPolys = new Polygon[3];
    private int smallPolyPointX;
    private int smallPolyPointY;

    //ints for doing midpoint algorithm
    private int x1;
    private int x2;
    private int y1;
    private int y2;

    //constructor
    public drawTriangle(){
        setBackground(Color.BLACK);
    }

    public void setSmallPolyPoint(int x, int y){
        smallPolyPointX = x;
        smallPolyPointY= y;
    }

    public void drawSmallPoly(Graphics2D g){
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(1));
        //make the next small poly if user is clicking
        if (Triangle.clicking){
            int x = smallPolyPointX;
            int y = smallPolyPointY;
            int[] polyX = {x-5, x+5, x+5, x-5};
            int[] polyY = {y-5, y-5, y+5, y+5};
            smallPolys[Triangle.index] = new Polygon(polyX, polyY, 4);
        }
        //redraw the polygon the user is dragging
        if (Triangle.dragging){
            for (int i = 0; i < smallPolys.length; i++){
                if (smallPolys[i].contains(smallPolyPointX, smallPolyPointY)){
                    int x = smallPolyPointX;
                    int y = smallPolyPointY;
                    int[] polyX = {x-5, x+5, x+5, x-5};
                    int[] polyY = {y-5, y-5, y+5, y+5};
                    smallPolys[i] = new Polygon(polyX, polyY, 4);
                }
            }
        }


        //draw all the available small polys
        for (int i = 0; i < smallPolys.length; i++){
            if (smallPolys[i] != null){
                g.drawPolygon(smallPolys[i]);
            }
        }
        //update
        Triangle.clicking = false;
        Triangle.index++;
    }

    public boolean clickInSmallPoly(Point click){

        //only allow dragging when all points are placed
        if (smallPolys[smallPolys.length-1] != null){
            //find if one of the polys is being dragged
            for (Polygon smallPoly : smallPolys) {
                if (smallPoly.contains(click)) {
                    return true;
                }
            }
        }
        return false;
    }

    //plots an individual point
    public void plotPoint(int x, int y, Graphics2D g){
        g.fillRect(x, y, 1, 1);
    }

    //handles when the slope of the line is less than 1
    public void slopeUnderOne(Graphics2D g){
        //set up variables necessary for midpoint algorithm
        int x = x1;
        int y = y1;
        int discriminate = 0;
        int xTerminate = 0;
        int changeX;
        int changeY;
        float slope;

        //plot initial given point
        plotPoint(x, y, g);

        //begin calculations and plotting of calculated points
        changeX = x1 - x2;
        changeY = y1 - y2;
        slope = (float) changeY / (float) changeX;

        changeX = Math.abs(x1 - x2);
        changeY = Math.abs(y1 - y2);
        discriminate = 2 * changeY - changeX;
        //determine direction that the line needs to be drawn in
        if (x1 > x2){
            x = x2;
            y = y2;
            xTerminate = x1;
        }
        else{
            x = x1;
            y = y1;
            xTerminate = x2;
        }
        //plot subsequent points
        plotPoint(x, y, g);
        while (x < xTerminate){
            x++;
            if (discriminate < 0){
                discriminate += + 2 * changeY;
            }
            else {
                if (slope < 0){
                    y--;
                }
                else {
                    y++;
                }
                discriminate += 2 * (changeY - changeX);
            }
            plotPoint(x, y, g);
        }
    }

    //handles when the slope of the line is over 1
    public void slopeOverOne(Graphics2D g){
        //set up variables necessary for midpoint algorithm
        int x = x1;
        int y = y1;
        int discriminate = 0;
        int yTerminate = 0;
        int changeX;
        int changeY;
        float slope;

        //plot initial given point
        plotPoint(x, y, g);

        //begin calculations and plotting of calculated points
        changeX = x1 - x2;
        changeY = y1 - y2;
        slope = (float) changeY / (float) changeX;

        changeX = Math.abs(x1 - x2);
        changeY = Math.abs(y1 - y2);
        discriminate = 2 * changeX - changeY;
        //determine direction that the line needs to be drawn in
        if (y1 > y2){
            x = x2;
            y = y2;
            yTerminate = y1;
        }
        else {
            x = x1;
            y = y1;
            yTerminate = y2;
        }
        //plot subsequent points
        plotPoint(x, y, g);
        while (y < yTerminate){
            y++;
            if (discriminate < 0) {
                discriminate += 2 * changeX;
            }
            else {
                if (slope < 0){
                    x--;
                }
                else {
                    x++;
                }
                discriminate += 2 * (changeX - changeY);
            }
            plotPoint(x, y, g);
        }
    }

    public void drawLine(Graphics2D g){
        float slope;

        int changeX = Math.abs(x2 - x1);
        int changeY = Math.abs(y2 - y1);
        slope = (float) changeY / (float) changeX;

        if (slope <= 1){
            slopeUnderOne(g);
        }
        else{
            slopeOverOne(g);
        }
    }

    //draw the lines of the triangle using midpoint algorithm
    public void drawPoly(Graphics2D g){
        x1 = Triangle.x[0];
        y1 = Triangle.y[0];
        x2 = Triangle.x[1];
        y2 = Triangle.y[1];
        drawLine(g);

        x1 = Triangle.x[0];
        y1 = Triangle.y[0];
        x2 = Triangle.x[2];
        y2 = Triangle.y[2];
        drawLine(g);

        x1 = Triangle.x[1];
        y1 = Triangle.y[1];
        x2 = Triangle.x[2];
        y2 = Triangle.y[2];
        drawLine(g);
    }

    public class task implements ActionListener{
        public void actionPerformed(ActionEvent e){
            int degree = 1;
            float centroidX = (Triangle.x[0] + Triangle.x[1] + Triangle.x[2]) / 3;
            float centroidY = (Triangle.y[0] + Triangle.y[1] + Triangle.y[2]) / 3;
            for (int i = 0; i < Triangle.x.length; i++){
                Triangle.x[i] = (int) (centroidX + (Triangle.x[i] - centroidX) * Math.cos(Math.toRadians(degree)) - (Triangle.y[i] - centroidY) * Math.sin(Math.toRadians(degree)));
                Triangle.y[i] = (int) (centroidY + (Triangle.x[i] - centroidX) * Math.sin(Math.toRadians(degree)) + (Triangle.y[i] - centroidY) * Math.cos(Math.toRadians(degree)));
                int x = Triangle.x[i];
                int y = Triangle.y[i];
                int[] polyX = {x-5, x+5, x+5, x-5};
                int[] polyY = {y-5, y-5, y+5, y+5};
                smallPolys[i] = new Polygon(polyX, polyY, 4);
            }
            repaint();
        }
    }

    //-------------------------------------------------------------------------------------------------------------------
    //rotation does not work correctly
    //for some reason the triangle changes shape as it rotates and does not rotate around the origin as expected
    //possibly something is faulty with the centroid calculation or the casting of variables but I was not able to find
    //the exact issue
    //Also, the stop button and reset button are not stopping the animation as expected but the reset button is correctly
    //clearing the previously inputted data points
    //the project is otherwise working and functional
    //-------------------------------------------------------------------------------------------------------------------
    public void rotateTriangle() throws InterruptedException {
        int delay = 10;
        task task = new task();
        Timer animationTimer = new Timer(delay, task);

        int degreeCounter = 0;
        while (Triangle.rotating && degreeCounter <= 360){
            degreeCounter++;
            animationTimer.start();
            System.out.println(Triangle.rotating + " " + degreeCounter);
        }

        Triangle.rotating = false;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D picture = (Graphics2D)g;
        if ( (Triangle.index < 3 && Triangle.clicking) || Triangle.dragging || Triangle.rotating){
            drawSmallPoly(picture);
        }

        if (Triangle.index >= 3 || Triangle.dragging || Triangle.rotating) {
            drawPoly(picture);
        }
    }
}
