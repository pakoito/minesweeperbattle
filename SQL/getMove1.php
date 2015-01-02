<?php
// Create connection
$con=mysqli_connect("localhost","YOUR USER NAME","YOUR PASSWORD","minesweeperBattle");
$result1 ="";
$message ="";
$matchID = "";
$turn= "";
$playerName = "";

// Check connection
if (mysqli_connect_errno())
  {
  $message =  "Failed to connect to MySQL: " . mysqli_connect_error();
  }
$matchID = $_POST['matchID'];	
$turn = $_POST['turn'];
$playerName = $_POST['playerName'];
$result = mysqli_query($con,"SELECT * FROM playerturns WHERE User ='".$playerName."' AND matchID = '".$matchID."' AND Turn = '".$turn."';");
  
while($row = mysqli_fetch_array($result))
  {
  //echo $row['Data'];
  //echo $result.','.$message;
  $result1 = "Success";
  $message = $row['Data'];
  }
echo $result1.','.$message;
mysqli_close($con);
?>