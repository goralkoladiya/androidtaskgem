<?php 
  session_start(); 
include '../include/header.php';
include '../include/dbconfig.php';
?>
<!DOCTYPE html>
<html lang="en">
<body class="hold-transition sidebar-mini layout-fixed layout-navbar-fixed layout-footer-fixed">
  <div class="wrapper">
    <?php include '../include/admin-sidebar.php'; ?>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
      <!-- Content Header (Page header) -->
      <section class="content-header">
        <div class="container-fluid">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1>Transaction Details</h1>
            </div>
            <div class="col-sm-6">
              <ol class="breadcrumb float-sm-right">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active">Transaction Details</li>
              </ol>
            </div>
          </div>
        </div><!-- /.container-fluid -->
      </section>

      <!-- Main content -->
      <section class="content">
        <div class="container-fluid">
          <div class="row">
            <div class="col-12">
              <div class="card">
                <div class="card-header">
                  <h3 class="card-title">Transaction Details</h3>
                </div>
                <!-- /.card-header -->
                <div class="card-body">
                  <table id="example1" class="table table-bordered table-striped">
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Payment Mode</th>
                        <th>Coin</th>
                        <th>Amount</th>
                        <th>Payment Status</th>
                        <th>Time Stamp</th>
                      </tr>
                    </thead>
                    <tbody>
                      <?php
                      $sel = $con->query("select user_details.`id`, user_details.`first_name`, user_details.`last_name`,user_details.`email`, transactions.`id` as `transaction_id`, transactions.`uid`,  transactions.`payment_mode`, transactions.`coin`, transactions.`amount`, transactions.`payment_status`, transactions.`timestamp` from user_details, transactions where user_details.id = transactions.uid order by transactions.id desc;");
                      while ($row = $sel->fetch_assoc()) {
                      ?>
                        <tr>
                          <td><?php echo $row['transaction_id']; ?></td>
                          <td><?php echo $row['first_name'] ." ". $row['last_name']; ?></td>
                          <td><a href="mailto:<?php echo $row['email']; ?>"><?php echo $row['email']; ?></a></td>
                          <td><?php echo $row['payment_mode']; ?></td>
                          <td><?php echo $row['coin']; ?></td>
                          <td><?php echo $row['amount']; ?></td>
                         <td>
                                    <?php if($row['payment_status'] == 'Approved') {
                                    ?>
									<a href="../controller/admin.php?payment_status=Pending&uid=<?php echo $row['uid'];?>">	<span class="badge badge-pill badge-success"   data-original-title="" title="">
                                           Approved
                                        </button></a>
									<?php
                                    } else {
									    
									?>
								<a	href="../controller/admin.php?payment_status=Approved&uid=<?php echo $row['uid'];?>">	<span class="badge badge-pill badge-danger"  href="../controller/admin.php?&payment_status=Approved&uid=<?php echo $row['transaction_id'];?>" data-original-title="" title="">
                                            Pending
                                        </button>
										</a>
									<?php 
									} ?>
										</td>
                          <td><?php echo $row['timestamp']; ?></td>
                        </tr>
                      <?php } ?>
                    </tbody>
                  </table>
                </div>
                <!-- /.card-body -->
              </div>
              <!-- /.card -->
            </div>
            <!-- /.col -->
          </div>
          <!-- /.row -->
        </div>
        <!-- /.container-fluid -->
      </section>
      <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
   <footer class="main-footer">
    <div class="float-right d-none d-sm-block">
      <b>Task Gem © 2023<br>
    </div>
    <strong> <a href="#">TaskGem</a></strong>
  </footer>

    <!-- Control Sidebar -->
    <aside class="control-sidebar control-sidebar-dark">
      <!-- Control sidebar content goes here -->
    </aside>
    <!-- /.control-sidebar -->
  </div>
  <!-- ./wrapper -->
  <?php include '../include/footer.php'; ?>

</body>

</html>