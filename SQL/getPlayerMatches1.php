<?php
// Create connection
$con=mysqli_connect("localhost","YOUR USER NAME","YOUR PASSWORD","minesweeperBattle");
$result = "";
$message ="";
$challenger = "";
// Check connection
if (mysqli_connect_errno())
  {
  $result = "Fail";
  $message = "".mysqli_connect_error();
  }
  
$challenger = $_POST['player1'];
//$challenger = stripslashes($challenger);
//$challenger = mysql_real_escape_string($challenger);

$sqlPlayerExists = "SELECT * FROM matches WHERE Player1 = '".$challenger. "' OR Player2 = '".$challenger. "';";
$rs = mysqli_query($con, $sqlPlayerExists);

$result = "Success";

while ($row = mysqli_fetch_array($rs, MYSQLI_NUM)){
	$message = $message."|"."-".$row[0]."-".$row[1]."-".$row[2]."-".$row[3]."-".$row[4]."-".$row[5];
}
	
echo $result.','.$message;
//echo json_encode($data);
mysqli_close($con);
?>