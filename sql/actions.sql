-- 1
INSERT INTO category VALUES (categoryid, name, categorytype);
-- 2
DELETE FROM category WHERE CategoryID = @CategoryID;
-- 3
SELECT
	name,
	ROUND(AVG(price),2) AS averageprice,
	COUNT(categoryid) AS totals
FROM
(
	book
	NATURAL JOIN category
)
GROUP BY name
ORDER BY totals DESC;
-- Average of Averages
-- Total Books
-- 4
SELECT
	book.title,
	shoporder.orderdate,
	SUM(orderline.quantity) as totalquantity,
	SUM(orderline.unitsellingprice) AS totalsellingvalue
FROM
(
	shop
	NATURAL JOIN shoporder
	NATURAL JOIN orderline
	NATURAL JOIN book
)
WHERE name = 'W H Smith'
GROUP BY book.title, shoporder.orderdate
ORDER BY shoporder.orderdate;
-- 5
SELECT
	shop.name,
	shoporder.orderdate,
	orderline.quantity,
	orderline.unitsellingprice
FROM
(
	book
	NATURAL JOIN orderline
	NATURAL JOIN shoporder
	NATURAL JOIN shop
)
WHERE book.bookid = 20;
-- summaryline
SELECT
	SUM(orderline.quantity) AS copiesordered,
	SUM(orderline.unitsellingprice) AS totalsellingvalue
FROM
(
	book
	NATURAL JOIN orderline
)
WHERE book.bookid = 20
GROUP BY book.bookid;
-- 6
SELECT
	salesrep.name,
	COUNT(shoporder.salesrepid) AS totalunitsold,
	SUM(unitsellingprice) AS totalordervalue,
	SUM(unitsellingprice) / COUNT(shoporder.salesrepid) AS ordersbyvalue
FROM (
	salesrep
	NATURAL JOIN shoporder
	NATURAL JOIN orderline
)
WHERE orderdate BETWEEN '2013-01-01' AND '2014-01-01'
GROUP BY salesrep.name
ORDER BY ordersbyvalue DESC;
-- 7
UPDATE book
SET price = price * 0.9
WHERE categoryid = 5;
-- 8
INSERT INTO archivedorderline
SELECT * FROM orderline;
DELETE FROM orderline;
-- Why doesn't this work?
INSERT INTO archivedshoporder
SELECT * FROM shoporder;
DELETE FROM shoporder;
-- report