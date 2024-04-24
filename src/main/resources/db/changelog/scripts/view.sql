create view trees_with_increments_for_period as
select t.id,
	t.forest_id,
	t.name,
	t.description,
	t.tree_type,
	t.created_at,
	t.tree_limit,
	t.period,
    coalesce(sum(il.value), 0) as inc_count
from forests f
         left join trees t on f.id = t.forest_id
         left join incrementation_log il on t.id = il.tree_id
 AND (
    t.tree_type != 'PERIODIC_TREE'
    OR (
        t.tree_type = 'PERIODIC_TREE'
        AND il.date >= CASE
            WHEN t.period = 'DAY' THEN CURRENT_DATE
            WHEN t.period = 'WEEK' THEN CURRENT_DATE - INTERVAL '1 week'
            WHEN t.period = 'MONTH' THEN CURRENT_DATE - INTERVAL '1 month'
            WHEN t.period = 'QUARTER' THEN CURRENT_DATE - INTERVAL '3 month'
            WHEN t.period = 'YEAR' THEN CURRENT_DATE - INTERVAL '1 year'
            ELSE CURRENT_DATE
        END
    )
)
group by f.id, t.id
having case
    when t.tree_type = 'BOOLEAN_TREE'
        then coalesce(sum(il.value), 0) = 0
    when t.tree_type = 'LIMITED_TREE'
        then coalesce(sum(il.value), 0) < t.tree_limit
    when t.tree_type = 'PERIODIC_TREE'
        then coalesce(sum(il.value), 0) = 0
    when t.tree_type = 'UNLIMITED_TREE'
        then 1 = 1
    end;