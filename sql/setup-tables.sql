-- Prevent conflicts with existing tables in the database.
DROP TABLE IF EXISTS Category CASCADE;
CREATE TABLE Category
(
	-- Ensure ID is NOT NULL and UNIQUE using the PRIMARY KEY constraint.
	CategoryID INTEGER PRIMARY KEY,
	Name VARCHAR(50) NOT NULL,	-- SHOULD I MAKE THIS UNIQUE/DISTINCT?
	CategoryType VARCHAR(20) NOT NULL,

	-- Prevent negative ID numbers.
	CONSTRAINT chk_CategoryID CHECK (CategoryID >= 0),

	-- Ensuring types are uniform regardless of case.
	CONSTRAINT chk_CategoryType CHECK (UPPER(CategoryType) IN ('FICTION', 'NON-FICTION'))

);
CREATE INDEX index_category_name ON Category(Name);

DROP TABLE IF EXISTS SalesRep CASCADE;
CREATE TABLE SalesRep 
(
	SalesRepID INTEGER PRIMARY KEY,
	Name VARCHAR(50) NOT NULL
);
CREATE INDEX index_salesrep_name ON SalesRep(Name);

DROP TABLE IF EXISTS Shop CASCADE;
CREATE TABLE Shop 
(
	ShopID INTEGER PRIMARY KEY,
	Name VARCHAR(50) NOT NULL
);
CREATE INDEX index_shop_name ON Shop(Name);

DROP TABLE IF EXISTS Publisher CASCADE;
CREATE TABLE Publisher 
(
	PublisherID INTEGER PRIMARY KEY,
	Name VARCHAR(50) NOT NULL
);
CREATE INDEX index_publisher_name ON Publisher(Name);

DROP TABLE IF EXISTS Book CASCADE;
CREATE TABLE Book 
(
	BookID INTEGER PRIMARY KEY,
	Title VARCHAR(50) NOT NULL,
	Price DECIMAL(10,2) NOT NULL,
	CategoryID INTEGER NOT NULL,
	PublisherID INTEGER NOT NULL,

	-- Referencing foreign keys in the Category and Publisher tables.
	FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID) ON DELETE RESTRICT,
	FOREIGN KEY (PublisherID) REFERENCES Publisher(PublisherID) ON DELETE RESTRICT
);
CREATE INDEX index_book_title ON Book(Title);

DROP TABLE IF EXISTS ShopOrder CASCADE;
CREATE TABLE ShopOrder
(
	ShopOrderID INTEGER PRIMARY KEY,
	OrderDate DATE NOT NULL,
	ShopID INTEGER NOT NULL,
	SalesRepID INTEGER NOT NULL,

	-- Referencing foreign keys in the Shop and SalesRep tables.
	FOREIGN KEY (ShopID) REFERENCES Shop(ShopID),
	FOREIGN KEY (SalesRepID) REFERENCES SalesRep(SalesRepID)
);
CREATE INDEX index_shoporder_orderdate ON ShopOrder(OrderDate);

DROP FUNCTION IF EXISTS archive_shop_order() CASCADE;
CREATE FUNCTION archive_shop_order () RETURNS TRIGGER AS $$
	BEGIN
		INSERT INTO archivedshoporder SELECT old.*;
		RETURN old;
	END
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS archive_shop_order_trigger ON shoporder CASCADE;
CREATE TRIGGER archive_shop_order_trigger AFTER DELETE ON shoporder
	FOR EACH ROW EXECUTE PROCEDURE archive_shop_order(); 

-- Creating duplicate archive tables for both ShopOrder and Orderline.
-- These are required for the "end of year" procedure.
DROP TABLE IF EXISTS ArchivedShopOrder CASCADE;
CREATE TABLE ArchivedShopOrder
(
	ShopOrderID INTEGER PRIMARY KEY,
	OrderDate DATE NOT NULL,
	ShopID INTEGER NOT NULL,
	SalesRepID INTEGER NOT NULL,

	-- Referencing foreign keys in the Shop and SalesRep tables.
	FOREIGN KEY (ShopID) REFERENCES Shop(ShopID) ON DELETE RESTRICT,
	FOREIGN KEY (SalesRepID) REFERENCES SalesRep(SalesRepID) ON DELETE RESTRICT
);

DROP TABLE IF EXISTS Orderline CASCADE;
CREATE TABLE Orderline
(
	ShopOrderID INTEGER NOT NULL,
	BookID INTEGER NOT NULL,
	Quantity INTEGER NOT NULL,
	UnitSellingPrice DECIMAL (10,2),

	-- Creating a composite primary key using the two foreign keys.
	PRIMARY KEY (ShopOrderID, BookID)
);

DROP FUNCTION IF EXISTS archive_order_line() CASCADE;
CREATE FUNCTION archive_order_line () RETURNS TRIGGER AS $$
	BEGIN
		INSERT INTO archivedorderline SELECT old.*;
		RETURN old;
	END
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS archive_order_line_trigger ON orderline CASCADE;
CREATE TRIGGER archive_order_line_trigger AFTER DELETE ON orderline
	FOR EACH ROW EXECUTE PROCEDURE archive_order_line();

