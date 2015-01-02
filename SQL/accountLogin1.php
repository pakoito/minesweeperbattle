<?php
// Create connection
$con=mysqli_connect("localhost","YOUR USER NAME","YOUR PASSWORD","minesweeperBattle");
$result = "";
$message ="";
$username="";
$password="";
// Check connection
if (mysqli_connect_errno())
  {
  //echo "Failed to connect to MySQL: " . mysqli_connect_error();
  $result = "Fail";
  $message = "".mysqli_connect_error();
  }
 
$username = $_POST['username'];
$password = $_POST['password'];

//$username = stripslashes($username);
//$password = stripslashes($password);
//$username = mysql_real_escape_string($username);
//$password = mysql_real_escape_string($password);

$check = "SELECT * FROM accounts WHERE Username = '". $username. "' and password = '".$password."';";
$rs = mysqli_query($con, $check);
$data = mysqli_fetch_array($rs, MYSQLI_NUM);
$count = count($data[0]);

if ($count == 0) {
  $result = "Fail";
  $message = "Username or password is incorrect";
}else{
	$result = "Success";
}

echo $result.','.$message;
mysqli_close($con);
?>