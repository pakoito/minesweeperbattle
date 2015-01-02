<?php
// Create connection
$con=mysqli_connect("localhost","YOUR USER NAME","YOUR PASSWORD","minesweeperBattle");
$result ="";
$message ="";
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
  
$sql="INSERT INTO playerturns(matchID, User, Turn, Data)
VALUES
('$_POST[matchID]','$_POST[user]','$_POST[turn]','$_POST[data]')";

if (!mysqli_query($con,$sql))
  {
  die('Error: ' . mysqli_error($con));
  $result = "Fail";
  }
$result = "Success";

$changeOpponent = "UPDATE matches SET CurrentPlayer = '$_POST[opponent]' WHERE GameID = '$_POST[matchID]'";
mysqli_query($con, $changeOpponent);

//change turn if player 2
$check="SELECT * FROM matches WHERE GameID = '$_POST[matchID]' AND Player2 = '$_POST[user]'";
$rs = mysqli_query($con, $check);
$data = mysqli_fetch_array($rs);
if (count($data[0]) > 0) {
	$turn = $data['Turn'];
	$turn = $turn +1;
	$sql1 = "UPDATE matches SET Turn = '".$turn."' WHERE GameID = '$_POST[matchID]'";
	mysqli_query($con, $sql1);
}

echo $result.','.$message;
mysqli_close($con);
?>