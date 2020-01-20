/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlegamewithownpicture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

/**
 *
 * @author sean
 */
public class PuzzleGame extends javax.swing.JPanel {

    /**
     * Creates new form PuzzleGame
     */
    String inputImagePath;
    String whitePath = "/resources/images/white.jpg";
    private int pWidth = 360;
    private int pHeight = 360;
    private int rows;
    private int cols;
    private int whiteI;
    private int whiteJ;
    private int clicked = 0;
    private int steps = 0;
    private int win = 0;
    private int userChoice = 0;
    private BufferedImage image;
    private ImagePiece[][] imageArray;
    private ImagePiece[][] imagePlayArray;
    private Puzzle[] puzzle;
    private Play play;
    
    public PuzzleGame() {
        initComponents();
    }
    
    //read information from play frame
    public void startPuzzleGame(String path, int r, int c, int uc) {
        inputImagePath = path;
        rows = r;
        cols = c;
        userChoice = uc;
    }
    
    //create a window for puzzle game
    public void showWindow() {
        readImage();
        cutImage(image, rows, cols);
        removeFirstPiece();
        setRandom();
        setButton();
    }
    
    //when puzzle completed output win
    public int readWin() {
        return win;
    }
    
    //after window being shown, start to play game
    public void startPlayGame() {
        resetButton();
        onClickButton();
        movePuzzle();
    }
    
    public void readImage() {
        image = resizedImage(inputImagePath, userChoice);
    
    }

