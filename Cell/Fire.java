package Cell;
import java.awt.Color;
import java.util.Random;

public class Fire extends Cell{
    private int fireAge;
    private Random random = new Random();

    public Fire() {
        super(Cell.state.BURNING);
        this.fireAge = 3;
    }
    
    public int getFireAge() {
        return fireAge;
    }

    public void setFireAge(int fireAge) {
        this.fireAge = fireAge;
    }

    public void progressFire() {
        //Randomly decrease the age of the fire (can stay the same age)
        fireAge -= random.nextInt(2); // Decrease by 0 or 1

    }

    @Override
    public Color getColor() {

        if (this.fireAge == 3) {
            return Color.RED;
        } else if (this.fireAge == 2) {
            return Color.ORANGE;
        } else { // if fireAge == 1
            return Color.YELLOW;
        }
    } 
}
