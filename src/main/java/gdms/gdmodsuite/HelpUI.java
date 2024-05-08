/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gdms.gdmodsuite;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author smark
 */
public class HelpUI extends javax.swing.JPanel {

    /**
     * Creates new form HelpUI2
     */
    public HelpUI() {
        initComponents();
    }
    
    public void browse(String dst)
    {
        try 
        {
            Desktop.getDesktop().browse(new URI(dst)); 
        }
        catch(URISyntaxException | IOException ex) {}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tabbedPane = new javax.swing.JTabbedPane();
        textTutorials = new javax.swing.JPanel();
        masterList = new javax.swing.JButton();
        beginnersGuide = new javax.swing.JButton();
        blacksmiths = new javax.swing.JButton();
        worldEditor = new javax.swing.JButton();
        consoleCommands = new javax.swing.JButton();
        decompile = new javax.swing.JButton();
        pseditor = new javax.swing.JButton();
        colorCodes = new javax.swing.JButton();
        makingItem = new javax.swing.JButton();
        downloads = new javax.swing.JPanel();
        toolWindow = new javax.swing.JButton();
        moddingGuide = new javax.swing.JButton();
        masteryGuideOfficial = new javax.swing.JButton();
        videos = new javax.swing.JPanel();
        masteryVideo = new javax.swing.JButton();

        textTutorials.setLayout(new java.awt.GridBagLayout());

        masterList.setText("Master List of Tutorials");
        masterList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                masterListMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        textTutorials.add(masterList, gridBagConstraints);

        beginnersGuide.setText("Beginner's Guide to Modding");
        beginnersGuide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                beginnersGuideMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        textTutorials.add(beginnersGuide, gridBagConstraints);

        blacksmiths.setText("Recipes and Blacksmiths");
        blacksmiths.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                blacksmithsMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        textTutorials.add(blacksmiths, gridBagConstraints);

        worldEditor.setText("Comprehensive World Editor Tutorial");
        worldEditor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                worldEditorMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        textTutorials.add(worldEditor, gridBagConstraints);

        consoleCommands.setText("In-game Console Command Reference");
        consoleCommands.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                consoleCommandsMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        textTutorials.add(consoleCommands, gridBagConstraints);

        decompile.setText("Using Archive Tool to Decompile arz/arc Files");
        decompile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                decompileMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        textTutorials.add(decompile, gridBagConstraints);

        pseditor.setText("Guide to Using the PSEditor");
        pseditor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pseditorMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        textTutorials.add(pseditor, gridBagConstraints);

        colorCodes.setText("List of Text Color Codes");
        colorCodes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorCodesMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        textTutorials.add(colorCodes, gridBagConstraints);

        makingItem.setText("Tutorial on Making an Item");
        makingItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                makingItemMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        textTutorials.add(makingItem, gridBagConstraints);

        tabbedPane.addTab("Text Tutorials", new javax.swing.ImageIcon(getClass().getResource("/images/appbar.book.open.text.image.png")), textTutorials, "Useful tutorials for modding purposes."); // NOI18N

        downloads.setLayout(new java.awt.GridBagLayout());

        toolWindow.setText("Elfe's Modding Toolwindow - Alternate Modding Suite");
        toolWindow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toolWindowMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        downloads.add(toolWindow, gridBagConstraints);

        moddingGuide.setText("Official Grim Dawn Modding Guide");
        moddingGuide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moddingGuideMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        downloads.add(moddingGuide, gridBagConstraints);

        masteryGuideOfficial.setText("Official Example Mastery Environment");
        masteryGuideOfficial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                masteryGuideOfficialMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        downloads.add(masteryGuideOfficial, gridBagConstraints);

        tabbedPane.addTab("Downloads", new javax.swing.ImageIcon(getClass().getResource("/images/appbar.download.png")), downloads, "Useful community-made files to download."); // NOI18N

        masteryVideo.setText("Making a Mastery");
        masteryVideo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                masteryVideoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout videosLayout = new javax.swing.GroupLayout(videos);
        videos.setLayout(videosLayout);
        videosLayout.setHorizontalGroup(
            videosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(videosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(masteryVideo, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                .addContainerGap())
        );
        videosLayout.setVerticalGroup(
            videosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(videosLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(masteryVideo)
                .addContainerGap(197, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Videos", new javax.swing.ImageIcon(getClass().getResource("/images/appbar.youtube.play.png")), videos, "Community-made tutorial videos for modding."); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void masteryVideoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_masteryVideoMouseClicked
        browse("https://www.youtube.com/watch?v=7DklUbH6tQo");
    }//GEN-LAST:event_masteryVideoMouseClicked

    private void toolWindowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toolWindowMouseClicked
        browse("https://forums.crateentertainment.com/t/tool-grim-dawn-toolwindow/31874");
    }//GEN-LAST:event_toolWindowMouseClicked

    private void moddingGuideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moddingGuideMouseClicked
        browse("https://www.grimdawn.com/downloads/Grim%20Dawn%20Modding%20Guide.pdf");
    }//GEN-LAST:event_moddingGuideMouseClicked

    private void masteryGuideOfficialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_masteryGuideOfficialMouseClicked
        browse("https://www.grimdawn.com/downloads/ModdingTutorial.zip");
    }//GEN-LAST:event_masteryGuideOfficialMouseClicked

    private void masterListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_masterListMouseClicked
        browse("https://forums.crateentertainment.com/t/tutorials-a-list-respawned/134129?u=ceno");
    }//GEN-LAST:event_masterListMouseClicked

