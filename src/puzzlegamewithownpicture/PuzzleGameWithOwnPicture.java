/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlegamewithownpicture;

/**
 *
 * @author sean
 */
public class PuzzleGameWithOwnPicture {

    /**
     * @param args the command line arguments
     */
    
    //open the main menu frame when begin the game
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(() -> {
            new MainMenu().setVisible(true);
        });
    }
    
}
