<?php
// Create connection
$hostname = "localhost";
$dbUsername = "YOUR USERNAME";
$dbPassword = "YOUR PASSWORD";
$dbName = "minesweeperBattle";
$con=mysqli_connect($hostname,$dbUsername,$dbPassword,$dbName);
$result = "";
$message ="";
$opponent = "";
$challenger = "";
$seed = "";
// Check connection
if (mysqli_connect_errno())
  {
  $result = "Fail";
  $message = "".mysqli_connect_error();
  }
  
$opponent = $_POST['player2'];
//$opponent = stripslashes($opponent);
//$opponent = mysql_real_escape_string($opponent);

$challenger = $_POST['player1'];
//$challenger = stripslashes($challenger);
//$challenger = mysql_real_escape_string($challenger);

$seed = $_POST['seed'];

$sqlPlayerExists = "SELECT * FROM accounts WHERE Username = '". $opponent. "';";
$rs = mysqli_query($con, $sqlPlayerExists);
$data = mysqli_fetch_array($rs, MYSQLI_NUM);

if (count($data[0]) > 0) {
	$db = new mysqli($hostname,$dbUsername,$dbPassword,$dbName);
	$sql="INSERT INTO matches(Player1, Player2, Seed, Turn, CurrentPlayer)
	VALUES
	('".$challenger."','".$opponent."','".$seed."',0,'".$challenger."')";
	
	$db->query($sql);
	  
	$result = "Success";
	$message = $db->insert_id;
}else{
	$result = "Fail";
	$message= "Opponent does not exist";
}
echo $result.','.$message;
mysqli_close($con);
?>