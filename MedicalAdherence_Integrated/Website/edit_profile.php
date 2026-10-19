<?php
require_once 'config.php'; require_login();
$pageTitle=page_title('Edit Profile'); $showBack=true; $backHref='profile.php'; include 'partials/header.php';
?>
<div class="edit-profile-page">
  <section class="edit-profile-heading"><p class="dashboard-eyebrow">ACCOUNT SETTINGS</p><h1 class="title">Edit Profile</h1><p class="subtitle">Changes are saved to your shared Firebase profile.</p></section>
  <div class="edit-profile-layout"><section class="edit-profile-intro"><div class="edit-profile-icon" aria-hidden="true"><span>✎</span></div><h2>Personal Information</h2><p>Update your details to keep your Medical Adherence account current.</p></section>
    <form class="edit-profile-form" data-firebase-profile>
      <div class="input-group"><label for="name">Full Name</label><input id="name" name="name" type="text" autocomplete="name" required></div>
      <div class="input-group"><label for="email">Email Address</label><input id="email" name="email" type="email" readonly><small>Your email address is managed by Firebase Authentication.</small></div>
      <div class="input-group"><label for="phone">Phone Number</label><input id="phone" name="phone" type="tel" autocomplete="tel"></div>
      <div class="input-group"><label for="condition">Medical Condition</label><input id="condition" name="condition" type="text"></div>
      <div class="edit-profile-actions"><button class="btn" type="submit">Save Changes</button><a class="btn cancel-profile-btn" href="profile.php">Cancel</a></div>
    </form>
  </div>
</div>
<?php include 'partials/footer.php'; ?>
