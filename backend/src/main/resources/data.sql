INSERT INTO subscription (plan, max_accounts, max_budgets, max_members_per_budget, has_ads, created_at, updated_at)
VALUES
    ('FREE',    1,  1,  2,  true,  NOW(), NOW()),
    ('PREMIUM', -1, -1, -1, false, NOW(), NOW())
ON CONFLICT (plan) DO NOTHING;
