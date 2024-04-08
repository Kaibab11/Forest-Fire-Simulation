package Cell;
import java.awt.Color;
import java.util.Random;

public class Ash extends Cell{
    private int ashAge;
    private Random random = new Random();

    public Ash() {
        super(Cell.state.ASH);
        this.ashAge = 6;
    }
    
    public int getAshAge() {
        return ashAge;
    }

    public void setAshAge(int ashAge) {
        this.ashAge = ashAge;
    }

    public void disperseAsh() {
        //Randomly decrease the age of the fire (can stay the same age)
        ashAge -= random.nextInt(2); // Decrease by 0 or 1

    }

    @Override
    public Color getColor() {
    if (this.ashAge > 3)
        return Color.GRAY;
    else
        return Color.LIGHT_GRAY;
    }
}