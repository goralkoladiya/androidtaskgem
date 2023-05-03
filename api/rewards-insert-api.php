<?php

include "../include/dbconfig.php";

//  -------------------  FOR INSERTING IN JSON FORMAT ------------------------- //


    // header('Content-Type: application/json');
    // header('Acess-Control-Allow-Origin: *');
    // header('Access-Control-Allow-Methods: POST');
    // header('Access-Control-Allow-Headers: Access-Control-Allow-Headers.Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');
    
    // $data = json_decode(file_get_contents("php://input"), true);
    
if($_SERVER['REQUEST_METHOD'] == 'POST'){

    // $email = $data["email"];
    // $first_name = $data["first_name"];
    // $last_name = $data["last_name"];
    // $rewards = $data["rewards"];
    // $task = $data["task"];


    $uid = $_POST["uid"];
    $rewards = $_POST["rewards"];
    $reason = $_POST["reason"];


    $sql = "INSERT INTO rewards(uid, rewards, reason) VALUES ('$uid', '$rewards', '$reason')";

    if(mysqli_query($con, $sql)){
     
        $rewards = 0;
     $sql2="select * from rewards where uid=$uid";
        $res2=mysqli_query($con,$sql2);
         while($arr=mysqli_fetch_assoc($res2))
        {
            if($reason=='rewards debited')
            {
                $rewards=$rewards-$arr['rewards'];
            }
            else
            {
                $rewards=$rewards+$arr['rewards'];
            }
            
        }
        $sql3="update user_details set rewards='$rewards' where id=$uid";
        mysqli_query($con,$sql3);
         echo json_encode(array('message' => 'Rewards data inserted.', 'status' => true , 'uid'  => $uid , 'rewards' => $rewards , 'reason' => $reason));
      mysqli_close($con);
    }
    else{
      echo json_encode(array('message' => 'User data not inserted.', 'status' => false));
      mysqli_close($con);
    }
}

// mysqli_close($con);
?>
