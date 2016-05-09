<?php

require_once 'include/DbHandler.php';
$db = new DbHandler();
$response = array();
$response["error"] = false;

if (isset($_POST['email']) && isset($_POST['uid'])){
$response["tasks"] = array();
$email = $_POST['email'];
$uid = $_POST['uid'];
$result = $db->deleteFavorites($email,$uid);

if($result){
// looping through result and preparing tasks array
while ($task = $result->fetch_assoc()) {
    $tmp = array();
    $tmp["uid"] = $task["unique_id"];
    $tmp["newadd"]["category"] = $task["category"];
    $tmp["newadd"]["image"] = $task["image"];
    $tmp["newadd"]["price"] = $task["price"];
    $tmp["newadd"]["description"] = $task["description"];
    $tmp["newadd"]["phone"] = $task["phone"];
    $tmp["newadd"]["name"] = $task["name"];
    $tmp["newadd"]["email"] = $task["email"];
    $tmp["newadd"]["created_at"] = $task["created_at"];
    $tmp["newadd"]["updated_at"] = $task["updated_at"];
    array_push($response["tasks"], $tmp);
}
echo json_encode($response);
}
else{
	$response["error"] = true;
	$response["error_msg"] = "Unknown error occurred in deleting favorite item!";
	echo json_encode($response);
}
}
else{
	
	$response["error"] = true;
	$response["error_msg"] = "itemId or email not posted!";
	echo json_encode($response);
}
?>