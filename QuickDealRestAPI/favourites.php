<?php

require_once 'include/DbHandler.php';
$db = new DbHandler();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email']) && isset($_POST['uid'])) {

    // receiving the post params
    $email = $_POST['email'];
    $uid = $_POST['uid'];
	
	// new favourite
    $favourite = $db->storeFavourites($email, $uid);
    if ($favourite) {
        // favourites stored successfully
        $response["error"] = FALSE;
        $response["uid"] = $favourite["unique_id"];
        $response["newadd"]["category"] = $favourite["category"];
        $response["newadd"]["image"] = $favourite["image"];
        $response["newadd"]["price"] = $favourite["price"];
        $response["newadd"]["description"] = $favourite["description"];
        $response["newadd"]["phone"] = $favourite["phone"];
        $response["newadd"]["name"] = $favourite["name"];
        $response["newadd"]["email"] = $favourite["email"];
        $response["newadd"]["created_at"] = $favourite["created_at"];
        $response["newadd"]["updated_at"] = $favourite["updated_at"];
        echo json_encode($response);
    } else {
        // newadd failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in showing favourites!";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (email or uid) is missing!";
    echo json_encode($response);
}
?>
