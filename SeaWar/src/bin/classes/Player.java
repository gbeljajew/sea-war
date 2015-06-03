/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.classes;
import static bin.classes.FleetConstants.*;
import bin.enums.ShipPositionEnum;
import bin.enums.ShootEnum;
import java.awt.Point;
import java.util.*;


/**
 *
 * @author gbeljajew
 */
public class Player 
{
    
    
    private Player enemy;
    private final int[][] map = new int[MAP_WIDE][MAP_HIGH];
    private final int[][] battleMap = new int[MAP_WIDE][MAP_HIGH];
    private final List<Ship> ships = new ArrayList<>();
    private boolean mapSet=false;
    private int lastx = -1, lasty = -1;
    private Point lastHit=null;
    
    //------------------------------------------------------
    private final Set<Point> points = new HashSet<>();
    //------------------------------------------------------

    public Player() 
    {
        for(int i = 0; i< shipNumber.length;i++)
            for(int j = 0; j<shipNumber[i][1];j++)
                ships.add(new Ship(shipNumber[i][0], shipNumber[i][0]*10+j));
        
        
    }

    public boolean isMapSet() {
        return mapSet;
    }
    
    public Player getEnemy() 
    {
        if(enemy == null)
            enemy = new Player();
        
        this.enemy.enemy = this;
        return enemy;
    }

    public int getLastX() {
        return lastx;
    }

    public int getLastY() {
        return lasty;
    }
    
    public int[][] getMap() {
        return map;
    }

    public int[][] getBattleMap() {
        return battleMap;
    }
    
    public boolean canBePlacedVertical(int x, int y, int length)
    {
        if(x<0 || x>=map.length || y<0 || y>map.length-length)
            return false;
        
        for(int i = 0; i < length; i++)
        {
            int m = map[x][y+i];
            if(m==BLCOKED || m>= 10)
                return false;
        }
        
        return true;
        
    }
    
    public boolean canBePlacedHorizontal(int x, int y, int length)
    {
        if(x<0 || x>map.length-length || y<0 || y>=map.length)
            return false;
        
        for(int i = 0; i < length; i++)
        {
            int m = map[x+i][y];
            if(m==BLCOKED || m>= 10)
                return false;
        }
        
        return true;
        
    }
    
    public void setPossiblePositions(int length)
    {
        cleanMap();
        for(int x = 0; x<map.length; x++)
            for(int y = 0 ; y<map[x].length; y++)
            {
                if(map[x][y]==EMPTY)
                {
                    boolean h,v;
                    h = canBePlacedHorizontal(x, y, length);
                    v = canBePlacedVertical(x, y, length);
                    
                    if(h && v)
                        map[x][y]=CAN_PLACE_BOTH;
                    else if (h)
                        map[x][y]=CAN_PLACE_HORISONTAL;
                    else
                        map[x][y]=CAN_PLACE_VERTIKAL;
                }
            }
        
    }
    
    public boolean putShipHorisontal(int x, int y, Ship s)
    {
        if(canBePlacedHorizontal(x, y, s.getLenght()))
        {
            for(int i=x;i<x+s.getLenght();i++)
            {
                map[i][y]=s.getId();
            }
            
            setBlocked(map, x, y);
            
            return true;
        }
        System.out.println("ERROR!!! -h");
        return false;
    }
    
    public boolean putShipVertical(int x, int y, Ship s)
    {
        if(canBePlacedVertical(x, y, s.getLenght()))
        {
            for(int i=y;i<y+s.getLenght();i++)
            {
                map[x][i]=s.getId();
            }
            
            setBlocked(map, x, y);
            
            return true;
        }
        System.out.println("ERROR!!! -v");
        return false;
    }
    
    public void resetMapsAndShips()
    {
        for(int x = 0; x<map.length; x++)
            for(int y = 0 ; y<map[x].length; y++)
            {
                map[x][y]=EMPTY;
                battleMap[x][y]=EMPTY;
            }
        
        for(Ship s: ships)
            s.resetHP();
        
        lastx = -1;
        lasty = -1;
    }
    
    public void cleanMap()
    {
        for(int x = 0; x<map.length; x++)
            for(int y = 0 ; y<map[x].length; y++)
            {
                int m = map[x][y];
                if(m == CAN_PLACE_BOTH || m == CAN_PLACE_HORISONTAL || m == CAN_PLACE_VERTIKAL)
                    map[x][y]=EMPTY;
            }
    }
    
