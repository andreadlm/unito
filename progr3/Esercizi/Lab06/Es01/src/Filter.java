import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Filter extends Observable implements Observer{
  private List<Integer> list;

  public Filter() {
    list = new ArrayList<>();
  }

  @Override
  public void update(Observable o, Object arg) {
    if(arg instanceof Integer) {
      list.add((Integer)arg);
      if(list.size() % 2 == 0)
        setChanged();
        notifyObservers(list);
    }
  }
}
