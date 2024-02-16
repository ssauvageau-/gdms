/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gdms.gdmodsuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author cenorayd
 */
public class ModPruneUI extends javax.swing.JPanel implements Readyable {
    private final Properties prop;
    private final Path gdlist;
    private final File file;
    private final String outdir = "prune\\";
    private String install_dir = "";
    private String working_dir = "";
    private boolean gdx1, gdx2, gdx3;
    private javax.swing.JFileChooser fc;
    private ArrayList<String> stagingDBR;
    private ArrayList<String> stagingRES; 
    private HashMap<String, String> trimStage;
    private Set<String> master;
    
    /**
     * Creates new form ModPruneUI
     */
    public ModPruneUI(Properties pr) {
        this.prop = pr;
        this.gdlist = Paths.get("gddb.cfg");
        initComponents();
        this.file = new File("gdms.cfg");
        String t_install = this.prop.getProperty("install");
        if(t_install != null && !t_install.equals("")) {
            this.install_dir = t_install;
            this.pruneButton.setEnabled(true);
            this.installField.setText(install_dir);
        }
        String t_work = this.prop.getProperty("working");
        if (t_work != null && !t_work.equals("")) {
            this.working_dir = t_work;
        }
        this.recheckGDX();
    }
    
    @Override
    public void ready() {
        this.recheckGDX();
        String t_install = this.prop.getProperty("install");
        if(t_install != null && !t_install.equals("")) {
            this.install_dir = t_install;
            this.pruneButton.setEnabled(true);
            this.installField.setText(install_dir);
        }
        String t_work = this.prop.getProperty("working");
        if (t_work != null && !t_work.equals("")) {
            this.working_dir = t_work;
        }
    }
    
    private void recheckGDX() {
        this.gdx1 = this.prop.getProperty("gdx1") != null && !this.prop.getProperty("gdx1").equals("0") && !this.prop.getProperty("gdx1").equals("");
        this.gdx2 = this.prop.getProperty("gdx2") != null && !this.prop.getProperty("gdx2").equals("0") && !this.prop.getProperty("gdx2").equals("");
        this.gdx3 = this.prop.getProperty("gdx3") != null && !this.prop.getProperty("gdx3").equals("0") && !this.prop.getProperty("gdx3").equals("");
    }
    
