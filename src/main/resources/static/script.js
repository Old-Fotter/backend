function showTab(tabId) {
    const tabs = document.getElementsByClassName('tab-content');
    for (let tab of tabs) {
        tab.classList.remove('active');
    }
    document.getElementById(tabId + '-tab').classList.add('active');

    const buttons = document.getElementsByClassName('tab-button');
    for (let button of buttons) {
        button.classList.remove('active');
    }
    document.querySelector(`.tab-button[onclick="showTab('${tabId}')"]`).classList.add('active');

    if (tabId === 'submit') {
        loadAddresses();
    } else if (tabId === 'history') {
        loadHistory();
    }
}

function showReadingsPage() {
    document.getElementById('requests-page').style.display = 'none';
    document.querySelector('.container').style.display = 'block';
    showTab('submit');
}
function showRequestsPage() {
    window.location.href = "index.html#requests";
    setTimeout(() => {
        document.querySelector('.container').style.display = 'none';
        document.getElementById('requests-page').style.display = 'block';
    }, 100); // Даем странице время загрузиться
}
document.addEventListener('DOMContentLoaded', function() {
    if (window.location.pathname.includes('requests.html')) {
        loadRequestAddresses();
        loadRequestHistory();
    }
});



document.addEventListener('DOMContentLoaded', function() {
    showTab('submit');
});

function loadAddresses() {
    const userId = 1;
    fetch(`http://localhost:8080/api/meters/addresses/${userId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке адресов');
        return response.json();
    })
    .then(addresses => {
        const addressesDiv = document.querySelector('.addresses');
        addressesDiv.innerHTML = '';
        addresses.forEach(address => {
            const addressCard = document.createElement('div');
            addressCard.className = 'address-card';
            addressCard.innerHTML = `
                <h3>${address.address}</h3>
                <button class="settings">Настройки ▼</button>
                <div class="meter-section" id="meters-${address.id}">
                    <!-- Счетчики будут загружены динамически -->
                </div>
            `;
            addressesDiv.appendChild(addressCard);

            const settingsBtn = addressCard.querySelector('.settings');
            const meterSection = addressCard.querySelector('.meter-section');
            settingsBtn.addEventListener('click', () => {
                meterSection.classList.toggle('active');
                settingsBtn.textContent = meterSection.classList.contains('active') ? 'Настройки ▲' : 'Настройки ▼';
                if (meterSection.classList.contains('active')) {
                    loadMeters(address.id);
                }
            });
        });
    })
    .catch(error => showMessage(error.message, 'error'));
}

function loadMeters(addressId) {
    fetch(`http://localhost:8080/api/meters/address/${addressId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке счетчиков');
        return response.json();
    })
    .then(meters => {
        const meterSection = document.getElementById(`meters-${addressId}`);
        meterSection.innerHTML = '';
        meters.forEach(meter => {
            const meterDiv = document.createElement('div');
            meterDiv.className = 'meter-select';
            meterDiv.innerHTML = `
                <label>${meter.meterCategory}:</label>
                <input type="number" class="meter-value" data-meter-id="${meter.id}" placeholder="Введите значение" step="0.1">
                <button class="submit-meter-btn" onclick="submitMeterReading(${meter.id})">Подать</button>
            `;
            meterSection.appendChild(meterDiv);
        });
    })
    .catch(error => showMessage(error.message, 'error'));
}

function submitMeterReading(meterId) {
    const userId = 1;
    const currentValue = document.querySelector(`.meter-value[data-meter-id="${meterId}"]`).value;

    if (!currentValue) {
        showMessage('Пожалуйста, введите значение', 'error');
        return;
    }

    const data = {
        userId: userId,
        meterId: meterId,
        currentValue: parseFloat(currentValue)
    };

    fetch('http://localhost:8080/api/meter-readings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при подаче показаний');
        return response.json();
    })
    .then(result => {
        showMessage('Показания успешно поданы!', 'success');
        document.querySelector(`.meter-value[data-meter-id="${meterId}"]`).value = '';
        loadAddresses();
    })
    .catch(error => showMessage(error.message, 'error'));
}

