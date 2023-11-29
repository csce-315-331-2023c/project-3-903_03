drop table  _status, _user_category, _user, _customer_order, _menu_item, _ingredient, _restock_order, _ordered_item, _item_ingredient, _restock_ingredient;

CREATE TABLE _user_category (
	category_id int PRIMARY KEY,
	name text
);

CREATE TABLE _user (
	id int PRIMARY KEY,
	name text,
	username text,
	password text,
	category_id int,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES _user_category(category_id)
);

CREATE TABLE _status (
	status_id int PRIMARY KEY,
	status text
);

CREATE TABLE _customer_order (
    customer_order_id int PRIMARY KEY,
    id int,
    cost money,
    order_date date,
	order_time time,
    status_id int,
    CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES _user(id),
    CONSTRAINT fk_satus_id FOREIGN KEY (status_id) REFERENCES _status(status_id)
);

CREATE TABLE _menu_item (
    menu_item_id int PRIMARY KEY,
    name text,
    price money,
	calories int,
	season text
);

CREATE TABLE _ingredient (
    ingredient_id int PRIMARY KEY,
    name text,
    current_qty int,
    needed_qty int,
    expiration_date date NULL,
    cost money
);

CREATE TABLE _restock_order (
    restock_order_id int PRIMARY KEY,
	id int,
    restock_date date,
    cost money,
	CONSTRAINT fk_id FOREIGN KEY (id) REFERENCES _user(id)
);

CREATE TABLE _ordered_item (
    customer_order_id int,
    menu_item_id int,
    CONSTRAINT fk_customer_order_id FOREIGN KEY (customer_order_id) REFERENCES _customer_order(customer_order_id),
    CONSTRAINT fk_menu_item_id FOREIGN KEY (menu_item_id) REFERENCES _menu_item(menu_item_id),
    amount int,
    PRIMARY KEY (customer_order_id, menu_item_id) /* composite primary key, prevents duplication */
);

CREATE TABLE _item_ingredient (
    ingredient_id int,
    menu_item_id int,
    CONSTRAINT fk_ingredient_id FOREIGN KEY (ingredient_id) REFERENCES _ingredient(ingredient_id),
    CONSTRAINT fk_menu_item_id FOREIGN KEY (menu_item_id) REFERENCES _menu_item(menu_item_id),
	quantity int,
    PRIMARY KEY (ingredient_id, menu_item_id) /* composite primary key, prevents duplication */    
);

CREATE TABLE _restock_ingredient (
    ingredient_id int,
    restock_order_id int,
    CONSTRAINT fk_ingredient_id FOREIGN KEY (ingredient_id) REFERENCES _ingredient(ingredient_id),
    CONSTRAINT fk_restock_order_id FOREIGN KEY (restock_order_id) REFERENCES _restock_order(restock_order_id),
    quantity int,
    PRIMARY KEY (ingredient_id, restock_order_id) /* composite primary key, prevents duplication */    
);


INSERT INTO 
	_user_category(category_id, name)
VALUES
	(0, 'admin'),
	(1, 'manager'),
	(2, 'cashier'),
	(3, 'customer'),
    (4, 'guest');


INSERT INTO 
	_status(status_id, status)
VALUES
	(0, 'Pending'),
	(1, 'Completed'),
	(2, 'Canceled');


INSERT INTO 
	_user(id, name, username, password, category_id)
VALUES
	(0, 'administrator', 'admin', 'admin', 0),
	(1, 'Philip Ritchey', 'pritchey', 'dollar1', 1),
	(2, 'Sophia Dronova', 'sdronova', 'frighten3', 2),
	(3, 'Brendan Fattig', 'bfattig', 'liver2', 2),
	(4, 'Megha Subhash', 'ssubhash', 'laboratory34', 2),
	(5, 'Aaron Weng', 'aweng', 'jump560', 2),
	(6, 'John Smith', 'jsmith', 'customer', 3),
    (7, '', 'guest', '', 4);
	
	
INSERT INTO 
	_menu_item(menu_item_id, name, price, calories, season) 
