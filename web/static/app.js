// 定期从服务器获取数据并更新界面
async function fetchAppData() {
    try {
        const response = await fetch('data/apps.json');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();

        const appListDiv = document.getElementById('app-list');
        appListDiv.innerHTML = ''; // 清空旧内容

        if (data.apps && data.apps.length > 0) {
            data.apps.forEach(app => {
                const appDiv = document.createElement('div');
                appDiv.className = 'app';
                appDiv.textContent = app;
                appListDiv.appendChild(appDiv);
            });
        } else {
            appListDiv.innerHTML = '<p>未运行任何应用。</p>';
        }
    } catch (error) {
        console.error('Failed to fetch app data:', error);
        document.getElementById('app-list').innerHTML = '<p>无法加载数据。</p>';
    }
}

// 定时每2秒刷新数据
setInterval(fetchAppData, 2000);

// 页面加载完成后立即刷新一次
document.addEventListener('DOMContentLoaded', fetchAppData);
