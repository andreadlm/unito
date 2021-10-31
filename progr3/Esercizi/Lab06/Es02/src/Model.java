import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class Model extends Observable {
  private final List<String> proverbs;
  private int currProverbIdx;
  private Random randomGen;

  public Model() {
    proverbs = new ArrayList<>();
    proverbs.add("A basta nen avèj ëd sòld, a venta dcò savèj-je spende");
    proverbs.add("Aria ëd filura, aria ëd sepoltura");
    proverbs.add("Për fesse n'amis a basta 'n bicer ëd vin, për goernelo a basta nen un botal");

    currProverbIdx = 0;

    randomGen =  new Random(System.currentTimeMillis());
  }

  public void newProverb() {
    currProverbIdx = Math.abs(randomGen.nextInt()) % proverbs.size();
    setChanged();
    notifyObservers();
  }

  public String getCurrentProverb() {
    return proverbs.get(currProverbIdx);
  }
}
