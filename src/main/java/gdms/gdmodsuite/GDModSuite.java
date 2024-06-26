/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gdms.gdmodsuite;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.io.File;
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
        String name = "Grim Dawn Modding Suite";
        String version = "0.9.5";
        File file = new File(fn);
        try(FileInputStream fis = new FileInputStream(fn)) {
            prop.load(fis);
            if(!prop.getProperty("gdms.version").equals(version)) {
                prop.setProperty("gdms.version", version);
                gdmsUtil.saveProperties(prop, file);
            }
        } catch(FileNotFoundException ex){
            try {
                Files.write(
                        Paths.get(fn), 
                        Arrays.asList(
                                "gdms.name="+name,
                                "gdms.version="+version,
                                "install=",
                                "gdx1=",
                                "gdx2=",
                                "gdx3=",
                                "mod="), 
                        StandardCharsets.UTF_8
                );
                prop.load(new FileInputStream(fn));
            } catch (IOException ex1) {}
        } catch(IOException ex) {}
        
        SwingUtilities.invokeLater(() -> {
            UIManager.put("swing.boldMetal",Boolean.FALSE);
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(GDModSuite.class.getName()).log(Level.SEVERE, null, ex);
            }
            createAndShowGUI(prop);
        });
    }
    
    private static void createAndShowGUI(Properties prop) {
        GDModSuiteUI msui = new GDModSuiteUI(prop);
        JFrame frame = new JFrame(prop.getProperty("gdms.name") + " ~ Ceno ~ v" + prop.getProperty("gdms.version"));
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
