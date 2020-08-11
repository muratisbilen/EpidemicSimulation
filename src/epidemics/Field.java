package epidemics;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import java.util.*;
import javax.accessibility.AccessibleContext;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;
import org.apache.commons.math3.distribution.*;

public class Field extends JPanel implements ActionListener{
    private int lastfamily = 1;
    private ArrayList<Person> people = new ArrayList<>();
    private javax.swing.Timer t = new javax.swing.Timer(10,this);
    private JSlider sld = new JSlider(JSlider.VERTICAL,0,100,100);
    private float uplim = 700;
    private float sidelim = 1200;
    private final int severelim = 10;
    private final int mildlim = 3;
    private JTextField tf1 = new JTextField("");
    private JTextField tf2 = new JTextField("");
    private JTextField tf3 = new JTextField("");
    private JTextField tf4 = new JTextField("");
    private JTextField tf5 = new JTextField("");
    private JTextField tf6 = new JTextField("");
    private int sevnum;
    private int mildnum;
    private int deathnum = 0;
    private int healednum = 0;
    private int uninfected;
    private Graph gr;
    private final int pop = 500;
    private final int hospcap = pop/50;
    private int inhosp = 0;
    
    public Field(double sld){
        super();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        Random rand = new Random();
        this.sevnum = 0;
        this.mildnum = 0;
        NormalDistribution normdist = new NormalDistribution();
        
        for(int i=0;i<pop;i++){
            
            int age = (int)Math.round(rand.nextGaussian()*20+30);
            if(age<0){
                age = -age;
            }
            
            int family = lastfamily++;
            boolean infected = false;
            
            if(i==0){
                infected = true;
            }
            
            LocalDate infdate = null;
            LocalTime inftime = null;
            
            boolean severe = false;
            
            if(infected){
                infdate = LocalDate.now();
                inftime = LocalTime.now();
                if(rand.nextGaussian()>normdist.inverseCumulativeProbability(0.6) && i!=0){
                    severe = true;
                    sevnum++;
                }else{
                    mildnum++;
                }
            }
            
            boolean athome = false;
            double ng = rand.nextGaussian();
            double nd = normdist.inverseCumulativeProbability(sld);
            
            if((severe || ng>nd) && i!=0){
                athome = true;
            }
            
            boolean alive = true;
            boolean check = false;
            float x = 0;
            float y = 0;
            float size = 10;
            
            do{
                check = false;
                x = rand.nextInt((int)this.sidelim);
                y = rand.nextInt((int)this.uplim);
                
                if(x<=size/2 || y<=size/2 || x>=sidelim-(size/2) || y>=uplim-(size/2)){
                    check = true;
                }else{
                    for(int j=0;j<i;j++){
                        float x1 = this.people.get(j).getX();
                        float y1 = this.people.get(j).getY();
                        double dist = Math.pow(x1-x,2)+Math.pow(y1-y,2);
                        if(dist<Math.pow((size+this.people.get(j).getSize())/2,2)){
                            check=true;
                        }
                    }
                }
            }while(check);
            
            double speed = 5;
            if(athome || severe){
                speed=0;
            }
            double angle = rand.nextDouble()*Math.PI*2;
            ArrayList<Integer> interact = new ArrayList<>();
            for(int j=0;j<pop;j++){
                interact.add(0);
            }
            
            double normval = normdist.sample()*2+6;
            double normval2 = normdist.sample()+3;
            if(normval<0){
                normval = -normval;
            }
            int survtime = (int)normval;
            
            if(normval2<0){
                normval2 = -normval2;
            }
            if(severe){
                normval2 = normval;
            }
            int healtime = (int)normval2;
            
            boolean survive = true;
            if(severe && rand.nextGaussian()>normdist.inverseCumulativeProbability(0.8)){
                survive = false;
            }
            boolean hosp = false;
            if(severe){
                hosp = true;
                inhosp++;
            }
            this.people.add(new Person(age, family, infected, infdate, inftime, severe, athome, alive, x, y, speed, angle, size,interact, survtime, survive, healtime, hosp));
        }
        
        tf1.setEditable(false);
        tf2.setEditable(false);
        tf3.setEditable(false);
        tf4.setEditable(false);
        tf5.setEditable(false);
        tf6.setEditable(false);
        
        tf1.setText(sevnum+"");
        tf2.setText(mildnum+"");
        tf3.setText(deathnum+"");
        tf4.setText(healednum+"");
        this.uninfected = pop - (sevnum + mildnum);
        tf5.setText(uninfected+"");
        this.gr = new Graph(uninfected,sevnum,mildnum,deathnum,healednum,hospcap);
        tf6.setText(inhosp+"");
    }
    
