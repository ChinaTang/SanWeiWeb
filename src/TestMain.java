import java.util.Random;

public class TestMain {

    public static void main(String[] args){
        Random rand = new Random();
        for(int i = 0; i < 10000; i++){
            System.out.println("" + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10));
        }
    }

}
