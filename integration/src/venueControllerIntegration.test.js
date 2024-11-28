const axios = require('axios');

describe('Venue Controller Integration Tests', () => {
    const baseUrl = 'http://127.0.0.1:8080';
    let venueDetail = null;

    test('Insert a new venue', async () => {
        const venue = {
            room: "test room",
            location: {
                building: "test building",
                floor: 10,
            },
            description: "test description",
            capacity: 100,
            imageUrl: "https://rooploor88/venue.jpg",
        }
        const expectedResult = {
            id: expect.any(String),
            room: venue.room,
            location: venue.location,
            description: venue.description,
            capacity: venue.capacity,
            imageUrl: venue.imageUrl,
        }

        const response = await axios.post(`${baseUrl}/api/venue`, venue);
        venueDetail = response.data.result;
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toMatchObject(expectedResult);
    });

    test('Get all venue', async () => {
        const response = await axios.get(`${baseUrl}/api/venue`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toEqual(expect.arrayContaining([venueDetail]));
    });

    test('Delete venue', async () => {
        const response = await axios.delete(`${baseUrl}/api/venue/${venueDetail.id}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toBe(true);
    });
})