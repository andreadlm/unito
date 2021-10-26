<?php
  header("Content-Type: application/json; charset=UTF-8");

  $ret = array("base"=>$_GET["base"], "exponent"=>$_GET["exponent"], "result"=>pow($_GET["base"], $_GET["exponent"]));
  echo json_encode($ret);
?>