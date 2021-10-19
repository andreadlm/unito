<?php
    $movieName = $_GET["film"];

    $movieInfo = file("./".$movieName."/info.txt");
    list($name, $year, $score) = $movieInfo;
?>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>TMNT - Rancid Tomatoes</title>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="movie.css" type="text/css" rel="stylesheet">
		<link rel="icon" href="httpss://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/rotten.gif">
	</head>

	<body>
		<div id="header">
			<img src="https://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/banner.png" alt="Rancid Tomatoes">
		</div>

		<h1><?= strtoupper(trim($movieName))." (".trim($year).")" ?></h1>

		<div id="content">
			<div id="side" class="bg-green">
				<div>
					<img src="<?= "./".$movieName."/overview.png" ?>" alt="general overview">
				</div>

				<dl>
                    <?php
                        $movieOverview = file("./".$movieName."/overview.txt");
                        foreach($movieOverview as &$line) {
                            $key = substr($line, 0, strpos($line, ":"));
                            $line = substr($line, strpos($line, ":") + 1);
                            $movieOverviewList[$key] = $line;
                        }

                        foreach($movieOverviewList as $key => $value) {
                    ?>
                    <dt><?= $key ?></dt>
                    <?php
                            if($key == "STARRING") {
                                $actors = explode(",", $value);
                                foreach ($actors as $actor) {
                    ?>
                    <dd><?= trim($actor) ?></dd>
                    <?php
                                }
                            } else {
                    ?>
                    <dd><?= trim($value) ?></dd>
                    <?php
                            }
                        }
                    ?>
				</dl>
			</div>

			<div id="article">
				<div id="score">
                    <?php
                        if($score > 60)
                            print "<img alt = \"fresh\" src=\"https://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/freshbig.png\">";
                        else
                            print "<img alt = \"rotten\" src=\"https://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/rottenbig.png\">";
                        print $score . "%";
                    ?>
				</div>

                <?php
                    $reviewFilesNames = glob("./$movieName/review*.txt");

                    if(count($reviewFilesNames) <= 10) $displayedReviewNum = count($reviewFilesNames);
                    else $displayedReviewNum = 10;

                    for($count = 1; $count <= $displayedReviewNum && $count <= 10; $count++) {
                        $reviewFileName = $reviewFilesNames[$count - 1];

                        if($count == 1) {
                ?>
                <div id="article-left">
                <?php
                        } else if($count == round($displayedReviewNum / 2) + 1) {
                ?>
                <div id="article-right">
                <?php
                        }

                        $file = file($reviewFileName);
                ?>
                    <p class="review">
                <?php
                        if(trim($file[1]) == "FRESH") {
                ?>
                        <img src="https://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/fresh.gif" alt="Fresh">
                <?php
                        } else {
                ?>
                        <img src="https://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/rotten.gif" alt="Rotten">
                <?php
                        }
                ?>
                        <q><?= trim($file[0]) ?></q>
                    </p>
                    <p class="reviewer">
                        <img src="https://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/critic.gif" alt="Critic">
                        <?= trim($file[2]) ?>
                        <span class="publication"><?= trim($file[3]) ?></span>
                    </p>
                <?php
                        if($count == $displayedReviewNum || $count == round($displayedReviewNum / 2)) {
                ?>
                </div>
                <?php
                        }
                    }
                ?>
		</div>
        <p id="content-footer" class="bg-green">(1-<?= $displayedReviewNum ?>) of <?= count($reviewFilesNames) ?></p>

		<div id="validators">
			<p>
				<a href="https://validator.w3.org/check/referer"><img width="88" src="https://upload.wikimedia.org/wikipedia/commons/b/bb/W3C_HTML5_certified.png" alt="Valid HTML5!"></a>
			<p> <br>
			<a href="https://jigsaw.w3.org/css-validator/check/referer"><img src="https://jigsaw.w3.org/css-validator/images/vcss" alt="Valid CSS!"></a>
		</div>
	</body>
</html>
