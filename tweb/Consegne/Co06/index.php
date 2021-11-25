<?php include("top.html");?>

        <script src="bacon.js" type="text/javascript"></script>
    </head>
    <body>

        <?php include("banner.html");?>

            <!-- uncomment below if you want to add login/logout and session management -->
            <!--
            <p>Welcome <span id="username"></span>!</p>
            -->
            <h1>The One Degree of Kevin Bacon</h1>
            <p>Type in an actor's name to see if he/she was ever in a movie with Kevin Bacon!</p>
            <p><img id="kevinphoto" src="img/kevin_bacon.jpg" alt="Kevin Bacon"></p>

            <!-- ErrMsg: actor not in our database or actor with no films in our data base -->
            <div id="errMsg"></div>
            <!-- results: no error, results (all movies with Kevin Bacon) follow here -->
            <div id="results">
                <h2>Results for <span id="firstN"></span> <span id="lastN"></span></h2>
                <table id="list">
                    <caption>
                        All Films below
                    </caption>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Year</th>
                    </tr>

                </table>
            </div>              

<?php include("bottom.html"); 
    
?>
