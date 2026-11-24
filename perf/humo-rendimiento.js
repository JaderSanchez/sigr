// Prueba de humo y rendimiento contra el entorno de staging (k6).
// 20 usuarios virtuales durante 30 s consultan el menú y la caja.
// El pipeline falla si el p95 supera 500 ms o si más del 1 % de peticiones falla.
import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  vus: 20,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<500'],
    http_req_failed: ['rate<0.01'],
  },
};

export default function () {
  const menu = http.get(`${BASE}/api/platos`);
  check(menu, {
    'menú responde 200': (r) => r.status === 200,
    'menú trae platos': (r) => r.json().length > 0,
  });
  const hoy = new Date().toISOString().slice(0, 10);
  const caja = http.get(`${BASE}/api/caja/cierre?fecha=${hoy}`);
  check(caja, { 'cierre de caja responde 200': (r) => r.status === 200 });
  sleep(1);
}
