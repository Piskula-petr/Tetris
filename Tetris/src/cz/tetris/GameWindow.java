package cz.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class GameWindow extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private final int HEIGHT = 800;
	private final int WIDTH = 400;
    
    private int blockWidth = HEIGHT / 20;	// Šířka bloku
    private int screenCenter = WIDTH / 2;   // Střed hracího pole
    private int shiftOfGameArea = 3;	// Posune hrací plochy
    private int stringHeight = HEIGHT / 20;	// Výška písma

    private Point[] currentBlock;	// Aktuální blok
    private Point[] shadowBlock;	// Dopad aktuálního bloku
    private Point[] nextBlock;		// Příští blok
    private Point[] assistantBlock;	// Pomocný blok při rotaci
    private Point centralPoint = new Point();	// Centrální bod bloku
    
    private int nextShape = 0;		// Další tvar bloku
    private int currentShape = 0;	// Aktuální tvar bloku
    private int rotation;		// Rotace bloku
    private boolean collision = false;	// Ověřuje kolizi při rotaci s jiným blokem
    private boolean inGame = true;		// Ověřuje zda hra běží
    private int score = 0;		// Skóre ve hře
    private int linesScore = 0;		// Skóre počtu smazaných řádků
    private boolean newHighScore = false;	// Ověřuje zda skóre na konci hry patří do Top 5
    
    private Timer timer;
    private Color color;
    private Font font;
    
    private List<Point> blocks = new ArrayList<>();		// List všech bloků
    private List<Color> colors = new ArrayList<>();		// List všech barev k blokům
    private List<Integer> data = new ArrayList<>();		// List dat ze souboru
    
