<?php include("top.html");?>

        <script src="bacon.js" type="text/javascript"></script>
    </head>
    <body>

        <?php include("banner.html");?>

            <!-- uncomment below if you want to add login/logout and session management -->
            <!--
            <p>Welcome <span id="username"></span>!</p>
            -->
            <h1>The One Degree of Massimo Boldi</h1>
            <p>Type in an actor's name to see if he/she was ever in a movie with Massimo Boldi!</p>
            <p><img id="massimophoto" src="img/massimo_boldi.jpg" alt="Massimo Boldi"></p>

            <!-- ErrMsg: actor not in our database or actor with no films in our data base -->
            <div id="errMsg"></div>
            <!-- results: no error, results (all movies with Kevin Bacon) follow here -->
            <div id="results">
            </div>

<?php include("bottom.html"); 
    
?>
