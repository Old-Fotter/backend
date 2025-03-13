// payments.js
import { auth } from "./firebase.js";
import { getCurrentUserId } from "./authService.js";

document.addEventListener("DOMContentLoaded", () => {
  // Ждём, пока Firebase разберётся с авторизацией
  auth.onAuthStateChanged(user => {
    // Обновим футер (Email пользователя или “Гость”)
    const profileSpan = document.querySelector("footer .profile span");
    if (profileSpan) {
      if (user) {
        profileSpan.textContent = user.email;
      } else {
        profileSpan.textContent = "Гость";
      }
    }

    // Если пользователь есть – тогда подгружаем платежи
    if (user) {
      loadPayments();
    }
  });
});

// Загружаем список платежей
export function loadPayments() {
  const userId = getCurrentUserId();
  if (!userId) {
    console.error("Ошибка: не удалось получить userId");
    return;
  }

  fetch(`http://localhost:8080/api/payments/${userId}`)
    .then(response => {
      if (!response.ok) throw new Error("Ошибка загрузки платежей");
      return response.json();
    })
    .then(data => {
      const tbody = document.getElementById("paymentsTable").querySelector("tbody");
      tbody.innerHTML = "";
      if (data.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8">Нет платежей</td></tr>`;
      } else {
        data.forEach(payment => {
          const row = document.createElement("tr");
          const isPaid = payment.paid;
          const buttonHtml = isPaid
            ? `<button disabled>Оплачено</button>`
            : `<button onclick="markAsPaid(${payment.id}, ${payment.amount})">Оплатить</button>`;

          let createdAt = payment.createdAt ? new Date(payment.createdAt).toLocaleString() : "";
          row.innerHTML = `
            <td>${payment.readingId || '-'}</td>
            <td>${payment.address}</td>
            <td>${payment.meterCategory}</td>
            <td>${payment.difference}</td>
            <td>${payment.tariff}</td>
            <td>${payment.amount}</td>
            <td>${createdAt}</td>
            <td>${buttonHtml}</td>
          `;
          tbody.appendChild(row);
        });
      }
    })
    .catch(error => console.error("Ошибка при загрузке платежей:", error));
}

// Оплата
window.markAsPaid = function markAsPaid(paymentId, amount) {
  fetch(`http://localhost:8080/api/payments/${paymentId}/pay`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' }
  })
    .then(response => {
      if (!response.ok) throw new Error("Ошибка при оплате");
      return response.json();
    })
    .then(() => {
      alert("Фиктивная оплата на сумму: " + amount);
      loadPayments(); // Перезагрузка платежей
    })
    .catch(error => {
      console.error("Ошибка:", error);
      alert("Ошибка при оплате");
    });
};
