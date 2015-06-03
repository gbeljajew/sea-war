/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.classes;

import java.awt.Point;
import java.util.Arrays;

/**
 *
 * @author gbeljajew
 */
public abstract class FleetConstants 
{
    public static final int EMPTY = 0;
    public static final int CAN_PLACE_VERTIKAL = 1;
    public static final int CAN_PLACE_HORISONTAL = 2;
    public static final int CAN_PLACE_BOTH = 3;
    public static final int BLCOKED = 4;
    public static final int SHOOT = 5;
    
    public static final int MAP_WIDE = 10;
    public static final int MAP_HIGH = 10;
    
    public static final int TILE_WIDE = 20;
    public static final int TILE_HIGH = 20;
    
    public static final int[][] shipNumber = {{4,1},{3,2},{2,3},{1,4}};
    
    private static final Point[][] points = new Point[MAP_WIDE][MAP_HIGH];
    
    static 
    {
        for(int x = 0; x<points.length; x++)
            for(int y = 0 ; y<points[x].length; y++)
                points[x][y]=new Point(x, y);
        
        
    }
    
    public static Point getPoint(int x, int y)
    {
        if(x<0 || x>=points.length || y<0 || y>= points[x].length)
            return null;
        
        return points[x][y];
    }
}
