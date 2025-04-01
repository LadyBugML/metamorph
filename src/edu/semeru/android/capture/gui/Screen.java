package edu.semeru.android.capture.gui;

import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class Screen extends JPanel {
    
    JButton nextBtn;
    JButton backBtn;

    public void setNextBtn(JButton btn) {
        this.nextBtn = btn;
    }

    public void setBackBtn(JButton btn) {
        this.backBtn = btn;
    }

    public Screen(LayoutManager layout) {
        super(layout);
    }
}
