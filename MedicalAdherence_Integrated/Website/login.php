<?php
require_once 'config.php';
$pageTitle = page_title('Login');
include 'partials/header.php';
?>
<div class="website-login">
  <section class="login-hero">
    <img class="login-hero-logo" src="assets/images/app_logo.png" alt="Medical Adherence Logo">
    <h1>Medical<br>Adherence</h1>
    <p class="login-tagline">Track your medication. Stay on schedule. Take control of your health.</p>
    <p class="login-footer-text">Your health, simplified.</p>
  </section>
  <section class="login-panel">
    <div class="login-brand"><img src="assets/images/app_logo.png" alt="Medical Adherence Logo"><span>Medical<br>Adherence App</span></div>
    <h2>Welcome Back</h2>
    <p class="login-description">Sign in with the same account you use in the Android app.</p>
    <form class="website-login-form" data-firebase-login>
      <div class="input-group"><label for="email">Email Address</label><input id="email" name="email" type="email" autocomplete="email" placeholder="Enter your email" required></div>
      <div class="input-group"><label for="password">Password</label><input id="password" name="password" type="password" autocomplete="current-password" placeholder="Enter your password" required></div>
      <div class="login-forgot"><a class="text-link" href="forgot_password.php">Forgot Password?</a></div>
      <button class="btn" type="submit">LOGIN</button>
      <button class="btn google-login-btn" type="button" data-google-login>Continue with Google</button>
      <p class="register-prompt">Don't have an account? <a href="register.php">Create Account</a></p>
    </form>
  </section>
</div>
<?php include 'partials/footer.php'; ?>
