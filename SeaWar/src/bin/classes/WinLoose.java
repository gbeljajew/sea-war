/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.classes;

import javax.swing.JTextField;

/**
 *
 * @author gbeljajew
 */
public class WinLoose 
{
    private int win=0;
    private int loose=0;
    
    private final JTextField wint, looset;

    public WinLoose(JTextField wint, JTextField looset) {
        this.wint = wint;
        this.looset = looset;
        wint.setText(String.valueOf(win));
        looset.setText(String.valueOf(loose));
    }
    
    
    
    public void win()
    {
        win++;
        wint.setText(String.valueOf(win));
    }
    
    public void loose()
    {
        loose++;
        looset.setText(String.valueOf(loose));
    }

    public int getWin() {
        return win;
    }

    public int getLoose() {
        return loose;
    }
    
    
}
