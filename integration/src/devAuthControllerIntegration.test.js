const axios = require('axios');

describe('Dev Auth Controller Integration Tests', () => {
    const baseUrl = 'http://127.0.0.1:8080';

    test('Login should be successful', async () => {
        const response = await axios.get(`${baseUrl}/api/dev/auth/login`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toBe(true);
    });
})