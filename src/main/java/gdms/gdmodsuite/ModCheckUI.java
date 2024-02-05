/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gdms.gdmodsuite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import static java.nio.file.Files.newBufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.SwingWorker;

/**
 *
 * @author smark
 */
public class ModCheckUI extends javax.swing.JPanel implements Readyable {
    private javax.swing.JFileChooser fc;
    private String install_dir = "";
    private final Properties prop;
    private final File file;
    private final Path gdlist;
    private final String outdir = "check\\";
    private boolean gdx1, gdx2, gdx3;
    private Set<String> master;
    private Set<String> tags = Collections.synchronizedSet(new HashSet<>());
    
    /**
     * Creates new form ModCheckUI
     */
    public ModCheckUI(Properties pr) {
        this.prop = pr;
        this.gdlist = Paths.get("gddb.cfg");
        initComponents();
        this.file = new File("gdms.cfg");
        String t_install = this.prop.getProperty("install");
        if(t_install != null && !t_install.equals("")) {
            this.install_dir = t_install;
            this.verifyButton.setEnabled(true);
            this.installField.setText(install_dir);
        }
        this.recheckGDX();
    }
    
    @Override
    public void ready() {
        this.recheckGDX();
        String t_install = this.prop.getProperty("install");
        if(t_install != null && !t_install.equals("")) {
            this.install_dir = t_install;
            this.verifyButton.setEnabled(true);
            this.installField.setText(install_dir);
        }
    }
    
    private void recheckGDX() {
        this.gdx1 = this.prop.getProperty("gdx1") != null && !this.prop.getProperty("gdx1").equals("0") && !this.prop.getProperty("gdx1").equals("");
        this.gdx2 = this.prop.getProperty("gdx2") != null && !this.prop.getProperty("gdx2").equals("0") && !this.prop.getProperty("gdx2").equals("");
        this.gdx3 = this.prop.getProperty("gdx3") != null && !this.prop.getProperty("gdx3").equals("0") && !this.prop.getProperty("gdx3").equals("");
    }

