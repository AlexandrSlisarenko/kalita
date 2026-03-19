import http from 'k6/http';
import { check } from 'k6';
import { Trend, Counter } from 'k6/metrics';
import { uuidv4 } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const successCount = new Counter('successful_requests');
export const failCount = new Counter('failed_requests');
export const responseTime = new Trend('response_time');

export const options = {
    scenarios: {
        contacts: {
            executor: 'shared-iterations',
            vus: 10,
            iterations: 20000,
            maxDuration: '30s',
        },
    },
};

const url = 'http://localhost:8998/api/v1/wallet';
const headers = { 'Content-Type': 'application/json' };

export default function () {
    const payload = JSON.stringify({
        walletId : "a31f2209-638a-4829-b3f3-94bc33a385e1",
        operationType : "DEPOSIT",
        amount : 1.00
    });

    const res = http.post(url, payload, { headers });

    const isOK = check(res,
        { 'status is 201': (r) => r.status === 200 }
    );

    responseTime.add(res.timings.duration);

    if (isOK) {
        successCount.add(1);
    } else {
        failCount.add(1);
        console.error(`[FAIL] ${res.status} - ${res.body}`);
    }
}