const axios = require('axios');
const {baseUrl} = require('./constants/baseUrl');

describe('Class Controller Integration Tests', () => {
    let classDetail = null;
    let classDetail2 = null;
    let classDetail3 = null;
    let venueDetail = null;

    beforeAll(async () => {
        const newVenue = {
            room: 'Test Room',
            location: {
                building: 'Test Building',
                floor: 10
            },
            description: 'Test Description',
            capacity: 100,
            imageUrl: 'https://rooploor88/venue.jpg'
        };
        await axios.get(`${baseUrl}/api/dev/auth/login`);
        const response = await axios.post(`${baseUrl}/api/venue`, newVenue);
        venueDetail = response.data.result;
    });

    test('User can create class normally', async () => {
        const newClass = {
            title: '1. Cybersecurity Essentials for the Digital World',
            details: 'This lecture covers fundamental cybersecurity principles, common threats, and practical strategies to protect digital assets.',
            target: 'IT professionals, students, and small business owners',
            prerequisite: 'Basic knowledge of IT systems',
            type: 'LECTURE',
            format: 'ONSITE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05 08:00:00'),
                        endDateTime: new Date('2024-12-05 16:00:00')
                    },
                    venueId: [venueDetail.id]
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: 'Familiarity to the topic',
            instructorAvatar: 'https://rooploor88/instructor.jpg',
            instructorFamiliarity: 'Familiarity to the topic'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        classDetail = response.data.result;

        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with a duration of 2 days', async () => {
        const newClass = {
            title: '2. Introduction to Cloud Computing',
            details: 'An overview of cloud computing concepts, service models (IaaS, PaaS, SaaS), and its applications in modern IT environments.',
            target: 'IT professionals, students, and businesses exploring cloud solutions',
            prerequisite: 'Basic knowledge of IT infrastructure',
            type: 'LECTURE',
            format: 'ONSITE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: [venueDetail.id]
                },
                {
                    date: {
                        startDateTime: new Date('2024-12-06T09:00:00'),
                        endDateTime: new Date('2024-12-06T17:00:00')
                    },
                    venueId: [venueDetail.id]
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: 'Familiarity to the topic',
            instructorAvatar: 'https://rooploor88/instructor.jpg',
            instructorFamiliarity: 'Familiarity to the topic'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        classDetail2 = response.data.result;

        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = LECTURE and class format = ONLINE', async () => {
        const newClass = {
            title: '3. Advanced Data Security and Encryption Techniques',
            details: 'This advanced lecture explores cutting-edge methods in data security, focusing on encryption techniques, secure communication protocols, and modern cybersecurity frameworks. Topics include AES, RSA, blockchain security, and quantum-safe cryptography. The session will provide practical insights into safeguarding sensitive information in cloud environments and on-premises systems.',
            target: 'IT professionals, cybersecurity experts, and students interested in deepening their understanding of data protection and encryption technologies.',
            prerequisite: 'Familiarity with basic cryptography, networking, and security protocols.',
            type: 'LECTURE',
            format: 'ONLINE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: [venueDetail.id]
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: 'Familiarity to the topic',
            instructorAvatar: 'https://rooploor88/instructor.jpg',
            instructorFamiliarity: 'Familiarity to the topic'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        classDetail3 = response.data.result;

        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = WORKSHOP and class format = ONLINE', async () => {
        const newClass = {
            title: 'Machine Learning for Data Science',
            details: 'This comprehensive course introduces the principles of machine learning and its applications in data science. Topics include supervised and unsupervised learning, deep learning, neural networks, model evaluation, and real-world applications in predictive analytics, natural language processing, and computer vision. The course also covers the use of popular machine learning frameworks like TensorFlow, Scikit-Learn, and Keras.',
            target: 'Data scientists, machine learning practitioners, and students looking to gain hands-on experience with real-world machine learning projects and algorithms.',
            prerequisite: 'Basic knowledge of programming (preferably Python) and statistics.',
            type: 'WORKSHOP',
            format: 'ONLINE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: []
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: '',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = DISCUSSION and class format = ONLINE', async () => {
        const newClass = {
            title: 'Artificial Intelligence and Ethical Considerations',
            details: 'This course delves into the ethical issues surrounding the use of artificial intelligence in various industries. Topics include bias in AI algorithms, privacy concerns, autonomous systems, the impact of AI on employment, and the regulation of AI technologies. Students will analyze case studies and learn about the global efforts to create ethical guidelines for AI deployment, focusing on ensuring fairness, transparency, and accountability in AI systems.',
            target: 'AI researchers, developers, ethicists, policy makers, and students interested in the intersection of artificial intelligence and ethics.',
            prerequisite: 'Familiarity with AI principles and basic programming concepts is recommended.',
            type: 'DISCUSSION',
            format: 'ONLINE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: []
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: '',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = LECTURE and class format = ON-SITE', async () => {
        const newClass = {
            title: 'Deep Learning and Neural Networks for AI Applications',
            details: 'This in-depth course covers the core principles of deep learning and neural networks, focusing on their applications in artificial intelligence. Students will learn the architecture of neural networks, including convolutional networks (CNNs), recurrent networks (RNNs), and advanced topics such as generative adversarial networks (GANs) and reinforcement learning. The course also explores hands-on implementation using popular deep learning frameworks, including TensorFlow and PyTorch.',
            target: 'AI researchers, deep learning engineers, data scientists, and students with a strong interest in neural networks and AI applications in areas like computer vision, NLP, and autonomous systems.',
            prerequisite: 'Intermediate knowledge of Python, linear algebra, calculus, and machine learning fundamentals. Familiarity with deep learning tools like TensorFlow or PyTorch is advantageous but not required.',
            type: 'LECTURE',
            format: 'ONSITE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: []
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: '',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass)
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = WORKSHOP and class format = ON-SITE', async () => {
        const newClass = {
            title: '7. Advanced Artificial Intelligence and Machine Learning Techniques',
            details: 'This course dives into advanced topics in artificial intelligence and machine learning, including deep reinforcement learning, natural language processing (NLP), and unsupervised learning techniques. Students will gain hands-on experience in applying machine learning algorithms to solve real-world problems. The course emphasizes both theoretical foundations and practical applications, allowing students to implement cutting-edge AI models using popular machine learning libraries such as TensorFlow, Keras, and Scikit-learn. Key topics also include model optimization, hyperparameter tuning, and AI in big data environments.',
            target: 'This course is designed for intermediate to advanced learners, including AI engineers, data scientists, machine learning specialists',
            prerequisite: 'Students should have a solid understanding of machine learning fundamentals, including supervised and unsupervised learning algorithms, as well as experience with Python programming.',
            type: 'WORKSHOP',
            format: 'ONSITE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: []
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: '',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = DISCUSSION and class format = ON-SITE', async () => {
        const newClass = {
            title: '8. Mastering Deep Learning and AI: Advanced Techniques and Applications',
            details: 'This comprehensive course provides an in-depth exploration of the most advanced techniques in deep learning and artificial intelligence. Students will learn about cutting-edge algorithms such as transformers, attention mechanisms, and graph neural networks. The course covers applications in computer vision, natural language processing (NLP), autonomous systems, and reinforcement learning. Along with theoretical concepts, students will work on real-world projects',
            target: 'This course is designed for experienced AI practitioners, including machine learning engineers, data scientists, AI researchers, and graduate students with a solid foundation in machine learning.',
            prerequisite: 'Students must have a solid understanding of machine learning and deep learning fundamentals',
            type: 'DISCUSSION',
            format: 'ONSITE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: []
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: '',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = LECTURE and class format = MIXED', async () => {
        const newClass = {
            title: '9. Advanced AI and Deep Learning: Cutting-Edge Algorithms and Applications',
            details: 'This advanced course covers the latest in AI and deep learning, including transformers, attention mechanisms, and graph neural networks. Students will apply these techniques in areas like image recognition, NLP, and robotics, with a focus on real-world projects, model optimization, and deployment in cloud environments. Ethical considerations and explainable AI (XAI) will also be explored.',
            target: 'Ideal for AI researchers, machine learning engineers, and data scientists with experience in deep learning who want to master advanced techniques and apply them to industries such as healthcare, finance, and autonomous systems.',
            prerequisite: 'Strong background in machine learning, deep learning, Python programming, and math (linear algebra, calculus, statistics). Experience with TensorFlow/PyTorch and cloud platforms (AWS, Google Cloud) is recommended.',
            type: 'LECTURE',
            format: 'MIXED',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: []
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: '',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = WORKSHOP and class format = MIXED', async () => {
        const newClass = {
            title: '10. Advanced AI and Deep Learning Techniques',
            details: 'Explore cutting-edge AI methods, including transformers, attention mechanisms, and graph neural networks. Apply these techniques in areas like NLP, computer vision, and robotics, focusing on real-world projects and ethical AI.',
            target: 'For AI professionals and advanced learners looking to deepen their expertise in deep learning and AI applications in industries like healthcare and autonomous systems.',
            prerequisite: 'Experience in machine learning, deep learning, Python, and math (linear algebra, calculus). Familiarity with TensorFlow/PyTorch and cloud platforms is helpful.',
            type: 'WORKSHOP',
            format: 'MIXED',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: new Date('2024-12-05T08:00:00'),
                        endDateTime: new Date('2024-12-05T16:00:00')
                    },
                    venueId: []
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: '',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT'
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with class type = DISCUSSION and class format = MIXED', async () => {
        const newClass = {
            title: '11. Advanced AI and Deep Learning',
            details: 'Learn cutting-edge AI techniques like transformers and graph neural networks, with a focus on real-world applications and ethical AI.',
            target: 'For AI professionals and advanced learners aiming to deepen their expertise in AI and deep learning.',
            prerequisite: 'Experience in machine learning, Python, and math (linear algebra, calculus). Familiarity with TensorFlow/PyTorch is helpful.',
            type: 'DISCUSSION',
            format: 'MIXED',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: '2024-12-05T08:00:00',
                        endDateTime: '2024-12-05T16:00:00'
                    },
                    venueId: ['venue1']
                }
            ],
            instructorName: 'SIT STUDENT',
            instructorBio: 'Experienced in AI and deep learning.',
            instructorAvatar: '',
            instructorFamiliarity: 'SIT STUDENT',
            coverImage: '',
            coOwners: []
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User can create a class with manual fill instructor information', async () => {
        const newClass = {
            title: '12. Introduction to Cloud Computing and DevOps',
            details: 'This course covers the fundamentals of cloud computing and DevOps, focusing on cloud infrastructure, automation, continuous integration, and deployment pipelines. Students will work with popular platforms like AWS, Azure, and Docker.',
            target: 'Beginners in cloud computing, IT professionals, and developers interested in learning cloud infrastructure and DevOps practices.',
            prerequisite: 'Basic understanding of software development and networking concepts.',
            type: 'LECTURE',
            format: 'ONSITE',
            capacity: 50,
            dates: [
                {
                    date: {
                        startDateTime: '2024-12-05T08:00:00',
                        endDateTime: '2024-12-05T16:00:00'
                    },
                    venueId: ['venue1']
                }
            ],
            instructorName: 'Ms. Diana Blake',
            instructorBio: 'Ms. Blake is a cybersecurity consultant with over 15 years of experience in threat detection and prevention strategies.',
            instructorAvatar: '',
            instructorFamiliarity: 'Highly experienced in cybersecurity frameworks and risk management',
            coverImage: '',
            coOwners: []
        };

        const response = await axios.post(`${baseUrl}/api/class`, newClass);
        expect(response.status).toBe(200);
        expect(response.data.success).toBe(true);
        expect(response.data.result.title).toBe(newClass.title);
    });

    test('User cannot create a class with all fields empty', async () => {
        const newClass = {};

        try {
            await axios.post(`${baseUrl}/api/class`, newClass);
        } catch (error) {
            expect(error.response.status).toBe(400);
        }
    });

    test('User can update class title', async () => {
        const updatedClass = {...classDetail, title: '1. Cybersecurity Essentials for the Digital World (EDITED)'};
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.title).toBe('1. Cybersecurity Essentials for the Digital World (EDITED)');
    });

    test('User can update class detail', async () => {
        const updatedClass = {
            ...classDetail,
            details: 'This lecture covers fundamental cybersecurity principles, common threats, and practical strategies to protect digital assets. (edited)'
        };
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.details).toBe('This lecture covers fundamental cybersecurity principles, common threats, and practical strategies to protect digital assets. (edited)');
    });

    test('User can update class target audience', async () => {
        const updatedClass = {...classDetail, target: 'IT professionals, students, and small business owners (edited)'};
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.target).toBe('IT professionals, students, and small business owners (edited)');
    });

    test('User can update class prerequisites', async () => {
        const updatedClass = {...classDetail, prerequisite: 'Basic knowledge of IT systems (edited)'};
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.prerequisite).toBe('Basic knowledge of IT systems (edited)');
    });

    test('User can update class type', async () => {
        const updatedClass = {...classDetail, type: 'WORKSHOP'};
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.type).toBe('WORKSHOP');
    });

    test('User can update class format', async () => {
        const updatedClass = {...classDetail, format: 'ONLINE'};
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.format).toBe('ONLINE');
    });

    test('User can update class available seats', async () => {
        const updatedClass = {
            ...classDetail,
            capacity: 40,
        };
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.capacity).toBe(40);
    });

    test('User can update class date and time', async () => {
        const updatedClass = {
            ...classDetail,
            dates: [
                {
                    date: {
                        startDateTime: '2024-12-06T09:00:00',
                        endDateTime: '2024-12-06T17:00:00',
                    },
                    venueId: classDetail.dates[0].venueId,
                },
            ],
        };
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.dates[0].date.startDateTime).toBe('2024-12-06T09:00:00');
        expect(response.data?.result.dates[0].date.endDateTime).toBe('2024-12-06T17:00:00');
    });

    test('User can update class instructor about', async () => {
        const updatedClass = {
            ...classDetail,
            instructorBio: 'edited@example.com',
        };
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.instructorBio).toBe('edited@example.com');
    });

    test('User can update class instructor familiarity', async () => {
        const updatedClass = {
            ...classDetail,
            instructorFamiliarity: 'SIT STUDENT (edited)',
        };
        const response = await axios.put(`${baseUrl}/api/class/${classDetail.id}`, updatedClass);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.instructorFamiliarity).toBe('SIT STUDENT (edited)');
    });

    test('User can publish class', async () => {
        const response = await axios.patch(`${baseUrl}/api/class/${classDetail.id}/toggle-publish-status`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.published).toBe(true);
    });

    test('User can un-publish class', async () => {
        const response = await axios.patch(`${baseUrl}/api/class/${classDetail.id}/toggle-publish-status`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.published).toBe(false);
    });

    test('User can view class', async () => {
        const response = await axios.get(`${baseUrl}/api/class/${classDetail.id}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
        expect(response.data?.result.title).toBe('1. Cybersecurity Essentials for the Digital World');
    });

    test('User can view more than one class', async () => {
        const response1 = await axios.get(`${baseUrl}/api/class/${classDetail.id}`);
        expect(response1.status).toBe(200);
        expect(response1.data?.success).toBe(true);
        expect(response1.data?.result.title).toBe('1. Cybersecurity Essentials for the Digital World');

        const response2 = await axios.get(`${baseUrl}/api/class/${classDetail2.id}`);
        expect(response2.status).toBe(200);
        expect(response2.data?.success).toBe(true);
        expect(response2.data?.result.title).toBe('2. Introduction to Cloud Computing');
    });

    test('User can delete class', async () => {
        const response = await axios.delete(`${baseUrl}/api/class/${classDetail.id}`);
        expect(response.status).toBe(200);
        expect(response.data?.success).toBe(true);
    });

    afterAll(async () => {
        if (classDetail) {
            await axios.delete(`${baseUrl}/api/class/${classDetail.id}`);
        }
        if (venueDetail) {
            await axios.delete(`${baseUrl}/api/venue/${venueDetail.id}`);
        }
    });
});