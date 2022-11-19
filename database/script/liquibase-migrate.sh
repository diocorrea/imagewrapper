#!/bin/sh
# liquibase-migrate.sh

liquibase --defaults-file=/script/liquibase.properties --search-path=/liquibase/changelog  update --changelog-file='2022-11-06-create-task-table.sql'