import { auth, onAuthStateChanged } from "./firebase.js";
import {
  setPersistence,
  browserLocalPersistence,
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
  sendEmailVerification,
  signOut
} from "https://www.gstatic.com/firebasejs/10.0.0/firebase-auth.js";

console.log(" authService.js загружен!");

setPersistence(auth, browserLocalPersistence)
    .then(() => console.log(" Firebase запоминает пользователя"))
    .catch(error => console.error("Ошибка персистентности:", error));

export const registerUser = async (email, password) => {
    try {
        const userCredential = await createUserWithEmailAndPassword(auth, email, password);
        const user = userCredential.user;
        await sendEmailVerification(user);
        alert("Письмо с подтверждением отправлено! Проверьте почту.");
    } catch (error) {
        console.error("Ошибка регистрации:", error.message);
    }
};

export const loginUser = async (email, password) => {
    try {
        console.log("Попытка входа пользователя:", email);
        const userCredential = await signInWithEmailAndPassword(auth, email, password);
        console.log("Успешный вход:", userCredential.user);
        alert("Вы успешно вошли!");
        console.log("Перенаправление на /index.html...");
        window.location.href = "http://localhost:8081/index.html";
    } catch (error) {
        console.error("Ошибка входа:", error.message);
        alert("Ошибка: " + error.message);
    }
};

let currentUserId = null;

onAuthStateChanged(auth, (user) => {
    if (user) {
        currentUserId = user.uid;
        console.log(" Пользователь найден:", user.uid);
    } else {
        currentUserId = null;
        console.error(" Пользователь не найден!");
    }
});

export function logout() {
    signOut(auth).then(() => {
        console.log(" Пользователь вышел");
        window.location.href = "/login.html";
    }).catch(error => {
        console.error("Ошибка при выходе: ", error);
    });
}

document.addEventListener("DOMContentLoaded", function () {
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }
});

export function getCurrentUserId() {
    return currentUserId;
}