    public void actionPerformed(ActionEvent e){
        float[] xs = new float[this.people.size()];
        float[] ys = new float[this.people.size()];
        double[] angs = new double[this.people.size()];
        Random rand = new Random();
        NormalDistribution normdist = new NormalDistribution();
        
        for(int i=0;i<this.people.size();i++){
            if(people.get(i).isAlive()){
                if(people.get(i).getSevere()){
                    if(inhosp<hospcap && !people.get(i).isHosp()){
                        people.get(i).setHosp(true);
                        people.get(i).setSurvive(people.get(i).getPresurvive());
                        inhosp++;
                    }else{
                        people.get(i).setSurvive(false);
                    }
                }
                float myx = people.get(i).getX();
                float myy = people.get(i).getY();

                xs[i] = myx;
                ys[i] = myy;
                angs[i] = people.get(i).getAngle();
                
                
                people.get(i).setX(people.get(i).getX()+(float)people.get(i).getSpeed()*(float)Math.cos(people.get(i).getAngle()));
                people.get(i).setY(people.get(i).getY()+(float)people.get(i).getSpeed()*(float)Math.sin(people.get(i).getAngle()));
                
                
                if(people.get(i).getX()<people.get(i).getSize()/2){
                    people.get(i).setAngle(Math.PI-people.get(i).getAngle());
                    people.get(i).setX(people.get(i).getSize()/2);
                }

                if(people.get(i).getX()>sidelim-(people.get(i).getSize()/2)){
                    people.get(i).setAngle(Math.PI-people.get(i).getAngle());
                    people.get(i).setX(sidelim-(people.get(i).getSize()/2));
                }

                if(people.get(i).getY()<people.get(i).getSize()/2){
                    people.get(i).setAngle(-people.get(i).getAngle());
                    people.get(i).setY(people.get(i).getSize()/2);
                }

                if(people.get(i).getY()>uplim-(people.get(i).getSize()/2)){
                    people.get(i).setAngle(-people.get(i).getAngle());
                    people.get(i).setY(uplim-(people.get(i).getSize()/2));
                }

                if(people.get(i).isInfected()){
                    LocalTime now = LocalTime.now();
                    LocalTime inf = people.get(i).getInftime();
                    if(!people.get(i).isSurvive()){
                        if(inf.until(now,ChronoUnit.SECONDS)>people.get(i).getSurvtime()){
                            people.get(i).setAlive(false);
                            deathnum++;
                            sevnum--;
                            if(people.get(i).isHosp()){
                                inhosp--;
                            }
                        }
                    }else{
                        if(inf.until(now,ChronoUnit.SECONDS)>people.get(i).getHealtime()){
                            people.get(i).setColor(Color.PINK);
                            people.get(i).setInfected(false);
                            people.get(i).setHealed(true);
                            healednum++;
                            if(people.get(i).getSevere()){
                                sevnum--;
                            }else{
                                mildnum--;
                            }
                            if(people.get(i).isHosp()){
                                inhosp--;
                            }
                        }
                    }
                }
            }
        }
        
        for(int i=0;i<this.people.size()-1;i++){
            for(int j=i+1;j<this.people.size();j++){
                if(people.get(i).isAlive() && people.get(j).isAlive()){
                    float xx1 = people.get(i).getX();
                    float yy1 = people.get(i).getY();

                    float xx2 = people.get(j).getX();
                    float yy2 = people.get(j).getY();
                    double distance = Math.pow(xx2-xx1, 2)+Math.pow(yy2-yy1,2);
                    double distlim = Math.pow((people.get(i).getSize()+people.get(j).getSize())/2,2);
                    if(distance <= distlim){
                        if(people.get(i).getInteract().get(j)==0){
                            double diff = distlim - distance;
                            double alpha = Math.atan((yy2-yy1)/(xx2-xx1));
                            
                            if(alpha<0 && yy1-yy2<0){
                                alpha +=2*Math.PI;
                            }else if(alpha<0 && yy1-yy2>0){
                                alpha += Math.PI;
                            }else if(alpha>0 && yy1-yy2<0){
                                alpha += Math.PI;
                            }else if(alpha>0 && yy1-yy2>0){
                                alpha = alpha;
                            }else if(alpha==0 && xx1-xx2<0){
                                alpha += Math.PI;
                            }else if(alpha==0 && xx1-xx2>0){
                                alpha = alpha;
                            }
                            
                            double beta = alpha - (Math.PI/2);
                            double angdiff = alpha - people.get(i).getAngle();
                            if((angdiff < Math.PI/2 && angdiff > -Math.PI/2)){
                                people.get(i).setAngle(people.get(i).getAngle()+(angdiff/2));
                            }else if(angdiff > 3*Math.PI/2){
                                people.get(i).setAngle(people.get(i).getAngle()+((angdiff-2*Math.PI)/2));
                            }else if(angdiff < -3*Math.PI/2){
                                people.get(i).setAngle(people.get(i).getAngle()+((angdiff+2*Math.PI)/2));
                            }else{
                                people.get(i).setAngle(2*beta-people.get(i).getAngle());
                            }
                            
                            double angdiff2 = alpha - people.get(j).getAngle();
                            if((angdiff2 < Math.PI/2 && angdiff2 > -Math.PI/2)){
                                people.get(j).setAngle(people.get(j).getAngle()+(angdiff2/2));
                            }else if(angdiff2 > 3*Math.PI/2){
                                people.get(j).setAngle(people.get(j).getAngle()+((angdiff2-2*Math.PI)/2));
                            }else if(angdiff2 < -3*Math.PI/2){
                                people.get(j).setAngle(people.get(j).getAngle()+((angdiff2+2*Math.PI)/2));
                            }else{
                                people.get(j).setAngle(2*beta-people.get(j).getAngle());
                            }
                            
                            if(people.get(i).isInfected() && people.get(j).isInfected()){

                            }else if(people.get(i).isInfected()){
                                if(!people.get(j).isHealed()){
                                    people.get(j).setInfected(true);
                                    people.get(j).setInftime(LocalTime.now());
                                    people.get(j).setInfdate(LocalDate.now());
                                    if(rand.nextGaussian()>normdist.inverseCumulativeProbability(0.8)){
                                        people.get(j).setSevere(true);
                                        people.get(j).setColor(Color.RED);
                                        sevnum++;
                                        uninfected--;
                                        if(inhosp<hospcap){
                                            inhosp++;
                                            people.get(j).setHosp(true);
                                        }else{
                                            people.get(j).setSurvive(false);
                                        }
                                    }else{
                                        people.get(j).setSevere(false);
                                        people.get(j).setColor(Color.ORANGE);
                                        mildnum++;
                                        uninfected--;
                                    }
                                }
                            }else if(people.get(j).isInfected()){
                                if(!people.get(i).isHealed()){
                                    people.get(i).setInfected(true);
                                    people.get(i).setInftime(LocalTime.now());
                                    people.get(i).setInfdate(LocalDate.now());
                                    if(rand.nextGaussian()>normdist.inverseCumulativeProbability(0.8)){
                                        people.get(i).setSevere(true);
                                        people.get(i).setColor(Color.RED);
                                        sevnum++;
                                        uninfected--;
                                        if(inhosp<hospcap){
                                            inhosp++;
                                            people.get(i).setHosp(true);
                                        }else{
                                            people.get(i).setSurvive(false);
                                        }
                                    }else{
                                        people.get(i).setSevere(false);
                                        people.get(i).setColor(Color.ORANGE);
                                        mildnum++;
                                        uninfected--;
                                    }
                                }
                            }
                            people.get(i).getInteract().set(j,1);
                            people.get(j).getInteract().set(i,1);
                        }
                    }else{
                        people.get(i).getInteract().set(j,0);
                        people.get(j).getInteract().set(i,0);
                    }
                }
            }
        }
        
        tf1.setText(sevnum+"");
        tf2.setText(mildnum+"");
        tf3.setText(deathnum+"");
        tf4.setText(healednum+"");
        tf5.setText(uninfected+"");
        tf6.setText(inhosp+"");
        
        repaint();
        
        gr.addUninfected(uninfected);
        gr.addSevnum(sevnum);
        gr.addMildnum(mildnum);
        gr.addDeath(deathnum);
        gr.addHealed(healednum);
        gr.repaint();
        
        if(sevnum+mildnum==0){
            t.stop();
        }
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawLine(0, (int)uplim, (int)sidelim, (int)uplim);
        g.drawLine((int)sidelim, 0, (int)sidelim, (int)uplim);
        g.drawLine(0, 0, 0, (int)uplim);
        
        for(int i=0;i<people.size();i++){
            if(people.get(i).getX()>=0 && people.get(i).getY()>=0){
                g.setColor(people.get(i).getColor());
                g.fillOval((int)(people.get(i).getX()-(people.get(i).getSize()/2)), (int)(people.get(i).getY()-(people.get(i).getSize()/2)), (int)(people.get(i).getSize()), (int)(people.get(i).getSize()));
            }
        }
    }

