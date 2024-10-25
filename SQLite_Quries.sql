-- SQLite
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    full_name TEXT
);

SELECT name FROM sqlite_master WHERE type='table';

SELECT * FROM users;

SELECT * FROM categories;

SELECT * FROM products;

SELECT * FROM product_photos;

SELECT * FROM orders;

SELECT * FROM carts;

SELECT * FROM cart_items;

SELECT * FROM order_items;

SELECT * FROM favorites;


SELECT * FROM user_activity;

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

CREATE TABLE carts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    total_price REAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE cart_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cart_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price REAL NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


SELECT name FROM sqlite_master WHERE type='table';



CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status TEXT DEFAULT 'pending',  -- Could be 'pending', 'shipped', 'delivered', etc.
    placed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,  -- Price at the time of the order
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE favorites (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);


CREATE TABLE user_activity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    product_id INTEGER,
    activity_type TEXT NOT NULL,
    activity_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    product_category_id INTEGER,
    activity_duration INTEGER,  -- Duration in seconds or another unit of time
    pricing_filter REAL,  -- Assuming this is a price range filter
    category_filter TEXT,
    top_category1 TEXT,
    top_category2 TEXT,
    top_category3 TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (product_category_id) REFERENCES product_categories(id)
);


ALTER TABLE users
ADD COLUMN top_category1 TEXT;

ALTER TABLE users
ADD COLUMN top_category2 TEXT;

ALTER TABLE users
ADD COLUMN top_category3 TEXT;


ALTER TABLE user_activity
ADD COLUMN search_filter TEXT;

