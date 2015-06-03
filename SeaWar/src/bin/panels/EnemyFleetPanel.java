/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.panels;

import static bin.classes.FleetConstants.*;
import bin.enums.GameState;
import static bin.panels.MyFleetPanel.high;
import static bin.panels.MyFleetPanel.width;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;


/**
 *
 * @author gbeljajew
 */
public class EnemyFleetPanel extends JPanel
{
    
    GamePanel gamePanel;
    public EnemyFleetPanel(GamePanel gamePanel) 
    {
        this.setPreferredSize(new Dimension(MyFleetPanel.width, MyFleetPanel.high));
        this.gamePanel = gamePanel;
        
        this.addMouseListener(new MouseAdapter() 
        {

            @Override
            public void mousePressed(MouseEvent e) 
            {
                if(gamePanel.gs == GameState.player)
                {
                    int x = e.getX()/TILE_WIDE;
                    int y = e.getY()/TILE_HIGH;
                    
                    gamePanel.shoot(x, y);
                    
                }
            }
            
        });
    }

    @Override
    public void paint(Graphics g) 
    {
        int[][]map = gamePanel.player.getBattleMap();
        
        g.clearRect(0, 0, width, high);
        Color c = g.getColor();
        for(int i = 0; i <= width; i++)
        {
            g.drawLine(0, i*TILE_HIGH, width, i*TILE_HIGH);
            g.drawLine(i*TILE_WIDE, 0, i*TILE_WIDE, high);
        }
        
        
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
                } else if(m == BLCOKED)
                {
                    g.setColor(Color.gray);
                    g.fillRect(x*TILE_WIDE, y*TILE_HIGH, TILE_WIDE, TILE_HIGH);
                    g.setColor(c);
                }
                
                
                
            }
        
        int lx = gamePanel.player.getLastX();
        int ly = gamePanel.player.getLastY();
        
        if(lx!= -1 && ly != -1)
        {
            g.setColor(Color.RED);
            g.drawOval(lx*TILE_WIDE-5, ly*TILE_HIGH-5, TILE_WIDE+10, TILE_HIGH+10);
            g.setColor(c);
        }
    }
    
    
    
}
