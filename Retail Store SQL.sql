USE javabase;

CREATE TABLE User (
id INTEGER PRIMARY KEY,
name VARCHAR(30),
address VARCHAR(100),
email VARCHAR(20),
password VARCHAR(20),
isStaff CHAR(1)
);
CREATE TABLE userOrder (
id INTEGER PRIMARY KEY,
totalPrice FLOAT,
orderDate DATE,
paid CHAR(1)
);
CREATE TABLE Product (
id INTEGER PRIMARY KEY,
name VARCHAR(30),
description VARCHAR(100),
stockQuantity INTEGER,
price FLOAT,
active CHAR(1)
);
CREATE TABLE Discount (
id INTEGER PRIMARY KEY,
name VARCHAR(30),
value FLOAT
);
CREATE TABLE Category (
id INTEGER PRIMARY KEY,
name VARCHAR(30),
description VARCHAR(100)
);
CREATE TABLE Supplier (
id INTEGER PRIMARY KEY,
name VARCHAR(30)
);
CREATE TABLE Shelf (
id INTEGER PRIMARY KEY,
name VARCHAR(30),
availableQuantity INTEGER
);
CREATE TABLE Orders (
userId INTEGER REFERENCES User (id)
ON DELETE CASCADE,
orderId INTEGER REFERENCES userOrder (id)
ON DELETE CASCADE
);
CREATE TABLE itContains (
orderId INTEGER REFERENCES userOrder (id),
productId INTEGER REFERENCES Product (id)
ON DELETE CASCADE
);
CREATE TABLE discountAvailable (
discountId INTEGER REFERENCES Discount (id),
productId INTEGER REFERENCES Product (id)
);
CREATE TABLE categoryAvailable (
discountId INTEGER REFERENCES Discount (id),
categoryId INTEGER REFERENCES Category (id)
);
CREATE TABLE Parent (
parentCategoryId INTEGER REFERENCES Category (id),
childCategoryId INTEGER REFERENCES Category (id)
);
CREATE TABLE PInC (
categoryId INTEGER REFERENCES Category (id),
productId INTEGER REFERENCES Product (id)
);
CREATE TABLE Supplys (
supplierId INTEGER REFERENCES Supplier (id),
productId INTEGER REFERENCES Product (id)
);
CREATE TABLE Location (
productId INTEGER REFERENCES Product (id),
shelfId INTEGER REFERENCES Shelf (id)
);