    public JTextField getTf6() {
        return tf6;
    }

    public void setTf6(JTextField tf6) {
        this.tf6 = tf6;
    }

    public int getInhosp() {
        return inhosp;
    }

    public void setInhosp(int inhosp) {
        this.inhosp = inhosp;
    }

    public Graph getGr() {
        return gr;
    }

    public void setGr(Graph gr) {
        this.gr = gr;
    }

    public int getSevnum() {
        return sevnum;
    }

    public void setSevnum(int sevnum) {
        this.sevnum = sevnum;
    }

    public int getMildnum() {
        return mildnum;
    }

    public void setMildnum(int mildnum) {
        this.mildnum = mildnum;
    }

    public int getDeathnum() {
        return deathnum;
    }

    public void setDeathnum(int deathnum) {
        this.deathnum = deathnum;
    }

    public int getHealednum() {
        return healednum;
    }

    public void setHealednum(int healednum) {
        this.healednum = healednum;
    }

    public int getUninfected() {
        return uninfected;
    }

    public void setUninfected(int uninfected) {
        this.uninfected = uninfected;
    }

    public JTextField getTf5() {
        return tf5;
    }

    public void setTf5(JTextField tf5) {
        this.tf5 = tf5;
    }

    public JTextField getTf1() {
        return tf1;
    }

