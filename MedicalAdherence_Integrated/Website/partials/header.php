<?php
if (!isset($pageTitle)) {
    $pageTitle = APP_NAME;
}
if (!isset($bodyClass)) {
    $bodyClass = '';
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta name="theme-color" content="#1976D2">
    <meta name="mobile-web-app-capable" content="yes">
    <title><?= h($pageTitle) ?></title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body class="<?= h($bodyClass) ?>">
<div class="phone-shell">
<?php if (!empty($showBack)): ?>
    <a class="back-link" href="<?= h($backHref ?? 'dashboard.php') ?>">‹ Back</a>
<?php endif; ?>
<?php if ($flash = flash()): ?>
    <div class="toast <?= h($flash['type']) ?>"><?= h($flash['message']) ?></div>
<?php endif; ?>