function loadHistory() {
    const userId = 1; // Фиксированный userId
    fetch(`http://localhost:8080/api/meter-readings/history-with-address/${userId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке истории');
        return response.json();
    })
    .then(readings => {
        const historyDiv = document.querySelector('.history');
        historyDiv.innerHTML = '';
        readings.forEach(reading => {
            const historyItem = document.createElement('div');
            historyItem.className = 'history-item';
            historyItem.innerHTML = `
                <div class="meter-type">${reading.meterCategory}</div>
                <div class="details">
                    <span class="address">Адрес: ${reading.address}</span>
                    <span>Дата подачи: ${new Date(reading.submittedAt).toLocaleString()}</span>
                    <span>Пред. показания: ${reading.previousValue} м³</span>
                    <span>Тек. показания: ${reading.currentValue} м³</span>
                </div>
            `;
            historyDiv.appendChild(historyItem);
        });
    })
    .catch(error => showMessage(error.message, 'error'));
}

function showAddAddressForm() {
    document.getElementById('add-address-form').style.display = 'block';
}

function hideAddAddressForm() {
    document.getElementById('add-address-form').style.display = 'none';
}

function addAddress() {
    const userId = 1;
    const address = document.getElementById('newAddress').value;

    if (!address) {
        showMessage('Пожалуйста, введите адрес', 'error');
        return;
    }

    fetch('http://localhost:8080/api/meters/addresses', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({ address: address })
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при добавлении адреса');
        return response.json();
    })
    .then(result => {
        showMessage('Адрес успешно добавлен!', 'success');
        hideAddAddressForm();
        loadAddresses();
    })
    .catch(error => showMessage(error.message, 'error'));
}

function loadRequestAddresses() {
    const userId = 1;
    fetch(`http://localhost:8080/api/meters/addresses/${userId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке адресов');
        return response.json();
    })
    .then(addresses => {
        const select = document.getElementById('requestAddressId');
        select.innerHTML = '<option value="">Выберите адрес</option>';
        addresses.forEach(address => {
            const option = document.createElement('option');
            option.value = address.id;
            option.text = address.address;
            select.appendChild(option);
        });
    })
    .catch(error => showMessage(error.message, 'error'));
}

function submitRequest() {
    const userId = 1;
    const addressId = document.getElementById('requestAddressId').value;
    const title = document.getElementById('requestTitle').value;
    const comment = document.getElementById('requestComment').value;

    if (!addressId || !title) {
        showMessage('Пожалуйста, заполните обязательные поля', 'error');
        return;
    }

    const data = {
        userId: userId,
        addressId: parseInt(addressId),
        title: title,
        comment: comment || null
    };

    fetch('http://localhost:8080/api/requests', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при подаче заявки');
        return response.json();
    })
    .then(result => {
        showMessage('Заявка успешно подана!', 'success');
        document.getElementById('requestTitle').value = '';
        document.getElementById('requestComment').value = '';
        loadRequestHistory();
    })
    .catch(error => showMessage(error.message, 'error'));
}

function loadRequestHistory() {
    const userId = 1;
    fetch(`http://localhost:8080/api/requests/history/${userId}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке истории заявок');
        return response.json();
    })
    .then(requests => {
        const requestsDiv = document.querySelector('.requests-history');
        requestsDiv.innerHTML = '';
        requests.forEach(request => {
            const requestItem = document.createElement('div');
            requestItem.className = 'request-item';
            requestItem.innerHTML = `
                <div class="title">${request.title}</div>
                <div class="details">
                    <span>Адрес: ${getAddressName(request.addressId)}</span>
                    <span class="date">Дата: ${new Date(request.date).toLocaleString()}</span>
                    <span>Комментарий: ${request.comment || 'Нет комментария'}</span>
                </div>
            `;
            requestsDiv.appendChild(requestItem);
        });
    })
    .catch(error => showMessage(error.message, 'error'));
}

function getAddressName(addressId) {
    // Здесь можно добавить реальный запрос к /api/meters/addresses/{addressId}, но для простоты используем заглушку
    return 'ул. Ленина, д. 10';
}
function showMessage(message, type) {
    const messageDiv = document.createElement('div');
    messageDiv.textContent = message;
    messageDiv.className = `message ${type}`;
    const content = document.querySelector('.content') || document.getElementById('requests-page').querySelector('.content');
    content.insertBefore(messageDiv, content.querySelector('h2') || content.firstChild);
    setTimeout(() => messageDiv.remove(), 5000);
}