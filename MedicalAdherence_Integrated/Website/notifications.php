<?php
require_once 'config.php'; require_login();
$pageTitle=page_title('Notifications'); $showBack=true; $backHref='dashboard.php'; include 'partials/header.php';
?>
<div class="notifications-page">
  <section class="notifications-heading"><div><p class="dashboard-eyebrow">YOUR UPDATES</p><h1 class="title">Notifications</h1><p class="subtitle">Notifications are shared with the Android app.</p></div></section>
  <section class="notifications-list-section"><div class="notifications-section-heading"><h2>Recent Notifications</h2><p>Your medication reminders and updates.</p></div><div data-notification-list><div class="empty-state"><p>Loading notifications…</p></div></div></section>
</div>
<?php include 'partials/footer.php'; ?>
