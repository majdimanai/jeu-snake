import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Frame extends JFrame {
    public Frame() {
        Pannel pannel = new Pannel();
        this.add(pannel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        SwingUtilities.invokeLater(pannel::requestFocusInWindow);
    }
}
