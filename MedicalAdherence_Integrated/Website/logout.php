<?php
require_once 'config.php';
$pageTitle=page_title('Signing out');
include 'partials/header.php';
?>
<section class="auth-screen"><h1 class="title">Signing out…</h1><p class="subtitle">Your Firebase session will be closed on this device.</p><a class="btn" href="login.php" data-firebase-logout>Sign out</a></section>
<?php include 'partials/footer.php'; ?>
