// services.js

import { getCurrentUserId } from './authService.js';
import { auth } from './firebase.js';

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

    if (tabId === 'orders') {
        loadOrderHistory();
    }
}

window.showTab = showTab; // Чтобы HTML-кнопка могла вызывать showTab(...)

// ====== Загрузка списка услуг по категории ======
function loadServicesByCategory(category) {
    const url = `http://localhost:8080/api/services/category/${category}`;
    fetch(url, {
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке услуг');
        return response.json();
    })
    .then(services => {
        const servicesList = document.getElementById('services-list');
        servicesList.innerHTML = '';
        if (services.length === 0) {
            servicesList.innerHTML = '<p>Нет услуг в этой категории</p>';
        } else {
            services.forEach(service => {
                const serviceCard = document.createElement('div');
                serviceCard.className = 'service-card';
                serviceCard.innerHTML = `
                    <h3>${service.name}</h3>
                    <div class="details">
                        <p>Категория: ${service.category}</p>
                        <p>Описание: ${service.description}</p>
                        <p>Пример: ${service.example}</p>
                        <p>Стоимость: ${service.price} руб.</p>
                    </div>
                    <button class="select-service-btn" data-service-id="${service.id}">Заказать</button>
                `;
                servicesList.appendChild(serviceCard);
            });
            document.querySelectorAll('.select-service-btn').forEach(button => {
                button.addEventListener('click', () => showOrderForm(parseInt(button.dataset.serviceId)));
            });
        }
    })
    .catch(error => showMessage(error.message, 'error'));
}
window.loadServicesByCategory = loadServicesByCategory; // чтобы вызывать из HTML кнопок

// ====== Показ формы заказа ======
function showOrderForm(serviceId) {
    document.getElementById('order-form').style.display = 'block';
    loadServiceDetails(serviceId);
    loadAddressesForOrder();
    setDefaultDateTime();
}

// ====== Подробности услуги ======
function loadServiceDetails(serviceId) {
    fetch(`http://localhost:8080/api/services/${serviceId}`, {
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    })
    .then(response => {
        if (!response.ok) {
            if (response.status === 404) throw new Error(`Услуга с ID ${serviceId} не найдена`);
            throw new Error('Ошибка при загрузке деталей услуги');
        }
        return response.json();
    })
    .then(service => {
        document.getElementById('selectedService').textContent = `${service.name} (${service.price} руб.)`;
        document.getElementById('serviceId').value = service.id;
    })
    .catch(error => showMessage(error.message, 'error'));
}

// ====== Загрузка адресов пользователя ======
function loadAddressesForOrder() {
    const userId = getCurrentUserId();
    if (!userId) {
        console.error("Ошибка: не удалось получить userId");
        return;
    }

    fetch(`http://localhost:8080/api/meters/addresses/${userId}`, {
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке адресов');
        return response.json();
    })
    .then(addresses => {
        const select = document.getElementById('addressId');
        select.innerHTML = '<option value="">Выберите адрес</option>';
        if (addresses.length === 0) {
            select.innerHTML += '<option value="" disabled>Нет доступных адресов</option>';
        } else {
            addresses.forEach(address => {
                const option = document.createElement('option');
                option.value = address.id;
                option.text = address.address;
                select.appendChild(option);
            });
        }
    })
    .catch(error => showMessage(error.message, 'error'));
}

// ====== Подставляем текущее время в поле dateTime ======
function setDefaultDateTime() {
    const dateTimeInput = document.getElementById('dateTime');
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    dateTimeInput.value = `${year}-${month}-${day}T${hours}:${minutes}`;
}

// ====== Отправка заказа ======
function submitServiceOrder() {
    const userId = getCurrentUserId();
    if (!userId) {
        console.error("Ошибка: не удалось получить userId");
        return;
    }

    const serviceId = document.getElementById('serviceId').value;
    const addressId = document.getElementById('addressId').value;
    const dateTime = document.getElementById('dateTime').value;
    const comment = document.getElementById('comment').value;

    if (!serviceId || !addressId || !dateTime) {
        showMessage('Пожалуйста, заполните обязательные поля', 'error');
        return;
    }

    const data = {
        userId: userId,
        serviceId: parseInt(serviceId),
        addressId: parseInt(addressId),
        dateTime: dateTime,
        comment: comment || null
    };

    fetch('http://localhost:8080/api/services/orders', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при создании заказа');
        return response.json();
    })
    .then(result => {
        showMessage('Заказ успешно создан!', 'success');
        hideOrderForm();
        loadOrderHistory();
    })
    .catch(error => showMessage(error.message, 'error'));
}
window.submitServiceOrder = submitServiceOrder; // чтобы кнопка могла вызвать

// ====== Скрыть форму заказа ======
function hideOrderForm() {
    document.getElementById('order-form').style.display = 'none';
}
window.hideOrderForm = hideOrderForm;

// ====== Загрузка истории заказов ======
function loadOrderHistory() {
    const userId = getCurrentUserId();
    if (!userId) {
        console.error("Ошибка: не удалось получить userId");
        return;
    }

    fetch(`http://localhost:8080/api/services/orders/history/${userId}`, {
        method: 'GET',
        headers: { 'Accept': 'application/json' }
    })
    .then(response => {
        if (!response.ok) throw new Error('Ошибка при загрузке истории заказов');
        return response.json();
    })
    .then(orders => {
        const ordersDiv = document.querySelector('.orders-history');
        ordersDiv.innerHTML = '';
        if (orders.length === 0) {
            ordersDiv.innerHTML = '<p>Нет истории заказов</p>';
        } else {
            orders.forEach(order => {
                // Здесь можно доработать: подгрузка деталей услуги, адреса, статуса и т.д.
                const orderItem = document.createElement('div');
                orderItem.className = 'order-item';
                orderItem.innerHTML = `
                    <div class="title">Заказ №${order.id}</div>
                    <div class="details">
                        <span>Адрес: ${getAddressName(order.addressId)}</span>
                        <span>Дата: ${new Date(order.dateTime).toLocaleString()}</span>
                        <span>Комментарий: ${order.comment || 'Нет комментария'}</span>
                        <span class="status">Статус: ${order.status}</span>
                    </div>
                `;
                ordersDiv.appendChild(orderItem);
            });
        }
    })
    .catch(error => showMessage(error.message, 'error'));
}

// Заглушка-метод для отображения адреса по ID
function getAddressName(addressId) {
    return 'ул. Ленина, д. 10';
}

// Вывод сообщений
function showMessage(message, type) {
    const messageDiv = document.createElement('div');
    messageDiv.textContent = message;
    messageDiv.className = `message ${type}`;
    const content = document.querySelector('.content');
    content.insertBefore(messageDiv, content.querySelector('h2') || content.firstChild);
    setTimeout(() => messageDiv.remove(), 5000);
}

// ====== При загрузке страницы делаем: ======
document.addEventListener('DOMContentLoaded', function() {
    // Сразу показываем вкладку категорий
    showTab('categories');

    // Аутентификация, чтобы внизу показать email
    auth.onAuthStateChanged(user => {
      const profileSpan = document.querySelector('.profile span');
      if (!profileSpan) return; // Мало ли

      if (user) {
        profileSpan.textContent = user.email;
      } else {
        profileSpan.textContent = "Не авторизован";
      }
    });
});
