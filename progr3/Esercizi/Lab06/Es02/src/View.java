import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class View extends JFrame implements Observer {
  private final JButton button;
  private final JLabel label;

  public View() {
    super("Proverbs:");

    JPanel panel = new JPanel();
    add(panel);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    button = new JButton("New proverb");
    panel.add(button);

    label = new JLabel();
    panel.add(label);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600,120);
  }

  @Override
  public void update(Observable o, Object arg) {
    if(o instanceof Model)
      label.setText(((Model) o).getCurrentProverb());
  }

  public void setController(Controller controller) {
    button.addActionListener(controller);
  }
}
