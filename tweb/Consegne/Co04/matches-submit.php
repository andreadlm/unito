<?php
    // == Extracts info about registered people from the file-db ==
    include "top.html";

    // Checks whether two people are compatible
    function personality_compatibility($pers1, $pers2) {
        return $pers1[0] == $pers2[0] ||
               $pers1[1] == $pers2[1] ||
               $pers1[2] == $pers2[2] ||
               $pers1[3] == $pers2[3];
    }

    if($_SERVER['REQUEST_METHOD'] == 'GET') {
        $userName = $_GET['name'];

        $singlesRecords = file('./singles.txt');
        foreach($singlesRecords as $record) {
            $singleData = explode(',', $record);
            $singleDataAssoc = array('name' => $singleData[0],
                                     'sex' => $singleData[1],
                                     'age' => $singleData[2],
                                     'personality' => $singleData[3],
                                     'os' => $singleData[4],
                                     'min_age' => $singleData[5],
                                     'max_age' => $singleData[6]);
            if($singleDataAssoc['name'] == $userName)
                $userData = $singleDataAssoc;
            else
                $singles[] = $singleDataAssoc;
        }
?>

<p><strong>Matches for <?= $userData['name'] ?></strong></p><br>

<?php
        // == For each registered people, checks the compatibility with the user and prints compatible ones ==
        foreach($singles as $single) {
            if($single['sex'] != $userData['sex'] &&
               $single['os'] == $userData['os'] &&
               $single['age'] >= $userData['min_age'] &&
               $single['age'] <= $userData['max_age'] &&
               personality_compatibility($userData['personality'], $single['personality'])) {
?>
<div class="match">
    <img src="https://courses.cs.washington.edu/courses/cse190m/12sp/homework/4/user.jpg" alt="user-img">
    <p><?= $single['name'] ?></p>
    <ul>
        <li><strong>gender: </strong><?= $single['sex'] ?></li>
        <li><strong>age: </strong><?= $single['age'] ?></li>
        <li><strong>type: </strong><?= $single['personality'] ?></li>
        <li><strong>os: </strong><?= $single['os'] ?></li>
    </ul>
</div>
<?php
            }
        }
    }

    include "bottom.html";
?>
