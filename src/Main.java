public class Main {
    public static void main(String[] args) {
        Frame f = new Frame(); //创建窗口
        while(true){ //循环刷新页面
            f.render();
            try {
                Thread.sleep(550);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
