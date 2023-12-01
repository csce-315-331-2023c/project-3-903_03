WITH max_id AS (SELECT COALESCE(MAX(menu_item_id), 0) + 1 AS new_id FROM _menu_item)
         INSERT INTO _menu_item(menu_item_id, name, price, calories, season)
         SELECT new_id, 'a', '$1.0', 10, 0 FROM max_id;