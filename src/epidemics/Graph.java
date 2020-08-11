
package epidemics;

import java.awt.*;
import java.util.ArrayList;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;

public class Graph extends JPanel {
    private ArrayList<Integer> uninfected = new ArrayList<>();
    private ArrayList<Integer> sevnum = new ArrayList<>();
    private ArrayList<Integer> mildnum = new ArrayList<>();
    private ArrayList<Integer> death = new ArrayList<>();
    private ArrayList<Integer> healed = new ArrayList<>();
    private int hospcap;
    
    public Graph(int uninfected, int sevnum, int mildnum, int death, int healed, int hospcap){
        super();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.uninfected.add(uninfected);
        this.sevnum.add(sevnum);
        this.mildnum.add(mildnum);
        this.death.add(death);
        this.healed.add(healed);
        this.hospcap = hospcap;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int h = this.getSize().height;
        for(int i=0;i<uninfected.size();i++){
            
            int tot = uninfected.get(i)+sevnum.get(i)+mildnum.get(i)+death.get(i)+healed.get(i);
            int ur = h*uninfected.get(i)/tot;
            int sr = h*sevnum.get(i)/tot;
            int mr = h*mildnum.get(i)/tot;
            int dr = h*death.get(i)/tot;
            int hr = h*healed.get(i)/tot;
            int cr = h*hospcap/tot;
            
            g.setColor(Color.WHITE);
            g.drawLine(i, 0, i, h);
            g.setColor(Color.RED);
            g.drawLine(i, h, i, h-sr);
            g.setColor(Color.ORANGE);
            g.drawLine(i, h-sr, i, h-(sr+mr));
            g.setColor(Color.BLACK);
            g.drawLine(i, h, i, h-dr);
            g.setColor(Color.PINK);
            g.drawLine(i, 0, i, hr);
            g.setColor(Color.BLACK);
            g.drawLine(0,h-cr,getSize().width,h-cr);
            
        }
        
    }
    
    public void addUninfected(int u){
        this.uninfected.add(u);
    }
    
    public void addSevnum(int u){
        this.sevnum.add(u);
    }
    
    public void addMildnum(int u){
        this.mildnum.add(u);
    }
    
    public void addDeath(int u){
        this.death.add(u);
    }
    
    public void addHealed(int u){
        this.healed.add(u);
    }
    
    public ArrayList<Integer> getUninfected() {
        return uninfected;
    }

    public void setUninfected(ArrayList<Integer> uninfected) {
        this.uninfected = uninfected;
    }

    public ArrayList<Integer> getSevnum() {
        return sevnum;
    }

    public void setSevnum(ArrayList<Integer> sevnum) {
        this.sevnum = sevnum;
    }

    public ArrayList<Integer> getMildnum() {
        return mildnum;
    }

    public void setMildnum(ArrayList<Integer> mildnum) {
        this.mildnum = mildnum;
    }

    public ArrayList<Integer> getDeath() {
        return death;
    }

    public void setDeath(ArrayList<Integer> death) {
        this.death = death;
    }

    public ArrayList<Integer> getHealed() {
        return healed;
    }

    public void setHealed(ArrayList<Integer> healed) {
        this.healed = healed;
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
