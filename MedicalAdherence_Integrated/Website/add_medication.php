<?php
require_once 'config.php'; require_login();
$pageTitle=page_title('Add Medication'); $showBack=true; $backHref='medications.php'; include 'partials/header.php';
?>
<div class="header-block center"><h1 class="title">Add Medication</h1><p class="subtitle">This medication will appear in the Android app too.</p></div>
<form class="form-card" data-medication-form>
  <div class="input-group"><label for="medicationName">Medication Name</label><input id="medicationName" name="medicationName" placeholder="Medication Name" required></div>
  <div class="input-group"><label for="dosage">Dosage</label><input id="dosage" name="dosage" placeholder="Dosage" required></div>
  <div class="input-group"><label for="medicationTime">Time</label><input id="medicationTime" type="time" name="medicationTime" required></div>
  <button class="btn" type="submit">Save Medication</button>
</form>
<?php include 'partials/footer.php'; ?>
