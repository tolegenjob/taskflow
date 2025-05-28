-- Вставка пользователей
INSERT INTO users (first_name, last_name, email, active) VALUES
                                                             ('John', 'Doe', 'john.doe@example.com', TRUE),
                                                             ('Jane', 'Smith', 'jane.smith@example.com', TRUE),
                                                             ('Alice', 'Johnson', 'alice.johnson@example.com', TRUE),
                                                             ('Bob', 'Williams', 'bob.williams@example.com', TRUE),
                                                             ('Charlie', 'Brown', 'charlie.brown@example.com', TRUE),
                                                             ('Diana', 'Davis', 'diana.davis@example.com', TRUE),
                                                             ('Eve', 'Miller', 'eve.miller@example.com', TRUE),
                                                             ('Frank', 'Taylor', 'frank.taylor@example.com', TRUE),
                                                             ('Grace', 'Anderson', 'grace.anderson@example.com', TRUE),
                                                             ('Hank', 'Thomas', 'hank.thomas@example.com', TRUE),
                                                             ('Ivy', 'Jackson', 'ivy.jackson@example.com', TRUE),
                                                             ('Jack', 'White', 'jack.white@example.com', TRUE),
                                                             ('Kara', 'Harris', 'kara.harris@example.com', TRUE),
                                                             ('Louis', 'Martin', 'louis.martin@example.com', TRUE),
                                                             ('Mona', 'Garcia', 'mona.garcia@example.com', TRUE),
                                                             ('Nick', 'Martinez', 'nick.martinez@example.com', TRUE),
                                                             ('Olivia', 'Rodriguez', 'olivia.rodriguez@example.com', TRUE),
                                                             ('Paul', 'Lopez', 'paul.lopez@example.com', TRUE),
                                                             ('Quincy', 'Walker', 'quincy.walker@example.com', TRUE),
                                                             ('Rachel', 'Hall', 'rachel.hall@example.com', TRUE),
                                                             ('Sam', 'Young', 'sam.young@example.com', TRUE),
                                                             ('Tina', 'King', 'tina.king@example.com', TRUE),
                                                             ('Ursula', 'Scott', 'ursula.scott@example.com', TRUE),
                                                             ('Victor', 'Green', 'victor.green@example.com', TRUE),
                                                             ('Wendy', 'Adams', 'wendy.adams@example.com', TRUE),
                                                             ('Xander', 'Baker', 'xander.baker@example.com', TRUE),
                                                             ('Yara', 'Gonzalez', 'yara.gonzalez@example.com', TRUE),
                                                             ('Zane', 'Nelson', 'zane.nelson@example.com', TRUE),
                                                             ('Ashley', 'Carter', 'ashley.carter@example.com', TRUE),
                                                             ('Brian', 'Mitchell', 'brian.mitchell@example.com', TRUE);

