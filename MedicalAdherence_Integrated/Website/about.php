<?php require_once 'config.php'; require_login(); $pageTitle = page_title('About Application'); $showBack = true; $backHref = 'dashboard.php'; include 'partials/header.php'; ?>
<div class="header-block center"><h1 class="title">About Application</h1></div>
<img class="avatar" src="assets/images/avatar.png" alt="Avatar">
<div class="profile-name">Medical Adherence App</div>
<p class="profile-role">Version 1.0</p>
<a class="action-card" href="about_account.php"><span class="left"><span>About Account</span></span><span class="chevron">›</span></a>
<a class="action-card" href="privacy_policy.php"><span class="left"><span>Privacy Policy</span></span><span class="chevron">›</span></a>
<a class="action-card" href="terms_of_use.php"><span class="left"><span>Terms of Use</span></span><span class="chevron">›</span></a>
<a class="action-card" href="app_info.php"><span class="left"><span>App Information</span></span><span class="chevron">›</span></a>
<a class="action-card" href="developer_info.php"><span class="left"><span>Development Team</span></span><span class="chevron">›</span></a>
<?php include 'partials/footer.php'; ?>
