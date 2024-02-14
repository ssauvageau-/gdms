/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gdms.gdmodsuite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.SwingWorker;

/**
 *
 * @author cenorayd
 */
public class GDSearch extends javax.swing.JPanel implements Readyable {
    private javax.swing.JFileChooser fc;
    private String install_dir = "";
    private final String DBR = "\\database";
    private final String text = "\\resources\\text_en";
    private final String mods = "\\mods";
    private final String gdx1 = "\\gdx1";
    private final String gdx2 = "\\gdx2";
    private final String gdx3 = "\\gdx3";
    private final Properties prop;
    private final File file;
    private final String outdir = "search\\";

    /**
     * Creates new form GDSearch
     */
    public GDSearch(Properties pr) {
        this.prop = pr; //needs to be before initComponents.
        initComponents();
        this.recheckGDX();
        this.file = new File("gdms.cfg");
        String t_install = this.prop.getProperty("install");
        if(t_install != null && !t_install.equals("")) {
            this.install_dir = t_install;
            this.search.setEnabled(true);
        }
    }
    
    @Override
    public void ready() {
        this.recheckGDX();
        String t_install = this.prop.getProperty("install");
        if(t_install != null && !t_install.equals("")) {
            this.install_dir = t_install;
            this.search.setEnabled(true);
        }
    }
    
    private void recheckGDX() {
        boolean gdx1B = this.prop.getProperty("gdx1") != null && !this.prop.getProperty("gdx1").equals("0") && !this.prop.getProperty("gdx1").equals("");
        this.gdx1_cb.setSelected(gdx1B);
        this.gdx1_cb.setVisible(gdx1B);
        boolean gdx2B = this.prop.getProperty("gdx2") != null && !this.prop.getProperty("gdx2").equals("0") && !this.prop.getProperty("gdx2").equals("");
        this.gdx2_cb.setSelected(gdx2B);
        this.gdx2_cb.setVisible(gdx2B);
        boolean gdx3B = this.prop.getProperty("gdx3") != null && !this.prop.getProperty("gdx3").equals("0") && !this.prop.getProperty("gdx3").equals("");
        this.gdx3_cb.setSelected(gdx3B);
        this.gdx3_cb.setVisible(gdx3B);
    }
    
