-- Prevent conflicts with existing tables in the database.
DROP TABLE IF EXISTS Category CASCADE;
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

DROP TABLE IF EXISTS SalesRep CASCADE;
CREATE TABLE SalesRep 
(
	SalesRepID INTEGER PRIMARY KEY,
	Name VARCHAR(50)
);

DROP TABLE IF EXISTS Shop CASCADE;
CREATE TABLE Shop 
(
	ShopID INTEGER PRIMARY KEY,
	Name VARCHAR(50)
);

DROP TABLE IF EXISTS Publisher CASCADE;
CREATE TABLE Publisher 
(
	PublisherID INTEGER PRIMARY KEY,
	Name VARCHAR(50)
);

DROP TABLE IF EXISTS Book CASCADE;
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

DROP TABLE IF EXISTS ShopOrder CASCADE;
CREATE TABLE ShopOrder
(
	ShopOrderID INTEGER PRIMARY KEY,
	OrderDate DATE,
	ShopID INTEGER,
	SalesRepID INTEGER,

	-- Referencing foreign keys in the Shop and SalesRep tables.
	FOREIGN KEY (ShopID) REFERENCES Shop(ShopID),
	FOREIGN KEY (SalesRepID) REFERENCES SalesRep(SalesRepID)
);

-- Creating duplicate archive tables for both ShopOrder and Orderline.
-- These are required for the "end of year" procedure.
DROP TABLE IF EXISTS ArchivedShopOrder CASCADE;
CREATE TABLE ArchivedShopOrder
(
	ShopOrderID INTEGER PRIMARY KEY,
	OrderDate DATE,
	ShopID INTEGER,
	SalesRepID INTEGER,

	-- Referencing foreign keys in the Shop and SalesRep tables.
	FOREIGN KEY (ShopID) REFERENCES Shop(ShopID),
	FOREIGN KEY (SalesRepID) REFERENCES SalesRep(SalesRepID)
);

DROP TABLE IF EXISTS Orderline CASCADE;
CREATE TABLE Orderline
(
	ShopOrderID INTEGER,
	BookID INTEGER,
	Quantity INTEGER,
	UnitSellingPrice DECIMAL (10,2),

	-- Referencing foreign keys in the ShopOrder and Book tables.
	FOREIGN KEY (ShopOrderID) REFERENCES ShopOrder(ShopOrderID),
	FOREIGN KEY (BookID) REFERENCES Book(BookID),

	-- Creating a composite primary key using the two foreign keys.
	PRIMARY KEY (ShopOrderID, BookID)
);

DROP TABLE IF EXISTS ArchivedOrderline CASCADE;
CREATE TABLE ArchivedOrderline
(
	ShopOrderID INTEGER,
	BookID INTEGER,
	Quantity INTEGER,
	UnitSellingPrice DECIMAL (10,2),

	-- Referencing foreign keys in the ShopOrder and Book tables.
	FOREIGN KEY (ShopOrderID) REFERENCES ArchivedShopOrder(ShopOrderID),
	FOREIGN KEY (BookID) REFERENCES Book(BookID),

	-- Creating a composite primary key using the two foreign keys.
	PRIMARY KEY (ShopOrderID, BookID)
);

-- Function for generating Publisher Report Data:
DROP FUNCTION IF EXISTS pub_report (pub_name VARCHAR(50)) CASCADE;
CREATE FUNCTION pub_report (pub_name VARCHAR(50))
RETURNS TABLE (
	orderdate DATE,
	title VARCHAR(50),
	totalquantity INTEGER,
	totalsellingvalue DECIMAL(10,2)
)
AS $$
BEGIN
	RETURN QUERY
	SELECT
		shoporder.orderdate,
		book.title,
		orderline.quantity,
		orderline.unitsellingprice
	-- Find 'publisherid' using 'pub_name' regardless of case.
	FROM
		(((SELECT publisherid FROM publisher WHERE UPPER(name) = UPPER($1)) AS pub_id
		NATURAL JOIN book)
		NATURAL JOIN shop
		NATURAL JOIN shoporder
		NATURAL JOIN orderline)
	ORDER BY
		shoporder.orderdate DESC;
END;
$$ LANGUAGE plpgsql;

