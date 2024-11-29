const axios = require('axios');
const {baseUrl} = require("./constants/baseUrl");

describe('Form Controller Integration Tests', () => {
    let formDetail = null;
    let form = {
        classroomId: '1234',
        title: 'Test Form',
        description: 'Test Form Description',
        openDate: new Date(),
        closeDate: new Date(),
        fields: [
            {
                name: 'Full Name',
                type: 'String',
                required: true,
                validation: 'TEXT',
            },
            {
                name: 'Age',
                type: 'Number',
                required: true,
                validation: 'NUMBER',
            },
            {
                name: 'Email',
                type: 'Email',
                required: true,
                validation: 'EMAIL',
            },
            {
                name: 'Phone',
                type: 'Number',
                required: true,
                validation: 'PHONE',
            },
            {
                name: 'Address',
                type: 'String',
                required: false,
                validation: 'TEXT',
            }
        ]
    }
    let formSubmission = {
        formId: null,
        classroomId: '1234',
        responses: {
            'Full Name': 'John Doe',
            'Age': 30,
            'Email': 'john.doe@example.com',
            'Phone': '1234567890',
            'Address': '123 Main St'
        }
    }

    test('Create form should success', async () => {
        const expectedResult = {
            id: expect.any(String),
            classroomId: form.classroomId,
            title: form.title,
            description: form.description,
            openDate: form.openDate.toISOString().replace('Z', ''),
            closeDate: form.closeDate.toISOString().replace('Z', ''),
            fields: form.fields,
        }
        const response = await axios.post(`${baseUrl}/api/form`, form);
        formDetail = response.data.result;
        formSubmission.formId = formDetail.id;
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toMatchObject(expectedResult);
    });

    test('Create form with blank classroomId should throw error', async () => {
        let invalidForm = {...form, classroomId: ''};
        try {
            await axios.post(`${baseUrl}/api/form`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Classroom ID is mandatory');
        }
    });

    test('Create form with blank title should throw error', async () => {
        let invalidForm = {...form, title: ''};
        try {
            await axios.post(`${baseUrl}/api/form`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Title is mandatory');
        }
    });

    test('Create form with blank description should throw error', async () => {
        let invalidForm = {...form, description: ''};
        try {
            await axios.post(`${baseUrl}/api/form`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Description is mandatory');
        }
    });

    test('Create form with empty fields should throw error', async () => {
        let invalidForm = {...form, fields: []};
        try {
            await axios.post(`${baseUrl}/api/form`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Fields are mandatory');
        }
    });

    test('Create form with blank field name should throw error', async () => {
        let invalidForm = {...form, fields: [{...form.fields[0], name: ''}]};
        try {
            await axios.post(`${baseUrl}/api/form`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Field 0: Field name is mandatory');
        }
    });

    test('Create form with blank field type should throw error', async () => {
        let invalidForm = {...form, fields: [{...form.fields[0], type: ''}]};
        try {
            await axios.post(`${baseUrl}/api/form`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Field 0: Field type is mandatory');
        }
    });

    test('Update form should success', async () => {
        const updatedForm = {...form, title: 'Updated Test Form'};
        const response = await axios.put(`${baseUrl}/api/form/${formDetail.id}`, updatedForm);
        formDetail = response.data.result;
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.title).toBe('Updated Test Form');
    });

    test('Update form with blank classroomId should throw error', async () => {
        let invalidForm = {...form, classroomId: ''};
        try {
            await axios.put(`${baseUrl}/api/form/${formDetail.id}`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Classroom ID is mandatory');
        }
    });

    test('Update form with blank title should throw error', async () => {
        let invalidForm = {...form, title: ''};
        try {
            await axios.put(`${baseUrl}/api/form/${formDetail.id}`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Title is mandatory');
        }
    });

    test('Update form with blank description should throw error', async () => {
        let invalidForm = {...form, description: ''};
        try {
            await axios.put(`${baseUrl}/api/form/${formDetail.id}`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Description is mandatory');
        }
    });

    test('Update form with empty fields should throw error', async () => {
        let invalidForm = {...form, fields: []};
        try {
            await axios.put(`${baseUrl}/api/form/${formDetail.id}`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Fields are mandatory');
        }
    });

    test('Update form with blank field name should throw error', async () => {
        let invalidForm = {...form, fields: [{...form.fields[0], name: ''}]};
        try {
            await axios.put(`${baseUrl}/api/form/${formDetail.id}`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Field 0: Field name is mandatory');
        }
    });

    test('Update form with blank field type should throw error', async () => {
        let invalidForm = {...form, fields: [{...form.fields[0], type: ''}]};
        try {
            await axios.put(`${baseUrl}/api/form/${formDetail.id}`, invalidForm);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Field 0: Field type is mandatory');
        }
    });

    test('Get form by Id should success', async () => {
        const response = await axios.get(`${baseUrl}/api/form/${formDetail.id}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toMatchObject(formDetail);
    });

    test('Get form by Id with invalid id should throw error', async () => {
        try {
            await axios.get(`${baseUrl}/api/form/123456`);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('Form not found');
        }
    });

    test('Get form questions by id should success', async () => {
        const response = await axios.get(`${baseUrl}/api/form/${formDetail.id}/questions`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        const expectedQuestions = form.fields.reduce((acc, field) => {
            acc[field.name] = field.type;
            return acc;
        }, {});
        expect(response.data?.result).toMatchObject(expectedQuestions);
    });

    test('Get form questions by id with invalid id should throw error', async () => {
        try {
            await axios.get(`${baseUrl}/api/form/invalid_id/questions`);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('Form not found');
        }
    });

    test('Submit form with missing required fields should throw error', async () => {
        let invalidSubmission = { ...formSubmission, responses: { 'Full Name': 'John Doe' } };
        try {
            await axios.post(`${baseUrl}/api/form/submit`, invalidSubmission);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toContain('Missing required fields');
        }
    });

    test('Submit form with invalid email field validation should throw error', async () => {
        let invalidSubmission = { ...formSubmission, responses: { ...formSubmission.responses, 'Email': 'invalid-email' } };
        try {
            await axios.post(`${baseUrl}/api/form/submit`, invalidSubmission);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toContain('Field \'Email\' does not match the validation pattern.');
        }
    });

    test('Submit form with invalid phone field validation should throw error', async () => {
        let invalidSubmission = { ...formSubmission, responses: { ...formSubmission.responses, 'Phone': 'invalid-phone' } };
        try {
            await axios.post(`${baseUrl}/api/form/submit`, invalidSubmission);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toContain('Field \'Phone\' does not match the validation pattern.');
        }
    });

    test('Submit form should succeed', async () => {
        const response = await axios.post(`${baseUrl}/api/form/submit`, formSubmission);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.formId).toBe(formSubmission.formId);
    });

    test('Submit form with already submitted answer should throw error', async () => {
        try {
            await axios.post(`${baseUrl}/api/form/submit`, formSubmission);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.error).toBe('Answer already submitted');
        }
    });

    test('Get form submissions by classroom id should succeed', async () => {
        const response = await axios.get(`${baseUrl}/api/form/submissions/${formDetail.classroomId}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toBeInstanceOf(Array);
        expect(response.data?.result.length).toBeGreaterThan(0);
    });

    test('Delete form should success', async () => {
        const response = await axios.delete(`${baseUrl}/api/form/${formDetail.id}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result).toBe('Form deleted');
    });

    test('Delete form with invalid id should throw error', async () => {
        try {
            await axios.delete(`${baseUrl}/api/form/invalid_id`);
        } catch (error) {
            expect(error.response.status).toBe(400);
            expect(error.response.data?.success).toBe(false);
            expect(error.response.data?.result).toBeNull();
            expect(error.response.data?.error).toBe('Form not found');
        }
    });
})