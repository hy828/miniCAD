import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;

class Coord implements Serializable{ //坐标类
    int x, y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public boolean equals(Object obj) {
        Coord temp = (Coord)obj;
        if(temp.x == x && temp.y == y){
            return true;
        }else if(this == obj){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public int hashCode() {
        return (x << 32) | y;
    }

    
}

public abstract class Shape implements Serializable {
    Color color; //颜色
    int thickness; //粗细
    Coord start, end; //起始和终点坐标
    HashSet<Coord> coordset = new HashSet<>(); //图形的所有坐标点

    protected void swap(int a, int b) { //交换
        int t;
        t = a;
        a = b;
        b = t;
    }
    abstract public void draw(Graphics g); //画图形
    abstract public void getCoordSet(); //获取图形的所有坐标点
    public void incSize(){ //图形变大
        start.x -= 5;
        start.y -= 5;
        end.x += 5;
        end.y += 5;
        getCoordSet();
    }
    public void decSize(){ //图形变小
        start.x += 5;
        start.y += 5;
        end.x -= 5;
        end.y -= 5;
        getCoordSet();
    }
    public void incThickness(){ //图形变粗
        thickness += 3;
        getCoordSet();
    }
    public void decThickness(){ //图形变细
        thickness -= 3;
        getCoordSet();
    }
    public void changeColor(Color c){ //更换颜色
        color = c;
    }
    public void move(int a, int b, int c, int d){ //移动图形
        //获取需要移动的距离
        System.out.println("a b c d:"+a+" "+b+" "+c+" "+d);
        int d1 = c - a;
        int d2 = d - b;
        start.x += d1;
        end.x += d1;
        start.y += d2;
        end.y += d2;
        System.out.println("x1 x2 y1 y2 d1 d2:"+start.x+" "+end.x+" "+start.y+" "+end.y+" "+d1+" "+d2);
        getCoordSet();
    }
}
class Line extends Shape {
    public Line(int x1, int y1, int x2, int y2, int thickness, Color color){
        if(x2 < x1){
            swap(x1, x2);
            swap(y1, y2);
        }
        start = new Coord(x1, y1);
        end = new Coord(x2, y2);
        this.thickness = thickness;
        this.color = color;
        getCoordSet();
    }
    
    public void getCoordSet() { //获取直线上每点的坐标
        coordset.clear();
        int y;
        for(int x=start.x-1; x<=end.x+1; x++){
            y = start.y + (end.y-start.y)*(x-start.x)/(end.x-start.x);
            for(int i=0; i<=thickness; i++){
                coordset.add(new Coord(x, y+i));
                coordset.add(new Coord(x, y-i));
            }
        }
    }
    public void draw(Graphics g){
        g.setColor(color);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thickness));
        g.drawLine(start.x, start.y, end.x, end.y);
    }
    
}
class Reactangle extends Shape {
    public Reactangle(int x1, int y1, int x2, int y2, int thickness, Color color){
        if(x2 < x1){
            swap(x1, x2);
            swap(y1, y2);
        }
        start = new Coord(x1, y1);
        end = new Coord(x2, y2);
        this.thickness = thickness;
        this.color = color;
        getCoordSet();
    }
    public void getCoordSet() { //获取整个四方形的所有坐标
        for(int x=start.x; x<=end.x; x++){
            for(int y=start.y; y<=end.y; y++){
                coordset.add(new Coord(x, y));
            }
        }
    }
    public void draw(Graphics g){
        g.setColor(color);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thickness));
        g.drawRect(start.x, start.y, end.x-start.x, end.y-start.y);
    }
}
class Circle extends Shape {
    public Circle(int x1, int y1, int x2, int y2, int thickness, Color color){
        if(x2 < x1){
            swap(x1, x2);
            swap(y1, y2);
        }
        start = new Coord(x1, y1);
        end = new Coord(x2, y2);
        this.thickness = thickness;
        this.color = color;
        getCoordSet();
    }
    public void getCoordSet() { //获取覆盖圆形得四方形的所有坐标
        for(int x=start.x; x<=end.x; x++){
            for(int y=start.y; y<=end.y; y++){
                coordset.add(new Coord(x, y));
            }
        }
    }
    public void draw(Graphics g){
        g.setColor(color);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(thickness));
        g.drawOval(start.x, start.y, end.x-start.x, end.y-start.y);
    }
}
class Text extends Shape {
    String s;
    FontMetrics fm;
    Font font;
    int style = Font.PLAIN;
    public Text(int x1, int y1, int thickness, Color color, String s, FontMetrics fm){
        start = new Coord(x1, y1);
        end = new Coord(0, 0);
        this.s = s;
        this.thickness = thickness;
        this.color = color;
        this.fm = fm;
        getCoordSet();
    }
    public void getCoordSet() { //获取整行文本覆盖的所有坐标
        int h = fm.getAscent();
        for(int x=start.x; x<=start.x+fm.stringWidth(s); x++){
            for(int y=start.y; y>=start.y-h; y--){
                coordset.add(new Coord(x, y));
            }
        }
    }
    public void draw(Graphics g){
        g.setColor(color);
        font = new Font(null, style, thickness);
        g.setFont(font);
        g.drawString(s, start.x, start.y);
    }
    public void incSize(){ //文本的粗细相当于大小
        thickness += 4;
        getCoordSet();
    }
    public void decSize(){
        thickness -= 4;
        getCoordSet();
    }
    public void incThickness(){ //文本的实际粗细只有PLAIN和BOLD
        style = Font.BOLD;
        getCoordSet();
    }
    public void decThickness(){
        style = Font.PLAIN;
        getCoordSet();
    }
}