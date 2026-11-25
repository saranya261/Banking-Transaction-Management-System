/* Visual effects only: cursor spotlight, 3D card tilt, button ripple, card shine. */
(() => {
  document.addEventListener('mousemove', e => {
    const g = e.target.closest && e.target.closest('.glass');
    if (g) { const r = g.getBoundingClientRect(); g.style.setProperty('--mx', (e.clientX - r.left) + 'px'); g.style.setProperty('--my', (e.clientY - r.top) + 'px'); }
    const c = e.target.closest && e.target.closest('.bank-card');
    document.querySelectorAll('.bank-card').forEach(b => {
      if (b !== c) { b.style.transform = ''; return; }
      const r = b.getBoundingClientRect();
      const x = (e.clientX - r.left) / r.width - .5, y = (e.clientY - r.top) / r.height - .5;
      b.style.transform = `perspective(900px) rotateY(${x * 10}deg) rotateX(${-y * 10}deg) scale(1.02)`;
    });
  });
  document.addEventListener('mouseleave', () => document.querySelectorAll('.bank-card').forEach(b => b.style.transform = ''));
  document.addEventListener('click', e => {
    const b = e.target.closest('.btn.primary'); if (!b) return;
    const r = b.getBoundingClientRect(), s = Math.max(r.width, r.height) / 2;
    const d = document.createElement('span'); d.className = 'ripple';
    d.style.cssText = `width:${s}px;height:${s}px;left:${e.clientX - r.left - s / 2}px;top:${e.clientY - r.top - s / 2}px`;
    b.appendChild(d); setTimeout(() => d.remove(), 650);
  });
  new MutationObserver(() => {
    document.querySelectorAll('.bank-card:not(:has(.shine))').forEach(b => { const s = document.createElement('div'); s.className = 'shine'; b.appendChild(s); });
  }).observe(document.getElementById('root'), { childList: true, subtree: true });
})();