    private void saveProperties(Properties p) {
        try {
            FileOutputStream fr = new FileOutputStream(this.file);
            p.store(fr, "");
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GDSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GDSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private synchronized void verify_process() {
        if(this.inputField.getText() == null || this.inputField.getText().equals("")) {
            javax.swing.JOptionPane.showMessageDialog(
            null,
            "Please enter your Mod's Name in the bottom field of the Config Tab.",
            "GDMS Version: " + this.prop.getProperty("gdms.version"),
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.prop.setProperty("mod", this.inputField.getText());
        saveProperties(this.prop);
        
        String modName = this.prop.getProperty("mod");
        
        if(!Files.exists(Paths.get(this.install_dir, "\\mods\\", modName))) {
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Could not find the mod given in the bottom\nfield of the Config Tab; please check your spelling!",
                "GDMS Version: " + this.prop.getProperty("gdms.version"),
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
        }
        
        this.outputText.setText("Output:\n");
            
        int malformed = 0; // dbr
        int malformedS = 0; // scripts (lua)
        int malformedQC = 0; // qst/cnv
        int brokenTags = 0;
        int baseSpeedAlterations = 0;
        
        boolean pruneVanilla = this.pruneCB.isSelected();
        
        try {
            /***
            Vanilla DBR Reference List
            ***/
            this.outputText.append("Building reference list from Crate DB...\n");
            this.outputText.updateUI();
            this.master = Collections.synchronizedSet(new HashSet<>(Files.readAllLines(this.gdlist, java.nio.charset.StandardCharsets.UTF_8)));
            
            /***
            Tag Data
            ***/
            this.outputText.append("Reading tag data...\n");
            this.outputText.updateUI();
            for(String str : master) {
                if(str.contains("text_en") && str.endsWith(".txt")) {
                    if(str.contains("gdx3")) 
                        tagBuilder("\\mods\\gdx3\\resources\\", str, this.tags);
                    else if(str.contains("gdx2")) 
                        tagBuilder("\\mods\\gdx2\\resources\\", str, this.tags);
                    else if(str.contains("gdx1")) 
                        tagBuilder("\\mods\\gdx1\\resources\\", str, this.tags);
                    else //vanilla
                        tagBuilder("\\resources\\", str, this.tags);
                }
            }
            
            ArrayList<String> modDBR = new ArrayList<>();
            ArrayList<String> modMaster = new ArrayList<>();
            Set<String> modTags = Collections.synchronizedSet(new HashSet<>());
            
            /***
            Mod DBR Reference List
            ***/
            this.outputText.append("Processing " + modName + " Files...\n");
            this.outputText.updateUI();
            if (this.prop.getProperty("install") != null && !this.prop.getProperty("install").equals("")) {
                String dbr = this.install_dir + "\\mods\\" + modName + "\\database";
                String res = this.install_dir + "\\mods\\" + modName + "\\resources";
                
                modMaster.addAll(Files.walk(Paths.get(res)).filter(Files::isRegularFile).map(Path::toString).map(s -> s.replace(res, "")).map(s -> s.replace("\\", "/")).map(s -> s.replaceFirst("/", "")).collect(Collectors.toList()));
                for(String str : modMaster) 
                    if(str.endsWith(".txt")) 
                        tagBuilder("\\mods\\" + modName + "\\resources\\", str, modTags);
                modDBR.addAll(Files.walk(Paths.get(dbr)).filter(Files::isRegularFile).map(Path::toString).map(s -> s.replace(dbr, "")).map(s -> s.replace("\\", "/")).map(s -> s.replaceFirst("/", "")).map(s->s.toLowerCase()).collect(Collectors.toList()));
                modMaster.addAll(modDBR);
            }
            
            /***
            Begin Pain
            ***/
            this.outputText.append("Beginning " + modName + " Integrity Check...\n");
            this.outputText.updateUI();
            for(String fn : modMaster) {
                boolean ignoreBaseSpeedChanges = false;
                if(fn.endsWith(".dbr")) { //just checking in case we hit a .arz or something
                    String path = this.install_dir + "\\mods\\" + modName + "\\database\\" + fn;
                    int lnum = 1;
                    for(String line : Files.readAllLines(Paths.get(path))) {
                        if (line.contains(".tpl")) {
                            if(line.contains("player.tpl") || line.contains("monster.tpl") ||
                                    line.contains("petplayerscaling.tpl") || line.contains("pet.tpl")
                                    || line.contains("npcmerchant.tpl") || line.contains("npccrafter.tpl")
                                    || line.contains("attributepak.tpl") || line.contains("npcskillreallocator.tpl")) {
                                ignoreBaseSpeedChanges = true;
                                continue;
                            }
                        }
                        String[] field = line.split(",|;");
                        String fieldName = field[0];
                        for (int index = 1; index < field.length; index++) {
                            String entry = field[index];
                            
                            //Malformed references:
                            String lc = entry.toLowerCase();
                            if(
                                    (lc.contains("/") && lc.contains(".")) &&
                                    !(lc.contains("charlevel") || lc.contains(".tpl") || lc.contains("playerlevel")) &&
                                    !this.master.contains(lc) &&
                                    !modMaster.contains(lc) &&
                                    !fieldName.equals("fileNameHistoryEntry")) {
                                boolean vanilla_issue = vanillaIssue(lc, path, modName);
                                if((pruneVanilla && !vanilla_issue) || (!pruneVanilla)) {
                                    malformed++;
                                    this.outputText.append("Malformed Reference in\n\t" + fn + "\n\t\tLine: " + lnum + "\n\t" + lc + "\n");
                                }
                            }
                            
                            //Missing tags:
                            else if(
                                    entry.contains("tag") &&
                                    !entry.contains(".") &&
                                    !this.tags.contains(entry) &&
                                    !modTags.contains(entry) &&
                                    !fieldName.equals("FileDescription")) {
                                boolean vanilla_issue = vanillaIssue(lc, path, modName);
                                if((pruneVanilla && !vanilla_issue) || (!pruneVanilla)) {
                                    brokenTags++;
                                    this.outputText.append("Broken Tag in\n\t" + fn + "\n\t\tLine; " + lnum + "\n\t" + entry + "\n");
                                }
                            }
                            
                            //Base Speed Alteration:
                            else if(
                                    !ignoreBaseSpeedChanges && 
                                    (fieldName.contains("characterAttackSpeed,") || 
                                    fieldName.contains("characterSpellCastSpeed,") || 
                                    fieldName.contains("characterRunSpeed,"))) {
                                if(Float.parseFloat(entry) != 0.0F) {
                                    boolean vanilla_issue = vanillaIssue(lc, path, modName);
                                    if((pruneVanilla && !vanilla_issue) || (!pruneVanilla)) {
                                        baseSpeedAlterations++;
                                        this.outputText.append("Base Speed Alteration in\n\t" + fn + "\n\t\tLine; " + lnum + "\n\t" + entry + "\n");
                                    }
                                }
                            }
                        }
                        lnum++;
                    }
                }
                else if(fn.endsWith(".lua")) {
                    String path = this.install_dir + "\\mods\\" + modName + "\\resources\\" + fn;
                    int lnum = 1;
                    for(String line : Files.readAllLines(Paths.get(path))) {
                        if(line.contains("\"")) {
                            Pattern p = Pattern.compile("\"([^\"]*)\""); //regex = match content within quotes
                            Matcher m = p.matcher(line);
                            while(m.find()) {
                                String datum = m.group(1).toLowerCase();
                                if((datum != null && datum.endsWith(".dbr")) && 
                                        !this.master.contains(datum) &&
                                        !modMaster.contains(datum)) {
                                    boolean vanilla_issue = vanillaIssue(datum, path, modName);
                                    if((pruneVanilla && !vanilla_issue) || (!pruneVanilla)) {
                                        malformedS++;
                                        this.outputText.append("Malformed Script Reference in\n\t" + fn + "\n\t\tLine: " + lnum + "\n\t" + datum + "\n");
                                    }
                                }
                            }
                        }
                        lnum++;
                    }
                }
                else if (fn.endsWith(".cnv") || fn.endsWith(".qst")) {
                    String path = this.install_dir + "\\mods\\" + modName + "\\resources\\" + fn;
                    for(String line : Files.readAllLines(Paths.get(path), StandardCharsets.ISO_8859_1)) {
                        line = line.toLowerCase();
                        Pattern p = Pattern.compile("[a-z0-9_-]+(\\/[a-z0-9_-]*)*\\.[a-z][a-z][a-z]"); //regex = match filepaths 
                        Matcher m = p.matcher(line);
                        while(m.find()) {
                            String datum = m.group(0);
                            if((datum != null && !datum.equals("") && datum.contains("/")) &&
                                    !this.master.contains(datum) &&
                                    !modMaster.contains(datum)) {
                                boolean vanilla_issue = vanillaIssue(datum, path, modName);
                                if((pruneVanilla && !vanilla_issue) || (!pruneVanilla)) {
                                    malformedQC++;
                                    this.outputText.append("Malformed QST/CNV Reference in\n\t" + fn + "\n\t-\n\t" + datum + "\n");
                                }
                            }
                        }
                    }
                }
            }
            
        } catch (IOException ex) {System.out.println(ex);}
        this.outputText.append(this.inputField.getText() + " checked successfully.\n");
        if(malformed > 0)
            this.outputText.append("\t" + malformed + " Malformed References\n");
        if(malformedS > 0)
            this.outputText.append("\t" + malformedS + " Malformed Script References\n");
        if(malformedQC > 0)
            this.outputText.append("\t" + malformedQC + " Malformed QST/CNV References\n");
        if(brokenTags > 0)
            this.outputText.append("\t" + brokenTags + " Broken Tags\n");
        if(baseSpeedAlterations > 0)
            this.outputText.append("\t" + baseSpeedAlterations + "Base Speed Alterations\n");
        this.exportButton.setEnabled(true);
    }
    
    private boolean vanillaIssue(String pattern, String path, String modName) {
        try { //Check expansions first
            return findInLines((Paths.get(path.replace("\\mods\\"+modName, "\\mods\\"+"gdx3"))), pattern);
        }
        catch(IOException ex) {/*could not find file*/}
        try {
            return findInLines((Paths.get(path.replace("\\mods\\"+modName, "\\mods\\"+"gdx2"))), pattern);
        }
        catch(IOException ex) {/*could not find file*/}
        try {
            return findInLines((Paths.get(path.replace("\\mods\\"+modName, "\\mods\\"+"gdx1"))), pattern);
        }
        catch(IOException ex) {/*could not find file*/}
        try {
            return findInLines((Paths.get(path.replace("\\mods\\"+modName, ""))), pattern);
        }
        catch(IOException ex) {/*could not find file*/}
        
        return false;
    }
    
    /**
     * Code adopted from Files.readAllLines
     */
    private boolean findInLines(Path path, String pattern) throws IOException {
        try (BufferedReader reader = newBufferedReader(path, java.nio.charset.StandardCharsets.ISO_8859_1)) {
            for (;;) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.contains(pattern))
                    return true;
            }
        }
        return false;
    }
    
    private synchronized void tagBuilder(String addedPath, String str, Set<String> set) throws IOException {
        for(String line : Files.readAllLines(Paths.get(this.install_dir, addedPath, str.replace("/","\\")))) {
            if (line.length() > 4 && line.contains("=")) {
                String[] tmp = line.split("=");
                set.add(tmp[0]);
            }
        }
    }
    
    private void export_process() {
        if(this.inputField.getText().equals("")) {
            this.exportButton.setEnabled(false);
            return;
        }
        String out = this.outputText.getText();
        String modName = this.inputField.getText();
        out = out.replace("Output:\n", "").
                replace("Building reference list from Crate DB...\n","").
                replace("Reading tag data...\n", "").
                replace("Processing " + modName + " Files...\n", "").
                replace("Beginning " + modName + " Integrity Check...\n", "");
        File f = new File(this.outdir + modName + " - " + new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()) + ".txt");
        try {
            File directory = new File(this.outdir);
            if(!directory.exists())
                directory.mkdirs();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(out.getBytes());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GDSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GDSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.exportButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        executionTab = new javax.swing.JPanel();
        verifyButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        pruneCB = new javax.swing.JCheckBox();
        outputSP = new javax.swing.JScrollPane();
        outputText = new javax.swing.JTextArea();
        configTab = new javax.swing.JPanel();
        installButton = new javax.swing.JButton();
        installField = new javax.swing.JTextField();
        inputField = new javax.swing.JTextField();
        aboutTab = new javax.swing.JPanel();
        aboutTextSP = new javax.swing.JScrollPane();
        aboutText = new javax.swing.JTextArea("Grim Dawn Mod Integrity Checker, by Ceno\n\n"
            + "Designed to verify database-record and resource references in mods. "
            + "If an invalid reference is used, this tool will report it. \n\n"
            + "How to use:\n\t"
            + "In the 'Config' tab, select 'Set Install Directy' and navigate to "
            + "Grim Dawn's steam installation directory.\n\t"
            + "In the textbox at the bottom of the 'Config' tab, enter the name "
            + "of the mod you intend to verify. This will be its folder name; "
            + "for instance, grimarillion or Grim Armory.\n\t"
            + "Both of these fields will persist between sessions, using configuration files "
            + "the tool makes in the folder it is executed from.\n\t"
            + "Then move to the 'Execution' tab, and select 'Verify Integrity'. "
            + "When the process finishes, you may export the results with the 'Export' "
            + "button, creating a .txt file in the aforementioned folder.", 11, 80);
        aboutText.setEditable(false);
        aboutText.setEnabled(true);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);

        setPreferredSize(new java.awt.Dimension(645, 320));

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setName("tabbedPane"); // NOI18N

        verifyButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        verifyButton.setText("Verify Integrity");
        verifyButton.setEnabled(false);
        verifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyButtonActionPerformed(evt);
            }
        });

        exportButton.setText("Export");
        exportButton.setEnabled(false);
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        pruneCB.setSelected(true);
        pruneCB.setText("Prune Vanilla Errors");

        outputText.setEditable(false);
        outputText.setColumns(20);
        outputText.setRows(5);
        outputText.setText("Output:\n");
        outputSP.setViewportView(outputText);

        javax.swing.GroupLayout executionTabLayout = new javax.swing.GroupLayout(executionTab);
        executionTab.setLayout(executionTabLayout);
        executionTabLayout.setHorizontalGroup(
            executionTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, executionTabLayout.createSequentialGroup()
                .addComponent(outputSP, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(executionTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pruneCB)
                    .addComponent(exportButton)
                    .addComponent(verifyButton))
                .addGap(26, 26, 26))
        );
        executionTabLayout.setVerticalGroup(
            executionTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(executionTabLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(verifyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pruneCB)
                .addContainerGap(104, Short.MAX_VALUE))
            .addComponent(outputSP)
        );

        tabbedPane.addTab("Execution", new javax.swing.ImageIcon(getClass().getResource("/images/appbar.console.png")), executionTab); // NOI18N

        installButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        installButton.setText("Set GD Install Directory");
        installButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installButtonActionPerformed(evt);
            }
        });

        installField.setEditable(false);
        installField.setText(this.install_dir);

        inputField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputField.setText(this.prop.getProperty("mod")
        );

        javax.swing.GroupLayout configTabLayout = new javax.swing.GroupLayout(configTab);
        configTab.setLayout(configTabLayout);
        configTabLayout.setHorizontalGroup(
            configTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configTabLayout.createSequentialGroup()
                .addGroup(configTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configTabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(installField))
                    .addGroup(configTabLayout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(installButton)
                        .addGap(0, 213, Short.MAX_VALUE))
                    .addComponent(inputField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        configTabLayout.setVerticalGroup(
            configTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(installButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(installField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("Config", new javax.swing.ImageIcon(getClass().getResource("/images/appbar.cog.png")), configTab); // NOI18N

        aboutText.setColumns(20);
        aboutText.setRows(5);
        aboutTextSP.setViewportView(aboutText);

        javax.swing.GroupLayout aboutTabLayout = new javax.swing.GroupLayout(aboutTab);
        aboutTab.setLayout(aboutTabLayout);
        aboutTabLayout.setHorizontalGroup(
            aboutTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aboutTextSP, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                .addContainerGap())
        );
        aboutTabLayout.setVerticalGroup(
            aboutTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aboutTextSP, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("About", new javax.swing.ImageIcon(getClass().getResource("/images/appbar.page.corner.bookmark.png")), aboutTab); // NOI18N

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

    private void installButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installButtonActionPerformed
        this.fc = new javax.swing.JFileChooser("Select Grim Dawn Installation Directory");
        this.fc.setFileSelectionMode(1);
        String t_install = this.prop.getProperty("install");
        if(t_install != null && !t_install.equals("")) {
            this.install_dir = t_install;
            this.fc.setCurrentDirectory(new File(this.install_dir));
        }
        if(this.fc.showOpenDialog(this) == 0) {
            this.install_dir = this.fc.getSelectedFile().getAbsolutePath();
            this.prop.setProperty("install", this.install_dir);
            this.saveProperties(this.prop);
            this.verifyButton.setEnabled(true);
            this.recheckGDX();
            this.installField.setText(install_dir);
        }
    }//GEN-LAST:event_installButtonActionPerformed

    private void verifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyButtonActionPerformed
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                verify_process();
                return "";
            }
        };
        worker.execute();
    }//GEN-LAST:event_verifyButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                export_process();
                return "";
            }
        };
        worker.execute();
    }//GEN-LAST:event_exportButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aboutTab;
    private javax.swing.JTextArea aboutText;
    private javax.swing.JScrollPane aboutTextSP;
    private javax.swing.JPanel configTab;
    private javax.swing.JPanel executionTab;
    private javax.swing.JButton exportButton;
    private javax.swing.JTextField inputField;
    private javax.swing.JButton installButton;
    private javax.swing.JTextField installField;
    private javax.swing.JScrollPane outputSP;
    private javax.swing.JTextArea outputText;
    private javax.swing.JCheckBox pruneCB;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JButton verifyButton;
    // End of variables declaration//GEN-END:variables
}
