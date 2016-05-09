<?php

require_once 'include/DbHandler.php';
$db = new DbHandler();
$response = array();
$response["error"] = false;

if (isset($_POST['email'])){
$response["class"] = array();
$email = $_POST['email'];
$result = $db->getAgeClass($email);

if($result){
// looping through result and preparing tasks array
while ($task = $result->fetch_assoc()) {
    $tmp = array();
	$tmp["email"] = $task["email"];
    $tmp["uid"] = $task["unique_id"];
	$tmp["date"] = $task["date"];
    $tmp["category"] = $task["category"];
    $tmp["image"] = $task["image"];
    $tmp["price"] = $task["price"];
    $tmp["description"] = $task["description"];
    $tmp["phone"] = $task["phone"];
    $tmp["name"] = $task["name"];
    $tmp["latitude"] = $task["latitude"];
    $tmp["longitude"] = $task["longitude"];
    $tmp["created_at"] = $task["created_at"];
    $tmp["updated_at"] = $task["updated_at"];
    array_push($response["class"], $tmp);
}
echo json_encode($response);
}
else{
	$response["error"] = true;
	$response["error_msg"] = "Unknown error occurred in classification!";
	echo json_encode($response);
}
}
else{
	
	$response["error"] = true;
	$response["error_msg"] = "email not posted!";
	echo json_encode($response);
}
?>