/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlegamewithownpicture;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author sean
 */

public class Puzzle extends JButton{
    private int bitmap;
    private Image image;
    private int clickable = 0;
    
    public void setBitmap(int bm) {
        bitmap = bm;
    }
    
    public int readBitmap() {
        return bitmap;
    }
    
    public void changeImage(Image i) {
        image = i;
    }
    
    public void updateImage() {
        setIcon(new ImageIcon(image));
    }
    
    //clickable means this puzzle is nearby the null puzzle and can exchange image with it
    public void resetClickable() {
        clickable = 0;
    }
    
    public void onClickable() {
        clickable = 1;
    }
    
    public int readClickable() {
        return clickable;
    }
}   
