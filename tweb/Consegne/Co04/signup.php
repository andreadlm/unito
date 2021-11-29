<?php include "top.html"; ?>

<form action="signup-submit.php" method="post">
    <fieldset>
        <legend>New User Signup:</legend>
        <label><input name="name" size="16"><strong>Name: </strong></label> <br>
        <strong>Gender: </strong>
        <label><input type="radio" name="gender" value="M"> M</label>
        <label><input type="radio" name="gender" value="F" checked="checked"> F</label> <br>
        <label><input name="age" size="6" maxlength="2"><strong>Age: </strong></label> <br>
        <label><input name="personality" size="6" maxlength="4"><strong>Personality type: </strong></label>
        (<a href="">Don't know your type?</a>) <br>
        <label><strong>Favorite OS:</strong>
        <select name="os">
            <option selected="selected">Linux</option>
            <option>Windows</option>
            <option>Os X</option>
        </select>
        </label> <br>
        <label>
            <strong>Seeking age: </strong>
            <input name="min_age" size="4" maxlength="2" placeholder="min"> to
            <input name="max_age" size="4" maxlength="2" placeholder="max">
        </label><br>
        <input type="submit" value="Sign Up">
    </fieldset>
</form>



<?php include "bottom.html"; ?>
