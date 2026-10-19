<?php require_once 'config.php'; require_login(); $pageTitle = page_title('App Information'); $showBack = true; $backHref = 'about.php'; include 'partials/header.php'; ?>
<div class="header-block center"><h1 class="title"> App Information</h1></div>
<div class="info-card"><div class="content-text">Application Name: Medical Adherence App

Version: 1.0

Purpose: To help users track medications, manage health records, and receive reminders for better adherence.

Main Features:
• User registration and login
• Medication creation, editing, and deletion
• Medication reminder scheduling concept
• Notifications and healthcare updates
• User profile management
• Privacy, terms, app, and development information pages</div></div>
<?php include 'partials/footer.php'; ?>
