<?php

class DbHandler {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DbConnect.php';
        // connecting to database
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password, $date) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uuid, $name, $email, $encrypted_password, $salt);
        $result = $stmt->execute();
        $stmt->close();
		
        $stmnt = $this->conn->prepare("INSERT INTO birthdate(email, date) VALUES(?, ?)");
        $stmnt->bind_param("ss", $email, $date);
        $stmnt->execute();
		$stmnt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

    /**
     * Storing new add
     * returns add
     */
    public function storeAdd($category, $image, $price, $description, $phone, $email, $name, $latitude, $longitude) {
        $uuid = uniqid('', true);

        $path = "imageuploads/$uuid.jpeg";
        $fullpath = "https://qdeal-wsayan.c9users.io/$path";
        file_put_contents($path, base64_decode($image));

        $stmt = $this->conn->prepare("INSERT INTO addpost(unique_id, category, image, price, phone, description, name, email, created_at, latitude, longitude) VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)");
        $stmt->bind_param("ssssssssss", $uuid, $category, $fullpath, $price, $phone, $description, $name, $email, $latitude, $longitude);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM addpost WHERE unique_id = ?");
            $stmt->bind_param("s", $uuid);
            $stmt->execute();
            $newadd = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $newadd;
        } else {
            return false;
        }
    }

    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllAdds($category) {
        if ($category == "recent") {
            $stmt = $this->conn->prepare("SELECT * FROM addpost");
        } else {
            //$stmt = $this->conn->prepare("SELECT * FROM addpost WHERE category = ?");
            $stmt = $this->conn->prepare("SELECT * FROM addpost where category = ?");
            $stmt->bind_param("s", $category);
        }

        if ($stmt->execute()) {
            $stmt->execute();
            $tasks = $stmt->get_result();
            $stmt->close();
            return $tasks;
        } else {
            return NULL;
        }
    }

    /**
     * Fetching one user tasks
     * @param String $user_id id of the user
     */
    public function getMyAdds($email) {
        $stmt = $this->conn->prepare("SELECT * FROM addpost WHERE email = ?");
        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $stmt->execute();
            $tasks = $stmt->get_result();
            $stmt->close();
            return $tasks;
        } else {
            return NULL;
        }
    }

    /**
     * Storing favourites
     * returns favourite
     */
    public function storeFavourites($email, $uid) {
        $stmt = $this->conn->prepare("INSERT INTO favouritelist(email, unique_id, created_at) VALUES(?, ?, NOW())");
        $stmt->bind_param("ss", $email, $uid);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM addpost WHERE unique_id = ?");
            $stmt->bind_param("s", $uid);
            $stmt->execute();
            $favourite = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $favourite;
        } else {
            return false;
        }
    }

    /**
     * Fetching favourites
     * returns favourites
     */
    public function getAllFavourites($email) {
        $stmt = $this->conn->prepare("SELECT addpost.* FROM addpost INNER JOIN favouritelist ON favouritelist.unique_id = addpost.unique_id where favouritelist.email = ?");
        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $stmt->execute();
            $result = $stmt->get_result();
            $stmt->close();
            return $result;
        } else {
            return NULL;
        }
    }
	
	/**
     * Fetching age classification
     * returns age class
     */
    public function getAgeClass($email) {       	
        $stmnt = $this->conn->prepare("SELECT date FROM birthdate WHERE email = ?");
        $stmnt->bind_param("s", $email);
		
        if ($stmnt->execute()) {
            $stmnt->execute();
            $result = $stmnt->get_result();
            $stmnt->close();            
        }
		
		$age = 0;
		while ($task = $result->fetch_assoc()) {
			$age = $task["date"];
		} 
		$ageone = $age - 2;
		$agetwo = $age + 2;
		
		$stmt = $this->conn->prepare("SELECT favouritelist.*,birthdate.date,addpost.* FROM favouritelist 
		INNER JOIN birthdate ON birthdate.email = favouritelist.email
		INNER JOIN addpost ON addpost.unique_id = favouritelist.unique_id
		WHERE birthdate.date BETWEEN ? AND ?");
		$stmt->bind_param("ss", $ageone,$agetwo);
		
		if ($stmt->execute()) {
            $stmt->execute();
            $result = $stmt->get_result();
            $stmt->close();
            return $result;
        } else {
            return NULL;
        }				 
    }

    /**
     * Deleting adds
     * returns rest of the adds
     */
    public function deleteAdd($uid,$email) {
        $stmt = $this->conn->prepare("DELETE FROM `addpost` WHERE unique_id = ?");
        $stmt->bind_param("s", $uid);

        $stmt2 = $this->conn->prepare("DELETE FROM `favouritelist` WHERE unique_id = ?");
        $stmt2->bind_param("s", $uid);

        $stmnt = $this->conn->prepare("SELECT * FROM `addpost` WHERE email = ?");
		$stmnt->bind_param("s", $email);
		
        $path = "imageuploads/$uid.jpeg";
        //$fullpath = "http://10.0.3.2/quickDealApi/$path";

        if ($stmt->execute()) {
            $stmt->execute();
            $stmt->close();
            unlink($path);
        } else if ($stmt2->execute()) {
            $stmt2->execute();
            $stmt2->close();
        }

        if ($stmnt->execute()) {
            $stmnt->execute();
            $result = $stmnt->get_result();
            $stmnt->close();
            return $result;
        } else {
            return NULL;
        }
    }

    /**
     * Deleting favorite item
     * returns rest of the favorites
     */
    public function deleteFavorites($email, $uid) {

        $stmt2 = $this->conn->prepare("DELETE FROM `favouritelist` WHERE unique_id = ?");
        $stmt2->bind_param("s", $uid);

        $stmnt = $this->conn->prepare("SELECT addpost.* FROM addpost INNER JOIN favouritelist ON favouritelist.unique_id = addpost.unique_id where favouritelist.email = ?");
        $stmnt->bind_param("s", $email);

        if ($stmt2->execute()) {
            $stmt2->execute();
            $stmt2->close();
        }

        if ($stmnt->execute()) {
            $stmnt->execute();
            $result = $stmnt->get_result();
            $stmnt->close();
            return $result;
        } else {
            return NULL;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            if ($encrypted_password == $hash) {
                return $user;
            } else {
                return NULL;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
