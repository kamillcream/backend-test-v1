INSERT INTO partner_fee_policy (partner_id, effective_from, percentage, fixed_fee)
VALUES
(1, NOW() - INTERVAL 30 DAY, 0.023500, 0),
(2, NOW() - INTERVAL 25 DAY, 0.021000, 0),
(3, NOW() - INTERVAL 20 DAY, 0.020000, 100);