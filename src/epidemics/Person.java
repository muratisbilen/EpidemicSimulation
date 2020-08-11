package epidemics;
import java.time.*;
import java.awt.*;
import java.util.ArrayList;

public class Person {
    private int age;
    private int family;
    private boolean infected;
    private LocalDate infdate;
    private LocalTime inftime; 
    private boolean severe;
    private boolean athome;
    private boolean alive;
    private boolean healed;
    private float x,y,size;
    private Color color;
    private double speed, angle;
    private ArrayList<Integer> interact;
    private int survtime, healtime;
    private boolean survive;
    private boolean hosp, presurvive;
    
    public Person(int family, float x, float y){
        age = 30;
        this.family = family;
        infected = false;
        infdate = null;
        inftime = null;
        severe = false;
        athome = false;
        alive = true;
        this.x = x;
        this.y = y;
        color = Color.WHITE;
        speed = 5;
        size = 3;
        interact = null;
        healed=false;
        survtime = 6;
        survive = true;
        healtime = 3;
        hosp = false;
        presurvive = survive;
    }
    
    public Person(int age, int family, boolean infected, LocalDate infdate, LocalTime inftime, boolean severe, boolean athome, boolean alive, float x, float y, double speed, double angle, float size, ArrayList<Integer> interact, int survtime, boolean survive, int healtime, boolean hosp){
        this.age = age;
        this.family = family;
        this.infected = infected;
        this.infdate = infdate;
        this.inftime = inftime;
        this.severe = severe;
        this.athome = athome;
        this.alive = alive;
        this.x = x;
        this.y = y;
        if(infected){
            if(severe){
                this.color = Color.RED;
            }else{
                this.color = Color.ORANGE;
            }
        }else if(!alive){
            this.color = Color.GRAY;
        }else{
            this.color = Color.WHITE;
        }
        this.speed = speed;
        this.angle = angle;
        this.size = size;
        this.interact = interact;
        this.healed = false;
        this.survtime = survtime;
        this.survive = survive;
        this.healtime = healtime;
        this.hosp = hosp;
        this.presurvive = survive;
    }

    public boolean getPresurvive() {
        return presurvive;
    }

    public void setPresurvive(boolean presurvive) {
        this.presurvive = presurvive;
    }

    public boolean isHosp() {
        return hosp;
    }

    public void setHosp(boolean hosp) {
        this.hosp = hosp;
    }

    public int getHealtime() {
        return healtime;
    }

    public void setHealtime(int healtime) {
        this.healtime = healtime;
    }

    public int getSurvtime() {
        return survtime;
    }

    public void setSurvtime(int survtime) {
        this.survtime = survtime;
    }

    public boolean isSurvive() {
        return survive;
    }

    public void setSurvive(boolean survive) {
        this.survive = survive;
    }

    public boolean isHealed() {
        return healed;
    }

    public void setHealed(boolean healed) {
        this.healed = healed;
        this.speed = 5;
    }
    
    public ArrayList<Integer> getInteract() {
        return interact;
    }

    public void setInteract(ArrayList<Integer> interact) {
        this.interact = interact;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
    
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        double angle2 = angle;
        if(angle>=2*Math.PI){
            while(angle2>=2*Math.PI){
                angle2 -= 2*Math.PI;
            }
        }else if(angle<0){
            while(angle2<0){
                angle2 += 2*Math.PI;
            }
        }
        
        this.angle = angle2;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFamily() {
        return family;
    }

    public void setFamily(int family) {
        this.family = family;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public LocalDate getInfdate() {
        return infdate;
    }

    public void setInfdate(LocalDate infdate) {
        this.infdate = infdate;
    }

    public LocalTime getInftime() {
        return inftime;
    }

    public void setInftime(LocalTime inftime) {
        this.inftime = inftime;
    }

    public boolean getSevere() {
        return severe;
    }

    public void setSevere(boolean severe) {
        this.severe = severe;
        if(severe){
            this.setSpeed(0);
        }
    }

    public boolean isAthome() {
        return athome;
    }

    public void setAthome(boolean athome) {
        this.athome = athome;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        if(!alive){
            this.color = Color.BLACK;
            this.speed = 0;
            this.x = -100;
            this.y = -100;
            this.infected = false;
        }
                        
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    
}
