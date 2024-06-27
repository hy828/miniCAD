import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class Frame extends JFrame {
    Container contentPane = getContentPane();
    JPanel toolBar = new JPanel(new GridLayout(2, 5, 5, 5)); //工具面板
    JPanel colorBar = new JPanel(new GridLayout(1, 8, 5, 5)); //颜色面板
    JPanel mainPanel = new JPanel(); //主面板
    Graphics g;
    JButton[] shapebtn = { //工具按钮
        new JButton("Select"),
        new JButton("Line"),
        new JButton("Rectangle"),
        new JButton("Circle"),
        new JButton("Text"),
        new JButton("Delete"),
        new JButton("Thickness++"),
        new JButton("Thickness--"),
        new JButton("Size++"),
        new JButton("Size--"),
    };
    JButton[] colorbtn = { //颜色按钮
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
        new JButton(" "),
    };
    ArrayList<Shape> shapes = new ArrayList<>(); //放置所有图形的数组
    DrawListener dl = new DrawListener(shapes); //监听函数，包括MouseListener, ActionListener

    public Frame() {
        setTitle("MiniCAD");
        setSize(580, 580);
        setLocationRelativeTo(null); //窗口居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //菜单
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem n = new JMenuItem("New"); //新建
        file.add(n);
        JMenuItem open = new JMenuItem("Open"); //打开
        file.add(open);
        JMenuItem save = new JMenuItem("Save"); //保存
        file.add(save);
        n.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clean(); //清屏
                shapes.clear(); //删除所有图形
            }
        });
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CAD File (*.cad)", "cad");
                fileChooser.setFileFilter(filter);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        //将读入的流反序列化为shape后放入数组
                        ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
                        while(true){
                            try {
                                Shape s = (Shape)input.readObject();
                                shapes.add(s);
                            }
                            catch (EOFException e3) {
                                break;
                            }
                        }
                        input.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //保存为.cad文件
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CAD File (*.cad)", "cad");
                fileChooser.setFileFilter(filter);
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File raw = fileChooser.getSelectedFile();
                    File file = new File(raw.getAbsolutePath() + ".cad");
                    try {
                        //将每个shape序列化后保存到文件
                        ObjectOutputStream output = new ObjectOutputStream((new FileOutputStream(file)));
                        for (Shape s : shapes) {
                            output.writeObject(s);
                        }
                        output.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        
        for(JButton btn: shapebtn){
            btn.addActionListener(dl);
            toolBar.add(btn);
        }

        //设定按钮颜色
        colorbtn[0].setBackground(Color.BLACK);
        colorbtn[1].setBackground(Color.DARK_GRAY);
        colorbtn[2].setBackground(Color.GRAY);
        colorbtn[3].setBackground(Color.LIGHT_GRAY);
        colorbtn[4].setBackground(Color.WHITE);
        colorbtn[5].setBackground(Color.RED);
        colorbtn[6].setBackground(Color.ORANGE);
        colorbtn[7].setBackground(Color.YELLOW);
        colorbtn[8].setBackground(Color.GREEN);
        colorbtn[9].setBackground(Color.CYAN);
        colorbtn[10].setBackground(Color.BLUE);
        colorbtn[11].setBackground(Color.MAGENTA);
        colorbtn[12].setBackground(Color.PINK);
        for(JButton btn: colorbtn){
            btn.addActionListener(dl);
            colorBar.add(btn);
        }

        //添加所有面板，并打开监听
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(colorBar, BorderLayout.NORTH);
        mainPanel.addMouseListener(dl);
        mainPanel.addMouseMotionListener(dl);
        contentPane.add(mainPanel);
        setVisible(true);
        g = mainPanel.getGraphics();
        dl.setG(g);
    }
    public void clean() { //清楚画板
        g.setColor(getBackground());
        g.fillRect(0, toolBar.getHeight()+colorBar.getHeight()+10, mainPanel.getWidth(), mainPanel.getHeight());
    }
    public void render() { //重新加载所有图形到画板
        clean();
        for(Shape s: shapes){
            s.draw(g);
        }
    }

}
