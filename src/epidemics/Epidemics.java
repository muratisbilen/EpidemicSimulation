package epidemics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.event.*;
import org.apache.commons.math3.distribution.NormalDistribution;

public class Epidemics implements ActionListener{
    
    static boolean active = false;
    
    public static void main(String[] args) throws Exception{
        JFrame mf = new JFrame("");
        mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mf.setSize(1500,1000);
        mf.setLayout(new BorderLayout());
        JLabel sldlabel = new JLabel("Percent Moving Outside");
        JSlider sld = new JSlider(JSlider.VERTICAL,0,100,100);
        JTextField homerate = new JTextField("%"+sld.getValue());
        homerate.setPreferredSize(new Dimension(100,20));
        sld.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                homerate.setText("%"+((JSlider)e.getSource()).getValue());
                sld.setFocusable(false);
            }
        });
        
        Field[] ff = new Field[1];
        ff[0] = new Field((1.0*sld.getValue())/100);
        
        mf.add(ff[0],BorderLayout.CENTER);
        
        JPanel sldpanel = new JPanel();
        sldpanel.add(sldlabel);
        sldpanel.add(sld);
        sldpanel.add(homerate);
        mf.add(sldpanel,BorderLayout.EAST);
        JButton start = new JButton("Start");
        JButton cancel = new JButton("Cancel");
        JLabel stop = new JLabel("");
        
        Graph[] gr = new Graph[1];
        gr[0] = ff[0].getGr();
        JPanel[] p0 = new JPanel[1];
        p0[0] = new JPanel();
        
        JPanel[] p = new JPanel[1];
        p[0] = new JPanel();
        
        p[0].setLayout(new GridLayout(7,3));
        p[0].add(start);
        p[0].add(cancel);
        p[0].add(stop);
        p[0].add(new JLabel("Severe Patients:"));
        p[0].add(ff[0].getTf1());
        p[0].add(new JLabel(""));
        p[0].add(new JLabel("Mild Patients:"));
        p[0].add(ff[0].getTf2());
        p[0].add(new JLabel(""));
        p[0].add(new JLabel("Death:"));
        p[0].add(ff[0].getTf3());
        p[0].add(new JLabel(""));
        p[0].add(new JLabel("Healed:"));
        p[0].add(ff[0].getTf4());
        p[0].add(new JLabel(""));
        p[0].add(new JLabel("Uninfected:"));
        p[0].add(ff[0].getTf5());
        p[0].add(new JLabel(""));
        p[0].add(new JLabel("In Hospital:"));
        p[0].add(ff[0].getTf6());
        p[0].add(new JLabel(""));
        p0[0].add(p[0]);
        //p0[0].add(gr[0]);
        gr[0].setPreferredSize(new Dimension(1200,100));
        mf.add(gr[0],BorderLayout.NORTH);
        mf.add(p0[0],BorderLayout.SOUTH);
        
        mf.setVisible(true);
        
        mf.validate();
        
        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(!active){
                    active = true;
                    Random rand = new Random();
                    ff[0].setSevnum(0);
                    ff[0].setMildnum(0);
                    NormalDistribution normdist = new NormalDistribution();
                    int lf = 1;
                    int sn = 0;
                    int mn = 0;
                    int ih = 0;

                    ArrayList<Person> peop = new ArrayList<>();

                    for(int i=0;i<ff[0].getPop();i++){

                        int age = (int)Math.round(rand.nextGaussian()*20+30);
                        if(age<0){
                            age = -age;
                        }

                        int family = lf++;
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
                                sn++;
                            }else{
                                mn++;
                            }
                        }

                        boolean athome = false;
                        double ng = rand.nextGaussian();
                        double nd = normdist.inverseCumulativeProbability(1.0*sld.getValue()/100);

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
                            x = rand.nextInt((int)ff[0].getSidelim());
                            y = rand.nextInt((int)ff[0].getUplim());

                            if(x<=size/2 || y<=size/2 || x>=ff[0].getSidelim()-(size/2) || y>=ff[0].getUplim()-(size/2)){
                                check = true;
                            }else{
                                for(int j=0;j<i;j++){
                                    float x1 = peop.get(j).getX();
                                    float y1 = peop.get(j).getY();
                                    double dist = Math.pow(x1-x,2)+Math.pow(y1-y,2);
                                    if(dist<Math.pow((size+peop.get(j).getSize())/2,2)){
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
                        for(int j=0;j<ff[0].getPop();j++){
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
                            ih++;
                        }
                        peop.add(new Person(age, family, infected, infdate, inftime, severe, athome, alive, x, y, speed, angle, size,interact, survtime, survive, healtime, hosp));
                    }

                    ff[0].setLastfamily(lf);
                    ff[0].setSevnum(sn);
                    ff[0].setMildnum(mn);
                    ff[0].setInhosp(ih);
                    ff[0].setPeople(peop);
                }
                
                ff[0].getT().start();
            }
        });
        
        cancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                active = false;
                ff[0].getT().stop();
                mf.remove(ff[0]);
                ff[0] = new Field((1.0*sld.getValue())/100);
                mf.add(ff[0],BorderLayout.CENTER);
                mf.remove(p0[0]);
                
                gr[0] = ff[0].getGr();
                p0[0] = new JPanel();

                p[0] = new JPanel();

                p[0].setLayout(new GridLayout(7,3));
                p[0].add(start);
                p[0].add(cancel);
                p[0].add(stop);
                
                p[0].add(new JLabel("Severe Patients:"));
                p[0].add(ff[0].getTf1());
                p[0].add(new JLabel(""));
                p[0].add(new JLabel("Mild Patients:"));
                p[0].add(ff[0].getTf2());
                p[0].add(new JLabel(""));
                p[0].add(new JLabel("Death:"));
                p[0].add(ff[0].getTf3());
                p[0].add(new JLabel(""));
                p[0].add(new JLabel("Healed:"));
                p[0].add(ff[0].getTf4());
                p[0].add(new JLabel(""));
                p[0].add(new JLabel("Uninfected:"));
                p[0].add(ff[0].getTf5());
                p[0].add(new JLabel(""));
                p[0].add(new JLabel("In Hospital:"));
                p[0].add(ff[0].getTf6());
                p[0].add(new JLabel(""));
                p0[0].add(p[0]);
                gr[0].setPreferredSize(new Dimension(1200,100));
                mf.add(gr[0],BorderLayout.NORTH);
                mf.add(p0[0],BorderLayout.SOUTH);
                mf.validate();
            }
        });
        
    }
    
    public void actionPerformed(ActionEvent e){
        System.out.println(e.getActionCommand());
    }
}
