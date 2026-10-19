<?php
require_once 'config.php'; require_login(); $pageTitle=page_title('About Account'); $showBack=true; $backHref='about.php'; include 'partials/header.php';
?>
<div class="header-block center"><h1 class="title">About Account</h1></div>
<img class="avatar" src="assets/images/avatar.png" alt="Avatar">
<div class="detail-row"><div class="detail-label">Email</div><div class="detail-value" data-profile="email">…</div></div>
<div class="detail-row"><div class="detail-label">Phone Number</div><div class="detail-value" data-profile="phone">…</div></div>
<div class="detail-row"><div class="detail-label">Medical Condition</div><div class="detail-value" data-profile="medicalCondition">…</div></div>
<div class="detail-row"><div class="detail-label">Account Status</div><div class="detail-value status">Active</div></div>
<?php include 'partials/footer.php'; ?>
