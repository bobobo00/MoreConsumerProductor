package Pattern;

/**
 * @ClassName CreateThreadPractice
 * @Description TODO
 * @Auther danni
 * @Date 2019/11/16 8:59]
 * @Version 1.0
 **/

public class CreateThreadPractice {
    public static void main(String[] args) {
        Container container=new Container(1);
        new Productor(container).start();
        new Consumer(container).start();
        new Consumer(container).start();
    }

}
class Productor extends Thread{
    private Container container;

    public Productor(Container container){
        this.container=container;
}

    public void run(){
        try {
            for (int i = 0; i <1000 ; i++) {
                this.container.push(new Goods(i,"a"+i));
                System.out.println("放入产品"+i);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Consumer extends  Thread{
    private Container container;

    public Consumer(Container container){  this.container=container ;}

    public void run(){
        try {
            for (int i = 0; i <100 ; i++) {
                Goods goods = this.container.pop();
                System.out.println(Thread.currentThread().getName()+"取到产品" + goods.getId());
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
class Container{
    private int count=0;
    private  Goods[] goods;
    private  int front=0;
    private int rear=0;

    public Container(int len){
        goods=new Goods[len];
    }

    public void push(Goods good) throws InterruptedException {
        synchronized (this){
            while(count==goods.length){
                this.wait();
            }
            goods[rear]=good;
            this.count++;
            rear=(rear+1)%goods.length;
            notifyAll();
        }
    }
    public Goods pop() throws InterruptedException {
        Goods g;
        synchronized (this) {
            while(count==0) {
                this.wait();
            }
            g = goods[front];
            this.count--;
            front = (front + 1) % goods.length;
            notifyAll();
        }
        return g;
    }
}
class Goods{   //BlockingQueue;

    private int id;
    private String name;

    public Goods(int id,String name) {
        this.id = id;
        this.name=name;
    }
    public int getId() {
        return id;
    }
}
