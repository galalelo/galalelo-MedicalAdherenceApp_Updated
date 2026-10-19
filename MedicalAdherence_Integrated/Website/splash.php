<?php require_once 'config.php'; $pageTitle = page_title('Splash'); $bodyClass = 'splash-body'; include 'partials/header.php'; ?>
<img class="logo big" src="assets/images/app_logo.png" alt="Medical Adherence Logo">
<h1 class="splash-title">Medical Adherence App</h1>
<p class="splash-subtitle">Track • Remind • Adhere</p>
<div class="progress" aria-label="Loading"></div>
<p class="version">Version 1.0</p>
<script>setTimeout(function(){ window.location.href = 'login.php'; }, 1600);</script>
<?php include 'partials/footer.php'; ?>
