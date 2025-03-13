import { auth } from "./firebase.js";
import { logout } from "./authService.js";

document.addEventListener("DOMContentLoaded", () => {

  auth.onAuthStateChanged(user => {
    const userEmailEl       = document.getElementById("userEmail");
    const footerUserEmailEl = document.getElementById("footerUserEmail");

    if (user) {
      if (userEmailEl)       userEmailEl.textContent       = user.email;
      if (footerUserEmailEl) footerUserEmailEl.textContent = user.email;

      const profilePic = document.getElementById("profilePic");
      if (profilePic) {
        profilePic.src = user.photoURL || "profile-placeholder.png";
      }

    } else {

      window.location.href = "login.html";
    }
  });

  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
      logout();
    });
  }
});
