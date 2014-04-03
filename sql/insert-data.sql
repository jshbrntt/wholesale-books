-- TEST DATA

-- EMPTY YOUR TABLES, you will also need to empty the archive tables 
DELETE FROM orderline;
DELETE FROM ShopOrder;
DELETE from SalesRep;
DELETE FROM Book;
DELETE FROM Shop;
DELETE FROM Publisher;
DELETE FROM Category;


-- CATEGORY

INSERT INTO Category (CategoryID, Name, CategoryType) VALUES (1, 'Computing - General', 'Non-fiction');
INSERT INTO Category (CategoryID, Name, CategoryType) VALUES (2, 'Computing - Databases', 'Non-fiction');
INSERT INTO Category (CategoryID, Name, CategoryType) VALUES (3, 'Computing - Programming', 'Non-fiction');
INSERT INTO Category (CategoryID, Name, CategoryType) VALUES (4, 'Science Fiction', 'fiction');
INSERT INTO Category (CategoryID, Name, CategoryType) VALUES (5, 'Crime', 'fiction');
INSERT INTO Category (CategoryID, Name, CategoryType) VALUES (6, 'Fiction - General', 'fiction');
INSERT INTO Category (CategoryID, Name, CategoryType) VALUES (7, 'Biography', 'Non-fiction');

--PUBLISHER

INSERT INTO Publisher (PublisherID, Name) VALUES (1, 'Addison Wesley');
INSERT INTO Publisher (PublisherID, Name) VALUES (2, 'Thompson');
INSERT INTO Publisher (PublisherID, Name) VALUES (3, 'Palgrave');
INSERT INTO Publisher (PublisherID, Name) VALUES (4, 'Microsoft Press');
INSERT INTO Publisher (PublisherID, Name) VALUES (5, 'Prentice Hall');
INSERT INTO Publisher (PublisherID, Name) VALUES (6, 'O Reilly');
INSERT INTO Publisher (PublisherID, Name) VALUES (7, 'Doubleday');
INSERT INTO Publisher (PublisherID, Name) VALUES (8, 'Oxford');
INSERT INTO Publisher (PublisherID, Name) VALUES (9, 'Bantam');
INSERT INTO Publisher (PublisherID, Name) VALUES (10, 'Orion');
INSERT INTO Publisher (PublisherID, Name) VALUES (11, 'Vintage');
INSERT INTO Publisher (PublisherID, Name) VALUES (12, 'Picador');
--SHOP

INSERT INTO Shop (ShopID, Name) VALUES (1, 'Waterstones, UEA');
INSERT INTO Shop (ShopID, Name) VALUES (2, 'Ottakers, Norwich');
INSERT INTO Shop (ShopID, Name) VALUES (3, 'Waterstones, Norwich');
INSERT INTO Shop (ShopID, Name) VALUES (4, 'Jarrolds, Norwich');
--SALES REP

INSERT INTO SalesRep (SalesRepID, Name) VALUES (1, 'John Smith');
INSERT INTO SalesRep (SalesRepID, Name) VALUES (2, 'Marie Johnson');
INSERT INTO SalesRep (SalesRepID, Name) VALUES (3, 'Rob Wilson');
INSERT INTO SalesRep (SalesRepID, Name) VALUES (4, 'Mark Davies');
INSERT INTO SalesRep (SalesRepID, Name) VALUES (5, 'Sarah Moore');
--BOOK

INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(1, 'ASP.NET Programming', 34.99, 3, 4);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(2, 'C# for Beginners', 19.99, 3, 4);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(3, 'Database Systems', 41.99, 2, 1);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(4, 'Relational Database Principles', 21.99, 2, 1);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(5, 'Database Programming with JDBC and Java', 23.95, 2, 6);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(6, 'Computer Networks and Internets', 29.99, 1, 5);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(7, 'Internet and World Wide Web: How to Program', 29.99, 3, 5);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(8, 'Core Servlets and JavaServer Pages', 34.95, 3, 5);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(9, 'C How To Program', 22.99, 3, 5);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(10, 'C# Essentials', 16.99, 3, 6);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(11, 'Designing & Implementation Databses in SQL Server', 34.99, 2, 4);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(12, 'A Hat Full of Sky', 9.99, 4, 7);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(13, 'The Swords of Night and Day', 17.99, 4, 9);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(14, 'No Second Chance', 6.99, 6, 10);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(15, 'Lost Light', 6.99, 6, 10);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(16, 'Touching the Void', 7.99, 7, 11);
INSERT INTO Book (BookID, Title, Price, CategoryID, PublisherID) VALUES
	(17, 'Lucky', 7.99, 7, 12);
