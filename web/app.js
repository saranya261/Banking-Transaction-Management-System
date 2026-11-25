/* ABC Bank — front-end. Talks to WebServer.java (which calls your AccountDAO). */
(() => {
  'use strict';

  // ---------------------------------------------------------------- icons
  const P = {
    bank: '<path d="M3 21h18M5 21V10m4 11V10m6 11V10m4 11V10M2 10l10-7 10 7"/>',
    user: '<path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/>',
    shield: '<path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><path d="m9 12 2 2 4-4"/>',
    lock: '<rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/>',
    arrow: '<path d="M5 12h14M13 6l6 6-6 6"/>',
    left: '<path d="M19 12H5m6-6-6 6 6 6"/>',
    eye: '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8S1 12 1 12z"/><circle cx="12" cy="12" r="3"/>',
    eyeoff: '<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24M1 1l22 22"/>',
    home: '<path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><path d="M9 22V12h6v10"/>',
    down: '<path d="M12 5v14m-6-6 6 6 6-6"/>',
    up: '<path d="M12 19V5m-6 6 6-6 6 6"/>',
    swap: '<path d="M7 4 3 8l4 4M3 8h14M17 20l4-4-4-4m4 4H7"/>',
    list: '<path d="M8 6h13M8 12h13M8 18h13M3 6h.01M3 12h.01M3 18h.01"/>',
    users: '<path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/>',
    plus: '<path d="M12 5v14M5 12h14"/>',
    trash: '<path d="M3 6h18M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2m3 0v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/>',
    search: '<circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/>',
    sun: '<circle cx="12" cy="12" r="5"/><path d="M12 1v2M12 21v2M4.2 4.2l1.4 1.4M18.4 18.4l1.4 1.4M1 12h2M21 12h2M4.2 19.8l1.4-1.4M18.4 5.6l1.4-1.4"/>',
    moon: '<path d="M21 12.8A9 9 0 1 1 11.2 3a7 7 0 0 0 9.8 9.8z"/>',
    logout: '<path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4m7 14 5-5-5-5m5 5H9"/>',
    check: '<path d="M20 6 9 17l-5-5"/>',
    x: '<path d="M18 6 6 18M6 6l12 12"/>',
    alert: '<circle cx="12" cy="12" r="10"/><path d="M12 8v4m0 4h.01"/>',
    menu: '<path d="M3 12h18M3 6h18M3 18h18"/>',
    wallet: '<path d="M21 12V7a2 2 0 0 0-2-2H5a2 2 0 0 1 0-4h14v4"/><path d="M3 5v14a2 2 0 0 0 2 2h16v-5"/><path d="M18 12a2 2 0 0 0 0 4h4v-4z"/>',
    bolt: '<path d="M13 2 3 14h9l-1 8 10-12h-9z"/>',
    inbox: '<path d="M22 12h-6l-2 3h-4l-2-3H2"/><path d="M5.45 5.11 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"/>',
    refresh: '<path d="M23 4v6h-6M1 20v-6h6"/><path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"/>',
    hash: '<path d="M4 9h16M4 15h16M10 3 8 21M16 3l-2 18"/>'
  };
  const ic = (n, cls = '') => `<svg class="i ${cls}" viewBox="0 0 24 24">${P[n]}</svg>`;

  // ---------------------------------------------------------------- helpers
  const $ = (s, r = document) => r.querySelector(s);
  const $$ = (s, r = document) => [...r.querySelectorAll(s)];
  const root = $('#root');
  const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
  const inr = new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', minimumFractionDigits: 2 });
  const money = n => inr.format(Number(n) || 0);
  const initials = n => (String(n || '?').trim().split(/\s+/).map(w => w[0]).join('').slice(0, 2) || '?').toUpperCase();

  function parseDate(s) {
    const d = new Date(String(s).replace(' ', 'T'));
    return isNaN(d) ? null : d;
  }
  const fmtDate = s => {
    const d = parseDate(s);
    if (!d) return s || '';
    return d.toLocaleString('en-IN', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
  };
  const kind = t => /deposit/i.test(t) ? 'dep' : /withdraw/i.test(t) ? 'wd' : 'tr';

  const S = {
    token: sessionStorage.getItem('abc_token') || '',
    user: null,
    theme: localStorage.getItem('abc_theme') || 'dark',
    page: 'overview',
    hideBal: false,
    history: [],
    customers: [],
    txs: [],
    total: 0
  };
  document.documentElement.setAttribute('data-theme', S.theme);

  async function api(path, method = 'GET', body) {
    const res = await fetch('/api/' + path, {
      method,
      headers: { 'Content-Type': 'application/json', ...(S.token ? { Authorization: 'Bearer ' + S.token } : {}) },
      body: body ? JSON.stringify(body) : undefined
    });
    let data = {};
    try { data = await res.json(); } catch (e) { /* ignore */ }
    if (res.status === 401 && S.user) { toast('Session expired. Please sign in again.', 'er'); doLogout(true); throw new Error('expired'); }
    if (!res.ok) throw new Error(data.error || 'Something went wrong');
    return data;
  }

  function toast(msg, type = 'ok') {
    const el = document.createElement('div');
    el.className = 'toast ' + type;
    el.innerHTML = `<div class="t-ico">${ic(type === 'ok' ? 'check' : 'alert')}</div><div>${esc(msg)}</div>`;
    $('#toasts').appendChild(el);
    setTimeout(() => { el.classList.add('out'); setTimeout(() => el.remove(), 300); }, 3800);
  }

  function countUp(el, to, ms = 1100) {
    const start = performance.now();
    const from = 0;
    const tick = now => {
      const p = Math.min(1, (now - start) / ms);
      const e = 1 - Math.pow(1 - p, 4);
      el.textContent = money(from + (to - from) * e);
      if (p < 1) requestAnimationFrame(tick);
    };
    requestAnimationFrame(tick);
  }

  function toggleTheme() {
    S.theme = S.theme === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', S.theme);
    localStorage.setItem('abc_theme', S.theme);
    $$('[data-act="theme"]').forEach(b => b.innerHTML = ic(S.theme === 'dark' ? 'sun' : 'moon'));
  }
  const themeBtn = () => `<button class="icon-btn" data-act="theme" title="Toggle theme">${ic(S.theme === 'dark' ? 'sun' : 'moon')}</button>`;

  // ---------------------------------------------------------------- landing
  function renderLanding() {
    root.innerHTML = `
    <div class="view landing">
      <div class="topbar">
        <div class="logo"><div class="logo-mark">${ic('bank')}</div>ABC BANK</div>
        ${themeBtn()}
      </div>
      <div class="hero">
        <div>
          <div class="pill"><span class="dot"></span> Secure online banking</div>
          <h1>Banking that feels <span class="grad-text">effortless.</span></h1>
          <p class="lead">Check balances, move money and track every transaction in real time — all in one beautifully simple place.</p>
          <div class="feature-row">
            <div class="feature">${ic('bolt')} Instant transfers</div>
            <div class="feature">${ic('shield')} Protected sessions</div>
            <div class="feature">${ic('list')} Full history</div>
          </div>
        </div>
        <div class="role-stack">
          <button class="glass role-card" data-role="CUSTOMER">
            <div class="role-ico">${ic('user')}</div>
            <div><h3>Customer Login</h3><p>View your balance, deposit, withdraw and transfer funds.</p></div>
            <span class="arrow">${ic('arrow')}</span>
          </button>
          <button class="glass role-card" data-role="ADMIN">
            <div class="role-ico">${ic('shield')}</div>
            <div><h3>Admin Login</h3><p>Manage customer accounts and monitor every transaction.</p></div>
            <span class="arrow">${ic('arrow')}</span>
          </button>
        </div>
      </div>
    </div>`;
    $$('[data-role]').forEach(b => b.onclick = () => renderLogin(b.dataset.role));
  }

  // ---------------------------------------------------------------- login
  function renderLogin(role) {
    const admin = role === 'ADMIN';
    root.innerHTML = `
    <div class="login-wrap view">
      <div class="glass login-card">
        <div class="login-side">
          <div class="logo"><div class="logo-mark">${ic('bank')}</div>ABC BANK</div>
          <div>
            <h2>${admin ? 'Command center for your bank.' : 'Welcome back to smarter banking.'}</h2>
            <p>${admin ? 'Oversee accounts, review activity and keep everything running smoothly.' : 'Your money, your transactions, your control — always at your fingertips.'}</p>
          </div>
        </div>
        <form class="login-form" id="lf" autocomplete="off">
          <button type="button" class="back" id="bk">${ic('left')} Back</button>
          <h2>${admin ? 'Admin Sign In' : 'Customer Sign In'}</h2>
          <p class="muted">Enter your credentials to continue.</p>
          <div class="field"><label>Username</label>
            <div class="input-wrap">${ic('user')}<input id="u" placeholder="Enter username" autofocus required></div></div>
          <div class="field"><label>Password</label>
            <div class="input-wrap has-eye">${ic('lock')}<input id="p" type="password" placeholder="Enter password" required>
              <button type="button" class="eye" id="eye">${ic('eye')}</button></div></div>
          <div id="err"></div>
          <button class="btn primary" id="go" type="submit">Sign in ${ic('arrow')}</button>
        </form>
      </div>
    </div>`;
    $('#bk').onclick = renderLanding;
    $('#eye').onclick = () => {
      const p = $('#p'); const show = p.type === 'password';
      p.type = show ? 'text' : 'password';
      $('#eye').innerHTML = ic(show ? 'eyeoff' : 'eye');
    };
    $('#lf').onsubmit = async e => {
      e.preventDefault();
      const btn = $('#go'); btn.disabled = true; btn.innerHTML = '<div class="spinner"></div> Signing in…';
      $('#err').innerHTML = '';
      try {
        const r = await api('login', 'POST', { role, username: $('#u').value.trim(), password: $('#p').value });
        S.token = r.token; sessionStorage.setItem('abc_token', r.token);
        await boot();
      } catch (ex) {
        $('#err').innerHTML = `<div class="form-error">${esc(ex.message)}</div>`;
        btn.disabled = false; btn.innerHTML = `Sign in ${ic('arrow')}`;
      }
    };
  }

  async function doLogout(silent) {
    try { if (!silent) await api('logout', 'POST'); } catch (e) { /* ignore */ }
    S.token = ''; S.user = null; S.page = 'overview';
    sessionStorage.removeItem('abc_token');
    renderLanding();
  }

  // ---------------------------------------------------------------- shell
  function shell(navHtml, role, name) {
    root.innerHTML = `
    <div class="app view">
      <aside class="sidebar" id="sb">
        <div class="logo"><div class="logo-mark">${ic('bank')}</div>ABC BANK</div>
        <nav class="nav" id="nav">${navHtml}</nav>
        <div class="side-foot">
          <div class="user-chip"><div class="avatar">${esc(initials(name))}</div>
            <div><div class="nm">${esc(name)}</div><div class="rl">${role}</div></div></div>
          <button class="btn ghost" id="lo">${ic('logout')} Sign out</button>
        </div>
      </aside>
      <main class="main" id="main"></main>
    </div>`;
    $('#lo').onclick = () => doLogout();
  }
  const pageHead = (title, sub, extra = '') => `
    <div class="page-head">
      <div style="display:flex;gap:14px;align-items:center">
        <button class="icon-btn menu-btn" id="mb">${ic('menu')}</button>
        <div><h1>${title}</h1><p>${sub}</p></div>
      </div>
      <div class="head-actions">${extra}${themeBtn()}</div>
    </div>`;
  function wireHead() {
    const mb = $('#mb'); if (mb) mb.onclick = () => $('#sb').classList.toggle('open');
  }

  // ================================================================ CUSTOMER
  const CUST_NAV = `
    <div class="nav-label">Menu</div>
    <button data-page="overview">${ic('home')} Overview</button>
    <button data-page="history">${ic('list')} Transactions</button>
    <div class="nav-label" style="margin-top:16px">Quick actions</div>
    <button data-modal="deposit">${ic('down')} Deposit</button>
    <button data-modal="withdraw">${ic('up')} Withdraw</button>
    <button data-modal="transfer">${ic('swap')} Transfer</button>`;

  function renderCustomer() {
    shell(CUST_NAV, 'Customer', S.user.name);
    $$('#nav [data-page]').forEach(b => b.onclick = () => { S.page = b.dataset.page; $('#sb').classList.remove('open'); customerPage(); });
    $$('#nav [data-modal]').forEach(b => b.onclick = () => { $('#sb').classList.remove('open'); openMoneyModal(b.dataset.modal); });
    customerPage();
  }

  async function refreshCustomer() {
    const [me, hist] = await Promise.all([api('me'), api('history')]);
    S.user = { ...S.user, ...me };
    S.history = hist;
  }

  async function customerPage() {
    $$('#nav [data-page]').forEach(b => b.classList.toggle('active', b.dataset.page === S.page));
    const main = $('#main');
    main.innerHTML = pageHead('Loading…', ' ') + `<div class="grid g-hero"><div class="skeleton" style="height:250px"></div><div class="skeleton" style="height:250px"></div></div>`;
    wireHead();
    try { await refreshCustomer(); } catch (e) { if (e.message !== 'expired') toast(e.message, 'er'); return; }
    S.page === 'history' ? customerHistory() : customerOverview();
  }

  function sums() {
    const o = { dep: 0, wd: 0, tr: 0, n: S.history.length };
    S.history.forEach(t => { o[kind(t.type)] += t.amount; });
    return o;
  }

  function txRow(t, i) {
    const k = kind(t.type);
    const incoming = k === 'dep';
    const cls = k === 'dep' ? 'c-good' : k === 'wd' ? 'c-bad' : 'c-acc';
    const icon = k === 'dep' ? 'down' : k === 'wd' ? 'up' : 'swap';
    return `<div class="tx" style="animation-delay:${Math.min(i, 12) * 40}ms">
      <div class="tx-ico ${cls}">${ic(icon)}</div>
      <div class="tx-main"><div class="tx-title">${esc(t.type)}</div><div class="tx-date">${esc(fmtDate(t.date))}</div></div>
      <div class="tx-amt ${incoming ? 'in' : 'out'}">${incoming ? '+' : '−'}${money(t.amount)}</div>
    </div>`;
  }
  const emptyTx = `<div class="empty"><div class="big">${ic('inbox')}</div><b>No transactions yet</b><p style="margin-top:6px">Make a deposit to get started.</p></div>`;

  function donut(s) {
    const total = s.dep + s.wd + s.tr;
    const parts = [['Deposits', s.dep, '#34d399'], ['Withdrawals', s.wd, '#fb7185'], ['Transfers', s.tr, '#6c7bff']];
    const R = 70, C = 2 * Math.PI * R;
    let off = 0, arcs = '';
    if (total > 0) {
      parts.forEach(([, v, col]) => {
        const len = (v / total) * C;
        if (len > 0) arcs += `<circle cx="90" cy="90" r="${R}" fill="none" stroke="${col}" stroke-width="22" stroke-linecap="butt"
          stroke-dasharray="${len} ${C - len}" stroke-dashoffset="${-off}" transform="rotate(-90 90 90)" style="transition:stroke-dasharray 1s"/>`;
        off += len;
      });
    }
    return `<div class="donut-wrap">
      <svg width="180" height="180" viewBox="0 0 180 180">
        <circle cx="90" cy="90" r="${R}" fill="none" stroke="var(--border)" stroke-width="22"/>${arcs}
        <text x="90" y="86" text-anchor="middle" fill="var(--muted)" font-size="12" font-weight="600">TRANSACTIONS</text>
        <text x="90" y="112" text-anchor="middle" fill="var(--text)" font-size="28" font-weight="800">${s.n}</text>
      </svg>
      <div class="legend">${parts.map(([n, v, c]) => `<div><i style="background:${c}"></i>${n}<b>${money(v)}</b></div>`).join('')}</div>
    </div>`;
  }

  function customerOverview() {
    const s = sums(), u = S.user, main = $('#main');
    const hour = new Date().getHours();
    const greet = hour < 12 ? 'Good morning' : hour < 18 ? 'Good afternoon' : 'Good evening';
    main.innerHTML = pageHead(`${greet}, ${esc(u.name.split(' ')[0])} 👋`, "Here's what's happening with your account today.",
      `<button class="icon-btn" data-act="reload" title="Refresh">${ic('refresh')}</button>`) + `
    <div class="grid g-hero">
      <div class="bank-card">
        <div class="bc-top"><div class="bc-chip"></div>
          <button class="eye-toggle" id="eyeb">${ic(S.hideBal ? 'eyeoff' : 'eye')}</button></div>
        <div><div class="bc-label">Available balance</div><div class="bc-balance" id="bal">${S.hideBal ? '₹ ••••••' : money(0)}</div></div>
        <div class="bc-bottom">
          <div><div class="bc-name">${esc(u.name)}</div><div class="bc-acc">A/C ${esc(u.account)}</div></div>
          <div class="logo" style="font-size:16px">${ic('bank')} ABC</div>
        </div>
      </div>
      <div class="glass quick">
        <h3>Quick actions</h3>
        <div class="quick-grid">
          <button class="qa dep" data-modal="deposit"><div class="ico">${ic('down')}</div><div><b>Deposit</b><small>Add money</small></div></button>
          <button class="qa wd" data-modal="withdraw"><div class="ico">${ic('up')}</div><div><b>Withdraw</b><small>Take money out</small></div></button>
          <button class="qa tr" data-modal="transfer"><div class="ico">${ic('swap')}</div><div><b>Transfer</b><small>Send to account</small></div></button>
          <button class="qa hs" data-goto="history"><div class="ico">${ic('list')}</div><div><b>History</b><small>All activity</small></div></button>
        </div>
      </div>
    </div>
    <div class="grid g-3" style="margin-top:22px">
      ${statCard('Total deposited', money(s.dep), 'down', 'c-good', 'Money in')}
      ${statCard('Total withdrawn', money(s.wd), 'up', 'c-bad', 'Cash out')}
      ${statCard('Total transferred', money(s.tr), 'swap', 'c-acc', 'Sent to others')}
    </div>
    <div class="grid g-2" style="margin-top:22px">
      <div class="glass panel">
        <div class="panel-head"><h3>Recent activity</h3><button class="link-btn" data-goto="history">View all →</button></div>
        <div class="tx-list">${S.history.length ? S.history.slice(0, 5).map(txRow).join('') : emptyTx}</div>
      </div>
      <div class="glass panel">
        <div class="panel-head"><h3>Money flow</h3></div>
        ${donut(s)}
      </div>
    </div>`;
    wireHead(); wirePageActions();
    if (!S.hideBal) countUp($('#bal'), u.balance);
    $('#eyeb').onclick = () => {
      S.hideBal = !S.hideBal;
      $('#eyeb').innerHTML = ic(S.hideBal ? 'eyeoff' : 'eye');
      $('#bal').textContent = S.hideBal ? '₹ ••••••' : money(u.balance);
    };
  }

  const statCard = (label, val, icon, cls, sub) => `
    <div class="glass stat"><div class="stat-top">${label}<div class="stat-ico ${cls}">${ic(icon)}</div></div>
      <div class="stat-val">${val}</div><div class="stat-sub">${sub}</div></div>`;

  function wirePageActions() {
    $$('#main [data-modal]').forEach(b => b.onclick = () => openMoneyModal(b.dataset.modal));
    $$('#main [data-goto]').forEach(b => b.onclick = () => { S.page = b.dataset.goto; customerPage(); });
    const r = $('[data-act="reload"]'); if (r) r.onclick = () => (S.user.role === 'ADMIN' ? adminPage() : customerPage());
  }

  function customerHistory() {
    const main = $('#main');
    let filter = 'all', q = '';
    main.innerHTML = pageHead('Transactions', `${S.history.length} total transactions on your account`,
      `<button class="icon-btn" data-act="reload" title="Refresh">${ic('refresh')}</button>`) + `
    <div class="glass panel">
      <div class="toolbar">
        <div class="search">${ic('search')}<input id="q" placeholder="Search by type or date…"></div>
        <div class="seg" id="seg">
          <button class="on" data-f="all">All</button><button data-f="dep">Deposits</button>
          <button data-f="wd">Withdrawals</button><button data-f="tr">Transfers</button>
        </div>
      </div>
      <div class="tx-list" id="list"></div>
    </div>`;
    wireHead(); wirePageActions();
    const draw = () => {
      const rows = S.history.filter(t => (filter === 'all' || kind(t.type) === filter) &&
        (!q || (t.type + ' ' + fmtDate(t.date) + ' ' + t.amount).toLowerCase().includes(q)));
      $('#list').innerHTML = rows.length ? rows.map(txRow).join('') : emptyTx.replace('No transactions yet', 'No matching transactions').replace('Make a deposit to get started.', 'Try a different filter.');
    };
    $('#q').oninput = e => { q = e.target.value.toLowerCase().trim(); draw(); };
    $$('#seg button').forEach(b => b.onclick = () => {
      filter = b.dataset.f; $$('#seg button').forEach(x => x.classList.toggle('on', x === b)); draw();
    });
    draw();
  }

  // ---------------------------------------------------------------- modals
  function closeModal() { $('#modal-root').innerHTML = ''; }
  function openModal(html) {
    $('#modal-root').innerHTML = `<div class="overlay" id="ov"><div class="modal">${html}</div></div>`;
    $('#ov').onmousedown = e => { if (e.target.id === 'ov') closeModal(); };
    const x = $('#ov .x'); if (x) x.onclick = closeModal;
    const c = $('#ov [data-close]'); if (c) c.onclick = closeModal;
  }
  document.addEventListener('keydown', e => { if (e.key === 'Escape') closeModal(); });

  const MODAL_CFG = {
    deposit: { title: 'Deposit money', sub: 'Add funds to your account instantly.', icon: 'down', cls: 'c-good', btn: 'Deposit', done: 'Deposit successful' },
    withdraw: { title: 'Withdraw money', sub: 'Take cash out of your account.', icon: 'up', cls: 'c-bad', btn: 'Withdraw', done: 'Withdrawal successful' },
    transfer: { title: 'Transfer funds', sub: 'Send money to another account.', icon: 'swap', cls: 'c-acc', btn: 'Send money', done: 'Transfer successful' }
  };

  function openMoneyModal(type) {
    const c = MODAL_CFG[type];
    openModal(`
      <div class="modal-head"><div class="tx-ico ${c.cls}" style="width:46px;height:46px">${ic(c.icon)}</div>
        <h3>${c.title}</h3><button class="x" type="button">${ic('x')}</button></div>
      <p class="sub">${c.sub}</p>
      <form id="mf" autocomplete="off">
        ${type === 'transfer' ? `<div class="field"><label>Receiver account number</label>
          <div class="input-wrap">${ic('hash')}<input id="to" inputmode="numeric" placeholder="e.g. 102" required></div></div>` : ''}
        <div class="field"><label>Amount</label>
          <div class="input-wrap amount-big"><span class="rs">₹</span><input id="amt" inputmode="decimal" placeholder="0.00" required autofocus></div>
          ${type !== 'deposit' ? `<div class="balance-hint">Available: <b>${money(S.user.balance)}</b></div>` : ''}
        </div>
        <div class="chips">${[500, 1000, 2000, 5000].map(v => `<button type="button" class="chip" data-v="${v}">+${v.toLocaleString('en-IN')}</button>`).join('')}</div>
        <div id="merr"></div>
        <div class="modal-actions">
          <button type="button" class="btn ghost" data-close>Cancel</button>
          <button class="btn primary" id="msub" type="submit">${c.btn}</button>
        </div>
      </form>`);
    $$('.chip').forEach(ch => ch.onclick = () => {
      const a = $('#amt'); a.value = (parseFloat(a.value) || 0) + Number(ch.dataset.v); a.focus();
    });
    $('#mf').onsubmit = async e => {
      e.preventDefault();
      const amount = parseFloat($('#amt').value);
      const err = m => { $('#merr').innerHTML = `<div class="form-error">${esc(m)}</div>`; };
      if (!isFinite(amount) || amount <= 0) return err('Please enter a valid amount.');
      const body = { amount };
      if (type === 'transfer') {
        const to = parseInt($('#to').value, 10);
        if (!to || to <= 0) return err('Please enter a valid account number.');
        body.to = to;
      }
      const btn = $('#msub'); btn.disabled = true; btn.innerHTML = '<div class="spinner"></div> Processing…';
      try {
        const r = await api(type, 'POST', body);
        S.user.balance = r.balance;
        $('#ov .modal').innerHTML = `
          <div class="success-pop"><div class="check">${ic('check')}</div>
            <h3>${c.done}</h3>
            <p class="muted" style="margin-bottom:10px">${money(amount)}${type === 'transfer' ? ' sent to account ' + body.to : ''}</p>
            <p class="new-bal">New balance: <b>${money(r.balance)}</b></p>
            <div class="modal-actions"><button class="btn primary" id="done">Done</button></div></div>`;
        $('#done').onclick = () => { closeModal(); customerPage(); };
      } catch (ex) {
        err(ex.message); btn.disabled = false; btn.textContent = c.btn;
      }
    };
  }

  // ================================================================== ADMIN
  const ADMIN_NAV = `
    <div class="nav-label">Administration</div>
    <button data-page="overview">${ic('home')} Overview</button>
    <button data-page="customers">${ic('users')} Customers</button>
    <button data-page="transactions">${ic('list')} All transactions</button>
    <button data-page="add">${ic('plus')} Add account</button>`;

  function renderAdmin() {
    shell(ADMIN_NAV, 'Administrator', S.user.username || 'Admin');
    $$('#nav [data-page]').forEach(b => b.onclick = () => { S.page = b.dataset.page; $('#sb').classList.remove('open'); adminPage(); });
    adminPage();
  }

  async function adminPage() {
    $$('#nav [data-page]').forEach(b => b.classList.toggle('active', b.dataset.page === S.page));
    const main = $('#main');
    main.innerHTML = pageHead('Loading…', ' ') + `<div class="grid g-3"><div class="skeleton" style="height:140px"></div><div class="skeleton" style="height:140px"></div><div class="skeleton" style="height:140px"></div></div>`;
    wireHead();
    try {
      const [c, t, tot] = await Promise.all([api('admin/customers'), api('admin/transactions'), api('admin/total')]);
      S.customers = c; S.txs = t; S.total = tot.total;
    } catch (e) { if (e.message !== 'expired') toast(e.message, 'er'); return; }
    ({ overview: adminOverview, customers: adminCustomers, transactions: adminTx, add: adminAdd }[S.page] || adminOverview)();
  }

  function adminOverview() {
    const main = $('#main');
    const top = [...S.customers].sort((a, b) => b.balance - a.balance).slice(0, 7);
    const max = Math.max(1, ...top.map(c => c.balance));
    main.innerHTML = pageHead('Admin overview', 'Live snapshot of the entire bank.',
      `<button class="icon-btn" data-act="reload" title="Refresh">${ic('refresh')}</button>`) + `
    <div class="grid g-3">
      ${statCard('Total bank balance', `<span id="tot">${money(0)}</span>`, 'wallet', 'c-acc', 'Across all accounts')}
      ${statCard('Customer accounts', S.customers.length, 'users', 'c-cyan', 'Active accounts')}
      ${statCard('Transactions', S.txs.length, 'swap', 'c-warn', 'All time')}
    </div>
    <div class="grid g-2" style="margin-top:22px">
      <div class="glass panel">
        <div class="panel-head"><h3>Top account balances</h3></div>
        ${top.length ? `<div class="bars">${top.map((c, i) => `
          <div class="bar-col"><div class="bar" style="height:${Math.max(6, c.balance / max * 100)}%;animation-delay:${i * 90}ms"><div class="tip">${money(c.balance)}</div></div>
          <span title="${esc(c.name)}">${esc(c.name)}</span></div>`).join('')}</div>` : emptyTx.replace('No transactions yet', 'No customers yet')}
      </div>
      <div class="glass panel">
        <div class="panel-head"><h3>Latest transactions</h3><button class="link-btn" data-go="transactions">View all →</button></div>
        <div class="tx-list">${S.txs.length ? S.txs.slice(0, 5).map(adminTxRow).join('') : emptyTx}</div>
      </div>
    </div>`;
    wireHead(); wireAdminLinks();
    countUp($('#tot'), S.total);
  }

  function adminTxRow(t, i) {
    const k = kind(t.type);
    const cls = k === 'dep' ? 'c-good' : k === 'wd' ? 'c-bad' : 'c-acc';
    const icon = k === 'dep' ? 'down' : k === 'wd' ? 'up' : 'swap';
    const sub = k === 'tr' && t.receiver ? `A/C ${t.account} → A/C ${t.receiver}` : `A/C ${t.account}`;
    return `<div class="tx" style="animation-delay:${Math.min(i, 12) * 40}ms">
      <div class="tx-ico ${cls}">${ic(icon)}</div>
      <div class="tx-main"><div class="tx-title">${esc(t.type)} · ${esc(sub)}</div><div class="tx-date">${esc(fmtDate(t.date))}</div></div>
      <div class="tx-amt ${k === 'dep' ? 'in' : 'out'}">${money(t.amount)}</div></div>`;
  }

  function wireAdminLinks() {
    $$('#main [data-go]').forEach(b => b.onclick = () => { S.page = b.dataset.go; adminPage(); });
    const r = $('[data-act="reload"]'); if (r) r.onclick = adminPage;
  }

  function adminCustomers() {
    const main = $('#main');
    let q = '';
    main.innerHTML = pageHead('Customers', `${S.customers.length} accounts · ${money(S.total)} total`,
      `<button class="btn primary sm" data-go="add">${ic('plus')} New account</button>`) + `
    <div class="glass panel">
      <div class="toolbar"><div class="search">${ic('search')}<input id="q" placeholder="Search by name or account number…"></div></div>
      <div class="table-wrap"><table><thead><tr><th>Customer</th><th>Account no.</th><th class="right">Balance</th><th class="right">Actions</th></tr></thead>
      <tbody id="tb"></tbody></table></div>
    </div>`;
    wireHead(); wireAdminLinks();
    const draw = () => {
      const rows = S.customers.filter(c => !q || (c.name + ' ' + c.account).toLowerCase().includes(q));
      $('#tb').innerHTML = rows.length ? rows.map((c, i) => `
        <tr style="animation-delay:${Math.min(i, 14) * 35}ms">
          <td><div class="cell-user"><div class="avatar">${esc(initials(c.name))}</div>${esc(c.name)}</div></td>
          <td class="mono">${c.account}</td>
          <td class="right num">${money(c.balance)}</td>
          <td class="right"><button class="btn danger sm" data-del="${c.account}" data-name="${esc(c.name)}">${ic('trash')} Delete</button></td>
        </tr>`).join('') : `<tr><td colspan="4"><div class="empty">No customers found.</div></td></tr>`;
      $$('[data-del]').forEach(b => b.onclick = () => confirmDelete(b.dataset.del, b.dataset.name));
    };
    $('#q').oninput = e => { q = e.target.value.toLowerCase().trim(); draw(); };
    draw();
  }

  function confirmDelete(acc, name) {
    openModal(`
      <div class="modal-head"><div class="tx-ico c-bad" style="width:46px;height:46px">${ic('trash')}</div><h3>Delete account?</h3><button class="x" type="button">${ic('x')}</button></div>
      <p class="sub" style="margin-top:10px;line-height:1.6">You're about to permanently delete <b style="color:var(--text)">${esc(name)}</b> (A/C ${esc(acc)}). This cannot be undone.</p>
      <div class="modal-actions"><button class="btn ghost" data-close>Cancel</button><button class="btn danger" id="yes">Delete account</button></div>`);
    $('#yes').onclick = async () => {
      const b = $('#yes'); b.disabled = true; b.innerHTML = '<div class="spinner"></div>';
      try { const r = await api('admin/delete', 'POST', { account: Number(acc) }); toast(r.message); closeModal(); adminPage(); }
      catch (e) { toast(e.message, 'er'); closeModal(); }
    };
  }

  function adminTx() {
    const main = $('#main');
    let q = '', f = 'all';
    main.innerHTML = pageHead('All transactions', `${S.txs.length} records across the bank`,
      `<button class="icon-btn" data-act="reload" title="Refresh">${ic('refresh')}</button>`) + `
    <div class="glass panel">
      <div class="toolbar">
        <div class="search">${ic('search')}<input id="q" placeholder="Search by ID, account or amount…"></div>
        <div class="seg" id="seg"><button class="on" data-f="all">All</button><button data-f="dep">Deposits</button><button data-f="wd">Withdrawals</button><button data-f="tr">Transfers</button></div>
      </div>
      <div class="table-wrap"><table><thead><tr><th>ID</th><th>Type</th><th>Account</th><th>Receiver</th><th class="right">Amount</th><th>Date</th></tr></thead>
      <tbody id="tb"></tbody></table></div>
    </div>`;
    wireHead(); wireAdminLinks();
    const draw = () => {
      const rows = S.txs.filter(t => (f === 'all' || kind(t.type) === f) &&
        (!q || `${t.id} ${t.account} ${t.receiver ?? ''} ${t.amount} ${t.type}`.toLowerCase().includes(q)));
      $('#tb').innerHTML = rows.length ? rows.slice(0, 300).map((t, i) => `
        <tr style="animation-delay:${Math.min(i, 14) * 30}ms">
          <td class="mono">#${t.id}</td>
          <td><span class="badge ${kind(t.type)}">${esc(t.type)}</span></td>
          <td class="mono">${t.account}</td>
          <td class="mono">${t.receiver ?? '—'}</td>
          <td class="right num">${money(t.amount)}</td>
          <td class="muted">${esc(fmtDate(t.date))}</td></tr>`).join('') : `<tr><td colspan="6"><div class="empty">No transactions found.</div></td></tr>`;
    };
    $('#q').oninput = e => { q = e.target.value.toLowerCase().trim(); draw(); };
    $$('#seg button').forEach(b => b.onclick = () => { f = b.dataset.f; $$('#seg button').forEach(x => x.classList.toggle('on', x === b)); draw(); });
    draw();
  }

  function adminAdd() {
    const main = $('#main');
    main.innerHTML = pageHead('Add customer account', 'Create a new bank account for a customer.') + `
    <div class="glass form-card">
      <h3>Account details</h3><p class="muted">All fields are required.</p>
      <form id="af" autocomplete="off">
        <div class="field"><label>Account number</label><div class="input-wrap">${ic('hash')}<input id="an" inputmode="numeric" placeholder="e.g. 103" required></div></div>
        <div class="field"><label>Customer name</label><div class="input-wrap">${ic('user')}<input id="nm" placeholder="Full name" required></div></div>
        <div class="field"><label>Initial balance</label><div class="input-wrap amount-big"><span class="rs">₹</span><input id="ib" inputmode="decimal" placeholder="0.00" value="0" required></div></div>
        <div id="aerr"></div>
        <button class="btn primary" id="asub" type="submit">${ic('plus')} Create account</button>
      </form>
    </div>`;
    wireHead();
    $('#af').onsubmit = async e => {
      e.preventDefault();
      const account = parseInt($('#an').value, 10), name = $('#nm').value.trim(), balance = parseFloat($('#ib').value);
      const err = m => { $('#aerr').innerHTML = `<div class="form-error">${esc(m)}</div>`; };
      if (!account || account <= 0) return err('Enter a valid account number.');
      if (!name) return err('Enter the customer name.');
      if (!isFinite(balance) || balance < 0) return err('Enter a valid initial balance.');
      const btn = $('#asub'); btn.disabled = true; btn.innerHTML = '<div class="spinner"></div> Creating…';
      try {
        const r = await api('admin/add', 'POST', { account, name, balance });
        toast(r.message); S.page = 'customers'; adminPage();
      } catch (ex) { err(ex.message); btn.disabled = false; btn.innerHTML = `${ic('plus')} Create account`; }
    };
  }

  // ---------------------------------------------------------------- boot
  async function boot() {
    if (!S.token) return renderLanding();
    try {
      // Admin sessions fail /me with 403; customers succeed.
      try {
        const me = await api('me');
        S.user = me; S.page = 'overview'; return renderCustomer();
      } catch (e) {
        if (e.message === 'expired') return;
        await api('admin/total');
        S.user = { role: 'ADMIN', username: 'Admin' }; S.page = 'overview'; return renderAdmin();
      }
    } catch (e) {
      S.token = ''; sessionStorage.removeItem('abc_token'); renderLanding();
    }
  }

  document.addEventListener('click', e => {
    const t = e.target.closest('[data-act="theme"]');
    if (t) toggleTheme();
  });

  boot();
})();
