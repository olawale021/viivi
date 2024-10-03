-- SQLite
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    full_name TEXT
);


SELECT * FROM users;

SELECT * FROM categories;

SELECT * FROM products;

CREATE TABLE products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category_id INTEGER,
    stock_quantity INTEGER,
    is_active INTEGER DEFAULT 1,  -- BOOLEAN equivalent, 1 for TRUE, 0 for FALSE
    tags TEXT,
    rating DECIMAL(3, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);


UPDATE users
SET role = 'ADMIN'
WHERE id = 4;

CREATE TABLE categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);



CREATE TABLE product_photos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id INTEGER NOT NULL,
    photo_url TEXT NOT NULL,
    is_primary INTEGER DEFAULT 0,  -- Boolean field, 1 = true, 0 = false
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


SELECT * FROM product_photos;


alter table categories add column category_name varchar(255);