    private BufferedImage resizedImage(String inputImagePath, int userChoice) {
        // reads input image
        BufferedImage inputImage = null;
        
        //user choose use absolute path
        if (userChoice == 0) {
            try {
                inputImage = ImageIO.read(PuzzleGame.class.getResource(inputImagePath));
            } catch (IOException ex) {
                Logger.getLogger(PuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //initial choose use related path
        else {
            try {
                inputImage = ImageIO.read(new File(inputImagePath));
            } catch (IOException ex) {
                Logger.getLogger(GameSetting.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(pWidth, pHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, pWidth, pHeight, null);
        g2d.dispose();
        
        return outputImage;
    }
        
    //according to level, cut image into pieces and store into an array
    public void cutImage(BufferedImage BI, int rows, int cols) {
        imageArray = new ImagePiece[rows][cols];
        int width = pWidth/cols;
        int height = pHeight/rows;
        Image piece;
        int ID = 0;
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {            
                ImageFilter filter = new CropImageFilter(j*width, i*height, width, height);
                piece = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(BI.getSource(), filter));
                imageArray[i][j] = new ImagePiece(ID, piece);
                ID++;
            }
        }
    }
    
    //according to level, create buttons in array and set their infromation 
    public void setButton() {
        int width = pWidth/cols;
        int height = pHeight/rows;
        int ID = 0;
        puzzle=new Puzzle[rows*cols];
        
        //place button
        setLayout(new GridLayout(rows, cols));
        for(int i=0;i<rows;i++){
            for(int j = 0; j < cols; j++) {
                String name = Integer.toString(ID);
                puzzle[ID]=new Puzzle();
                puzzle[ID].setActionCommand(name);
                puzzle[ID].setBitmap(ID);
                puzzle[ID].setPreferredSize(new Dimension(width, height));
                puzzle[ID].changeImage(imagePlayArray[i][j].readImage());
                puzzle[ID].updateImage();
                puzzle[ID].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                imagePlayArray[i][j].setBitmap(puzzle[ID].readBitmap());
//                System.out.println(imagePlayArray[i][j].readID()+" "+imagePlayArray[i][j].readBitmap()+" "+imagePlayArray[i][j].readWhite()+" "+puzzle[ID].readBitmap());
                add(puzzle[ID]);
                ID++;
            }            
        }
    }
    
    public int readSteps() {
        return steps;
    }
    
    //random the image array
    public void setRandom() {
        Random gen = new Random(); 
        int n = rows*cols;  
        int[] result = new int[n];  
        Set<Integer> used = new HashSet<Integer>(); 
        //create a random int array according to level
        for (int i = 0; i < n; i++) {  
            int newRandom;  
            do {  
                newRandom = gen.nextInt(n);  
            } while (used.contains(newRandom)); 
            result[i] = newRandom;
            for(int j = 0; j < i; j++) {
                if(newRandom == result[j]) {
                    i -= 1;
                    break;
                }
            }
            used.add(newRandom);  
        }  
        
        //random the image array
        imagePlayArray = new ImagePiece[rows][cols];
        for(int ID = 0; ID < n; ID++) {
            int jj = ID % rows;
            int ii = (ID-jj)/cols;
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    if(imageArray[i][j].readID() == result[ID]) {
                        imagePlayArray[ii][jj] = new ImagePiece(result[ID], imageArray[i][j].readImage());
                        
                        if (imageArray[i][j].readWhite() == 1) {
                            imagePlayArray[ii][jj].setWhite();
                            whiteI = ii;
                            whiteJ = jj;
                        }
                    }
                }
            }
        }
    }
    
    //create a "null" picture for puzzle
    public void removeFirstPiece() {
        Image temp = null;
        try {
            temp = ImageIO.read(PuzzleGame.class.getResource(whitePath));
        } catch (IOException ex) {
            Logger.getLogger(PuzzleGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        imageArray[0][0].saveImage(temp);
        imageArray[0][0].setWhite();
    }
        
    //reset all puzzles' clickable to 0 
    public void resetButton() {
        for(int i = 0; i < rows*cols; i++) {
            puzzle[i].resetClickable();
        }
        clicked = 0;
    }
    
    //on click all the button near the 'null' puzzle
    public void onClickButton() {
        int I, J, location;
        location = imagePlayArray[whiteI][whiteJ].readBitmap();
        I = imagePlayArray[whiteI][whiteJ].getI(rows, cols);
        J = imagePlayArray[whiteI][whiteJ].getJ(rows, cols);
        if(I == 0 && J != 0 && J != cols-1) {
            puzzle[location-1].onClickable();
            puzzle[location+1].onClickable();
            puzzle[location+cols].onClickable();
        }
        else if(I == rows-1 && J != 0 && J != cols-1) {
            puzzle[location-1].onClickable();
            puzzle[location+1].onClickable();
            puzzle[location-cols].onClickable();
        }
        else if(J == 0 && I != 0 && I != rows-1) {
            puzzle[location+1].onClickable();
            puzzle[location-cols].onClickable();
            puzzle[location+cols].onClickable();
        }
        else if(J == cols-1 && I != 0 && I != rows-1) {
            puzzle[location-1].onClickable();
            puzzle[location-cols].onClickable();
            puzzle[location+cols].onClickable();
        }
        else if(I == 0 && J == 0) {
            puzzle[location+1].onClickable();
            puzzle[location+cols].onClickable();
        }
        else if(I == rows-1 && J == 0) {
            puzzle[location+1].onClickable();
            puzzle[location-cols].onClickable();
        }
        else if(I == 0 && J == cols-1) {
            puzzle[location-1].onClickable();
            puzzle[location+cols].onClickable();
        }
        else if(I == rows-1 && J == cols-1) {
            puzzle[location-1].onClickable();
            puzzle[location-cols].onClickable();
        }
        else {
            puzzle[location-1].onClickable();
            puzzle[location+1].onClickable();
            puzzle[location-cols].onClickable();
            puzzle[location+cols].onClickable();
        }
    }
    
    //when user click a clickable puzzle 'move' the puzzle to 'null' puzzle
    public void movePuzzle() {
        for(int i = 0; i < rows*cols; i++) {
            puzzle[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //find bitmap of 'null' puzzle and clicked puzzle
                    int whiteBitmap;
                    whiteBitmap = imagePlayArray[whiteI][whiteJ].readBitmap();
                    int clickedBitmap = Integer.parseInt(e.getActionCommand());
                    
                    //check if clicked puzzle is clickable
                    if(puzzle[clickedBitmap].readClickable() == 1) {
                        for(int I = 0; I < rows; I++) {
                            for(int J = 0; J < cols; J++) {
                                //exchange bitmap of images and image of puzzles
                                if (imagePlayArray[I][J].readBitmap() == clickedBitmap) {
                                    imagePlayArray[I][J].setBitmap(whiteBitmap);
                                    imagePlayArray[whiteI][whiteJ].setBitmap(clickedBitmap);
                                    puzzle[whiteBitmap].changeImage(imagePlayArray[I][J].readImage());
                                    puzzle[clickedBitmap].changeImage(imagePlayArray[whiteI][whiteJ].readImage());
                                    puzzle[whiteBitmap].updateImage();
                                    puzzle[clickedBitmap].updateImage(); 
                                    clicked = 1;
                                    break;
                                }
                            }
                            
                            //if a clickable puzzle has been clicked, rest and step count
                            if(clicked == 1) {
                                steps += 1;
                                resetButton();
                                onClickButton();
                                clicked = 0;
                                checkWin();
                                play.updateSteps(steps);
                                break;
                            }
                        }     
                    }    
                }
            });
        }
    }
    
    //check whether puzzle game be completed
    public void checkWin() {
        int match = 0;
        
        //if image's ID equal to it's bitmap, this image is at right place
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                if(imagePlayArray[i][j].readID() == imagePlayArray[i][j].readBitmap()) {
                    match += 1;
                }
                else {
                    break;
                }
            }
        }
        
        //if all images are at right place, the puzzle is completed
        if(match == rows*cols){
            win = 1; 
            End e = new End(win,steps);
            e.checkSteps();
            e.checkWin();
            this.setVisible(false);
            play.setVisible(false);
            e.setVisible(true);
        }
    }
    
    //to send information to play
    public void readPlay(Play p) {
        play = p;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(360, 360));
        setMinimumSize(new java.awt.Dimension(360, 360));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