-- Function for generating Book Order History:
DROP FUNCTION IF EXISTS book_hist (book_id INTEGER) CASCADE;
CREATE FUNCTION book_hist (book_id INTEGER)
RETURNS TABLE (
	shopname VARCHAR(50),
	orderdate DATE,
	quantity INTEGER,
	unitsellingprice DECIMAL(10,2)
)
AS $$
BEGIN
	RETURN QUERY
	SELECT
		shop.name,
		shoporder.orderdate,
		orderline.quantity,
		orderline.unitsellingprice
	FROM
		book
		NATURAL JOIN orderline
		NATURAL JOIN shoporder
		NATURAL JOIN shop
WHERE book.bookid = $1;
END;
$$ LANGUAGE plpgsql;

-- Function for generating Book Order History Summary Line:
DROP FUNCTION IF EXISTS book_hist_summary (book_id INTEGER) CASCADE;
CREATE FUNCTION book_hist_summary (book_id INTEGER)
RETURNS TABLE (
	copiesordered BIGINT,
	totalsellingvalue DECIMAL(10,2)
)
AS $$
BEGIN
	RETURN QUERY
	SELECT
		SUM(orderline.quantity),
		SUM(orderline.unitsellingprice)
	FROM
		book
		NATURAL JOIN orderline
	WHERE book.bookid = $1
	GROUP BY book.bookid;
END;
$$ LANGUAGE plpgsql;

-- Function for generating Sales Perfomance Report Data:
DROP FUNCTION IF EXISTS sales_perm_report (startdate DATE, enddate DATE) CASCADE;
CREATE FUNCTION sales_perm_report (startdate DATE, enddate DATE)
RETURNS TABLE (
	name VARCHAR(50),
	orders BIGINT,
	value DECIMAL(10,2)
)
AS $$
BEGIN
	RETURN QUERY
	SELECT
		salesrep.name,
		COUNT(shoporder.salesrepid),
		SUM(unitsellingprice) AS total
	FROM (
		salesrep
		NATURAL JOIN shoporder
		NATURAL JOIN orderline
	)
	WHERE orderdate BETWEEN $1 AND $2
	GROUP BY salesrep.name
	ORDER BY total DESC;
END;
$$ LANGUAGE plpgsql;

-- Function for discounting Books in a given Category:
DROP FUNCTION IF EXISTS discount_category (id INTEGER, discount DECIMAL(5,2)) CASCADE;
CREATE FUNCTION discount_category (id INTEGER, discount DECIMAL(5,2))
RETURNS TABLE (
	id INTEGER,
	title VARCHAR(50),
	newprice DECIMAL(10,2)
)
AS $$
BEGIN
	
	UPDATE	book
	SET	price = ROUND(book.price * ((100 - $2) / 100), 2)
	WHERE	book.categoryid = $1;

	RETURN	QUERY

	SELECT	book.bookid,
		book.title,
		book.price
	FROM	book
	WHERE	book.categoryid = $1;

END;
$$ LANGUAGE plpgsql;

-- Function for end of year procedure:
DROP FUNCTION IF EXISTS end_of_year () CASCADE;
CREATE FUNCTION end_of_year ()
RETURNS TABLE (
	repname VARCHAR(50),
	totalsales DECIMAL(10,2),
	bonus DECIMAL(10,2)
)
AS $$
BEGIN


	-- Archive Data
	INSERT INTO archivedshoporder SELECT * FROM shoporder;
	INSERT INTO archivedorderline SELECT * FROM orderline;

	-- Delete Data
	DELETE FROM orderline;
	DELETE FROM shoporder;

	RETURN	QUERY
	
	-- Return Bonus Report
	SELECT
		salesrep.name,
		SUM(archivedorderline.unitsellingprice * archivedorderline.quantity),
		SUM(archivedorderline.unitsellingprice * archivedorderline.quantity) *
		CASE WHEN SUM(archivedorderline.unitsellingprice * archivedorderline.quantity) BETWEEN 0 AND 1000 THEN 0
		WHEN SUM(archivedorderline.unitsellingprice * archivedorderline.quantity) BETWEEN 1000 AND 5000 THEN 0.01
		ELSE 0.03
		END
	FROM
		salesrep NATURAL JOIN
		archivedshoporder NATURAL JOIN
		archivedorderline
	GROUP BY
		salesrep.name;
	
END;
$$ LANGUAGE plpgsql;