import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Triangle extends JFrame {

    //holds coordinates of the drawn triangle
    public static int[] x = new int[3];
    public static int[] y = new int[3];
    public static int index = 0;


    private Container content;
    private drawTriangle drawTriangle;
    public static boolean dragging = false;
    public static boolean clicking = false;
    public static boolean rotating = false;
    private JPanel controlPanel = new JPanel();

    public Triangle(){
        content = getContentPane();
        drawTriangle = new drawTriangle();

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");

        //mouse listener for mouse clicking and releasing
        drawTriangle.addMouseListener(new MouseListener(){
            public void mouseExited(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseReleased(MouseEvent e){

                dragging = false;
            }
            public void mousePressed(MouseEvent e){
                dragging = drawTriangle.clickInSmallPoly(e.getPoint());
            }
            public void mouseClicked(MouseEvent e){
                System.out.println("Clicked");
                if (index < x.length){
                    x[index] = e.getX();
                    y[index] = e.getY();
                    drawTriangle.setSmallPolyPoint(x[index], y[index]);
                    clicking = true;
                    drawTriangle.repaint();
                }
            }
        });

        //motion listener for dragging the polygon
        drawTriangle.addMouseMotionListener(new MouseMotionListener(){
            public void mouseDragged(MouseEvent e) {
                if(dragging){
                    int x2 = e.getX();
                    int y2 = e.getY();
                    //update the points used for line drawing
                    for (int i = 0; i < drawTriangle.smallPolys.length; i++){
                        if (drawTriangle.smallPolys[i].contains(x2, y2)){
                            x[i] = x2;
                            y[i] = y2;
                        }
                    }
                    //set point for new small poly and repaint board
                    drawTriangle.setSmallPolyPoint(x2, y2);
                    drawTriangle.repaint();
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });

        startButton.addActionListener(e -> {
            if (index >= 3){
                rotating = true;
                try {
                    drawTriangle.rotateTriangle();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }

        });

        stopButton.addActionListener(e -> {
            rotating = false;
        });

        resetButton.addActionListener(e -> {
            index = 0;
            x = new int[3];
            y = new int[3];
            clicking = false;
            dragging = false;
            rotating = false;
            drawTriangle.smallPolys = new Polygon[3];
            drawTriangle.repaint();
        });

        controlPanel.add(startButton, BorderLayout.WEST);
        controlPanel.add(stopButton, BorderLayout.CENTER);
        controlPanel.add(resetButton, BorderLayout.EAST);
        content.add(controlPanel, BorderLayout.NORTH);
        content.add(drawTriangle, BorderLayout.CENTER);
        setSize(400, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        Triangle triangle = new Triangle();
        triangle.addWindowListener(
                new WindowAdapter(){
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
    }


}
