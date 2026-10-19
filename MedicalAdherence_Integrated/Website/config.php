<?php
// Firebase Authentication and Cloud Firestore are the sole identity/data source.
declare(strict_types=1);
define('APP_NAME', 'Medical Adherence App');

function h($value): string
{
    return htmlspecialchars((string) $value, ENT_QUOTES, 'UTF-8');
}

function page_title(string $title): string
{
    return $title . ' - ' . APP_NAME;
}

// Authentication is enforced client-side by Firebase Auth and Firestore Security Rules.
// These compatibility helpers deliberately never authenticate via the former SQLite store.
function current_user(): ?array { return null; }
function require_login(): void {}
function medication_count(): int { return 0; }
function redirect(string $path): void { header('Location: ' . $path); exit; }
function flash(?string $message = null, string $type = 'success'): ?array
{
    if ($message !== null) return null;
    return null;
}
?>
