async function loadHealth() {

  const el = document.getElementById('health');
  try {
    const r = await fetch('/api/docs');
    el.textContent = r.ok ? "Backend reachable ✅" : `HTTP ${r.status}`;
  } catch (e) {
    el.textContent = `Error: ${e}`;
  }
}

async function searchItems() {
  const ul = document.getElementById('list');
  ul.innerHTML = 'Loading…';
  try {
    const query = document.getElementById('search').value.trim();
    if (!query) return;


    const r = await fetch(`/api/docs/search?query=${encodeURIComponent(query)}`);
    if (!r.ok) { ul.innerHTML = `HTTP ${r.status}`; return; }
    const data = await r.json();
    ul.innerHTML = '';
    (Array.isArray(data) ? data : []).forEach(item => {
      const li = document.createElement('li');
      li.textContent = `${item.id}: ${item.title}`;


      if (item.status !== "processing") {
        li.style.cursor = 'pointer'
        li.classList.add('doc-list');
        li.addEventListener('click', () => {
          window.location.href = `detail.html?id=${encodeURIComponent(item.id)}`;
        })
      }
      else {
        li.textContent += ' - PROCESSING';
        li.classList.add('doc-unproc');
      }


      ul.appendChild(li);
    });
    if (ul.childElementCount === 0) {
      ul.innerHTML = 'No documents matched your query.';
    }
  } catch (e) {
    ul.innerHTML = `Error: ${e}`;
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
      li.textContent = `${item.id}: ${item.title}`;


      if (item.status !== "processing") {
        li.style.cursor = 'pointer'
        li.classList.add('doc-list');
        li.addEventListener('click', () => {
          window.location.href = `detail.html?id=${encodeURIComponent(item.id)}`;
        })
      }
      else {
        li.textContent += ' - PROCESSING';
        li.classList.add('doc-unproc');
      }


      ul.appendChild(li);
    });
  } catch (e) {
    ul.innerHTML = `Error: ${e}`;
  }
}


document.getElementById('upload').addEventListener('click', () => {
  window.location.href='upload.html'
})

document.getElementById('reload').addEventListener('click', loadItems);

document.getElementById('searchbtn').addEventListener('click', searchItems);


window.addEventListener('DOMContentLoaded', () => {
  loadHealth();
  loadItems();
});
