/**
 * Created by sunboyu on 2017/9/1.
 */
public class Test {

    public static void main(String[] args) {

        try {
            try {
                throw new Sneeze();
            }
            catch ( Annoyance a ) {
                System.out.println("Caught Annoyance");
                throw a;
            }
        }
        catch ( Sneeze s ) {
            System.out.println("Caught Sneeze");
            return ;
        }
        finally {
            System.out.println("Hello World!");
        }
    }
}

class Annoyance extends Exception {}
class Sneeze extends Annoyance {}