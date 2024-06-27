import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class DrawListener implements ActionListener, MouseListener, MouseMotionListener {
    Graphics g;
    int x1, y1;
    ArrayList<Shape> shapes;
    String state;
    Shape selectedShape;

    public DrawListener(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        state = btn.getText(); //设置状态
        if(state.compareTo("Thickness++") == 0){
            selectedShape.incThickness();
        }else if(state.compareTo("Thickness--") == 0){
            selectedShape.decThickness();
        }else if(state.compareTo("Size++") == 0){
            selectedShape.incSize();
        }else if(state.compareTo("Size--") == 0){
            selectedShape.decSize();
        }else if(state.compareTo(" ") == 0){
            selectedShape.changeColor(btn.getBackground());
        }else if(state.compareTo("Delete") == 0){
            shapes.remove(selectedShape);
        }
    }
    private Shape getSelectedShape(int x, int y) { //获取点击的图形
        Coord c = new Coord(x, y);
        for(Shape s: shapes){
            if(s.coordset.contains(c)){
                return s;
            }
        }
        return null;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(state.compareTo("Text") == 0){ //选中文本要放置的位置
            String msg = JOptionPane.showInputDialog("Enter text:"); //提供窗口让用户输入文本
            Text t = new Text(e.getX(), e.getY(), 20, Color.BLACK, msg, g.getFontMetrics()); //创建新图形
            shapes.add(t); //图形放入数组
        }else if(state.compareTo("Select") == 0){
            selectedShape = getSelectedShape(e.getX(), e.getY()); //获取点击的图形
        }
        
        
    }
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){ //滑鼠左击，获得当前坐标
            x1 = e.getX();
            y1 = e.getY();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(state.compareTo("Select") == 0){
            selectedShape.move(x1, y1, e.getX(), e.getY()); //移动图形
        }else if(state.compareTo("Line") == 0){ //画直线
            Line l = new Line(x1, y1, e.getX(), e.getY(), 3, Color.BLACK);
            shapes.add(l);
        }else if(state.compareTo("Rectangle") == 0){ //画矩形
            Reactangle r = new Reactangle(x1, y1, e.getX(), e.getY(), 3, Color.BLACK);
            shapes.add(r);
        }else if(state.compareTo("Circle") == 0){ //画圆形
            Circle c = new Circle(x1, y1, e.getX(), e.getY(), 3, Color.BLACK);
            shapes.add(c);
        }
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    public void setG(Graphics g2) {
        g = g2;
    }
}
