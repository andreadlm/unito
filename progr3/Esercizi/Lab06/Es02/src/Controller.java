import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
  private Model model;

  public Controller(Model model) {
    this.model = model;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    model.newProverb();
  }
}