    private void prune_process() {
        // Reset info
        this.stagingDBR = new ArrayList<>();
        this.stagingRES = new ArrayList<>();
        this.trimStage = new HashMap<>();
        this.outputText.setText("Output:\n");
        
        if(this.inputField.getText() == null || this.inputField.getText().equals("")) {
            javax.swing.JOptionPane.showMessageDialog(
            null,
            "Please enter your Mod's Name in the bottom field of the Config Tab.",
            "GDMS Version: " + this.prop.getProperty("gdms.version"),
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.prop.setProperty("mod", this.inputField.getText());
        gdmsUtil.saveProperties(this.prop, this.file);
        
        try {
            String modName = this.prop.getProperty("mod");
            
            /***
             * Vanilla DBR Reference List
             ***/
            this.outputText.append("Building reference list from Crate DB...\n");
            this.outputText.updateUI();
            this.master = Collections.synchronizedSet(new HashSet<>(Files.readAllLines(this.gdlist, java.nio.charset.StandardCharsets.UTF_8)));
            
            /***
             * Mod DBR/Resource Reference List
             ***/
            this.outputText.append("Processing " + modName + " Files...\n");
            this.outputText.updateUI();
            
            boolean useWork = (this.prop.getProperty("working") != null && !this.prop.getProperty("working").equals(""));
            ArrayList<String> modMaster = new ArrayList<>();
            String modDBR = (useWork ? this.working_dir : this.install_dir) + "\\mods\\" + modName + "\\database";
            String modRes = this.install_dir + "\\mods\\" + modName + "\\resources";
            if (this.prop.getProperty("install") != null && !this.prop.getProperty("install").equals("")) {                
                modMaster.addAll(Files.walk(Paths.get(modRes)).filter(Files::isRegularFile).map(Path::toString).map(s -> s.replace(modRes, "")).map(s -> s.replace("\\", "/")).map(s -> s.replaceFirst("/", "")).collect(Collectors.toList()));
                modMaster.addAll(Files.walk(Paths.get(modDBR)).filter(Files::isRegularFile).map(Path::toString).map(s -> s.replace(modDBR, "")).map(s -> s.replace("\\", "/")).map(s -> s.replaceFirst("/", "")).map(s->s.toLowerCase()).collect(Collectors.toList()));
            }
            else
                return;
            
            /***
             * Begin Pruning Process
             ***/
            this.outputText.append("Beginning " + modName + " Overlap Check...\n");
            this.outputText.updateUI();
            
            for(String modFN : modMaster) {
                if(this.master.contains(modFN)) {
                    if(modFN.endsWith(".dbr")) {
                        // All this garbage could be thrown in a method of its own. 
                        // Should do so to clean up code later.
                        File gdx3F = new File((useWork ? this.working_dir : this.install_dir) + "\\mods\\gdx3\\database\\" + modFN);
                        if(gdx3 && gdx3F.isFile()) {
                            String modTrim = this.trim(Files.readAllLines(Paths.get(modDBR + "\\" + modFN)));
                            if(modTrim.equals(this.trim(Files.readAllLines(Paths.get(gdx3F.getPath()))))) {
                                this.stagingDBR.add(modDBR + "\\" + modFN);
                                this.outputText.append("Found identical file\n\t" + modFN + "\nWithin GDX3 data. Staged for deletion.\n");
                                this.outputText.updateUI();
                            }
                            else { //file was changed compared to GD files and should be preserved, but consider trimming
                                if(this.trimCB.isSelected() && !this.autotrimCB.isSelected())
                                    this.trimStage.put(modFN, modTrim);
                                else if(this.trimCB.isSelected() && this.autotrimCB.isSelected()) { //in theory only need to check autotrimCB, but better safe than sorry
                                    String path = (useWork ? this.working_dir : this.install_dir) + "\\mods\\" + modName + "\\database\\" + modFN;
                                    FileOutputStream fos = new FileOutputStream(new File(path));
                                    fos.write(modTrim.getBytes());
                                }
                            }
                            continue; //do not waste time checking earlier content if we found a matching file in newer crate content
                        }
                        File gdx2F = new File((useWork ? this.working_dir : this.install_dir) + "\\mods\\gdx2\\database\\" + modFN);
                        if(gdx2 && gdx2F.isFile()) {
                            String modTrim = this.trim(Files.readAllLines(Paths.get(modDBR + "\\" + modFN)));
                            if(modTrim.equals(this.trim(Files.readAllLines(Paths.get(gdx2F.getPath()))))) {
                                this.stagingDBR.add(modDBR + "\\" + modFN);
                                this.outputText.append("Found identical file\n\t" + modFN + "\nWithin GDX2 data. Staged for deletion.\n");
                                this.outputText.updateUI();
                            }
                            else { //file was changed compared to GD files and should be preserved, but consider trimming
                                if(this.trimCB.isSelected() && !this.autotrimCB.isSelected())
                                    this.trimStage.put(modFN, modTrim);
                                else if(this.trimCB.isSelected() && this.autotrimCB.isSelected()) { //in theory only need to check autotrimCB, but better safe than sorry
                                    String path = (useWork ? this.working_dir : this.install_dir) + "\\mods\\" + modName + "\\database\\" + modFN;
                                    FileOutputStream fos = new FileOutputStream(new File(path));
                                    fos.write(modTrim.getBytes());
                                }
                            }
                            continue; //do not waste time checking earlier content if we found a matching file in newer crate content
                        }
                        File gdx1F = new File((useWork ? this.working_dir : this.install_dir) + "\\mods\\gdx1\\database\\" + modFN);
                        if(gdx1 && gdx1F.isFile()) {
                            String modTrim = this.trim(Files.readAllLines(Paths.get(modDBR + "\\" + modFN)));
                            if(modTrim.equals(this.trim(Files.readAllLines(Paths.get(gdx1F.getPath()))))) {
                                this.stagingDBR.add(modDBR + "\\" + modFN);
                                this.outputText.append("Found identical file\n\t" + modFN + "\nWithin GDX1 data. Staged for deletion.\n");
                                this.outputText.updateUI();
                            }
                            else { //file was changed compared to GD files and should be preserved, but consider trimming
                                if(this.trimCB.isSelected() && !this.autotrimCB.isSelected())
                                    this.trimStage.put(modFN, modTrim);
                                else if(this.trimCB.isSelected() && this.autotrimCB.isSelected()) { //in theory only need to check autotrimCB, but better safe than sorry
                                    String path = (useWork ? this.working_dir : this.install_dir) + "\\mods\\" + modName + "\\database\\" + modFN;
                                    FileOutputStream fos = new FileOutputStream(new File(path));
                                    fos.write(modTrim.getBytes());
                                }
                            }
                            continue; //do not waste time checking earlier content if we found a matching file in newer crate content
                        }
                        File gdv = new File((useWork ? this.working_dir : this.install_dir) + "\\database\\" + modFN);
                        if(gdv.isFile()) {
                            String modTrim = this.trim(Files.readAllLines(Paths.get(modDBR + "\\" + modFN)));
                            if(modTrim.equals(this.trim(Files.readAllLines(Paths.get(gdv.getPath()))))) {
                                this.stagingDBR.add(modDBR + "\\" + modFN);
                                this.outputText.append("Found identical file\n\t" + modFN + "\nWithin Vanilla GD data. Staged for deletion.\n");
                                this.outputText.updateUI();
                            }
                            else { //file was changed compared to GD files and should be preserved, but consider trimming
                                if(this.trimCB.isSelected() && !this.autotrimCB.isSelected())
                                    this.trimStage.put(modFN, modTrim);
                                else if(this.trimCB.isSelected() && this.autotrimCB.isSelected()) { //in theory only need to check autotrimCB, but better safe than sorry
                                    String path = (useWork ? this.working_dir : this.install_dir) + "\\mods\\" + modName + "\\database\\" + modFN;
                                    FileOutputStream fos = new FileOutputStream(new File(path));
                                    fos.write(modTrim.getBytes());
                                }
                            }
                        }
                    }
                    else if (!(modFN.endsWith(".arc") || modFN.endsWith(".arz") || modFN.endsWith(".dll"))) {
                        File gdx3F = new File((useWork ? this.working_dir : this.install_dir) + "\\mods\\gdx3\\resources\\" + modFN);
                        if(gdx3 && gdx3F.isFile() && 
                                hashEquality(gdx3F, new File(this.install_dir + "\\mods\\" + modName + "\\resources\\" + modFN))) {
                            this.stagingRES.add(modRes + "\\" + modFN);
                            this.outputText.append("Found Resource File with identical SHA-256 hash to a GDX3 file:\n\t" + modFN + "\nStaged for deletion.\n");
                            this.outputText.updateUI();
                        }
                        File gdx2F = new File((useWork ? this.working_dir : this.install_dir) + "\\mods\\gdx2\\resources\\" + modFN);
                        if(gdx2 && gdx2F.isFile() && 
                                hashEquality(gdx2F, new File(this.install_dir + "\\mods\\" + modName + "\\resources\\" + modFN))) {
                            this.stagingRES.add(modRes + "\\" + modFN);
                            this.outputText.append("Found Resource File with identical SHA-256 hash to a GDX2 file:\n\t" + modFN + "\nStaged for deletion.\n");
                            this.outputText.updateUI();
                        }
                        File gdx1F = new File((useWork ? this.working_dir : this.install_dir) + "\\mods\\gdx1\\resources\\" + modFN);
                        if(gdx1 && gdx1F.isFile() && 
                                hashEquality(gdx1F, new File(this.install_dir + "\\mods\\" + modName + "\\resources\\" + modFN))) {
                            this.stagingRES.add(modRes + "\\" + modFN);
                            this.outputText.append("Found Resource File with identical SHA-256 hash to a GDX1 file:\n\t" + modFN + "\nStaged for deletion.\n");
                            this.outputText.updateUI();
                        }
                        File gdvF = new File((useWork ? this.working_dir : this.install_dir) + "\\resources\\" + modFN);
                        if(gdvF.isFile() && 
                                hashEquality(gdvF, new File(this.install_dir + "\\mods\\" + modName + "\\resources\\" + modFN))) {
                            this.stagingRES.add(modRes + "\\" + modFN);
                            this.outputText.append("Found Resource File with identical SHA-256 hash to a Vanilla GD file:\n\t" + modFN + "\nStaged for deletion.\n");
                            this.outputText.updateUI();
                        }
                    }
                }
                else {
                    //duplicate file was not found, but consider trimming
                    if(this.trimCB.isSelected() && modFN.endsWith(".dbr")) {
                        String modTrim = this.trim(Files.readAllLines(Paths.get(modDBR + "\\" + modFN)));
                        if(!this.autotrimCB.isSelected())
                            this.trimStage.put(modFN, modTrim);
                        else {
                            String path = (useWork ? this.working_dir : this.install_dir) + "\\mods\\" + modName + "\\database\\" + modFN;
                            FileOutputStream fos = new FileOutputStream(new File(path));
                            fos.write(modTrim.getBytes());
                        }
                    }
                }
            }
            this.outputText.append("Finished Overlap Check.\n");
            this.outputText.updateUI();
        } catch (IOException ex) {System.err.println(ex);}
    }
    
    private boolean hashEquality(File f1, File f2) {
        try {
            return new BigInteger(
                    1, MessageDigest.getInstance("SHA-256").digest(
                            Files.readAllBytes(Paths.get(f1.getPath())))
            ).compareTo(new BigInteger(
                    1, MessageDigest.getInstance("SHA-256").digest(
                            Files.readAllBytes(Paths.get(f2.getPath()))))
            ) == 0;
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(ModPruneUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private String trim(List<String> input) {
        StringBuilder sb = new StringBuilder();
        boolean isST = false;
        for(String line : input) {
            if(line.contains("Class,SkillTree,"))
                isST = true; //Skilltrees shouldn't have their initial skill levels ("0", usually) removed - results in UI errors.
            String[] spl = line.split(",");
            if(spl.length == 1)
                continue;
            String arr1 = spl[spl.length - 1];
            if(null == arr1);
            else switch (arr1) {
                case "" -> {}
                case "0", "0.0", "0.000000" -> {
                    if(!isST);
                    else sb.append(line).append("\n");
                }
                case "Physical;Pierce;Elemental;Cold;Fire;Poison;Lightning;Life;Chaos;Aether;Stun" -> {}
                case "None;Quest;Boss;Custom;Regular" -> {}
                case "Box;Sphere;Cylinder;Capsule" -> {}
                case "R Hand;L Hand;Upper Body;Lower Body;Head;Forearm;Particle1;Particle2;Particle3;Target;SpecialHit01;SpecialHit02;SpecialHit03;SpecialHit04;" -> {}
                case "Melee;Short;Moderate;Long;Maximum;Boss;" -> {}
                case "Default;Point;Object;Target" -> {}
                case "None;Fire;Poison;" -> {}
                default -> sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
    
    private void export_process() {
        if(this.inputField.getText().equals("")) {
            this.exportButton.setEnabled(false);
            return;
        }
        String out = this.outputText.getText();
        out = out.replace("Output:\n", "");
        String modName = this.inputField.getText();
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
    
    private void delete_process() {
        if(this.stagingDBR == null || this.stagingRES == null || this.trimStage == null)
            return; //Can't happen? This button should only be pressable after prune_process() initializes the datastructures.
        int res = JOptionPane.showConfirmDialog(null, 
                "<html>Are you sure you want to delete staged files?<br>This process cannot be undone!</html>",
                "GDMS Version: " + this.prop.getProperty("gdms.version"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if(res != JOptionPane.YES_OPTION)
            return;
        this.stagingDBR.forEach(str -> {
            try {
                Files.deleteIfExists(Paths.get(str));
            } catch (IOException ex) {/*May not have access to file to delete it.*/}
        });
        this.stagingDBR.clear();
        this.stagingRES.forEach(str -> {
            try {
                Files.deleteIfExists(Paths.get(str));
            } catch (IOException ex) {/*May not have access to file to delete it.*/}
        });
        this.stagingRES.clear();
        //trim files as appropriate
        //trimStage will be empty (but non-null) if the checkbox was not checked pre-prune process
        String modName = this.prop.getProperty("mod");
        boolean useWork = (this.prop.getProperty("working") != null && !this.prop.getProperty("working").equals(""));
        this.trimStage.entrySet().forEach(entry -> {
            try {
                String path = (useWork ? this.working_dir : this.install_dir) + "\\mods\\" + modName + "\\database\\" + (String)entry.getKey();
                FileOutputStream fos = new FileOutputStream(new File(path));
                fos.write(((String)entry.getValue()).getBytes());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ModPruneUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ModPruneUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        JOptionPane.showMessageDialog(
                null,
                "Staged Files deleted successfully.",
                "GDMS Version: " + prop.getProperty("gdms.version"),
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                deleteButton.setEnabled(false);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        outputText = new javax.swing.JTextArea();
        pruneButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        trimCB = new javax.swing.JCheckBox();
        autotrimCB = new javax.swing.JCheckBox();
        configTab = new javax.swing.JPanel();
        installButton1 = new javax.swing.JButton();
        installField = new javax.swing.JTextField();
        inputField = new javax.swing.JTextField();
        aboutTab = new javax.swing.JPanel();
        aboutTextSP = new javax.swing.JScrollPane();
        aboutText = new javax.swing.JTextArea("Grim Dawn Mod Pruner, by Ceno\n\n"
            + "Searches for files in a mod that are equivalent to respective files "
            + "in vanilla Grim Dawn.  Requires both the mod's files and Grim Dawn's "
            + "files to be extracted.\n\nOnce a file has been found to overlap with vanilla, it will "
            + "be staged for deletion; however, deleting files is optional and the program will "
            + "confirm your choice to delete files should you choose to take that route.\n\n"
            + "Choosing to export the results of the search will instead create a text file inside "
            + "a special folder within your computer's default directory.", 11, 80);
        aboutText.setEditable(false);
        aboutText.setEnabled(true);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);

        tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        outputText.setEditable(false);
        outputText.setColumns(20);
        outputText.setRows(5);
        outputText.setText("Output:\n");
        jScrollPane1.setViewportView(outputText);

        pruneButton.setText("Determine Overlap");
        pruneButton.setEnabled(false);
        pruneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pruneButtonActionPerformed(evt);
            }
        });

        exportButton.setText("Export Overlap Data");
        exportButton.setEnabled(false);
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete Staged Files");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        trimCB.setText("Trim Unneeded Data Fields");
        trimCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trimCBActionPerformed(evt);
            }
        });

        autotrimCB.setText("<html>Without Hitting<br>Delete Staged Files<html>");
        autotrimCB.setVisible(false);

        javax.swing.GroupLayout executionTabLayout = new javax.swing.GroupLayout(executionTab);
        executionTab.setLayout(executionTabLayout);
        executionTabLayout.setHorizontalGroup(
            executionTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(executionTabLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(executionTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pruneButton)
                    .addComponent(exportButton)
                    .addComponent(deleteButton)
                    .addComponent(trimCB)
                    .addGroup(executionTabLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(autotrimCB)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        executionTabLayout.setVerticalGroup(
            executionTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(executionTabLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(pruneButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trimCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotrimCB)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Execution", new javax.swing.ImageIcon(getClass().getResource("/images/appbar.console.png")), executionTab); // NOI18N

        installButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        installButton1.setText("Set GD Install Directory");
        installButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installButton1ActionPerformed(evt);
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
                        .addComponent(installButton1)
                        .addGap(0, 226, Short.MAX_VALUE))
                    .addComponent(inputField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        configTabLayout.setVerticalGroup(
            configTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(installButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(aboutTextSP, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
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

    private void pruneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pruneButtonActionPerformed
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                prune_process();
                return "";
            }
            
            @Override
            protected void done() {
                if(stagingDBR.isEmpty() && stagingRES.isEmpty()) {
                    outputText.append(prop.getProperty("mod") + " has no overlap with Crate Entertainment files! :)\n");
                    outputText.updateUI();
                }
                exportButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        };
        worker.execute();
    }//GEN-LAST:event_pruneButtonActionPerformed

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

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                delete_process();
                return "";
            }
            
            @Override
            protected void done() {
                if(stagingDBR.isEmpty() && stagingRES.isEmpty()) {
                    deleteButton.setEnabled(false);
                    trimStage.clear();
                }
            }
        };
        worker.execute();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void trimCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trimCBActionPerformed
        if(this.trimCB.isSelected()) {
            this.autotrimCB.setVisible(true);
        }
        else {
            this.autotrimCB.setVisible(false);
            this.autotrimCB.setSelected(false);
        }
            
    }//GEN-LAST:event_trimCBActionPerformed

    private void installButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installButton1ActionPerformed
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
            gdmsUtil.saveProperties(this.prop, this.file);
            this.pruneButton.setEnabled(true);
            this.recheckGDX();
            this.installField.setText(install_dir);
        }
    }//GEN-LAST:event_installButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aboutTab;
    private javax.swing.JTextArea aboutText;
    private javax.swing.JScrollPane aboutTextSP;
    private javax.swing.JCheckBox autotrimCB;
    private javax.swing.JPanel configTab;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel executionTab;
    private javax.swing.JButton exportButton;
    private javax.swing.JTextField inputField;
    private javax.swing.JButton installButton1;
    private javax.swing.JTextField installField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputText;
    private javax.swing.JButton pruneButton;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JCheckBox trimCB;
    // End of variables declaration//GEN-END:variables
}
