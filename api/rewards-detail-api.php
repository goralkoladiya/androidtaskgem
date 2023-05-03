<?php

include "../include/dbconfig.php";

// Check if an emp_id parameter was passed in the API request
if (isset($_GET['uid'])) {
    // Sanitize the input to prevent SQL injection
    $uid = mysqli_real_escape_string($con, $_GET['uid']);

    // Build the SQL query to fetch employee details using emp_id
    $sql = "SELECT * FROM rewards WHERE uid = $uid order by id  DESC";

       // Execute query
    $result = mysqli_query($con, $sql);

    // Check if query returned any rows
    if (mysqli_num_rows($result) > 0) {

        // Fetch all rows and store in an array
        $rows = array();
        while ($row = mysqli_fetch_assoc($result)) {
            $rows[] = $row;
        }

        // Return JSON response
        header('Content-Type: application/json');
        echo json_encode($rows);

    } else {

        // Return error response if no rows were found
        header('Content-Type: application/json');
        http_response_code(404);
        echo json_encode(array('message' => 'No records found'));

    }

} else {

    // Return error response if emp_id parameter was not provided
    header('Content-Type: application/json');
    http_response_code(400);
    echo json_encode(array('message' => 'uid parameter is required'));

}

// Close the database connection
$con->close();
?>