<html>
  <body>
    <?php
      print "Twinkle, Twinkle little star";
    ?>
      <br>
    <?php
      $twinkle = "Twinkle";
      $star = "star";
      print "$twinkle, $twinkle little $star"
    ?>
    
    <br>

    <?php
      $x = 10;
      $y = 7;

      print "$x + $y = " . ($x + $y);
    ?>
    <br>
    <?php
      print "$x - $y = " . ($x - $y);
    ?>
    <br>
    <?php
      print "$x * $y = " . ($x * $y);
    ?>
    <br>
    <?php
      print "$x / $y = " . ($x / $y);
    ?>
    <br>
    <?php
      print "$x % $y = " . ($x % $y);
    ?>

    <br>

    <?php
      $x = 8;
    ?>
    Il valore adesso è <?php print $x ?><br>
    Somma 2. <?php $x += 2 ?>  Il valore adesso è <?php print $x ?>. <br>
    Sottrai 4. <?php $x -= 4 ?>  Il valore adesso è <?php print $x ?>. <br>
    Moltiplica per 5. <?php $x *= 5 ?> Il valore adesso è <?php print $x ?>.

    <br>

    <?php
      $around="around";
      print 'What goes ' . $around . ', comes ' . $around;
    ?>

    <br>

    <?php if(date('F', time()) == "August") { ?>
        E' agosto, e c'è davvero caldo
    <?php } else { ?>
        Non e' agosto, per lo meno non e' il periodo piu' caldo dell'anno
    <?php } ?>
    <br>

    <?php
      for($i = 1; $i <= 12; $i++)
        print "$i * $i = " . ($i * $i) . "<br>"
    ?>

    <table border="1">
    <?php for($i = 1; $i <= 7; $i++) { ?>
        <tr>
        <?php for($j = 1; $j <= 7; $j++) { ?>
          <td><?= $i * $j ?></td>
        <?php } ?>
        </tr>
    <?php } ?>
    </table>
  </body>
</html>