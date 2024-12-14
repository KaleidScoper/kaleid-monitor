<?php
// 设置接收JSON格式数据
header("Content-Type: application/json");

// 获取POST请求的数据
$input = file_get_contents('php://input');

// 验证数据有效性
if ($input) {
    // 创建数据存储目录（如果不存在）
    $dataDir = __DIR__ . '/data';
    if (!is_dir($dataDir)) {
        mkdir($dataDir, 0777, true);
    }

    // 保存数据到apps.json文件
    $filePath = $dataDir . '/apps.json';
    file_put_contents($filePath, $input);

    // 返回响应
    echo json_encode(["status" => "success", "message" => "Data received and stored."]);
} else {
    echo json_encode(["status" => "error", "message" => "No data received."]);
}
?>
