// ── NAV SCROLL ──
const navbar = document.getElementById('navbar');
window.addEventListener('scroll', () => {
  navbar.classList.toggle('scrolled', window.scrollY > 40);
  document.getElementById('scrollTop').classList.toggle('visible', window.scrollY > 400);
});

// ── HAMBURGER ──
document.getElementById('hamburger').addEventListener('click', () => {
  document.querySelector('.nav-links').classList.toggle('open');
});

// ── COPY TO CLIPBOARD ──
function copyCode(id) {
  const el = document.getElementById(id);
  const text = el.innerText;
  navigator.clipboard.writeText(text).then(() => {
    const btn = el.closest('.install-card, .code-wrap')?.querySelector('.copy-btn');
    if (btn) {
      const orig = btn.textContent;
      btn.textContent = '✓ Copied!';
      btn.style.background = 'var(--green)';
      btn.style.color = '#fff';
      setTimeout(() => {
        btn.textContent = orig;
        btn.style.background = '';
        btn.style.color = '';
      }, 2000);
    }
  });
}

// ── FEATURE CARD COLORS ──
// Wire --fc-color from each card's data-color attribute
document.querySelectorAll('.feature-card[data-color]').forEach(card => {
  card.style.setProperty('--fc-color', card.dataset.color);
});

// ── DEPENDENCY TAB SWITCHER ──
function switchTab(btn, id) {
  // deactivate all tabs and panels
  btn.closest('.dep-tabs').querySelectorAll('.dep-tab').forEach(t => t.classList.remove('active'));
  btn.closest('.sonatype-block').querySelectorAll('.dep-panel').forEach(p => p.classList.remove('active'));
  // activate clicked
  btn.classList.add('active');
  document.getElementById('dep-' + id).classList.add('active');
}

// ── SCROLL REVEAL ──
const revealEls = document.querySelectorAll(
  '.feature-card, .step, .api-card, .install-card, .flow-step, .cmd-table'
);
revealEls.forEach(el => el.classList.add('reveal'));

const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry, i) => {
    if (entry.isIntersecting) {
      setTimeout(() => entry.target.classList.add('visible'), i * 60);
      observer.unobserve(entry.target);
    }
  });
}, { threshold: 0.12 });

revealEls.forEach(el => observer.observe(el));

// ── TERMINAL TYPEWRITER ──
const lines = document.querySelectorAll('.terminal-line .cmd');
lines.forEach((line, i) => {
  const text = line.textContent;
  line.textContent = '';
  let j = 0;
  setTimeout(() => {
    const interval = setInterval(() => {
      line.textContent += text[j++];
      if (j >= text.length) clearInterval(interval);
    }, 18);
  }, 800 + i * 1200);
});

// ── ACTIVE NAV LINK HIGHLIGHT ──
const sections = document.querySelectorAll('section[id]');
const navLinks = document.querySelectorAll('.nav-links a');

window.addEventListener('scroll', () => {
  let current = '';
  sections.forEach(sec => {
    if (window.scrollY >= sec.offsetTop - 120) current = sec.id;
  });
  navLinks.forEach(a => {
    a.style.color = '';
    if (a.getAttribute('href') === '#' + current) {
      a.style.color = 'var(--accent)';
    }
  });
});
