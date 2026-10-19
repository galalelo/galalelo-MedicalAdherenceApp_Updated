<?php
http_response_code(410);
header('Content-Type: application/json; charset=utf-8');
echo json_encode([
    'status' => 'error',
    'message' => 'This legacy API is retired. The website and Android app now use Firebase Authentication and Cloud Firestore directly.'
]);
