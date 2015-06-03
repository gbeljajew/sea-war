/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.classes;

import bin.enums.ShootEnum;

/**
 *
 * @author gbeljajew
 */
public class Ship 
{
    private final int lenght;
    private final int id;
    private int hp;

    public Ship(int lenght, int id) {
        this.lenght = lenght;
        this.id = id;
        this.hp = lenght;
    }

    public int getLenght() {
        return lenght;
    }

    public int getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }
    
    public ShootEnum shoot()
    {
        hp--;
        
        if(hp<=0) return ShootEnum.destroed;
        
        return ShootEnum.hit;
    }
    
    public void resetHP()
    {
        hp = lenght;
    }
    
    
}