    private List<String> search(String pattern, String path, boolean dbr) throws IOException {
        List<String> ret = new ArrayList<>();
        if(dbr) {
            List<Path> res;
            try (Stream<Path> walk = Files.walk(Paths.get(path))) {
                res = walk
                        .filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().endsWith(".dbr"))
                        .collect(Collectors.toList());
            }
            for(Path p : res) {
                for(String line : Files.readAllLines(p))
                    if(line.contains(pattern))
                        ret.add(p.toString().replace(path, "") + "\n\t" + line);
            }
        }
        else {
            List<Path> res;
            try (Stream<Path> walk = Files.walk(Paths.get(path))) {
                res = walk
                        .filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().endsWith(".txt"))
                        .collect(Collectors.toList());
            }
            for(Path p : res) {
                for(String line : Files.readAllLines(p))
                    if(line.contains(pattern))
                        ret.add(line);
            }
        }
        return ret;
    }
    
    private void search_process() {
        String pattern = this.input.getText();
        if(pattern.equals(""))
            return;
        this.output.setText("Output:\n");
        this.output.updateUI();
        this.search.setEnabled(false);
        this.export.setEnabled(false);
        this.clear.setEnabled(false);
        try {
            if(this.dbr_cb.isSelected()) {
                if(this.vanilla_cb.isSelected()) {
                    this.output.append("Scanning Vanilla DBR Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.DBR, true))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
                if(this.gdx1_cb.isSelected()) {
                    this.output.append("Scanning GDX1 DBR Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.mods+this.gdx1+this.DBR, true))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
                if(this.gdx2_cb.isSelected()) {
                    this.output.append("Scanning GDX2 DBR Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.mods+this.gdx2+this.DBR, true))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
                if(this.gdx3_cb.isSelected()) {
                    this.output.append("Scanning GDX3 DBR Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.mods+this.gdx3+this.DBR, true))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
            }
            if(this.text_cb.isSelected()) {
                if(this.vanilla_cb.isSelected()) {
                    this.output.append("Scanning Vanilla Text Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.text, false))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
                if(this.gdx1_cb.isSelected()) {
                    this.output.append("Scanning GDX1 Text Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.mods+this.gdx1+this.text, false))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
                if(this.gdx2_cb.isSelected()) {
                    this.output.append("Scanning GDX2 Text Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.mods+this.gdx2+this.text, false))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
                if(this.gdx3_cb.isSelected()) {
                    this.output.append("Scanning GDX3 Text Entries\n");
                    this.output.updateUI();
                    for(String str : search(pattern, this.install_dir+this.mods+this.gdx3+this.text, false))
                        this.output.append(str.replaceFirst("\\\\", "").replaceAll("\\\\", "/") + "\n");
                }
            }
            this.output.append("Search Completed.");
            this.output.updateUI();
        }
        catch(IOException ex){}
        this.export.setEnabled(true);
        this.clear.setEnabled(true);
        this.search.setEnabled(true);
    }
    
    private void export_process() {
        if(this.input.getText().equals("")) {
            this.export.setEnabled(false);
            return;
        }
        String outputTXT = this.output.getText();
        File f = new File(outdir + new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()) + ".txt");
        StringBuilder sb = new StringBuilder().append("Searched for ").append(this.input.getText()).append(" in ");
        outputTXT = outputTXT.replace("Output:\n", "");
        outputTXT = outputTXT.replace("*Searching in " + this.install_dir + "\n", "");
        if(this.dbr_cb.isSelected()) {
            if(this.vanilla_cb.isSelected()) {
                sb.append("Vanilla Database Records, ");
                outputTXT = outputTXT.replace("Scanning Vanilla DBR Entries\n","");
            }
            if(this.gdx1_cb.isSelected()) {
                sb.append("GDX1 Database Records, ");
                outputTXT = outputTXT.replace("Scanning GDX1 DBR Entries\n","");
            }
            if(this.gdx2_cb.isSelected()) {
                sb.append("GDX2 Database Records, ");
                outputTXT = outputTXT.replace("Scanning GDX2 DBR Entries\n","");
            }
            if(this.gdx3_cb.isSelected()) {
                sb.append("GDX3 Database Records, ");
                outputTXT = outputTXT.replace("Scanning GDX3 DBR Entries\n","");
            }
        }
        if(this.text_cb.isSelected()) {
            if(this.vanilla_cb.isSelected()) {
                sb.append("Vanilla Text Files, ");
                outputTXT = outputTXT.replace("Scanning Vanilla Text Entries\n","");
            }
            if(this.gdx1_cb.isSelected()) {
                sb.append("GDX1 Text Files, ");
                outputTXT = outputTXT.replace("Scanning GDX1 Text Entries\n","");
            }
            if(this.gdx2_cb.isSelected()) {
                sb.append("GDX2 Text Files, ");
                outputTXT = outputTXT.replace("Scanning GDX2 Text Entries\n","");
            }
            if(this.gdx3_cb.isSelected()) {
                sb.append("GDX3 Text Files, ");
                outputTXT = outputTXT.replace("Scanning GDX3 Text Entries\n","");
            }
        }
        String param = sb.toString();
        param = this.replaceLast(param, ", ", ":\n");
        param = param + outputTXT;
        try {
            File directory = new File(this.outdir);
            if(!directory.exists())
                directory.mkdirs();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(param.getBytes());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GDSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GDSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.export.setEnabled(false);
    }
    
    private String replaceLast(String txt, String regex, String repl) {
        return txt.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", repl);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        output = new javax.swing.JTextArea();
        input = new javax.swing.JTextField();
        dir = new javax.swing.JButton();
        dbr_cb = new javax.swing.JCheckBox();
        text_cb = new javax.swing.JCheckBox();
        vanilla_cb = new javax.swing.JCheckBox();
        gdx1_cb = new javax.swing.JCheckBox();
        gdx2_cb = new javax.swing.JCheckBox();
        gdx3_cb = new javax.swing.JCheckBox();
        search = new javax.swing.JButton();
        export = new javax.swing.JButton();
        clear = new javax.swing.JButton();

        output.setEditable(false);
        output.setBackground(new java.awt.Color(255, 255, 255));
        output.setColumns(20);
        output.setRows(5);
        output.setText("Output:\n");
        output.setToolTipText("");
        jScrollPane1.setViewportView(output);

        input.setText("Query String");

        dir.setText("Install Directory");
        dir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dirActionPerformed(evt);
            }
        });

        dbr_cb.setSelected(true);
        dbr_cb.setText("Search DBR");

        text_cb.setText("Search Texts");

        vanilla_cb.setSelected(this.prop.getProperty("install") != null);
        vanilla_cb.setText("Search Vanilla");

        gdx1_cb.setSelected(true);
        gdx1_cb.setText("Search GDX1");

        gdx2_cb.setSelected(true);
        gdx2_cb.setText("Search GDX2");

        gdx3_cb.setSelected(true);
        gdx3_cb.setText("Search GDX3");

        search.setText("Search");
        search.setEnabled(false);
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        export.setText("Export");
        export.setEnabled(false);
        export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportActionPerformed(evt);
            }
        });

        clear.setText("Clear");
        clear.setEnabled(false);
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(input)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dir)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dbr_cb)
                                    .addComponent(text_cb)
                                    .addComponent(search)
                                    .addComponent(export)
                                    .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(gdx1_cb)
                                    .addComponent(vanilla_cb)
                                    .addComponent(gdx2_cb)
                                    .addComponent(gdx3_cb))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dbr_cb)
                            .addComponent(vanilla_cb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(text_cb)
                            .addComponent(gdx1_cb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gdx2_cb)
                            .addComponent(search))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gdx3_cb)
                            .addComponent(export))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clear)
                        .addGap(0, 118, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dirActionPerformed
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
            this.search.setEnabled(true);
            this.recheckGDX();
        }
    }//GEN-LAST:event_dirActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                search_process();
                return "";
            }
        };
        worker.execute();
    }//GEN-LAST:event_searchActionPerformed
    
    private void exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportActionPerformed
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                export_process();
                return "";
            }
        };
        worker.execute();
    }//GEN-LAST:event_exportActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        this.output.setText("Output:\n");
        this.output.updateUI();
        this.export.setEnabled(false);
    }//GEN-LAST:event_clearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clear;
    private javax.swing.JCheckBox dbr_cb;
    private javax.swing.JButton dir;
    private javax.swing.JButton export;
    private javax.swing.JCheckBox gdx1_cb;
    private javax.swing.JCheckBox gdx2_cb;
    private javax.swing.JCheckBox gdx3_cb;
    private javax.swing.JTextField input;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea output;
    private javax.swing.JButton search;
    private javax.swing.JCheckBox text_cb;
    private javax.swing.JCheckBox vanilla_cb;
    // End of variables declaration//GEN-END:variables
}
