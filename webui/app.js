async function loadHealth() {
  const el = document.getElementById('health');
  try {
    // If you don’t have a dedicated /api/health, just check docs
    const r = await fetch('/api/docs');
    el.textContent = r.ok ? "Backend reachable ✅" : `HTTP ${r.status}`;
  } catch (e) {
    el.textContent = `Error: ${e}`;
  }
}

async function loadItems() {
  const ul = document.getElementById('list');
  ul.innerHTML = 'Loading…';
  try {
    const r = await fetch('/api/docs');   // <-- real backend endpoint
    if (!r.ok) { ul.innerHTML = `HTTP ${r.status}`; return; }
    const data = await r.json();
    ul.innerHTML = '';
    (Array.isArray(data) ? data : []).forEach(item => {
      const li = document.createElement('li');
      li.textContent = `${item.id}: ${item.title} — ${item.content}`;
      ul.appendChild(li);
    });
  } catch (e) {
    ul.innerHTML = `Error: ${e}`;
  }
}



document.getElementById('reload').addEventListener('click', loadItems);

// run once on page load
window.addEventListener('DOMContentLoaded', () => {
  loadHealth();
  loadItems();
});
