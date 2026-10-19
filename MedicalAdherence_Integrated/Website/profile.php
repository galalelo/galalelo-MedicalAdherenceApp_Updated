<?php
require_once 'config.php'; require_login();
$pageTitle=page_title('My Profile'); $showBack=true; $backHref='dashboard.php'; include 'partials/header.php';
?>
<div class="profile-page">
  <section class="profile-page-heading"><p class="dashboard-eyebrow">ACCOUNT SETTINGS</p><h1 class="title">My Profile</h1><p class="subtitle">This profile is shared with the Android app.</p></section>
  <div class="profile-layout">
    <section class="profile-summary-card"><img class="avatar profile-avatar" src="assets/images/avatar.png" alt="Profile avatar"><h2 class="profile-name" data-profile="fullName">…</h2><p class="profile-role">Medical Adherence User</p><a class="btn" href="edit_profile.php">Edit Profile</a><a class="btn danger" href="logout.php" data-firebase-logout>Logout</a></section>
    <section class="profile-details-section"><h2>Personal Information</h2><p class="profile-section-description">Your account details are displayed below.</p><div class="profile-information-card">
      <div class="profile-information-row"><div class="profile-information-label">Full Name</div><div class="profile-information-value" data-profile="fullName">…</div></div>
      <div class="profile-information-row"><div class="profile-information-label">Email Address</div><div class="profile-information-value" data-profile="email">…</div></div>
      <div class="profile-information-row"><div class="profile-information-label">Phone Number</div><div class="profile-information-value" data-profile="phone">…</div></div>
      <div class="profile-information-row"><div class="profile-information-label">Medical Condition</div><div class="profile-information-value" data-profile="medicalCondition">…</div></div>
    </div></section>
  </div>
</div>
<?php include 'partials/footer.php'; ?>