-- Вставка проектов
INSERT INTO projects (name, description, status, owner_id) VALUES
                                                               ('Project Apollo', 'A space exploration project', 'ACTIVE', 1),
                                                               ('Project Zeus', 'A project for creating a new web platform', 'COMPLETED', 2),
                                                               ('Project Artemis', 'Research and development of lunar base technology', 'ARCHIVED', 3),
                                                               ('Project Hermes', 'A mobile app for fitness tracking', 'ACTIVE', 4),
                                                               ('Project Neptune', 'A research project on ocean life sustainability', 'ACTIVE', 5),
                                                               ('Project Helios', 'Renewable energy generation system', 'COMPLETED', 6),
                                                               ('Project Titan', 'Mars habitat construction plan', 'ACTIVE', 7),
                                                               ('Project Olympus', 'AI-based data analysis platform', 'ARCHIVED', 8),
                                                               ('Project Athena', 'A community health monitoring application', 'ACTIVE', 9),
                                                               ('Project Apollo II', 'Extended mission for Project Apollo', 'COMPLETED', 10),
                                                               ('Project Borealis', 'AI system for advanced weather forecasting', 'ACTIVE', 11),
                                                               ('Project Cascade', 'A multi-platform gaming application', 'COMPLETED', 12),
                                                               ('Project Vanguard', 'Cybersecurity defense system', 'ACTIVE', 13),
                                                               ('Project Sentinel', 'Autonomous vehicle control system', 'ACTIVE', 14),
                                                               ('Project Odyssey', 'Space tourism business development', 'COMPLETED', 15),
                                                               ('Project Mercury', 'Digital art creation platform', 'ACTIVE', 16),
                                                               ('Project Horizon', 'Exploration of new frontiers in robotics', 'ARCHIVED', 17),
                                                               ('Project Inception', 'Developing a new film production software', 'ACTIVE', 18),
                                                               ('Project Horizon II', 'Follow-up research on Project Horizon', 'COMPLETED', 19),
                                                               ('Project Matrix', 'Advanced AI for matrix-based computations', 'ACTIVE', 20),
                                                               ('Project Vector', 'Cybersecurity and data protection for corporations', 'COMPLETED', 21),
                                                               ('Project Orion', 'Creating a new online marketplace', 'ACTIVE', 22),
                                                               ('Project Nexus', 'Next-generation social media platform', 'ARCHIVED', 23),
                                                               ('Project Vanguard II', 'Enhanced version of Project Vanguard', 'ACTIVE', 24),
                                                               ('Project Arcadia', 'Green energy for sustainable living', 'ACTIVE', 25),
                                                               ('Project Nebula', 'Research in quantum computing', 'COMPLETED', 26),
                                                               ('Project Rebirth', 'Revamping old infrastructure with modern technology', 'ACTIVE', 27),
                                                               ('Project Equinox', 'Space-based observatory construction', 'ARCHIVED', 28),
                                                               ('Project Sigma', 'Next-gen operating system for mobile devices', 'ACTIVE', 29),
                                                               ('Project Lunar', 'Developing automated drones for lunar missions', 'COMPLETED', 30);

