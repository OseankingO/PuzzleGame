/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlegamewithownpicture;

import java.awt.Image;

/**
 *
 * @author sean
 */
public class ImagePiece {
    private int ID;
    private int bitmap;
    private Image imagePiece;
    private int white = 0;
    
    public ImagePiece() {
        ID = 0;
        imagePiece = null;
    }
    
    public ImagePiece(int id, Image image) {
        ID = id;
        imagePiece = image;
    }
    
    public void setID(int id) {
        ID = id;
    }
    
    public void saveImage(Image image) {
        imagePiece = image;
    }
    
    public void setBitmap(int bm) {
        bitmap = bm;
    }
    
    public int readID() {
        return ID;
    }
    
    public Image readImage() {
        return imagePiece;
    }
    
    public int readBitmap() {
         return bitmap;
    }
    
    //white means this picture is "null" in the puzzle
    public int readWhite() {
        return white;
    }
    
    public void setWhite() {
        white = 1;
    }
    
    //according to bitmap to get the position of this image in array
    public int getJ(int rows, int cols) {
        return bitmap%rows;
    }
    
    public int getI(int rows, int cols) {
        int j = getJ(rows, cols);
        return (bitmap - j)/cols;
    }
}
