const axios = require('axios');
const {baseUrl} = require("./constants/baseUrl");

describe('Dev Auth Controller Integration Tests', () => {
    test('Login should be successful', async () => {
        const response = await axios.get(`${baseUrl}/api/dev/auth/login`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toBe(true);
    });
})