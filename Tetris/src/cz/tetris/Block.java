package cz.tetris;

import java.awt.Point;

public class Block {
	
	/**
	 * 	Block O
	 * 
	 * 	@param rotation - rotace bloku
	 * 	@return vrací souřadnice bloku
	 */
    public Point[] getBlockO(int rotation){
    	
        // Souřadnice pro všechny rotace
        Point[][] coordinatesAll = {
            {new Point(1, 0),   new Point(1, 1),    new Point(2, 0),    new Point(2, 1)},
            {new Point(1, 0),   new Point(1, 1),    new Point(2, 0),    new Point(2, 1)},
            {new Point(1, 0),   new Point(1, 1),    new Point(2, 0),    new Point(2, 1)},
            {new Point(1, 0),   new Point(1, 1),    new Point(2, 0),    new Point(2, 1)},
        };
        
        // Pole pro výstup
        Point [] coordinatesBlock = new Point[coordinatesAll[0].length];
        
        // Naplnění pole souřadnicemi konkrétní rotace
        for (int i = 0; i < coordinatesAll[0].length; i++){
             coordinatesBlock[i] = coordinatesAll[rotation][i];
        }
        
        return coordinatesBlock;
    }
    
    /**
	 * 	Block I
	 * 
	 * 	@param rotation - rotace bloku
	 * 	@return vrací souřadnice bloku
	 */
    public Point[] getBlockI(int rotation){
    	
        // Souřadnice pro všechny rotace
        Point[][] coordinatesAll = {
            {new Point(0, 0),   new Point(1, 0),    new Point(2, 0),    new Point(3, 0)},
            {new Point(1, 0),   new Point(1, 1),    new Point(1, 2),    new Point(1, 3)},
            {new Point(0, 0),   new Point(1, 0),    new Point(2, 0),    new Point(3, 0)},
            {new Point(1, 0),   new Point(1, 1),    new Point(1, 2),    new Point(1, 3)}
        };  
        
        // Pole pro výstup
        Point [] coordinatesBlock = new Point[coordinatesAll[0].length];
        
        // Naplnění pole souřadnicemi konkrétní rotace
        for (int i = 0; i < coordinatesAll[0].length; i++){
             coordinatesBlock[i] = coordinatesAll[rotation][i];
        }
        
        return coordinatesBlock;
    }
    
    /**
	 * 	Block T
	 * 
	 * 	@param rotation - rotace bloku
	 * 	@return vrací souřadnice bloku
	 */
    public Point[] getBlockT(int rotation){
    	
        // Souřadnice pro všechny rotace
        Point[][] coordinatesAll = {
            {new Point(0, 0),   new Point(1, 0),    new Point(2, 0),    new Point(1, 1)},
            {new Point(2, 0),   new Point(1, 1),    new Point(2, 1),    new Point(2, 2)},
            {new Point(0, 1),   new Point(1, 1),    new Point(2, 1),    new Point(1, 0)},
            {new Point(1, 0),   new Point(1, 1),    new Point(2, 1),    new Point(1, 2)}
        };
        
        // Pole pro výstup
        Point [] coordinatesBlock = new Point[coordinatesAll[0].length];
        
        // Naplnění pole souřadnicemi konkrétní rotace
        for (int i = 0; i < coordinatesAll[0].length; i++){
             coordinatesBlock[i] = coordinatesAll[rotation][i];
        }
        
        return coordinatesBlock;
    }
    
