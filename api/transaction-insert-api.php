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
    $payment_mode = $_POST["payment_mode"];
    $coin = $_POST["coin"];
    $amount = $_POST["amount"];
    $payment_status = "Pending";
    


    $sql = "INSERT INTO transactions(uid, payment_mode, coin, amount, payment_status) VALUES ('$uid', '$payment_mode', '$coin', '$amount', '$payment_status')";

    if(mysqli_query($con, $sql)){
        
        
         $sql1 = "INSERT INTO rewards(uid, rewards, reason) VALUES ('$uid', '$coin', 'rewards debited')";
    
        if(mysqli_query($con, $sql1)){
             echo json_encode(array('message' => 'Transaction data inserted.', 'status' => true , 'uid'  => $uid , 'payment_mode' => $payment_mode , 'coin' => $coin, 'amount' => $amount, 'payment_status' => $payment_status));
              }
        else{
          echo json_encode(array('message' => 'Transaction data not inserted.', 'status' => false));
        //   mysqli_close($con);
        }
        
     
    //     echo "\n";
    //   echo json_encode(array('Email'  => $email , 'First Name' => $first_name , 'Last Name' => $last_name , 'Rewards' => $rewards, 'Task' => $task , 'Token' => $token));
      mysqli_close($con);
    }
    else{
      echo json_encode(array('message' => 'Transaction data not inserted.', 'status' => false));
      mysqli_close($con);
    }
}

// mysqli_close($con);
?>