--SHOP ORDERS

-- WATERSTONES UEA
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(1, '2014-01-03', 1, 2);
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(2, '2014-02-15', 1, 2);
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(3, '2014-03-07', 1, 2);
-- WATERSTONES NORWICH
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(4, '2014-01-04', 3, 2);
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(5, '2014-02-18', 3, 2);
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(6, '2014-03-09', 3, 2);
-- JARROLDS NORWICH
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(7, '2014-01-29', 4, 4);
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) VALUES
	(8, '2014-02-25', 4, 4);
-- OTTAKERS, NORWICH
INSERT INTO ShopOrder (ShopOrderID, OrderDate, ShopID, SalesRepID) 
     VALUES (100, '2014-03-10', 2, 5);

--ORDERLINES

--ORDER 1 (Waterstones UEA, Marie Johnson)
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (1, 3, 20, 38.99);	
-- DB Prog with JDBC
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (1, 5, 10, 21.95);	
-- SQL Server
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (1, 11, 3, 34.99);	
-- Rel DB Princ (21.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (1, 4, 15, 19.99);	

--ORDER 2 (Waterstones UEA, Marie Johnson)
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (2, 3, 10, 41.99);	
-- Rel DB Princ  (21.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (2, 4, 5, 21.99);	
-- Internet and WWW
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (2, 7, 10, 29.99);	

--ORDER 3 (Waterstones UEA, Marie Johnson )
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (3, 3, 5, 41.99);	
-- Rel DB Princ  (21.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (3, 4, 2, 21.99);	
-- Internet and WWW
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (3, 7, 20, 25.99);	
-- Core Servlets
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (3, 8, 10, 32.95);	
-- C How To
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (3, 9, 3, 22.99);	

--ORDER 4 (Waterstones Norwich, Marie Johnson)
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (4, 3, 5, 41.99);	
-- DB Prog with JDBC
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (4, 5, 2, 21.95);	
-- SQL Server
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (4, 11, 1, 34.99);	
-- Rel DB Princ
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (4, 4, 5, 21.99);	
-- Hat Full of Sky (9.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (4, 12, 20, 7.99);	
-- Swords (17.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (4, 13, 4, 17.99);	

--ORDER 5 (Water stones Norwich, Marie Johnson)
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (5, 3, 2, 41.99);	
-- Rel DB Princ
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (5, 4, 1, 21.99);	
-- Internet and WWW
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (5, 7, 5, 29.99);	
-- Hat Full of Sky (9.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (5, 12, 10, 9.99);	
-- Swords (17.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (5, 13, 2, 17.99);	

--ORDER 6 (Waterstones Norwich, Marie Johnson )
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (6, 3, 2, 41.99);	
-- Internet and WWW
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (6, 7, 1, 15.99);	
-- Core Servlets
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (6, 8, 3, 32.95);	
-- C How To
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (6, 9, 1, 22.99);	
-- Hat Full of Sky (9.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (6, 12, 10, 9.99);	
-- Swords (17.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (6, 13, 2, 17.99);	

--ORDER 7 (Jarrolds Norwich, Mark Davies)
-- ASP.NET (34.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (7, 1, 2, 34.99);	
-- C# Essentials (16.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (7, 10, 3, 16.99);	
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (7, 3, 2, 41.99);	
-- Hat Full of Sky (9.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (7, 12, 10, 9.99);	
-- Swords (17.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (7, 13, 2, 17.99);	
-- No Second Chance (6.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (7, 14, 10, 5.99);	
-- Lucky (7.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (7, 17, 8, 7.99);	

--ORDER 8 (Jarrolds Norwich, Mark Davies)
-- ASP.NET (34.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 1, 1, 34.99);	
-- C# Essentials (16.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 10, 1, 16.99);	
-- Database Systems
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 3, 1, 41.99);	
-- Hat Full of Sky (9.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 12, 5, 9.99);	
-- Swords (17.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 13, 1, 17.99);	
-- No Second Chance (6.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 14, 3, 5.99);	
-- Lost Light (6.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 15, 20, 5.99);	
-- Touching the Void (7.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 16, 15, 6.99);	
-- Lucky (7.99)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (8, 17, 3, 7.99);

--ORDER 9 (Ottakers Norwich, Sarah Moore)
INSERT INTO OrderLine (ShopOrderID, BookID, Quantity, UnitSellingPrice) VALUES (100, 1, 300, 34.99);
	
