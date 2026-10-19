<?php
require_once 'config.php'; require_login();
$pageTitle=page_title('My Medications'); $showBack=true; $backHref='dashboard.php'; include 'partials/header.php';
?>
<div class="medications-page">
  <section class="medications-heading"><div><p class="dashboard-eyebrow">MEDICATION MANAGEMENT</p><h1 class="title">My Medications</h1><p class="subtitle">Manage your medication schedule and track daily doses. Updates sync with the Android app.</p></div><a class="btn add-medication-btn" href="add_medication.php">+ Add Medication</a></section>
  <section class="medications-list-section"><div class="medications-section-heading"><h2>Your Medication List</h2><p>Review dosage, scheduled time and today's dose status.</p></div><div data-medication-list><div class="empty-state"><p>Loading your medications…</p></div></div></section>
</div>
<?php include 'partials/footer.php'; ?>
