<?php
require_once 'config.php';
require_login();
$pageTitle = page_title('Dashboard');
include 'partials/header.php';
?>
<div class="dashboard-page">
  <section class="dashboard-welcome">
    <img class="dashboard-logo" src="assets/images/app_logo.png" alt="Medical Adherence Logo">
    <div class="dashboard-welcome-text"><p class="dashboard-eyebrow">MEDICAL ADHERENCE</p><h1 class="title">Welcome, <span data-firebase-name>...</span></h1><p class="subtitle">Manage your medications and keep track of your health.</p></div>
  </section>
  <section class="dashboard-overview"><div class="dashboard-section-heading"><h2>Overview</h2><p>Your medication summary</p></div>
    <div class="stat-card dashboard-stat"><div class="stat-icon" aria-hidden="true">+</div><div><div class="stat-label">Total Medications</div><div class="stat-number" data-medication-count>…</div></div></div>
  </section>
  <section class="dashboard-actions"><div class="dashboard-section-heading"><h2>Quick Actions</h2><p>Choose a section to continue</p></div>
    <div class="dashboard-action-grid">
      <a class="action-card" href="medications.php"><span class="dashboard-action-icon" aria-hidden="true">+</span><span class="dashboard-action-copy"><strong>My Medications</strong><small>View and manage your medications</small></span><span class="chevron" aria-hidden="true">›</span></a>
      <a class="action-card" href="notifications.php"><span class="dashboard-action-icon" aria-hidden="true">!</span><span class="dashboard-action-copy"><strong>Notifications</strong><small>View your notifications and reminders</small></span><span class="chevron" aria-hidden="true">›</span></a>
      <a class="action-card" href="profile.php"><span class="dashboard-action-icon" aria-hidden="true">&#9679;</span><span class="dashboard-action-copy"><strong>My Profile</strong><small>View and update your profile</small></span><span class="chevron" aria-hidden="true">›</span></a>
      <a class="action-card" href="about.php"><span class="dashboard-action-icon" aria-hidden="true">i</span><span class="dashboard-action-copy"><strong>About Application</strong><small>Learn more about Medical Adherence</small></span><span class="chevron" aria-hidden="true">›</span></a>
    </div>
  </section>
  <p><a class="btn danger" href="logout.php" data-firebase-logout>Logout</a></p>
</div>
<?php include 'partials/footer.php'; ?>