// Konstruktor //////////////////////////////////////////////////////////////////////////////////////
    
    public GameWindow() {
    	
        setPreferredSize(new Dimension(WIDTH + (HEIGHT / 4), HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        font = new Font(Font.SANS_SERIF, Font.BOLD, stringHeight);
        
        // Namapování akcí
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "Left");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "Right");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Rotation");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownPressed");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownReleased");
        
        // Seznam akcí
        ActionMap actionMap = this.getActionMap();
        
        addKeyBinding(this, KeyEvent.VK_LEFT, "Left", (action) -> {
            moveLeft();
        });
        
        addKeyBinding(this, KeyEvent.VK_RIGHT, "Right", (action) -> {
            moveRight();
        });

        addKeyBinding(this, KeyEvent.VK_UP, "Rotation", (action) -> {
            
        	if (!collision) {
            	rotation();
            }
        });
        
        addKeyBinding(this, KeyEvent.VK_DOWN, "DownPressed", (action) -> {
            
        	if (inGame) {
            	timer.setInitialDelay(30);
            	timer.setDelay(30);
            	timer.restart();
            
        	} else timer.stop();
        });

        addKeyBinding(this, KeyEvent.VK_DOWN, "DownReleased", (action) -> {
            timer.setDelay(1000);
        });
        
        // Časová smyčka
        timer = new Timer(1000, new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                if (inGame){
                    moveDown();
                    score++;
                }
            }
         });
        timer.start();
        
        nextBlock();	// Vygeneruje příští blok,
        newBlock();		// předá nad ním kontrolu,
        nextBlock();	// a vygeneruje další blok
    }
    
    /**
     * 	Přidání akce ke stisknuté klávese
     * 
     * @param component - komponenta
     * @param keyCode - kód klávesy
     * @param id - id klávesy
     * @param actionListener - posluchač
     */
    public void addKeyBinding(JComponent component, int keyCode, String id, ActionListener actionListener){
        
    	ActionMap actionMap = component.getActionMap();
        actionMap.put(id, new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });
    }
    
    /**
     * 	Vytvoření aktuálního bloku
     */
    private void newBlock() { 
    	
        Block block = new Block();
        
        rotation = 0;
        collision = false;
        currentShape = nextShape;   // Předá tvar bloku
        
        switch (currentShape) {
        
            case 0:
            	
                // Block O
                currentBlock = block.getBlockO(rotation);
                shadowBlock = block.getBlockO(rotation);
                break;
                
            case 1:
            	
                // Block I
                currentBlock = block.getBlockI(rotation);
                shadowBlock = block.getBlockI(rotation);
                break;
                
            case 2:
            	
                // Block T
                currentBlock = block.getBlockT(rotation);
                shadowBlock = block.getBlockT(rotation);
                break;
                
            case 3:
            	
                // Block S
                currentBlock = block.getBlockS(rotation);
                shadowBlock = block.getBlockS(rotation);
                break;
                
            case 4:
            	
                // Block Z
                currentBlock = block.getBlockZ(rotation);
                shadowBlock = block.getBlockZ(rotation);
                break;
                
            case 5:
            	
                // Block L
                currentBlock = block.getBlockL(rotation);
                shadowBlock = block.getBlockL(rotation);
                break;
                
            case 6:
            	
                // Block J
                currentBlock = block.getBlockJ(rotation);
                shadowBlock = block.getBlockJ(rotation);
                break;
        }
        
        centralPoint.setLocation((screenCenter + shiftOfGameArea) - (2 * blockWidth), 0);
        
        for (int i = 0; i < 4; i++) {
        	
            // Nastavení souřadnice aktuálního bloku 
            currentBlock[i].x = currentBlock[i].x * blockWidth;
            currentBlock[i].y = currentBlock[i].y * blockWidth;
            
            // Zarovnání aktuálního bloku na střed
            currentBlock[i].x = currentBlock[i].x + (screenCenter + shiftOfGameArea) - (2 * blockWidth);
            
            // Nastavení souřadnice dopadu aktuálního bloku
            shadowBlock[i].x = (shadowBlock[i].x * blockWidth) + centralPoint.x;
            shadowBlock[i].y = (shadowBlock[i].y * blockWidth);
            
            // Přidání bloku a barvy do listu
            blocks.add(currentBlock[i]);
            colors.add(color);
        }
        
        setShadowOfCurrentBlock();  // Nastaví dopad aktuálního bloku
    }
    
    /**
     * 	Vygenerování příštího bloku
     */
    private void nextBlock(){
    	
        Random random = new Random();
        nextShape = random.nextInt(7);	// 0 - 7 varianty bloku
        
        Block block = new Block();
        
        switch (nextShape){
        
            case 0:
            	
                // Block O
                color = new Color(255, 204, 51); 	// Yellow
                nextBlock = block.getBlockO(rotation);
                break;
                
            case 1:
            	
                // Block I
                color = new Color(51, 153, 255); 	// Blue
                nextBlock = block.getBlockI(rotation);
                break;
                
            case 2:
            	
                // Block T
                color = new Color(102, 0, 153); 	// Purple
                nextBlock = block.getBlockT(rotation);
                break;
                
            case 3:
            	
                // Block S
                color = new Color(153, 102, 0); 	// Brown
                nextBlock = block.getBlockS(rotation);
                break;
                
            case 4:
            	
                // Block Z
                color = new Color(204, 0, 0); 	// Red
                nextBlock = block.getBlockZ(rotation);
                break;
                
            case 5:
            	
                // Block L
                color = new Color(0, 153, 0); 	// Green
                nextBlock = block.getBlockL(rotation);
                break;
                
            case 6:
            	
                // Block J
                color = new Color(255, 102, 0); 	// Orange
                nextBlock = block.getBlockJ(rotation);
                break;
        }
        
        // Umístění příštího bloku na boční panel
        for (int i = 0; i < 4; i++) {
        	
            nextBlock[i].x = (nextBlock[i].x * blockWidth) + (WIDTH + (stringHeight / 2));
            nextBlock[i].y = (nextBlock[i].y * blockWidth) + (8 * stringHeight);
        }
    }
    
    /**
     * 	Posun bloku o jeden díl doleva
     */
    private void moveLeft() {
    	
        boolean blockOut = false;
        
        // Kontrola kolizí
        for (int i = 0; i < 4; i++) {
        	
            if (currentBlock[i].x - blockWidth < 0) {
                blockOut = true;
            }

            for (int j = 0; j < blocks.size() - currentBlock.length; j++) {
            	
                if (currentBlock[i].x - blockWidth == blocks.get(j).x &&
                    currentBlock[i].y == blocks.get(j).y){
                	
                    blockOut = true;
                }
            }    
        }
        
        // Pokud není kolize provede posun
        if (!blockOut) {
        	
            for (int i = 0; i < 4; i++) {
            	
                currentBlock[i].x = currentBlock[i].x - blockWidth;
                shadowBlock[i].x = shadowBlock[i].x - blockWidth;
                shadowBlock[i].y = currentBlock[i].y;
            }
            centralPoint.x = centralPoint.x - blockWidth;
        }
        setShadowOfCurrentBlock();    // Nastaví dopad aktuálního bloku
        repaint();
    }
    
    /**
     * 	Posun bloku o jeden díl doprava
     */
    private void moveRight() {
    	
        boolean blockOut = false;
        
        // Kontrola kolizí
        for (int i = 0; i < 4; i++) {
        	
            if (currentBlock[i].x + blockWidth > (WIDTH - blockWidth) + shiftOfGameArea) {
                blockOut = true;
            }
        
            for (int j = 0; j < blocks.size() - currentBlock.length; j++) {
            	
                if (currentBlock[i].x + blockWidth == blocks.get(j).x &&
                    currentBlock[i].y == blocks.get(j).y){
                	
                    blockOut = true;
                }
            }    
        }
        
        // Pokud není kolize provede posun
        if (!blockOut) {
        	
            for (int i = 0; i < 4; i++) {
            	
                currentBlock[i].x = currentBlock[i].x + blockWidth;
                shadowBlock[i].x = shadowBlock[i].x + blockWidth;
                shadowBlock[i].y = currentBlock[i].y;
            }
            centralPoint.x = centralPoint.x + blockWidth;
        }
        setShadowOfCurrentBlock();  // Nastaví dopad aktuálního bloku
        repaint();
    }
    
    /**
     * 	Posun bloku o jeden díl dolů
     */
    private void moveDown(){
    	
        boolean blockOut = false;
        
        // Kontrola kolizí
        for (int i = 0; i < 4; i++) {
            
            if (currentBlock[i].y + blockWidth > (HEIGHT - blockWidth)) {
                blockOut = true;
            }
            
            for (int j = 0; j < blocks.size() - currentBlock.length; j++) {
            	
                if (currentBlock[i].x == blocks.get(j).x &&
                    currentBlock[i].y + blockWidth == blocks.get(j).y){
                    blockOut = true;
                }
            }
        }
        
        // Pokud není kolize provede posun
        if (!blockOut){
        	
            for (int i = 0; i < 4; i++) {
                
                currentBlock[i].y = currentBlock[i].y + blockWidth;
                shadowBlock[i].y = currentBlock[i].y;
            }
            centralPoint.y = centralPoint.y + blockWidth;
            
        } else {
            deleteRow();    // Po dopadu zkonroluje zda není plný řádek,
            isPlaying();    // ověří zda hra běží,
            newBlock();    	// vytvoří příští blok,
            nextBlock();  	// vytvoří nový hratelný blok
        }   
        setShadowOfCurrentBlock();   // Nastaví dopad aktuálního bloku
        repaint();
    }
    
    /**
     * 	Rotace bloku
     */
    private void rotation() {
    	
    	collision = false;
    	
    	// Kontrola rotace 0 - 3
    	if (rotation < 3) {
           	rotation++; 
           	
        } else rotation = 0;
        
        Block block = new Block();
        
        switch(currentShape) {
        
        	case 0:
        		
        		// Block O
        		assistantBlock = block.getBlockO(rotation);
        		break;
        		
        	case 1:
        		
        		// Block I
        		assistantBlock = block.getBlockI(rotation);
        		break;
        		
        	case 2:
        		
        		// Block T
        		assistantBlock = block.getBlockT(rotation);
        		break;
        		
        	case 3:
        		
        		// Block S
        		assistantBlock = block.getBlockS(rotation);
        		break;
        		
        	case 4:
        		
        		// Block Z
        		assistantBlock = block.getBlockZ(rotation);
        		break;
        		
        	case 5:
        		
        		// Block L
        		assistantBlock = block.getBlockL(rotation);
        		break;
        		
        	case 6:
        		
        		// Block J
        		assistantBlock = block.getBlockJ(rotation);
        		break;
        }
        
        // Nastaví souřadnice pomocnému bloku (nevykreslí)
        for (int i = 0; i < 4; i++) {
        	
        	assistantBlock[i].x = (assistantBlock[i].x * blockWidth) + centralPoint.x;
        	assistantBlock[i].y = (assistantBlock[i].y * blockWidth) + centralPoint.y;
        }
        
        fixBlockOut();	// Zkontroluje zda se pomocný blok nachází na hrací ploše
        
        // Kontrola kolizí s bloky okolo
        for (int i = 0; i < 4; i++) {
        	
        	for (int j = 0; j < blocks.size() - assistantBlock.length; j++) {
        		
                if (assistantBlock[i].x == blocks.get(j).x &&
                	assistantBlock[i].y == blocks.get(j).y){
                	
                    collision = true;
                }
            }
        }
        
        // Pokud nenastane kolize přidělý souřadnice aktuálnímu bloku a dopadu bloku
        if (!collision) {
        	
        	for (int i = 0; i < 4; i++) {
        		
                blocks.remove(currentBlock[i]);
                
                currentBlock[i].x = assistantBlock[i].x;
                currentBlock[i].y = assistantBlock[i].y;
                
                shadowBlock[i].x = currentBlock[i].x;
                shadowBlock[i].y = currentBlock[i].y;
                
                blocks.add(currentBlock[i]);
            }
        }
        
        setShadowOfCurrentBlock();	// Nastaví dopad aktuálního bloku
        repaint();
    }
    
    /**
     * 	Opravení pomocného bloku, pokud při rotaci opustí herní plochu
     */
    private void fixBlockOut() {
    	
        boolean leftOut = false;
        boolean rightOut = false;
        boolean downOut = false;
        
        // Kontrola kolizí
        for (int i = 0; i < 4; i++) {
        	
            if (assistantBlock[i].x < 0){
                leftOut = true;
            
            } else if (assistantBlock[i].x > (WIDTH - blockWidth) + shiftOfGameArea) {
                rightOut = true;
            
            } else if (assistantBlock[i].y > (HEIGHT - blockWidth)) {
                downOut = true;
            }
        }
        
        // Posune aktuální blok zpět na herní plochu
        if (leftOut) { 
        	
            for (int i = 0; i < 4; i++) {
            	assistantBlock[i].x = assistantBlock[i].x + blockWidth;
            }
            
            fixBlockOut();      // Rekurze
        
        } else if (rightOut) {
           
        	for (int i = 0; i < 4; i++) {
            	assistantBlock[i].x = assistantBlock[i].x - blockWidth;
            }
        	
            fixBlockOut();      // Rekurze
        
        } else if (downOut) {
           
        	for (int i = 0; i < 4; i++) {
            	assistantBlock[i].y = assistantBlock[i].y - blockWidth;
            }
        	
            fixBlockOut();      // Rekurze
        }
    }
    
    /**
     * 	Vymazání plného řádků
     */
    private void deleteRow() {
    	
        int column = 0;         // Aktuálně kontrolovaný řádek
        int columnCounter = 0; 	// Čítač bloků
        int rowCounter = 0;   	// Čítač řádků
        
        // Procházení herní plochy (po řádku)
        while (column != HEIGHT) {
        	
            for (int i = 0; i < blocks.size(); i++) {  
            	
                if (column == blocks.get(i).y) {
                    columnCounter++;
                }
            }
            
            // Pokud čítač dosáhne 10 (plný řádek) smaže jeho obsah 
            // a posune zbytek herní plochy o jeden díl dolů
            if (columnCounter >= 10 ) {
            	
                for (int i = 0; i < blocks.size(); i++) {
                	
                    if (blocks.get(i).y == column) {
                    	
                        blocks.remove(i);
                        colors.remove(i);
                        i--;
                        rowCounter++;
                    }
                }
                
                for (int i = 0; i < blocks.size(); i++) {
                	
                    if (blocks.get(i).y < column) {
                        blocks.get(i).y = blocks.get(i).y + blockWidth;
                    }
                }
            }
            columnCounter = 0;
            column = column + blockWidth;
        }
        
        // Zvýší skóre smazaných řádků
        if (rowCounter % 10 == 0 && inGame) {
            linesScore = linesScore + (rowCounter / 10);
        }
        
        // Přičte skóre podle počtu smazaných řádků
        if (rowCounter == 10 && inGame) {
            score = score + 100;
        
        } else if (rowCounter == 20 && inGame) {
            score = score + 400;
        
        } else if (rowCounter == 30 && inGame) {
            score = score + 900;
        
        } else if (rowCounter == 40 && inGame) {
            score = score + 2000;
        }
    }
    
    /**
     * 	Nastavení dopadu aktuálního bloku
     */
    public void setShadowOfCurrentBlock() {
    	
        boolean blockOut = false;
        
        // Kontrola kolizí
        for (int i = 0; i < 4; i++) {
        	
            if (shadowBlock[i].y + blockWidth > (HEIGHT - blockWidth) + shiftOfGameArea) {
                blockOut = true;
            }
            
            for (int j = 0; j < blocks.size() - shadowBlock.length; j++) {
            	
                if (shadowBlock[i].x == blocks.get(j).x &&
                    shadowBlock[i].y + blockWidth == blocks.get(j).y) {
                	
                    blockOut = true;
                }
            }
        }
        
        // Posune dopad aktuálního bloku
        if (!blockOut) {
        	
            for (int i = 0; i < 4; i++) {
                shadowBlock[i].y = shadowBlock[i].y + blockWidth;
            }
            setShadowOfCurrentBlock();  // Rekurze
        }
    }
    
    /**
     * 	Ověření zda hra běží
     * 	@return 
     */
    private boolean isPlaying() {
    	
        for (int i = 0; i < blocks.size(); i++) {
        	
            if (blocks.get(i).y < 2 * blockWidth) {
                return inGame = false;
            }
        }
        
        return inGame = true;
    }
    
    /**
     * 	Vykreslení herního plátna
     */
    @Override
    public void paintComponent(Graphics g) {
    	
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Hrací plocha
        if (inGame) {
        	
            for (int i = 0; i < colors.size(); i++) {
            	
                g.setColor(colors.get(i));
                g.fillRect(blocks.get(i).x, blocks.get(i).y, blockWidth - 3, blockWidth - 3);
            }
        
            for (int i = 0; i < shadowBlock.length; i++) {
                g.drawRect(shadowBlock[i].x, shadowBlock[i].y, blockWidth - 4, blockWidth - 4);
            }
            
        } else gameOver(g);

        // Boční panel
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawRect(1, 1, WIDTH, HEIGHT - shiftOfGameArea);
        
        g.drawString("Score", WIDTH + (stringHeight / 2), stringHeight);
        g.drawString(String.valueOf(score), WIDTH + (stringHeight / 2), 2 * stringHeight);
        
        g.drawString("Lines", WIDTH + (stringHeight / 2), 4 * stringHeight);
        g.drawString(String.valueOf(linesScore), WIDTH + (stringHeight / 2), 5 * stringHeight);
        
        g.drawString("Next", WIDTH + (stringHeight / 2), 7 * stringHeight);
        
        for (int i = 0; i < 4; i++) {
        	
            g.setColor(color);
            g.fillRect(nextBlock[i].x, nextBlock[i].y, blockWidth - 3, blockWidth - 3);
        }
        
        if (!data.isEmpty()) {
        	
        	g.setColor(Color.WHITE);
            g.drawString("High", WIDTH + (stringHeight / 2), 12*stringHeight);
            g.drawString("Score", WIDTH + (stringHeight / 2), 13*stringHeight);
        }
        loadScore(g);	// Načtení skóre ze souboru
    }
    
    /**
     * 	Výpis konce hry
     * 	@param g
     */
    private void gameOver(Graphics g) {
    	
    	saveScore();
    	
        String gameOver = "GAME OVER";
        String highScore = "NEW HIGH SCORE";
        FontMetrics fm = g.getFontMetrics(font);
        int gameOverWidth = fm.stringWidth(gameOver);
        int highScoreWidth = fm.stringWidth(highScore);
        
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(gameOver, (WIDTH - gameOverWidth) / 2, (HEIGHT - stringHeight) / 2);
        
        if (newHighScore) {
        	g.drawString(highScore, (WIDTH - highScoreWidth) / 2, (HEIGHT + stringHeight) / 2);
        }
    }
    
    /**
     * 	Přepsání skóre
     * 	@param g
     */
    private void loadScore(Graphics g) {
    	
    	String scoreString = "";
		int StringPossition = 15;
		
    	try(BufferedReader br = new BufferedReader(new FileReader("score.txt"))){
    		
    		while ((scoreString = br.readLine()) != null) {
    			
    			if (!data.toString().contains(scoreString)) {
    				
    				data.add(Integer.parseInt(scoreString));
    			}
    			
    			g.drawString(scoreString, WIDTH + (stringHeight / 2), StringPossition*stringHeight);
    			StringPossition++;
    		}
    		
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 	Uložení skóre
     */
    private void saveScore() {
    	
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter("score.txt"))) {
    		
			data.add(score);
			Collections.sort(data);
			Collections.reverse(data);
			
			if (data.indexOf(score) < 1) {
				newHighScore = true;
			}
			
			// Přepsání 5 nějvyšších skóre
			if (data.size() > 5) {
				
				for (int i = 0; i < 5; i++) {
					
					bw.write(data.get(i).toString());
					bw.newLine();
				}
			
			} else {
				
				for (int i = 0; i < data.size(); i++) {
					
					bw.write(data.get(i).toString());
					bw.newLine();
				}
			}
			bw.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
