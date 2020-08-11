package epidemics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.*;

public class Epidemics implements ActionListener{
    
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
        JButton stop = new JButton("Stop");
        JButton cancel = new JButton("Cancel");
        
        Graph[] gr = new Graph[1];
        gr[0] = ff[0].getGr();
        JPanel[] p0 = new JPanel[1];
        p0[0] = new JPanel();
        
        JPanel[] p = new JPanel[1];
        p[0] = new JPanel();
        
        p[0].setLayout(new GridLayout(7,3));
        p[0].add(start);
        p[0].add(stop);
        p[0].add(cancel);
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
                ff[0].getT().start();
            }
        });
        
        stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                ff[0].getT().stop();
                mf.getAccessibleContext();
            }
        });
        
        cancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
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
                p[0].add(stop);
                p[0].add(cancel);
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
