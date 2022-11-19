#!/bin/sh
# wait-for-postgres.sh

set -e

shift

until liquibase --defaults-file=/script/liquibase.properties --search-path=/liquibase/changelog status --changelog-file=2022-11-06-create-task-table.sql; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 10
done

>&2 echo "Postgres is up - executing command"
exec "$@"