    private void sb(int[][] map, int x, int y)
    {
        if(!points.add(getPoint(x, y)))
            return;
        
        if(x<0 || x>=map.length || y<0 || y>= map[x].length)
            return;
        
        if(map[x][y] >= 10)
        {
            for(int i=x-1; i<=x+1;i++)
                for(int j=y-1; j<=y+1; j++)
                    if(i==x && j==y)
                        ;
                    else
                        sb(map, i, j);
        }
        else
        {
            if(map[x][y]!=SHOOT)
                map[x][y]=BLCOKED;
        }
    }
    
    private void setBlocked(int[][] map, int x, int y)
    {
        points.clear();
        
        sb(map, x, y);
        
    }
    
    private int countEmpty(int[][] map)
    {
        int erg = 0;
        
        for(int x = 0; x<map.length; x++)
            for(int y = 0 ; y<map[x].length; y++)
            {
                int m = map[x][y];
                if(m == EMPTY || m == CAN_PLACE_BOTH || m == CAN_PLACE_HORISONTAL || m == CAN_PLACE_VERTIKAL)
                    erg++;
            }
        
        
        
        return erg;
    }
    
    private List<Point> getPossibleShootPoints()
    {
        List<Point> erg = new ArrayList<>();
        for(int x = 0; x<battleMap.length; x++)
            for(int y = 0 ; y<battleMap[x].length; y++)
            {
                int m = battleMap[x][y];
                if(m == EMPTY)
                    erg.add(getPoint(x, y));
            }
        
        return erg;
    }
    
    public void autoplacement()
    {
        boolean ready;
        
        do
        {
            ready = true;
            
            resetMapsAndShips();
            
            for(Ship s: ships)
            {
                List<Point> pts = getPossiblePlacements(s.getLenght());
                
                if(pts.isEmpty())
                {
                    ready = false;
                    break;
                }
                
                Point p = pts.get((int)(Math.random()*pts.size()));
                
                boolean h,v;
                h = canBePlacedHorizontal(p.x, p.y, s.getLenght());
                v = canBePlacedVertical(p.x, p.y, s.getLenght());
                
                if(h && v)
                {
                    double r = Math.random();
                    if(r<.5)
                        putShipHorisontal(p.x, p.y, s);
                    else
                        putShipVertical(p.x, p.y, s);
                    
                }else if(h)
                {
                    putShipHorisontal(p.x, p.y, s);
                }
                else
                {
                    putShipVertical(p.x, p.y, s);
                }
                
                setBlocked(map, p.x, p.y);
                
            }
            
            
        }
        while(!ready);
        
        mapSet = true;
    }
    
    private List<Point> getPossiblePlacements(int length)
    {
        List<Point> erg = new ArrayList<>();
        
        for(int x = 0; x<map.length; x++)
            for(int y = 0 ; y<map[x].length; y++)
            {
                boolean h,v;
                h = canBePlacedHorizontal(x, y, length);
                v = canBePlacedVertical(x, y, length);
                
                if(h || v)
                    erg.add(getPoint(x, y));
            }
        
        return erg;
        
    }
    
    public ShootEnum hit(int x, int y)
    {
        mapSet = false;
        if(x<0 || x>=map.length || y<0 || y>=map[x].length)
            return ShootEnum.miss;
        ShootEnum se = ShootEnum.miss;
        int m = map[x][y];
        
        if(m>=10 && m<100)
        {
            for(Ship s: ships)
            {
                if(s.getId() == m)
                {
                    se = s.shoot();
                }
            }
            
            map[x][y] = m*10;
            
            if(se == ShootEnum.destroed && loose())
                return ShootEnum.loose;
            
            
            return se;
        }
        
        map[x][y] = SHOOT;
        
        return se;
    }
    
    private boolean loose()
    {
        for(Ship s: ships)
            if(s.getHp()>0)
                return false;
        
        return true;
    }
    
