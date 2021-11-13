import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
  private final Model model;
  private final View view;

  public Controller(Model model, View view) {
    this.model = model;
    this.view = view;

    this.view.setController(this);
    this.model.addObserver(this.view);

    view.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    model.newProverb();
  }
}
