<?php
http_response_code(410);
header('Content-Type: application/json; charset=utf-8');
echo json_encode(['status'=>'error','message'=>'Reminder notifications are now stored in the signed-in user Firestore notifications collection.']);
