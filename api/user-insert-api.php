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


    $email = $_POST["email"];
    $first_name = $_POST["first_name"];
    $last_name = $_POST["last_name"];
    // $rewards = 100;
    $task = $_POST["task"];
    $rewards='100';
    $uid=0;
    
    // $token = bin2hex(random_bytes(16));
    // $token1 = md5($token);
    
    
    function random_code($limit)
    {
        return substr(base_convert(sha1(uniqid(mt_rand())), 16, 36), 0, $limit);
    }
    
    $token1 = random_code(6);
    $token = strtoupper($token1);  

    //get data
    $sql1="select * from user_details where email='$email'";
    $res=mysqli_query($con,$sql1);
    $n=mysqli_num_rows($res);
    if($n==0)
    {
         $sql = "INSERT INTO user_details(email, first_name, last_name, rewards, task, token) VALUES ('$email', '$first_name', '$last_name', '$rewards', '$task','$token')";

        if(mysqli_query($con, $sql)){
        //   echo json_encode(array('message' => 'User data inserted.', 'status' => true , 'Email'  => $email , 'First Name' => $first_name , 'Last Name' => $last_name , 'Rewards' => $rewards, 'Task' => $task , 'Token' => $token));
        //     echo "\n";
        //   echo json_encode(array('Email'  => $email , 'First Name' => $first_name , 'Last Name' => $last_name , 'Rewards' => $rewards, 'Task' => $task , 'Token' => $token));
        //   mysqli_close($con);
        }
        else{
          echo json_encode(array('message' => 'User data not inserted.', 'status' => false));
        //   mysqli_close($con);
        }
    
    // INSERT DATA IN 'REWARDS' TABLE 
        $uid = mysqli_insert_id($con);
        $rewards = 100;
        $reason = ("Welcome Rewards");
    
        $sql1 = "INSERT INTO rewards(uid, rewards, reason) VALUES ('$uid', '$rewards', '$reason')";
    
        if(mysqli_query($con, $sql1)){
          echo json_encode(array('message' => 'Transaction data inserted.', 'status' => true ,'Email'  => $email , 'First Name' => $first_name , 'Last Name' => $last_name ,  'Task' => $task , 'Token' => $token, 'uid'  => $uid , 'Rewards' => $rewards , 'Reason' => $reason));
        }
        else{
          echo json_encode(array('message' => 'Transaction data not inserted.', 'status' => false));
        //   mysqli_close($con);
        }
    }
    else
    {
        while($arr=mysqli_fetch_assoc($res))
        {
     
            $uid=$arr['id'];
            $token=$arr['token'];
            $rewards=$arr['rewards'];
            
        }
        // $sql2="select * from rewards where uid=$uid";
        // $res2=mysqli_query($con,$sql2);
        //  while($arr=mysqli_fetch_assoc($res2))
        // {
        //     $rewards=$rewards+$arr['rewards'];
             
        // }
        echo json_encode(array('message' => 'Transaction data inserted.', 'status' => true ,'Email'  => $email , 'First Name' => $first_name , 'Last Name' => $last_name ,  'Task' => $task , 'Token' => $token, 'uid'  => $uid , 'Rewards' => $rewards , 'Reason' => $reason));

        
    }

   
}

mysqli_close($con);
?>
