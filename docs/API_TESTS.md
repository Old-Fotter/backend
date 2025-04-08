# Тесты + маршруты.

Рекомендуется использовать postman.

Заменяйте (193.176.153.153:8080) на свой сервер или localhost.


## Аутентификация  
`http://193.176.153.153:8080`

- **POST**
  ➤ `http://193.176.153.153:8080/auth/register`  
   Тело:
  ```json
  {
    "email": "test@example.com",
    "password": "12345678"
  }
  ```

---

##  Адреса и Счётчики
`http://193.176.153.153:8080/api/meters`

- **GET**   ➤ `http://193.176.153.153:8080/api/meters/addresses/{userId}`

- **POST**
  ➤ `http://193.176.153.153:8080/api/meters/addresses`  
   Тело:
  ```json
  {
    "userId": "userId",
    "address": "ул. Ленина, д. 10"
  }
  ```

- **GET**
  ➤ `http://193.176.153.153:8080/api/meters/address/{addressId}`

---

##  Показания счётчиков
`http://193.176.153.153:8080/api/meter-readings`

- **POST** 
  ➤ `http://193.176.153.153:8080/api/meter-readings`  
  Тело:
  ```json
  {
    "userId": "userId",
    "meterId": 1,
    "currentValue": 42.5
  }
  ```

- **GET** `/history/{userId}`  
  ➤ `http://193.176.153.153:8080/api/meter-readings/history/abc123`

- **GET** `/history-with-address/{userId}`  
  ➤ `http://193.176.153.153:8080/api/meter-readings/history-with-address/abc123`

- **GET** `/{id}`  
  ➤ `http://193.176.153.153:8080/api/meter-readings/5`

---

## Платежи
`http://193.176.153.153:8080/api/payments`

- **GET** `/{userId}`  
  ➤ `http://193.176.153.153:8080/api/payments/abc123`

- **POST** `/{paymentId}/pay`  
  ➤ `http://193.176.153.153:8080/api/payments/10/pay`  
   Без тела

---

## Заявки
`http://193.176.153.153:8080/api/requests`

- **POST**   ➤ `http://193.176.153.153:8080/api/requests`  
   Тело:
  ```json
  {
    "userId": "abc123",
    "addressId": 1,
    "title": "Проблема с газом",
    "comment": "Утечка"
  }
  ```

- **GET** `/history/{userId}`  
  ➤ `http://193.176.153.153:8080/api/requests/history/abc123`

- **GET** `/{id}`  
  ➤ `http://193.176.153.153:8080/api/requests/5`

---

##  Услуги и заказы
`http://193.176.153.153:8080/api/services`

- **GET** `/`  
  ➤ `http://193.176.153.153:8080/api/services`

- **GET** `/category/{category}`  
  ➤ `http://193.176.153.153:8080/api/services/category/Водоснабжение`

- **GET** `/{serviceId}`  
  ➤ `http://193.176.153.153:8080/api/services/2`

- **POST** `/orders`  
  ➤ `http://193.176.153.153:8080/api/services/orders`  
  Тело:
  ```json
  {
    "userId": "abc123",
    "serviceId": 1,
    "addressId": 1,
    "dateTime": "2025-04-09T14:30:00",
    "comment": "Сделайте как можно раньше"
  }
  ```

- **GET** `/orders/history/{userId}`  
  ➤ `http://193.176.153.153:8080/api/services/orders/history/abc123`

