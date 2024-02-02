/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gdms.gdmodsuite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author cenorayd
 */
public class GDModSuiteUI extends JPanel implements ActionListener  {
    JTabbedPane tabbedPane;
    public GDModSuiteUI(Properties prop) {
        super(new BorderLayout());
        if(prop.getProperty("gdms.version") == null) {
            String fn = "gdms.cfg";
            try(FileInputStream fis = new FileInputStream(fn)) {
                prop.load(fis);
            } catch (FileNotFoundException ex) {
                /*
                    Shouldn't happen. 
                    GDModSuite should have created properties file by this point
                    if it did not exist before.
                */
            } catch (IOException ex) {
            }
        }
        tabbedPane = new JTabbedPane();
        
        JPanel suite = new JPanel();
        //Operational Tabs
        JPanel home = new JPanel();
        JPanel search = new JPanel();
        JPanel check = new JPanel();
        JPanel prune = new JPanel();
        //End Operational Tabs
        JPanel help = new JPanel();
        
        home.add(new HomeUI(prop, tabbedPane));
        search.add(new GDSearch(prop));
        check.add(new ModCheckUI(prop));
        prune.add(new ModPruneUI(prop));
        help.add(new HelpUI());
        tabbedPane.addTab("Home", Images.HOME.image, home, "Various tools and utilities not made by Ceno.");
        tabbedPane.addTab("GDSearch", Images.SEARCH.image, search, "Scans the game files for a particular phrase.");
        tabbedPane.addTab("GDModChecker", Images.CHECK.image, check, "Looks for invalid links in a mod.");
        tabbedPane.addTab("GDModPruner", Images.PRUNE.image, prune, "Prunes files in a mod that overlap with vanilla.");
        tabbedPane.addTab("Resources", Images.HELP.image, help, "Various modding-related resources for prospective modders.");
        
        suite.add(tabbedPane);
        this.add(suite);
        for(Component c : home.getComponents()) {
            if(c instanceof HomeUI homeUI) {
                homeUI.ready();
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