    public void setTf1(JTextField tf1) {
        this.tf1 = tf1;
    }

    public JTextField getTf2() {
        return tf2;
    }

    public void setTf2(JTextField tf2) {
        this.tf2 = tf2;
    }

    public JTextField getTf3() {
        return tf3;
    }

    public void setTf3(JTextField tf3) {
        this.tf3 = tf3;
    }

    public JTextField getTf4() {
        return tf4;
    }

    public void setTf4(JTextField tf4) {
        this.tf4 = tf4;
    }
    
    public int getLastfamily() {
        return lastfamily;
    }

    public void setLastfamily(int lastfamily) {
        this.lastfamily = lastfamily;
    }

    public JSlider getSld() {
        return sld;
    }

    public void setSld(JSlider sld) {
        this.sld = sld;
    }
    
    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    public javax.swing.Timer getT() {
        return t;
    }

    public void setT(javax.swing.Timer t) {
        this.t = t;
    }

    public ComponentUI getUi() {
        return ui;
    }

    public void setUi(ComponentUI ui) {
        this.ui = ui;
    }

    public EventListenerList getListenerList() {
        return listenerList;
    }

    public void setListenerList(EventListenerList listenerList) {
        this.listenerList = listenerList;
    }

    public AccessibleContext getAccessibleContext() {
        return accessibleContext;
    }

    public void setAccessibleContext(AccessibleContext accessibleContext) {
        this.accessibleContext = accessibleContext;
    }
    
    
}
