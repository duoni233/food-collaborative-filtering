USE food_recommendation_demo;

INSERT INTO `user` (id, username, password, name, role) VALUES
    (1, 'demo-reader', 'demo-local-only', '演示用户', 'USER'),
    (2, 'demo-neighbor', 'demo-local-only', '近邻用户', 'USER');

INSERT INTO business (id, username, password, name, role, description, status) VALUES
    (1, 'demo-store', 'demo-local-only', '演示食堂', 'BUSINESS', '本地示例店铺', '审核通过');

INSERT INTO `type` (id, name, description) VALUES
    (1, '主食', '演示分类');

INSERT INTO goods (id, name, description, price, unit, type_id, business_id) VALUES
    (1, '南瓜粥', '演示餐品', 4.00, '碗', 1, 1),
    (2, '鸡丝面', '演示餐品', 12.00, '碗', 1, 1),
    (3, '番茄饭', '演示餐品', 10.00, '份', 1, 1),
    (4, '菌菇粥', '演示餐品', 6.00, '碗', 1, 1);

-- Both users interact with dishes 1–3. Only the neighbor interacts with dish 4.
INSERT INTO collect (user_id, goods_id, business_id) VALUES
    (1, 1, 1), (1, 3, 1), (2, 3, 1);

INSERT INTO cart (user_id, goods_id, business_id, num) VALUES
    (1, 2, 1, 1), (2, 1, 1, 1), (2, 4, 1, 1);

INSERT INTO orders (order_id, goods_id, business_id, num, user_id, price, status) VALUES
    ('DEMO-001', 1, 1, 1, 1, 4.00, '已完成'),
    ('DEMO-002', 1, 1, 1, 2, 4.00, '已评价'),
    ('DEMO-003', 2, 1, 1, 2, 12.00, '已完成');

INSERT INTO comment (user_id, goods_id, business_id, content, time) VALUES
    (1, 1, 1, '本地演示评论', '2024-03-01 12:00:00'),
    (2, 1, 1, '本地演示评论', '2024-03-01 12:00:00');
