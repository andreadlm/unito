import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Filter extends Observable {
  private List<Integer> list;

  public Filter() {
    list = new ArrayList<>();
  }
  public List<Integer> getList() {
    return list;
  }

  public void add(Integer i) {
    if(i % 5 == 0) {
      list.add(i);
      setChanged();
      notifyObservers();
    }
  }
}
