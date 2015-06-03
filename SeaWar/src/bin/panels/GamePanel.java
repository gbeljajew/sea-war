/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.panels;

import bin.classes.Player;
import bin.classes.WinLoose;
import bin.enums.GameState;
import bin.enums.ShootEnum;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * this class will contain all you game logic and graphic<br/>
 * use it for smaller games
 * 
 * @author gbeljajew
 */
public class GamePanel extends JPanel
{
    public static final int width = 650;
    public static final int high = 270;
    private static final int TIMER = 35; 
    
    //-----------------------------------------------
    
    public final Player player, enemy;
    public GameState gs=GameState.setup;
    
    public MyFleetPanel myFleet = new MyFleetPanel(this);
    public EnemyFleetPanel enemyFleet = new EnemyFleetPanel(this);
    
    JTextField wint = new JTextField();
    JTextField looset = new JTextField();
    
    private int timer = TIMER;
    
    WinLoose winLoose = new WinLoose(wint, looset);
    
    public GamePanel() 
    {
        this.setPreferredSize(new Dimension(width, high));
        
        player = new Player();
        enemy = player.getEnemy();
        
        this.setLayout(null);
        
        myFleet.setBounds(20, 50, 201, 201);
        this.add(myFleet);
        
        enemyFleet.setBounds(290, 50, 201, 201);
        this.add(enemyFleet);
        
        JLabel pl, el, winl, losel;
        
        pl = new JLabel("PLAYER");
        pl.setBounds(20, 5, 200, 40);
        pl.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(pl);
        
        el = new JLabel("ENEMY");
        el.setBounds(290, 5, 200, 40);
        el.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(el);
        
        winl = new JLabel("WINS");
        winl.setBounds(520, 50, 50, 20);
        this.add(winl);
        
        losel = new JLabel("LOOSE");
        losel.setBounds(520, 80, 50, 20);
        this.add(losel);
        
        wint.setBounds(580, 50, 50, 20);
        wint.setEditable(false);
        wint.setHorizontalAlignment(JTextField.RIGHT);
        this.add(wint);
        
        looset.setBounds(580, 80, 50, 20);
        looset.setEditable(false);
        looset.setHorizontalAlignment(JTextField.RIGHT);
        this.add(looset);
        
        JButton newGameB, autoplaceB, startB;
        
        newGameB = new JButton("NEW GAME");
        newGameB.setBounds(520, 110, 110, 20);
        this.add(newGameB);
        
        autoplaceB = new JButton("AUTOPLACE");
        autoplaceB.setBounds(520, 140, 110, 20);
        this.add(autoplaceB);
        
        startB = new JButton("START");
        startB.setBounds(520, 170, 110, 20);
        this.add(startB);
        
        newGameB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(gs == GameState.enemy || gs == GameState.player)
                    winLoose.loose();
                
                gs = GameState.setup;
                init();
            }
        });
        
        autoplaceB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if(gs == GameState.setup)
                    player.autoplacement();
            }
        });
        
        startB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
                if(gs == GameState.setup)
                {
                    
                    if(!enemy.isMapSet())
                        enemy.autoplacement();
                    
                    if(!player.isMapSet())
                        player.autoplacement();
                    
                    if(Math.random()<.5)
                        gs = GameState.enemy;
                    else
                        gs = GameState.player;
                }
            }
        });
        
    }
    
    
    
    
    
    public void init()
    {
        player.resetMapsAndShips();
        enemy.autoplacement();
    }
    
    public void update()
    {
        if(gs == GameState.enemy)
        {
            timer--;
            if(timer < 0)
            {
                
                ShootEnum she = enemy.aiShoot();
                
                switch(she)
                {
                    case loose:
                        winLoose.loose();
                        player.lookup();
                        gs=GameState.setup;
                        break;
                    case miss:
                        gs = GameState.player;
                        break;
                    case destroed:
                    case hit:
                        timer = TIMER;
                        break;
                    default:
                        throw new AssertionError(she.name());
                    
                }
                
            }
        }
    }

    @Override
    public void paint(Graphics g1) 
    {
        Graphics2D g = (Graphics2D)g1;
        
        super.paint(g1);
        
        Color c = g.getColor();
        
        if(gs == GameState.player)
        {
            g.setColor(Color.green);
            g.draw3DRect(20, 5, 200, 40, false);
            
        }
        else if(gs == GameState.enemy)
        {
            g.setColor(Color.red);
            g.draw3DRect(290, 5, 200, 40, false);
        }
         
    }
    
    public void shoot(int x, int y)
    {
        ShootEnum shoot = player.shoot(x, y);
                    
        switch(shoot)
        {
            case hit:
                break;
            case miss:
                gs = GameState.enemy;
                timer = TIMER;
                break;
            case destroed:
                break;
            case loose:
                winLoose.win();
                player.lookup();
                gs = GameState.setup;
                break;
            case wrongPlace:
                break;
            default:
                throw new AssertionError(shoot.name());
            
            
        }
        
        
        
    }
    
    
}
