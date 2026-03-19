-- Inserir dados iniciais para testes dos endpoints

-- Smartphones
INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (1, 'iPhone 15 Pro', 'Smartphone Apple com chip A17 Pro e câmera de 48MP', 1299.99, 'FIVE_STARS', '128GB, Titânio Natural, 5G', 'https://apple.com/iphone15pro');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (2, 'Samsung Galaxy S24 Ultra', 'Smartphone Samsung com S Pen e zoom 100x', 1199.99, 'FIVE_STARS', '256GB, Preto Titânio, 5G', 'https://samsung.com/galaxy-s24-ultra');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (3, 'Google Pixel 8', 'Smartphone Google com IA avançada e câmera computacional', 699.99, 'FOUR_STARS', '128GB, Obsidian, 5G', 'https://store.google.com/pixel8');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (4, 'OnePlus 12', 'Smartphone OnePlus com carregamento rápido 100W', 799.99, 'FOUR_STARS', '256GB, Verde Floresta, 5G', 'https://oneplus.com/12');

-- Notebooks
INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (5, 'MacBook Pro M3', 'Notebook Apple com chip M3 para profissionais', 2499.99, 'FIVE_STARS', '14 polegadas, 16GB RAM, 512GB SSD', 'https://apple.com/macbook-pro');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (6, 'Dell XPS 13', 'Ultrabook Dell com design premium e tela InfinityEdge', 1299.99, 'FOUR_STARS', '13.3 polegadas, Intel i7, 16GB RAM, 512GB SSD', 'https://dell.com/xps-13');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (7, 'Lenovo ThinkPad X1', 'Notebook empresarial Lenovo com segurança avançada', 1599.99, 'FOUR_STARS', '14 polegadas, Intel i7, 32GB RAM, 1TB SSD', 'https://lenovo.com/thinkpad-x1');

-- Tablets
INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (8, 'iPad Pro 12.9', 'Tablet Apple com chip M2 e tela Liquid Retina XDR', 1099.99, 'FIVE_STARS', '12.9 polegadas, 128GB, Wi-Fi + Cellular', 'https://apple.com/ipad-pro');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (9, 'Samsung Galaxy Tab S9', 'Tablet Samsung com S Pen incluída', 799.99, 'FOUR_STARS', '11 polegadas, 128GB, Wi-Fi', 'https://samsung.com/galaxy-tab-s9');

-- Fones de ouvido
INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (10, 'AirPods Pro 2', 'Fones Apple com cancelamento ativo de ruído', 249.99, 'FIVE_STARS', 'Bluetooth 5.3, Cancelamento de ruído, Case MagSafe', 'https://apple.com/airpods-pro');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (11, 'Sony WH-1000XM5', 'Headphone Sony com melhor cancelamento de ruído', 399.99, 'FIVE_STARS', 'Bluetooth 5.2, 30h bateria, Cancelamento adaptativo', 'https://sony.com/wh-1000xm5');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (12, 'JBL Tune 770NC', 'Headphone JBL com boa relação custo-benefício', 99.99, 'THREE_STARS', 'Bluetooth 5.3, 44h bateria, Cancelamento de ruído', 'https://jbl.com/tune-770nc');

-- Smartwatches
INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (13, 'Apple Watch Series 9', 'Smartwatch Apple com GPS e monitoramento de saúde', 399.99, 'FIVE_STARS', '45mm, GPS + Cellular, Caixa de Alumínio', 'https://apple.com/watch');

INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (14, 'Samsung Galaxy Watch 6', 'Smartwatch Samsung com Wear OS', 329.99, 'FOUR_STARS', '44mm, Bluetooth, Monitoramento do sono', 'https://samsung.com/galaxy-watch6');

-- Produto com preço baixo para teste
INSERT INTO product (id, name, description, price, rating, specification, url) 
VALUES (15, 'Cabo USB-C', 'Cabo USB-C para carregamento rápido', 19.99, 'THREE_STARS', '2 metros, USB 3.2, 100W', 'https://example.com/cabo-usbc');