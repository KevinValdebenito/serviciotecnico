INSERT INTO users (email, password, active, rol)
VALUES ('admin@serviciotecnico.cl', crypt('Admin.1234', gen_salt('bf')), TRUE, 'ADMIN')
ON CONFLICT (email) DO NOTHING;