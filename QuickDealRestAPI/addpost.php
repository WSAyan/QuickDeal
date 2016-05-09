<?php

require_once 'include/DbHandler.php';
$db = new DbHandler();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['category']) && isset($_POST['price']) && isset($_POST['image']) && isset($_POST['description']) && isset($_POST['phone']) && isset($_POST['email']) && isset($_POST['name']) && isset($_POST['latitude']) && isset($_POST['longitude'])) {

    // receiving the post params
    $category = $_POST['category'];
    $image = $_POST['image'];
    $price = $_POST['price'];
    $description = $_POST['description'];
    $phone = $_POST['phone'];
    $email = $_POST['email'];
    $name = $_POST['name'];
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];


    // create a new user
    $newadd = $db->storeAdd($category, $image, $price, $description, $phone, $email, $name, $latitude, $longitude );
    if ($newadd) {
        // newadd stored successfully
        $response["error"] = FALSE;
        $response["uid"] = $newadd["unique_id"];
        $response["newadd"]["category"] = $newadd["category"];
        $response["newadd"]["image"] = $newadd["image"];
        $response["newadd"]["price"] = $newadd["price"];
        $response["newadd"]["description"] = $newadd["description"];
        $response["newadd"]["phone"] = $newadd["phone"];
        $response["newadd"]["name"] = $newadd["name"];
        $response["newadd"]["email"] = $newadd["email"];
        $response["newadd"]["latitude"] = $newadd["latitude"];
        $response["newadd"]["longitude"] = $newadd["longitude"];
        $response["newadd"]["created_at"] = $newadd["created_at"];
        $response["newadd"]["updated_at"] = $newadd["updated_at"];
        echo json_encode($response);
    } else {
        // newadd failed to store
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in posting add!";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (category,price or image) is missing!";
    echo json_encode($response);
}
?>