DROP TABLE IF EXISTS ArchivedOrderline CASCADE;
CREATE TABLE ArchivedOrderline
(
	ShopOrderID INTEGER NOT NULL,
	BookID INTEGER NOT NULL,
	Quantity INTEGER NOT NULL,
	UnitSellingPrice DECIMAL (10,2) NOT NULL,
	
	-- Creating a composite primary key using the two foreign keys.
	PRIMARY KEY (ShopOrderID, BookID)
);

-- Create view for category report which.
DROP VIEW IF EXISTS category_report CASCADE;
CREATE VIEW category_report AS
	SELECT
		category.name AS category_name,
		ROUND(AVG(book.price),2) AS average_price,
		COUNT(category.categoryid) AS number_of_books
	FROM (book NATURAL JOIN category)
	GROUP BY category.name
	ORDER BY number_of_books DESC;

-- Create view for category report summary line:
DROP VIEW IF EXISTS category_report_summary_line CASCADE;
CREATE VIEW category_report_summary_line AS
	SELECT
		SUM(average_price) AS total_average_price,
		SUM(number_of_books) AS total_books
	FROM category_report;

-- Function for generating Publisher Report Data:
DROP FUNCTION IF EXISTS publisher_report (publisher_name VARCHAR(50)) CASCADE;
CREATE FUNCTION publisher_report (publisher_name VARCHAR(50))
RETURNS TABLE (
	order_date DATE,
	book_title VARCHAR(50),
	total_quantity INTEGER,
	total_selling_value DECIMAL(10,2)
)
AS $$
BEGIN
	RETURN QUERY
	SELECT
		shoporder.orderdate,
		book.title,
		orderline.quantity,
		orderline.unitsellingprice
	-- Find 'publisherid' using 'publisher_name' regardless of case.
	FROM
		(((SELECT publisherid FROM publisher WHERE UPPER(name) = UPPER($1)) AS publisher_id
		NATURAL JOIN book)
		NATURAL JOIN shop
		NATURAL JOIN shoporder
		NATURAL JOIN orderline)
	ORDER BY
		shoporder.orderdate DESC;
END;
$$ LANGUAGE plpgsql;

-- Function for generating book order history:
DROP FUNCTION IF EXISTS book_order_history (book_id INTEGER) CASCADE;
CREATE FUNCTION book_order_history (book_id INTEGER)
RETURNS TABLE (
	shop_name VARCHAR(50),
	order_date DATE,
	quantity INTEGER,
	unit_selling_price DECIMAL(10,2)
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

-- Function for generating book order history summary line:
DROP FUNCTION IF EXISTS book_order_history_summary (book_id INTEGER) CASCADE;
CREATE FUNCTION book_order_history_summary (book_id INTEGER)
RETURNS TABLE (
	copies_ordered BIGINT,
	total_selling_value DECIMAL(10,2)
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

-- Function for generating the sales perfomance report:
DROP FUNCTION IF EXISTS sales_performance_report (start_date DATE, end_date DATE) CASCADE;
CREATE FUNCTION sales_performance_report (start_date DATE, end_date DATE)
RETURNS TABLE (
	name VARCHAR(50),
	number_of_orders BIGINT,
	order_value DECIMAL(10,2)
)
AS $$
BEGIN
	RETURN QUERY
	SELECT
		salesrep.name,
		COUNT(shoporder.salesrepid),
		SUM(unitsellingprice * quantity) AS total_selling_price
	FROM (
		salesrep
		NATURAL JOIN shoporder
		NATURAL JOIN orderline
	)
	WHERE orderdate BETWEEN $1 AND $2
	GROUP BY salesrep.name
	ORDER BY total_selling_price DESC;
END;
$$ LANGUAGE plpgsql;

-- Function for discounting books in a given category:
DROP FUNCTION IF EXISTS discount_category (category_id INTEGER, discount_percentage DECIMAL(5,2)) CASCADE;
CREATE FUNCTION discount_category (category_id INTEGER, discount_percentage DECIMAL(5,2))
RETURNS TABLE (
	category_id INTEGER,
	book_title VARCHAR(50),
	new_price DECIMAL(10,2)
)
AS $$
BEGIN
	
	-- Update book prices within specified category.
	UPDATE	book
	SET		price = ROUND(book.price * ((100 - $2) / 100), 2)
	WHERE	book.categoryid = $1;

	RETURN	QUERY

	-- Return a table containing the discounted books with their new prices.
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
	name VARCHAR(50),
	total_sales DECIMAL(10,2),
	bonus DECIMAL(10,2)
)
AS $$
BEGIN

	-- Delete data from tables.
	DELETE FROM shoporder;
	DELETE FROM orderline;

	RETURN	QUERY
	
	-- Return table containing the staff bonuses.
	SELECT
		salesrep.name,
		SUM(archivedorderline.unitsellingprice * archivedorderline.quantity) AS sum_sales,
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
		salesrep.name
	ORDER BY
		sum_sales DESC;
	
END;
$$ LANGUAGE plpgsql;