-- Вставка задач
INSERT INTO tasks (title, description, status, priority, deadline, assigned_user_id, project_id) VALUES
                                                                                                     ('Design Spacecraft', 'Design a spacecraft for the mission', 'TODO', 'HIGH', '2025-12-31 12:00:00', 1, 1),
                                                                                                     ('Develop App UI', 'Create the user interface for the new app', 'IN_PROGRESS', 'MEDIUM', '2025-06-30 15:00:00', 2, 4),
                                                                                                     ('Research Solar Panels', 'Conduct research on efficient solar panels', 'DONE', 'LOW', '2025-05-20 18:00:00', 3, 5),
                                                                                                     ('Test Prototype', 'Testing the prototype of autonomous vehicle', 'IN_PROGRESS', 'HIGH', '2025-07-15 10:00:00', 4, 14),
                                                                                                     ('Create Database Schema', 'Design the database for fitness app', 'TODO', 'MEDIUM', '2025-06-01 09:00:00', 5, 4),
                                                                                                     ('Develop Weather Prediction Algorithm', 'Develop an algorithm for weather prediction', 'DONE', 'LOW', '2025-08-01 14:00:00', 6, 12),
                                                                                                     ('Write Documentation', 'Write the documentation for new AI model', 'IN_PROGRESS', 'MEDIUM', '2025-07-20 11:00:00', 7, 11),
                                                                                                     ('Launch Website', 'Launch the website for Project Artemis', 'DONE', 'HIGH', '2025-06-10 16:00:00', 8, 3),
                                                                                                     ('Write API Specifications', 'Write the API specifications for new platform', 'TODO', 'HIGH', '2025-09-01 17:00:00', 9, 9),
                                                                                                     ('Build Marketing Campaign', 'Create a marketing campaign for the new product', 'IN_PROGRESS', 'MEDIUM', '2025-07-05 10:30:00', 10, 17),
                                                                                                     ('Install Solar Panels', 'Install solar panels for the new project', 'DONE', 'LOW', '2025-06-15 13:00:00', 11, 5),
                                                                                                     ('Integrate AI Models', 'Integrate AI models into the main platform', 'IN_PROGRESS', 'MEDIUM', '2025-07-25 14:00:00', 12, 18),
                                                                                                     ('Setup Cloud Infrastructure', 'Setup the cloud infrastructure for new platform', 'TODO', 'HIGH', '2025-12-01 08:00:00', 13, 19),
                                                                                                     ('Deploy Website', 'Deploy the website for Project Sentinel', 'DONE', 'LOW', '2025-05-15 12:30:00', 14, 14),
                                                                                                     ('Review Codebase', 'Review the codebase for Project Apollo', 'IN_PROGRESS', 'MEDIUM', '2025-07-10 09:00:00', 15, 1),
                                                                                                     ('Train Machine Learning Model', 'Train the machine learning model for Project Matrix', 'TODO', 'HIGH', '2025-08-20 11:00:00', 16, 20),
                                                                                                     ('Prepare Marketing Content', 'Create content for marketing campaign', 'DONE', 'LOW', '2025-06-25 10:30:00', 17, 22),
                                                                                                     ('Conduct User Testing', 'Test the product with real users', 'IN_PROGRESS', 'MEDIUM', '2025-07-18 13:00:00', 18, 9),
                                                                                                     ('Fix Bugs in App', 'Fix bugs in the fitness tracking app', 'TODO', 'MEDIUM', '2025-05-30 14:00:00', 19, 4),
                                                                                                     ('Optimize AI Algorithm', 'Optimize the AI algorithm for better accuracy', 'DONE', 'HIGH', '2025-06-12 11:00:00', 20, 18),
                                                                                                     ('Update Software', 'Update the software for autonomous drones', 'IN_PROGRESS', 'MEDIUM', '2025-08-01 10:00:00', 21, 26),
                                                                                                     ('Create User Stories', 'Create user stories for new social media platform', 'DONE', 'LOW', '2025-06-05 15:00:00', 22, 23),
                                                                                                     ('Develop Data Collection System', 'Create a system for collecting data from drones', 'TODO', 'HIGH', '2025-09-15 12:00:00', 23, 30),
                                                                                                     ('Setup Payment Gateway', 'Integrate payment gateway for new platform', 'IN_PROGRESS', 'MEDIUM', '2025-06-28 13:00:00', 24, 28),
                                                                                                     ('Write Test Cases', 'Write test cases for the new website', 'TODO', 'MEDIUM', '2025-07-12 16:00:00', 25, 12),
                                                                                                     ('Deploy Cloud Application', 'Deploy the cloud-based application for Project Apollo', 'DONE', 'LOW', '2025-07-05 12:00:00', 26, 1),
                                                                                                     ('Design Marketing Plan', 'Design a marketing plan for new platform', 'IN_PROGRESS', 'MEDIUM', '2025-08-10 14:00:00', 27, 18),
                                                                                                     ('Write Technical Specification', 'Write the technical specification for new system', 'TODO', 'HIGH', '2025-08-30 17:00:00', 28, 14),
                                                                                                     ('Fix App Security Issues', 'Fix the security issues in the app', 'IN_PROGRESS', 'MEDIUM', '2025-06-18 10:00:00', 29, 3),
                                                                                                     ('Test New Features', 'Test the new features of the new app', 'TODO', 'LOW', '2025-09-05 12:00:00', 30, 4);

