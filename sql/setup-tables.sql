
-- Prevent conflicts with existing tables in the database.
DROP TABLE IF EXISTS Category CASCADE;
DROP TABLE IF EXISTS SalesRep CASCADE;
DROP TABLE IF EXISTS Shop CASCADE;
DROP TABLE IF EXISTS Publisher CASCADE;
DROP TABLE IF EXISTS Book CASCADE;
DROP TABLE IF EXISTS ShopOrder CASCADE;
DROP TABLE IF EXISTS Orderline CASCADE;
DROP TABLE IF EXISTS ArchivedShopOrder CASCADE;
DROP TABLE IF EXISTS ArchivedOrderline CASCADE;

CREATE TABLE Category
(
	-- Ensure ID is NOT NULL and UNIQUE using the PRIMARY KEY constraint.
	CategoryID INTEGER PRIMARY KEY,
	Name VARCHAR(50),	-- SHOULD I MAKE THIS UNIQUE/DISTINCT?
	CategoryType VARCHAR(20),

	-- Prevent negative ID numbers.
	CONSTRAINT chk_CategoryID CHECK (CategoryID >= 0),

	-- Ensuring types are uniform regardless of case.
	CONSTRAINT chk_CategoryType CHECK (UPPER(CategoryType) IN ('FICTION', 'NON-FICTION'))

);
CREATE TABLE SalesRep 
(
	SalesRepID INTEGER PRIMARY KEY,
	Name VARCHAR(50)
);
CREATE TABLE Shop 
(
	ShopID INTEGER PRIMARY KEY,
	Name VARCHAR(50)
);
CREATE TABLE Publisher 
(
	PublisherID INTEGER PRIMARY KEY,
	Name VARCHAR(50)
);
CREATE TABLE Book 
(
	BookID INTEGER PRIMARY KEY,
	Title VARCHAR(50),
	Price DECIMAL(10,2),
	CategoryID INTEGER,
	PublisherID INTEGER,

	-- Referencing foreign keys in the Category and Publisher tables.
	FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID) ON DELETE CASCADE,
	FOREIGN KEY (PublisherID) REFERENCES Publisher(PublisherID) ON DELETE CASCADE
);
CREATE TABLE ShopOrder
(
	ShopOrderID INTEGER PRIMARY KEY,
	OrderDate DATE,
	ShopID INTEGER,
	SalesRepID INTEGER,

	-- Referencing foreign keys in the Shop and SalesRep tables.
	FOREIGN KEY (ShopID) REFERENCES Shop(ShopID) ON DELETE CASCADE,
	FOREIGN KEY (SalesRepID) REFERENCES SalesRep(SalesRepID) ON DELETE CASCADE
);
CREATE TABLE Orderline
(
	ShopOrderID INTEGER,
	BookID INTEGER,
	Quantity INTEGER,
	UnitSellingPrice DECIMAL (10,2),

	-- Referencing foreign keys in the ShopOrder and Book tables.
	FOREIGN KEY (ShopOrderID) REFERENCES ShopOrder(ShopOrderID) ON DELETE CASCADE,
	FOREIGN KEY (BookID) REFERENCES Book(BookID) ON DELETE CASCADE,

	-- Creating a composite primary key using the two foreign keys.
	PRIMARY KEY (ShopOrderID, BookID)
);

-- Creating duplicate archive tables for both ShopOrder and Orderline.
-- These are required for the "end of year" procedure.
CREATE TABLE ArchivedShopOrder
(
	ShopOrderID INTEGER PRIMARY KEY,
	OrderDate DATE,
	ShopID INTEGER,
	SalesRepID INTEGER,
	FOREIGN KEY (ShopID) REFERENCES Shop(ShopID) ON DELETE CASCADE,
	FOREIGN KEY (SalesRepID) REFERENCES SalesRep(SalesRepID) ON DELETE CASCADE
);
CREATE TABLE ArchivedOrderline
(
	ShopOrderID INTEGER,
	BookID INTEGER,
	Quantity INTEGER,
	UnitSellingPrice DECIMAL (10,2),
	FOREIGN KEY (ShopOrderID) REFERENCES ShopOrder(ShopOrderID) ON DELETE CASCADE,
	FOREIGN KEY (BookID) REFERENCES Book(BookID) ON DELETE CASCADE,
	PRIMARY KEY (ShopOrderID, BookID)
);


-- Function for generating Publisher Report Data:
CREATE OR REPLACE FUNCTION pub_report (pub_name VARCHAR(50))
RETURNS TABLE (
	orderdate DATE,
	title VARCHAR(50),
	totalquantity INTEGER,
	totalsellingvalue DECIMAL(10,2)
)
AS $$
BEGIN
	RETURN QUERY
	SELECT	shoporder.orderdate,
		book.title,
		orderline.quantity,
		orderline.unitsellingprice
	FROM (((SELECT	publisherid FROM publisher WHERE name = $1) AS pub_id
		NATURAL JOIN book)
		NATURAL JOIN shop
		NATURAL JOIN shoporder
		NATURAL JOIN orderline)
	ORDER BY shoporder.orderdate DESC;
END;
$$ LANGUAGE plpgsql;