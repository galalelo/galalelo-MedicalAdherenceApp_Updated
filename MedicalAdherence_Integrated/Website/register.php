<?php
require_once 'config.php';
$pageTitle = page_title('Create Account'); $showBack = true; $backHref = 'login.php';
include 'partials/header.php';
?>
<section class="auth-screen">
  <img class="logo" src="assets/images/app_logo.png" alt="Medical Adherence Logo">
  <h1 class="title">Create Account</h1><p class="subtitle">Use this account in the Android app and website.</p>
  <form class="form-card" data-firebase-register>
    <div class="input-group"><label for="name">Full Name</label><input id="name" name="name" autocomplete="name" placeholder="Full Name" required></div>
    <div class="input-group"><label for="email">Email</label><input id="email" type="email" name="email" autocomplete="email" placeholder="Email" required></div>
    <div class="input-group"><label for="phone">Phone Number</label><input id="phone" name="phone" type="tel" autocomplete="tel" placeholder="Phone Number"></div>
    <div class="input-group"><label for="condition">Medical Condition</label><input id="condition" name="condition" placeholder="Medical Condition"></div>
    <div class="input-group"><label for="password">Password</label><input id="password" type="password" name="password" autocomplete="new-password" minlength="6" placeholder="Password" required></div>
    <button class="btn" type="submit">CREATE ACCOUNT</button>
  </form>
  <p class="footnote">Your health journey starts here.</p>
</section>
<?php include 'partials/footer.php'; ?>