-- Вставка комментариев
INSERT INTO comments (content, task_id, user_id) VALUES
                                                     ('Looking good, I will review the design.', 1, 2),
                                                     ('I am working on it, should be ready soon.', 2, 3),
                                                     ('The research is complete and ready for review.', 3, 4),
                                                     ('Testing is in progress, should finish this week.', 4, 5),
                                                     ('The schema looks fine, will implement it today.', 5, 6),
                                                     ('The weather prediction algorithm is working well.', 6, 7),
                                                     ('I will write the documentation once the algorithm is finalized.', 7, 8),
                                                     ('The website is now live and ready for public.', 8, 9),
                                                     ('Waiting for feedback on the API specs.', 9, 10),
                                                     ('Marketing campaign is being prepared for launch.', 10, 11),
                                                     ('The installation was completed successfully.', 11, 12),
                                                     ('Integration of AI models is going well.', 12, 13),
                                                     ('The cloud infrastructure setup is on track.', 13, 14),
                                                     ('Deployment is completed, next step is monitoring.', 14, 15),
                                                     ('Code review is in progress, will finalize it soon.', 15, 16),
                                                     ('The model is performing better than expected.', 16, 17),
                                                     ('Content creation for marketing is almost finished.', 17, 18),
                                                     ('User testing has provided useful feedback.', 18, 19),
                                                     ('Bug fixes are in progress.', 19, 20),
                                                     ('The AI algorithm is now optimized.', 20, 21),
                                                     ('Drone software update is almost ready.', 21, 22),
                                                     ('Test cases are being written, should be ready soon.', 22, 23),
                                                     ('The payment gateway integration is on schedule.', 23, 24),
                                                     ('Testing new app features this weekend.', 24, 25),
                                                     ('Cloud application deployment is successful.', 25, 26),
                                                     ('Marketing plan is in final stages.', 26, 27),
                                                     ('Technical specification is being written.', 27, 28),
                                                     ('App security fixes are almost done.', 28, 29),
                                                     ('New features testing is complete.', 29, 30);
-- Вставка записей в таблицу projects_users (связь между проектами и пользователями)
INSERT INTO users_projects (project_id, user_id) VALUES
                                                     (1, 1),  -- Проект Apollo, пользователь John Doe
                                                     (1, 2),  -- Проект Apollo, пользователь Jane Smith
                                                     (2, 3),  -- Проект Zeus, пользователь Alice Johnson
                                                     (3, 4),  -- Проект Artemis, пользователь Bob Williams
                                                     (4, 5),  -- Проект Hermes, пользователь Charlie Brown
                                                     (5, 6),  -- Проект Neptune, пользователь Diana Davis
                                                     (6, 7),  -- Проект Helios, пользователь Eve Miller
                                                     (7, 8),  -- Проект Titan, пользователь Frank Taylor
                                                     (8, 9),  -- Проект Olympus, пользователь Grace Anderson
                                                     (9, 10), -- Проект Athena, пользователь Hank Thomas
                                                     (10, 11),-- Проект Apollo II, пользователь Ivy Jackson
                                                     (11, 12),-- Проект Borealis, пользователь Jack White
                                                     (12, 13),-- Проект Cascade, пользователь Kara Harris
                                                     (13, 14),-- Проект Vanguard, пользователь Louis Martin
                                                     (14, 15),-- Проект Sentinel, пользователь Mona Garcia
                                                     (15, 16),-- Проект Odyssey, пользователь Nick Martinez
                                                     (16, 17),-- Проект Mercury, пользователь Olivia Rodriguez
                                                     (17, 18),-- Проект Horizon, пользователь Paul Lopez
                                                     (18, 19),-- Проект Inception, пользователь Quincy Walker
                                                     (19, 20),-- Проект Horizon II, пользователь Rachel Hall
                                                     (20, 21),-- Проект Matrix, пользователь Sam Young
                                                     (21, 22),-- Проект Vector, пользователь Tina King
                                                     (22, 23),-- Проект Orion, пользователь Ursula Scott
                                                     (23, 24),-- Проект Nexus, пользователь Victor Green
                                                     (24, 25),-- Проект Vanguard II, пользователь Wendy Adams
                                                     (25, 26),-- Проект Arcadia, пользователь Xander Baker
                                                     (26, 27),-- Проект Nebula, пользователь Yara Gonzalez
                                                     (27, 28),-- Проект Rebirth, пользователь Zane Nelson
                                                     (28, 29),-- Проект Equinox, пользователь Ashley Carter
                                                     (29, 30),-- Проект Sigma, пользователь Brian Mitchell
                                                     (30, 1);  -- Проект Lunar, пользователь John Doe