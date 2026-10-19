<?php
http_response_code(410);
header('Content-Type: application/json; charset=utf-8');
echo json_encode(['status'=>'error','message'=>'Reminders are now read from Firestore by the Firebase-enabled website client.']);