    public ShootEnum shoot(int x, int y)
    {
        mapSet = false;
        if(x<0 || x>=battleMap.length || y<0 || y>=battleMap[x].length)
            return ShootEnum.miss;
        
        if(battleMap[x][y] != EMPTY)
            return ShootEnum.wrongPlace;
        
        lastx = x;
        lasty = y;
        
        ShootEnum hit = enemy.hit(x, y);
        
        switch(hit)
        {
            case hit:
                battleMap[x][y]=999;
                break;
            case miss:
                battleMap[x][y]=SHOOT;
                break;
            case destroed:
                battleMap[x][y]=999;
                setBlocked(battleMap, x, y);
                break;
            case loose:
                battleMap[x][y]=999;
                break;
            default:
                throw new AssertionError(hit.name());
            
        }
        
        return hit;
    }
    
    public ShootEnum aiShoot()
    {
        
        List<Point> pp;
        
        if(lastHit != null)
        {
            
            pp = allPlacesToShootAI(getTargetPosition(lastHit.x, lastHit.y), lastHit.x, lastHit.y);
            
            
        }else
        {
            pp = allPlacesToShoot();
        }
        
        
        
        Point p = pp.get((int)(Math.random()*pp.size()));
        
        ShootEnum erg = shoot(p.x, p.y);
        
        if(erg == ShootEnum.destroed)
        {
            setBlocked(battleMap, p.x, p.y);
            lastHit = null;
        }
        
        if(erg == ShootEnum.hit)
            lastHit = p;
        
        return erg;
        
    }
    
    private List<Point> allPlacesToShoot()
    {
        List<Point> erg = new ArrayList();
        
        for(int x = 0; x<map.length; x++)
            for(int y = 0 ; y<map[x].length; y++)
            {
                if(battleMap[x][y]==EMPTY)
                    erg.add(getPoint(x, y));
            }
        
        return erg;
    }
    
    private int getInfo(int x, int y)
    {
        if(x<0 || x>= battleMap.length || y<0 || y>=battleMap[x].length)
            return -1;
        
        return battleMap[x][y];
    }
    
    private ShipPositionEnum getTargetPosition(int x, int y)
    {
        
        int xp, xm, yp, ym;
        xp = getInfo(x+1, y);
        xm = getInfo(x-1, y);
        yp = getInfo(x, y+1);
        ym = getInfo(x, y-1);
        
        if(xp >= 100 || xm >= 100)
            return ShipPositionEnum.horizontal;
        
        if(yp >= 100 || ym >= 100)
            return ShipPositionEnum.vertical;
        
        if(xp != EMPTY && xm != EMPTY)
            return ShipPositionEnum.vertical;
        
        if(yp != EMPTY && ym != EMPTY)
            return ShipPositionEnum.horizontal;
        
        return ShipPositionEnum.noIdea;
    }
    
    private List<Point> allPlacesToShootAI(ShipPositionEnum pos, int x, int y)
    {
        List<Point> erg = new ArrayList<>(10);
        int i;
        
        
        if(pos == ShipPositionEnum.horizontal)
        {
            i = 0;
            while(getInfo(x+i, y)>=100)
                i++;
            if(getInfo(x+i, y)==EMPTY)
                erg.add(getPoint(x+i, y));
            
            i = 0;
            while(getInfo(x+i, y)>=100)
                i--;
            if(getInfo(x+i, y)==EMPTY)
                erg.add(getPoint(x+i, y));
            
        }
        
        if(pos == ShipPositionEnum.vertical)
        {
            i = 0;
            while(getInfo(x, y+i)>=100)
                i++;
            if(getInfo(x, y+i)==EMPTY)
                erg.add(getPoint(x, y+i));
            
            i = 0;
            while(getInfo(x, y+i)>=100)
                i--;
            if(getInfo(x, y+i)==EMPTY)
                erg.add(getPoint(x, y+i));
            
        }
        
        if(pos == ShipPositionEnum.noIdea)
        {
            if(getInfo(x+1, y)== EMPTY)
                erg.add(getPoint(x+1, y));
            if(getInfo(x-1, y)== EMPTY)
                erg.add(getPoint(x-1, y));
            if(getInfo(x, y+1)== EMPTY)
                erg.add(getPoint(x, y+1));
            if(getInfo(x, y-1)== EMPTY)
                erg.add(getPoint(x, y-1));
        }
        
        return erg;
    }
    
    public void lookup()
    {
         for(int x = 0; x<battleMap.length; x++)
            for(int y = 0 ; y<battleMap[x].length; y++)
            {
                if(battleMap[x][y]==EMPTY)
                    battleMap[x][y]=enemy.map[x][y];
            }
    }
    
}
