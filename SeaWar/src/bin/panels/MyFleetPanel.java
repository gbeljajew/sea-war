/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.panels;

import static bin.classes.FleetConstants.*;
import bin.classes.Player;
import bin.enums.GameState;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author gbeljajew
 */
public class MyFleetPanel extends JPanel 
{
    public static final int width = 200;
    public static final int high = 200;
    
    //-------------------------------------
    
    GamePanel gamePanel;
    
    public MyFleetPanel(GamePanel gamePanel) 
    {
        this.setPreferredSize(new Dimension(width, high));
        this.gamePanel = gamePanel;
    }

    @Override
    public void paint(Graphics g) 
    {
        int[][]map = gamePanel.player.getMap();
        
        g.clearRect(0, 0, width, high);
        
        for(int i = 0; i <= width; i++)
        {
            g.drawLine(0, i*TILE_HIGH, width, i*TILE_HIGH);
            g.drawLine(i*TILE_WIDE, 0, i*TILE_WIDE, high);
        }
        
        Color c = g.getColor();
        for(int x = 0; x<map.length; x++)
            for(int y = 0 ; y<map[x].length; y++)
            {
                int m = map[x][y];
                
                if(m>=100)
                {
                    g.setColor(Color.red);
                    g.fill3DRect(x*TILE_WIDE, y*TILE_HIGH, TILE_WIDE, TILE_HIGH, false);
                    g.setColor(c);
                }else if(m>=10)
                {
                    g.fill3DRect(x*TILE_WIDE, y*TILE_HIGH, TILE_WIDE, TILE_HIGH, false);
                }else if(m == SHOOT)
                {
                    g.setColor(Color.red);
                    
                    g.drawLine(x*TILE_WIDE, y*TILE_HIGH+1, x*TILE_WIDE+TILE_WIDE-1, y*TILE_HIGH+TILE_HIGH-1);
                    g.drawLine(x*TILE_WIDE, y*TILE_HIGH+TILE_HIGH-1, x*TILE_WIDE+TILE_WIDE-1, y*TILE_HIGH+1);
                    
                    g.setColor(c);
                }
                
                if(gamePanel.gs == GameState.setup)
                {
                    switch(m)
                    {
                        case BLCOKED:
                            g.setColor(Color.gray);
                            g.fillRect(x*TILE_WIDE, y*TILE_HIGH, TILE_WIDE, TILE_HIGH);
                            g.setColor(c);
                            break;
                        case CAN_PLACE_BOTH:
                            g.setColor(Color.green);
                            
                            g.drawLine(x*TILE_WIDE+1, y*TILE_HIGH+TILE_HIGH/2, x*TILE_WIDE+TILE_WIDE-1, y*TILE_HIGH+TILE_HIGH/2);
                            g.drawLine(x*TILE_WIDE+TILE_WIDE/2, y*TILE_HIGH+1, x*TILE_WIDE+TILE_WIDE/2, y*TILE_HIGH+TILE_HIGH-1);
                            
                            g.setColor(c);
                            break;
                        case CAN_PLACE_HORISONTAL:
                            g.setColor(Color.green);
                            
                            g.drawLine(x*TILE_WIDE+1, y*TILE_HIGH+TILE_HIGH/2, x*TILE_WIDE+TILE_WIDE-1, y*TILE_HIGH+TILE_HIGH/2);
                            
                            g.setColor(c);
                            break;
                        case CAN_PLACE_VERTIKAL:
                            g.setColor(Color.green);
                            
                            g.drawLine(x*TILE_WIDE+TILE_WIDE/2, y*TILE_HIGH+1, x*TILE_WIDE+TILE_WIDE/2, y*TILE_HIGH+TILE_HIGH-1);
                            
                            g.setColor(c);
                            break;
                        default:
                                
                    }
                    
                }
                
                
                
            }
        
        int lx = gamePanel.enemy.getLastX();
        int ly = gamePanel.enemy.getLastY();
        
        if(lx!= -1 && ly != -1)
        {
            g.setColor(Color.RED);
            g.drawOval(lx*TILE_WIDE-5, ly*TILE_HIGH-5, TILE_WIDE+10, TILE_HIGH+10);
            g.setColor(c);
        }
        
    }
    
    
    
}
