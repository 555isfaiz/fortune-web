const REFRESH_MS = 5000;
let refreshTimer = null;

function toggleAutoRefresh() {
    const on = document.getElementById("autoRefresh").checked;
    if (on) {
        refreshTimer = setInterval(loadFortune, REFRESH_MS);
    } else {
        clearInterval(refreshTimer);
        refreshTimer = null;
    }
}

async function loadFortune() {
    const el = document.getElementById("fortune");
    const authorEl = document.getElementById("author");
    el.textContent = "...";
    authorEl.textContent = "";
    try {
        const res = await fetch("/api/fortune");
        const data = await res.json();
        if (!data) {
            el.textContent = "No fortunes yet. Add one below!";
            return;
        }
        el.textContent = data.message;
        authorEl.textContent = data.author ? "— " + data.author : "";
    } catch (e) {
        el.textContent = "Something went wrong";
    }
}

async function addFortune() {
    const msgEl = document.getElementById("newMessage");
    const authorInput = document.getElementById("newAuthor");
    const status = document.getElementById("addMsg");
    const message = msgEl.value.trim();
    if (!message) {
        status.textContent = "Cannot add empty stuff";
        return;
    }
    const body = { message };
    const author = authorInput.value.trim();
    if (author) body.author = author;
    try {
        const res = await fetch("/api/fortune", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (res.ok) {
            status.textContent = "Added!";
            msgEl.value = "";
            authorInput.value = "";
        } else {
            status.textContent = "Failed to add (" + res.status + ").";
        }
    } catch (e) {
        status.textContent = "Failed to add.";
    }
}