VALUES
	(1, 'Chocolate Chip Cookie', 1.75, 100, 'None'),
	(2, 'Snickerdoodle Cookie', 1.75, 100, 'None'),
	(3, 'Sugar Cookie with M&Ms Cookie', 1.75, 100, 'None'),
	(4, 'Double Chocolate Chip Cookie', 1.75, 100, 'None'),
	(5, 'Pecan Chocolate Chip Cookie', 1.75, 100, 'None'),
	(6, 'Peanut Butter Cookie', 1.75, 100, 'None'),
	(7, 'Sugar Cookie', 1.75, 100, 'None'),
	(8, 'Oatmeal Raisin Cookie', 1.75, 100, 'None'),
	(9, 'Peanut Butter Chocolate Chip Cookie', 1.75, 100, 'None'),
	(10, 'Connor Man Brookie', 3.75, 100, 'None'),
	(11, 'Brownie', 4.50, 100, 'None'),
	(12, 'Peanut Butter Chocolate Bar', 4.50, 100, 'None'),
	(13, 'Salted Caramel Blondie', 4.50, 100, 'None'),
    (14, 'Oatmeal Chocolate Chip Cookie', 1.75, 100, 'None'),
    (15, 'Vanilla Ice Cream Pint', 5.50, 100, 'None'),
    (16, 'Mint Chocolate Chip Pint', 5.50, 100, 'None'),
    (17, 'Chocolate Ice Cream Pint', 5.50, 100, 'None'),
    (18, 'Cookies n Cream Pint', 5.50, 100, 'None'),
    (19, '1% Milk', 2.00, 100, 'None'),
    (20, 'Chocolate Milk', 2.00, 100, 'None'),
	(21, 'Gingerbread Cookie', 3.00, 100, 'Christmas');
    

INSERT INTO 
	_ingredient(ingredient_id, name, current_qty, needed_qty, expiration_date, cost) 
VALUES
	(1, 'Sugar', 											50, 250, '2023-10-31', 0.10),
	(2, 'Salted Butter', 									25, 250, '2023-10-02', 0.50),
	(3, 'Brown Sugar', 										100, 350, '2023-12-02', 0.15),
	(4, 'Vanilla Extract', 									300, 300, '2023-10-02', 0.20),
	(5, 'Eggs', 											120, 360, '2023-10-14', 0.35),
	(6, 'All-Purpose Flour', 								200, 400, NULL, 0.05),
	(7, 'Baking Soda', 										100, 360, NULL, 0.35),
	(8, 'Salt', 											120, 360, NULL, 0.10),
	(9, 'Chocolate Chips', 									250, 500, '2023-10-23', 0.60),
	(10, 'Cream of Tartar', 								20, 425, '2023-11-13', 1.00),
	(11, 'Unsalted Butter', 								350, 360, '2023-11-23', 0.35),
	(12, 'M&Ms', 											500, 750, '2023-11-14', 1.69),
	(13, 'Cocoa Powder', 									120, 500, NULL, 0.55),
	(14, 'Pecans', 											50, 375, '2023-12-12', 1.01),
	(15, 'Peanut Butter', 									100, 450, '2023-11-09', 2.49),
	(16, 'Molasses',										25, 400, NULL, 1.35),
	(17, 'Cinnamon', 										350, 360, NULL, 0.30),
	(18, 'Whole Rolled Oats', 								120, 360, '2023-12-09', 0.45),
	(19, 'Raisins', 										120, 360, NULL, 0.25),
	(20, 'Powdered Sugar', 									200, 500, NULL, 0.25),
	(21, 'Canola Oil', 										100, 250, '2023-12-14', 4.39),
	(22, 'Water', 											700, 1000, NULL, 0.05),
	(23, 'Caramel', 										135, 450, '2023-10-14', 0.60),
	(24, 'Blue Bell Vanilla Ice Cream Pint', 				100, 450, '2023-10-14', 1.00),
	(25, 'Blue Bell Mint Chocolate Chip Ice Cream Pint', 	250, 450, '2023-11-01', 1.50),
	(26, 'Blue Bell Chocolate Ice Cream Pint', 				90, 450, '2023-12-20', 1.25),
	(27, 'Blue Bell Cookies n Cream Ice Cream Pint', 		380, 450, '2023-10-28', 1.75),
	(28, 'Baking Powder', 									380, 500, '2023-10-20', 0.27),
	(29, '1% Milk Bottle', 									600, 1000, '2023-11-19', 0.30),
	(30, 'Chocolate Milk Bottle', 							750, 500, '2023-11-18', 0.20);