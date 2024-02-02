/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gdms.gdmodsuite;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author cenorayd
 */
public class GDModSuite {
    
    public static void main(String[] args) {
        Properties prop = new Properties();
        String fn = "gdms.cfg";
        try(FileInputStream fis = new FileInputStream(fn)) {
            prop.load(fis);
        } catch(FileNotFoundException ex){
            try {
                Files.write(
                        Paths.get(fn), 
                        Arrays.asList(
                                "gdms.name=Grim Dawn Modding Suite",
                                "gdms.version=0.9",
                                "install=",
                                "gdx1=",
                                "gdx2=",
                                "gdx3=",
                                "mod="), 
                        StandardCharsets.UTF_8
                );
            } catch (IOException ex1) {}
        } catch(IOException ex) {}
        
        SwingUtilities.invokeLater(() -> {
            UIManager.put("swing.boldMetal",Boolean.FALSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(GDModSuite.class.getName()).log(Level.SEVERE, null, ex);
            }
            createAndShowGUI(prop);
        });
    }
    
    private static void createAndShowGUI(Properties prop) {
        GDModSuiteUI msui = new GDModSuiteUI(prop);
        JFrame frame = new JFrame("Grim Dawn Modding Suite ~ Ceno ~ v0.9.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(msui);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	frame.setLocation(dim.width / 2 - frame.getWidth() / 2,
		dim.height / 2 - frame.getHeight() / 2);
        frame.getRootPane().addComponentListener(new ComponentAdapter(){});
        frame.setIconImage(Images.FRAME.image.getImage());
	SwingUtilities.updateComponentTreeUI(frame);
	frame.setVisible(true);
    }
}
