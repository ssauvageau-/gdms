/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package gdms.gdmodsuite;

import javax.swing.ImageIcon;

/**
 *
 * @author cenorayd
 */
public enum Images {
    ABOUT("/images/appbar.page.corner.bookmark.png"),
    CONFIG("/images/appbar.cog.png"),
    EXECUTION("/images/appbar.console.png"),
    FRAME("/images/frame_icon.png"),
    SEARCH("/images/appbar.magnify.browse.png"),
    CHECK("/images/appbar.list.gear.png"),
    PRUNE("/images/appbar.cell.row.delete.png"),
    HOME("/images/appbar.home.variant.png"),
    MERGE("/images/appbar.cell.merge.png"),
    HELP("/images/appbar.book.perspective.help.png"),
    TUTORIALS("/images/appbar.book.open.text.image.png"),
    DOWNLOADS("/images/appbar.download.png"),
    VIDEO("/images/appbar.youtube.play.png");
    
    final ImageIcon image;
    
    Images(String path) {
        ImageIcon img = new javax.swing.ImageIcon(getClass().getResource(path));
        if (img != null) {
            image = img;
        } else {
            System.err.println("Couldn't find file: " + path);
            image = null;
        }
    }
}
