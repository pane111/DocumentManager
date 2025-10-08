async function loadHealth() {
  print("Loading items")
  const el = document.getElementById('health');
  try {
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
    const r = await fetch('/api/docs');
    if (!r.ok) { ul.innerHTML = `HTTP ${r.status}`; return; }
    const data = await r.json();
    ul.innerHTML = '';
    (Array.isArray(data) ? data : []).forEach(item => {
      const li = document.createElement('li');
      li.textContent = `${item.id}: ${item.title} — ${item.content}`;
      li.style.cursor = 'pointer'
      li.classList.add('doc-list');
      li.addEventListener('click', () => {
        window.location.href = `detail.html?id=${encodeURIComponent(item.id)}`;
      })

      ul.appendChild(li);
    });
  } catch (e) {
    ul.innerHTML = `Error: ${e}`;
  }
}


document.getElementById('upload').addEventListener('click', () => {
  print("Clicked the upload button, should redirect now")
  window.location.href='upload.html'
})

document.getElementById('reload').addEventListener('click', loadItems);


window.addEventListener('DOMContentLoaded', () => {
  loadHealth();
  loadItems();
});
