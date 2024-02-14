/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gdms.gdmodsuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cenorayd
 */
public class gdmsUtil {    
    public static void saveProperties(Properties p, File file) {
        try (FileOutputStream fr = new FileOutputStream(file)) {
                p.store(fr, "");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(gdmsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(gdmsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