    private void beginnersGuideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_beginnersGuideMouseClicked
        browse("https://forums.crateentertainment.com/t/script-basics-modding-beginners-guide-i/37525");
    }//GEN-LAST:event_beginnersGuideMouseClicked

    private void colorCodesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorCodesMouseClicked
        browse("https://forums.crateentertainment.com/t/text-color-codes/34682/4");
    }//GEN-LAST:event_colorCodesMouseClicked

    private void pseditorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pseditorMouseClicked
        browse("https://forums.crateentertainment.com/t/guide-the-pseditor/36014");
    }//GEN-LAST:event_pseditorMouseClicked

    private void decompileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_decompileMouseClicked
        browse("https://forums.crateentertainment.com/t/tutorial-use-the-archive-tool-to-uncompile-arz-and-arc/32787");
    }//GEN-LAST:event_decompileMouseClicked

    private void makingItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_makingItemMouseClicked
        browse("https://forums.crateentertainment.com/t/mod-tutorial-introduction-to-item-editing/32264");
    }//GEN-LAST:event_makingItemMouseClicked

    private void consoleCommandsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_consoleCommandsMouseClicked
        browse("https://forums.crateentertainment.com/t/console-commands-reference/32174");
    }//GEN-LAST:event_consoleCommandsMouseClicked

    private void worldEditorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_worldEditorMouseClicked
        browse("https://forums.crateentertainment.com/t/script-learning-world-editor-tools-i/32634");
    }//GEN-LAST:event_worldEditorMouseClicked

    private void blacksmithsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blacksmithsMouseClicked
        browse("https://forums.crateentertainment.com/t/script-learning-recipes-blacksmith-integration/31904");
    }//GEN-LAST:event_blacksmithsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton beginnersGuide;
    private javax.swing.JButton blacksmiths;
    private javax.swing.JButton colorCodes;
    private javax.swing.JButton consoleCommands;
    private javax.swing.JButton decompile;
    private javax.swing.JPanel downloads;
    private javax.swing.JButton makingItem;
    private javax.swing.JButton masterList;
    private javax.swing.JButton masteryGuideOfficial;
    private javax.swing.JButton masteryVideo;
    private javax.swing.JButton moddingGuide;
    private javax.swing.JButton pseditor;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel textTutorials;
    private javax.swing.JButton toolWindow;
    private javax.swing.JPanel videos;
    private javax.swing.JButton worldEditor;
    // End of variables declaration//GEN-END:variables
}
