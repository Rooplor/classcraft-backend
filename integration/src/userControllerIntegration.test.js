const axios = require('axios');
const {baseUrl} = require("./constants/baseUrl");

describe('User Controller Integration Tests', () => {
    let userDetail = null;

    test('Insert a new user', async () => {
        const user = {
            username: 'Test User1',
            email: 'test1@mail.com',
        }
        const expectedResult = {
            id: expect.any(String),
            username: user.username,
            email: user.email,
            profilePicture: null,
        }
        const response = await axios.post(`${baseUrl}/api/user`, user);
        userDetail = response.data.result;
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toMatchObject(expectedResult);
    });

    test('Insert a new user with empty username and email should fail with validation error', async () => {
        const user = {
            username: '',
            email: '',
        };
        try {
            await axios.post(`${baseUrl}/api/user`, user);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('User username cannot be blank, User email cannot be blank');
        }
    });

    test('Insert a new user with empty username should fail with validation error', async () => {
        const user = {
            username: '',
            email: 'test2@mail.com',
        };
        try {
            await axios.post(`${baseUrl}/api/user`, user);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('User username cannot be blank');
        }
    });

    test('Insert a new user with empty email should fail with validation error', async () => {
        const user = {
            username: 'Test User 2',
            email: '',
        };
        try {
            await axios.post(`${baseUrl}/api/user`, user);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('User email cannot be blank');
        }
    });

    test('Insert a new user with existing username and email should fail with conflict error', async () => {
        const user = {
            username: 'Test User1',
            email: 'test1@mail.com',
        };
        try {
            await axios.post(`${baseUrl}/api/user`, user);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe(`User with username ${user.username} already exists, User with email ${user.email} already exists`);
        }
    });

    test('Insert a new user with invalid email format should fail with conflict error', async () => {
        const user = {
            username: 'Test User 2',
            email: 'testInvalidMail.com',
        };
        try {
            await axios.post(`${baseUrl}/api/user`, user);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('User email is invalid');
        }
    });

    test('Get All User', async () => {
        const response = await axios.get(`${baseUrl}/api/user`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toBeInstanceOf(Array);
        expect(response.data?.result).toContainEqual(userDetail);
    });

    test('Get User Detail by Id', async () => {
        const response = await axios.get(`${baseUrl}/api/user/${userDetail.id}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toMatchObject(userDetail);
    });

    test ('Get User Detail by Id should be fail and throw user not found', async () => {
        try {
            await axios.get(`${baseUrl}/api/user/123456`);
        }catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('User not found');
        }
    });

    test('Update User', async () => {
        const user = {
            username: 'Test User Updated',
            email: 'testUpdate@mail.com',
        }
        const expectedResult = {
            id: userDetail.id,
            username: user.username,
            email: user.email,
            profilePicture: null,
        }
        const response = await axios.put(`${baseUrl}/api/user/${userDetail.id}`, user);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toMatchObject(expectedResult);
    });

    test('Get User Profile', async () => {
        const response = await axios.get(`${baseUrl}/api/user/profile`);
        const expectedResult = {
            id: expect.any(String),
            username: 'dev',
            email: 'test@mail.com',
            profilePicture: 'https://media.stickerswiki.app/oneesanstickers117/6747778.512.webp'
        }
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toMatchObject(expectedResult);
    })

    test('Delete User', async () => {
        const response = await axios.delete(`${baseUrl}/api/user/${userDetail.id}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toBe('User deleted');
    });
});