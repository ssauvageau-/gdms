/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gdms.gdmodsuite;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author cenorayd
 */
public class HelpUI extends JPanel implements ActionListener {
    JTabbedPane tabbedPane;
    
    JPanel textTutorials;
    JPanel downloads;
    JPanel videos;
    
    JButton masterlist;
    JButton toolwindow;
    JButton blacksmiths;
    JButton worldEditor;
    JButton consoleCommands;
    JButton extraction;
    JButton pseditor;
    JButton colorCodes;
    JButton moddingGuide;
    JButton masteryGuideOfficial;
    JButton itemMaking;
    JButton beginningModding;
    JButton masteryVideo;
    
    public HelpUI() {
        super(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        masterlist = new JButton("Master List of Tutorials");
        masterlist.setEnabled(true);
        masterlist.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/tutorials-a-list-respawned/134129?u=ceno");
        });
        
        toolwindow = new JButton("Elfe's Modding Toolwindow - Alternate Modding Suite");
        toolwindow.setEnabled(true);
        toolwindow.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/tool-grim-dawn-toolwindow/31874");
        });
        
        blacksmiths = new JButton("Recipes and Blacksmiths");
        blacksmiths.setEnabled(true);
        blacksmiths.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/script-learning-recipes-blacksmith-integration/31904");
        });
        
        worldEditor = new JButton("Comprehensive World Editor Tutorial");
        worldEditor.setEnabled(true);
        worldEditor.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/script-learning-world-editor-tools-i/32634");
        });
        
        consoleCommands = new JButton("In-game Console Command Reference");
        consoleCommands.setEnabled(true);
        consoleCommands.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/console-commands-reference/32174");
        });
        
        extraction = new JButton("Using Archive Tool to Decompile arz/arc Files");
        extraction.setEnabled(true);
        extraction.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/tutorial-use-the-archive-tool-to-uncompile-arz-and-arc/32787");
        });
        
        pseditor = new JButton("Guide to Using the PSEditor");
        pseditor.setEnabled(true);
        pseditor.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/guide-the-pseditor/36014");
        });
        
        colorCodes = new JButton("List of Textual Color Codes");
        colorCodes.setEnabled(true);
        colorCodes.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/text-color-codes/34682/4");
        });
        
        moddingGuide = new JButton("Official Grim Dawn Modding Guide");
        moddingGuide.setEnabled(true);
        moddingGuide.addActionListener((ActionEvent e) -> {
            browse("https://www.grimdawn.com/downloads/Grim%20Dawn%20Modding%20Guide.pdf");
        });
        
        masteryGuideOfficial = new JButton("Official Example Mastery Environment");
        masteryGuideOfficial.setEnabled(true);
        masteryGuideOfficial.addActionListener((ActionEvent e) -> {
            browse("https://www.grimdawn.com/downloads/ModdingTutorial.zip");
        });
        
        itemMaking = new JButton("Tutorial on Making an Item");
        itemMaking.setEnabled(true);
        itemMaking.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/mod-tutorial-introduction-to-item-editing/32264");
        });
        
        beginningModding = new JButton("Beginner's Guide to Modding");
        beginningModding.setEnabled(true);
        beginningModding.addActionListener((ActionEvent e) -> {
            browse("https://forums.crateentertainment.com/t/script-basics-modding-beginners-guide-i/37525");
        });
        
        masteryVideo = new JButton("Making a Mastery");
        masteryVideo.setEnabled(true);
        masteryVideo.addActionListener((ActionEvent e) -> {
            browse("https://www.youtube.com/watch?v=7DklUbH6tQo");
        });
        
        textTutorials = new JPanel();
        textTutorials.setLayout(new GridLayout(3,3));
        textTutorials.add(masterlist);
        textTutorials.add(beginningModding);
        textTutorials.add(blacksmiths);
        textTutorials.add(worldEditor);
        textTutorials.add(consoleCommands);
        textTutorials.add(extraction);
        textTutorials.add(pseditor);
        textTutorials.add(colorCodes);
        textTutorials.add(itemMaking);
        
        
        downloads = new JPanel();
        downloads.setLayout(new GridLayout(3,2));
        downloads.add(toolwindow);
        downloads.add(moddingGuide);
        downloads.add(masteryGuideOfficial);
        
        
        videos = new JPanel();
        videos.setLayout(new GridLayout(1,3));
        videos.add(masteryVideo);
        
        tabbedPane.addTab("Textual Tutorials", Images.TUTORIALS.image, textTutorials, "Useful tutorials for modding purposes.");
        tabbedPane.addTab("Downloads", Images.DOWNLOADS.image, downloads, "Useful community-made files to download.");
        tabbedPane.addTab("Videos", Images.VIDEO.image, videos, "Community-made tutorial videos for modding.");
        
        this.add(new JPanel().add(tabbedPane));
    }
    
    public void browse(String dst)
    {
        try 
        {
            Desktop.getDesktop().browse(new URI(dst)); 
        }
        catch(URISyntaxException | IOException ex) {}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}