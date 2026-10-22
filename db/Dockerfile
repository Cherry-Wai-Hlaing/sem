
FROM mysql:8.4

WORKDIR /tmp

# Copy all supporting SQL files and database dumps
COPY test_db/*.sql /tmp/
COPY test_db/*.dump /tmp/

# Run only the main database initialization script
COPY test_db/employees.sql /docker-entrypoint-initdb.d/

ENV MYSQL_ROOT_PASSWORD=example
