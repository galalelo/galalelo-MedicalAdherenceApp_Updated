<?php
require_once 'config.php';
$pageTitle=page_title('Forgot Password'); $showBack=true; $backHref='login.php'; include 'partials/header.php';
?>
<section class="auth-screen"><img class="logo" src="assets/images/app_logo.png" alt="Medical Adherence Logo"><h1 class="title">Forgot Password</h1><p class="subtitle">Firebase will email a password reset link for your shared account.</p>
<form class="form-card" data-firebase-reset><div class="input-group"><label for="email">Email Address</label><input id="email" name="email" type="email" autocomplete="email" placeholder="Enter your email" required></div><button class="btn" type="submit">SEND RESET EMAIL</button></form>
<p class="footnote"><a href="login.php">Back to login</a></p></section>
<?php include 'partials/footer.php'; ?>