    /**
	 * 	Block S
	 * 
	 * 	@param rotation - rotace bloku
	 * 	@return vrací souřadnice bloku
	 */
    public Point[] getBlockS(int rotation){
    	
        // Souřadnice pro všechny rotace
        Point[][] coordinatesAll = {
            {new Point(1, 0),   new Point(2, 0),    new Point(0, 1),    new Point(1, 1)},
            {new Point(1, 0),   new Point(1, 1),    new Point(2, 1),    new Point(2, 2)},
            {new Point(1, 0),   new Point(2, 0),    new Point(0, 1),    new Point(1, 1)},
            {new Point(1, 0),   new Point(1, 1),    new Point(2, 1),    new Point(2, 2)}
        };
        
        // Pole pro výstup
        Point [] coordinatesBlock = new Point[coordinatesAll[0].length];
        
        // Naplnění pole souřadnicemi konkrétní rotace
        for (int i = 0; i < coordinatesAll[0].length; i++){
             coordinatesBlock[i] = coordinatesAll[rotation][i];
        }
        
        return coordinatesBlock;
    }
    
    /**
	 * 	Block Z
	 * 
	 * 	@param rotation - rotace bloku
	 * 	@return vrací souřadnice bloku
	 */
    public Point[] getBlockZ(int rotation){
    	
        // Souřadnice pro všechny rotace
        Point[][] coordinatesAll = {
            {new Point(0, 0),   new Point(1, 0),    new Point(1, 1),    new Point(2, 1)},
            {new Point(2, 0),   new Point(1, 1),    new Point(2, 1),    new Point(1, 2)},
            {new Point(0, 0),   new Point(1, 0),    new Point(1, 1),    new Point(2, 1)},
            {new Point(2, 0),   new Point(1, 1),    new Point(2, 1),    new Point(1, 2)}
        };
        
        // Pole pro výstup
        Point [] coordinatesBlock = new Point[coordinatesAll[0].length];
        
        // Naplnění pole souřadnicemi konkrétní rotace
        for (int i = 0; i < coordinatesAll[0].length; i++){
             coordinatesBlock[i] = coordinatesAll[rotation][i];
        }
        
        return coordinatesBlock;
    }
    
    /**
	 * 	Block L
	 * 
	 * 	@param rotation - rotace bloku
	 * 	@return vrací souřadnice bloku
	 */
    public Point[] getBlockL(int rotation){
    	
        // Souřadnice pro všechny rotace
        Point[][] coordinatesAll = {
            {new Point(0, 0),   new Point(1, 0),    new Point(2, 0),    new Point(0, 1)},
            {new Point(1, 0),   new Point(2, 0),    new Point(2, 1),    new Point(2, 2)},
            {new Point(0, 1),   new Point(1, 1),    new Point(2, 1),    new Point(2, 0)},
            {new Point(1, 0),   new Point(1, 1),    new Point(1, 2),    new Point(2, 2)}
        };
        
        // Pole pro výstup
        Point [] coordinatesBlock = new Point[coordinatesAll[0].length];
        
        // Naplnění pole souřadnicemi konkrétní rotace
        for (int i = 0; i < coordinatesAll[0].length; i++){
             coordinatesBlock[i] = coordinatesAll[rotation][i];
        }
        
        return coordinatesBlock;
    }
    
    /**
	 * 	Block J
	 * 
	 * 	@param rotation - rotace bloku
	 * 	@return vrací souřadnice bloku
	 */
    public Point[] getBlockJ(int rotation){
    	
        // Souřadnice pro všechny rotace
        Point[][] coordinatesAll = {
            {new Point(0, 0),   new Point(1, 0),    new Point(2, 0),    new Point(2, 1)},
            {new Point(2, 0),   new Point(2, 1),    new Point(2, 2),    new Point(1, 2)},
            {new Point(0, 1),   new Point(1, 1),    new Point(2, 1),    new Point(0, 0)},
            {new Point(1, 0),   new Point(1, 1),    new Point(1, 2),    new Point(2, 0)}
        };
        
        // Pole pro výstup
        Point [] coordinatesBlock = new Point[coordinatesAll[0].length];
        
        // Naplnění pole souřadnicemi konkrétní rotace
        for (int i = 0; i < coordinatesAll[0].length; i++){
             coordinatesBlock[i] = coordinatesAll[rotation][i];
        }
        
        return coordinatesBlock;
    }

}
